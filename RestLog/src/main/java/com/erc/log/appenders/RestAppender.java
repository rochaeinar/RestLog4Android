package com.erc.log.appenders;

import com.erc.log.AppContext;
import com.erc.log.containers.LOG;
import com.erc.log.model.LogModel;
import com.erc.log.services.NetworkSchedulerJobBuilder;

import java.util.List;

public class RestAppender extends BaseAppender {

    private String host;

    public RestAppender() {
    }

    @Override
    public void append(LOG log) {
        LogModel.addLog(log);
        NetworkSchedulerJobBuilder.scheduleJob(AppContext.getContext());
    }

    @Override
    public void append(List<LOG> logs) {
        if (logs == null || logs.isEmpty()) {
            return;
        }
        // Persist the whole batch in one transaction and schedule the upload job once,
        // instead of once per log.
        LogModel.addAll(logs);
        NetworkSchedulerJobBuilder.scheduleJob(AppContext.getContext());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
