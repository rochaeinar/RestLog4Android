package com.erc.log.helpers;

import android.content.Context;
import android.os.Build;

/**
 * Caches the device/app information that a {@link com.erc.log.containers.LOG} needs, so it
 * is not gathered from scratch on every single log call.
 * <p>
 * Building a LOG used to query the battery, memory, network, display and even run a
 * (slow) root check <b>on every call</b>. Under load that work dominated the cost of
 * logging. This cache splits the information in two:
 * <ul>
 *     <li><b>Constant</b> data (device model, app version, screen, total RAM, root state…)
 *     is captured <b>once per process</b> and reused forever.</li>
 *     <li><b>Dynamic</b> data (memory usage, free storage, battery, network, screen state,
 *     orientation) is refreshed at most once every {@link #DYNAMIC_TTL_MS} ms; a burst of
 *     thousands of logs then reuses one snapshot instead of recomputing it thousands of
 *     times.</li>
 * </ul>
 * All capture calls are guarded so the cache can never crash the host app.
 */
public final class DeviceInfoCache {

    /** How long a dynamic snapshot is reused before being refreshed. */
    private static final long DYNAMIC_TTL_MS = 3_000;

    private static volatile DeviceInfoCache instance;

    // ---- constant, captured once ----
    public String deviceId = "";
    public String packageName = "";
    public String appName = "";
    public String version = "";
    public String manufacturer = "";
    public String brand = "";
    public String model = "";
    public String totalMemoryRam = "";
    public String totalInternalMemory = "";
    public String androidVersion = "";
    public String rootedState = "";
    public String locale = "";
    public String density = "";
    public String dpi = "";
    public String resolution = "";

    private final Context context;
    private volatile Dynamic dynamic;

    private DeviceInfoCache(Context context) {
        Context app = context != null ? context.getApplicationContext() : null;
        this.context = app != null ? app : context;
        captureConstants();
    }

    public static DeviceInfoCache getInstance(Context context) {
        DeviceInfoCache local = instance;
        if (local == null) {
            synchronized (DeviceInfoCache.class) {
                local = instance;
                if (local == null) {
                    local = new DeviceInfoCache(context);
                    instance = local;
                }
            }
        }
        return local;
    }

    /** Forces the constant data to be re-captured on next use (e.g. after a locale change). */
    public static void invalidate() {
        instance = null;
    }

    private void captureConstants() {
        try { deviceId = AndroidId.getUniquePseudoID(context); } catch (Throwable ignored) { }
        try { packageName = ApplicationInformation.getPackageName(context); } catch (Throwable ignored) { }
        try { appName = ApplicationInformation.getApplicationName(context); } catch (Throwable ignored) { }
        try { version = ApplicationInformation.getApplicationVersion(context); } catch (Throwable ignored) { }
        manufacturer = Build.MANUFACTURER;
        brand = Build.BRAND;
        model = Build.MODEL;
        androidVersion = Build.VERSION.RELEASE;
        try { totalMemoryRam = MemoryInformation.getTotalMemory(context); } catch (Throwable ignored) { }
        try { totalInternalMemory = MemoryInformation.getTotalInternalMemorySize(); } catch (Throwable ignored) { }
        try { rootedState = String.valueOf(Root.isDeviceRooted()); } catch (Throwable ignored) { }
        try { locale = Location.getCurrentLocale(context).getDisplayName(); } catch (Throwable ignored) { }
        try { density = Display.getDensityName(context); } catch (Throwable ignored) { }
        try { dpi = Display.getDpi(context); } catch (Throwable ignored) { }
        try { resolution = Display.getScreenResolution(context); } catch (Throwable ignored) { }
    }

    /** Returns a dynamic snapshot, refreshing it only when the cached one is stale. */
    public Dynamic getDynamic() {
        Dynamic d = dynamic;
        long now = System.currentTimeMillis();
        if (d == null || now - d.capturedAt > DYNAMIC_TTL_MS) {
            synchronized (this) {
                d = dynamic;
                now = System.currentTimeMillis();
                if (d == null || now - d.capturedAt > DYNAMIC_TTL_MS) {
                    d = Dynamic.capture(context, now);
                    dynamic = d;
                }
            }
        }
        return d;
    }

    /** Snapshot of the values that change while the app runs. */
    public static final class Dynamic {
        final long capturedAt;
        public String ramMemoryUsage = "";
        public String internalMemoryAvailable = "";
        public String batteryLevel = "";
        public String chargingState = "";
        public String networkType = "";
        public String networkState = "";
        public String activeScreen = "";
        public String orientation = "";

        private Dynamic(long capturedAt) {
            this.capturedAt = capturedAt;
        }

        static Dynamic capture(Context ctx, long now) {
            Dynamic d = new Dynamic(now);
            try { d.ramMemoryUsage = MemoryInformation.getMemoryUsage(ctx); } catch (Throwable ignored) { }
            try { d.internalMemoryAvailable = MemoryInformation.getAvailableInternalMemorySize(); } catch (Throwable ignored) { }
            try { d.batteryLevel = Battery.getBatteryPercentage(ctx); } catch (Throwable ignored) { }
            try { d.chargingState = Battery.isPlugged(ctx); } catch (Throwable ignored) { }
            try { d.networkType = Network.getConnectionType(ctx).value(); } catch (Throwable ignored) { }
            try { d.networkState = Network.connectivityString(ctx); } catch (Throwable ignored) { }
            try { d.activeScreen = Display.getScreenStatus(ctx).value(); } catch (Throwable ignored) { }
            try { d.orientation = Display.getOrientation(ctx); } catch (Throwable ignored) { }
            return d;
        }
    }
}
