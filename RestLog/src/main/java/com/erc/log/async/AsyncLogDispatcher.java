package com.erc.log.async;

import com.erc.log.AppContext;
import com.erc.log.appenders.AvailableAppenders;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;
import com.erc.log.model.LogModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Off-the-main-thread writer for logs.
 * <p>
 * Every {@code Log.x(...)} call used to persist synchronously on the caller thread
 * (usually the UI thread), so writing thousands of records blocked the app and produced
 * ANRs / kills. This dispatcher decouples producing a log from writing it:
 * <ul>
 *     <li><b>Non-blocking enqueue:</b> callers only drop the log into a bounded queue and
 *     return immediately.</li>
 *     <li><b>Bounded queue + drop-oldest:</b> under extreme load the queue never grows
 *     without limit; when it is full the <i>oldest</i> pending log is discarded to make
 *     room for the newest one, so the app can never be brought down by a log storm.</li>
 *     <li><b>Batched writes:</b> a single background thread drains the queue and hands
 *     whole batches to the appenders, which persist them in one transaction.</li>
 * </ul>
 * A single dedicated thread also serialises all database access, avoiding write
 * contention on SQLite.
 */
public final class AsyncLogDispatcher {

    /** Maximum number of pending logs kept in memory before the oldest ones are dropped. */
    private static final int DEFAULT_QUEUE_CAPACITY = 10_000;

    /** Maximum number of logs written together in a single transaction. */
    private static final int MAX_BATCH_SIZE = 200;

    /** How long the worker waits for new logs before looping again. */
    private static final long POLL_TIMEOUT_MS = 1_000;

    /** Emit a "dropped N logs" warning to logcat at most this often. */
    private static final long DROP_REPORT_INTERVAL_MS = 5_000;

    private static final String TAG = "RestLog";

    private static volatile AsyncLogDispatcher instance;

    private final LinkedBlockingDeque<LOG> queue;
    private final AvailableAppenders availableAppenders = new AvailableAppenders();
    private final AtomicLong droppedCount = new AtomicLong();
    private volatile long lastDropReportAt = 0;

    private AsyncLogDispatcher(int capacity) {
        this.queue = new LinkedBlockingDeque<>(capacity);
        Thread worker = new Thread(this::runLoop, "RestLog-Dispatcher");
        worker.setDaemon(true);
        worker.setPriority(Thread.MIN_PRIORITY);
        worker.start();
    }

    public static AsyncLogDispatcher getInstance() {
        if (instance == null) {
            synchronized (AsyncLogDispatcher.class) {
                if (instance == null) {
                    instance = new AsyncLogDispatcher(DEFAULT_QUEUE_CAPACITY);
                }
            }
        }
        return instance;
    }

    /**
     * Hands a log to the background writer. Never blocks and never throws. If the queue is
     * full, the oldest pending logs are discarded (drop-oldest) so that recent logs win
     * and the calling app is never stalled.
     */
    public void enqueue(LOG log) {
        if (log == null) {
            return;
        }
        while (!queue.offerLast(log)) {
            if (queue.pollFirst() != null) {
                droppedCount.incrementAndGet();
            }
        }
    }

    private void runLoop() {
        final List<LOG> batch = new ArrayList<>(MAX_BATCH_SIZE);
        while (true) {
            try {
                LOG first = queue.pollFirst(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (first == null) {
                    reportDropsIfDue();
                    continue;
                }
                batch.clear();
                batch.add(first);
                queue.drainTo(batch, MAX_BATCH_SIZE - 1);
                processBatch(batch);
                reportDropsIfDue();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Throwable t) {
                // The writer thread must survive any failure so logging keeps working.
                android.util.Log.e(TAG, "RestLog dispatcher failed to process a batch", t);
            }
        }
    }

    private void processBatch(List<LOG> batch) {
        LogConfiguration config = LogConfiguration.getInstance(AppContext.getContext());

        // Enforce the optional daily record cap with a single COUNT query per batch
        // instead of one per log.
        List<LOG> allowed = applyDailyLimit(config, batch);
        if (allowed.isEmpty()) {
            return;
        }

        if (config.isAvoidDuplicated()) {
            // Deduplication needs a per-log read/update, so fall back to single dispatch.
            // Still off the main thread, so it can no longer cause an ANR.
            for (LOG log : allowed) {
                LOG existent = LogModel.getLog(log);
                if (existent != null) {
                    existent.count++;
                    availableAppenders.append(existent);
                } else {
                    availableAppenders.append(log);
                }
            }
        } else {
            availableAppenders.append(allowed);
        }
    }

    private List<LOG> applyDailyLimit(LogConfiguration config, List<LOG> batch) {
        long maxRecordNumber = config.getMaxRecordNumber();
        if (maxRecordNumber <= 0) {
            return batch;
        }
        long current = LogModel.getDailyRecordsCount();
        long remaining = maxRecordNumber - current;
        if (remaining <= 0) {
            return new ArrayList<>();
        }
        if (remaining >= batch.size()) {
            return batch;
        }
        return new ArrayList<>(batch.subList(0, (int) remaining));
    }

    private void reportDropsIfDue() {
        long dropped = droppedCount.get();
        if (dropped == 0) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastDropReportAt < DROP_REPORT_INTERVAL_MS) {
            return;
        }
        lastDropReportAt = now;
        long reported = droppedCount.getAndSet(0);
        if (reported > 0) {
            android.util.Log.w(TAG, "RestLog dropped " + reported
                    + " log(s) due to overload (queue full, oldest discarded)");
        }
    }
}
