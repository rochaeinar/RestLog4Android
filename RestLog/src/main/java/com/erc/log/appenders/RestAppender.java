package com.erc.log.appenders;

import com.erc.log.AppContext;
import com.erc.log.containers.LOG;
import com.erc.log.model.LogModel;
import com.erc.log.services.NetworkSchedulerJobBuilder;

public class RestAppender extends BaseAppender {

    private String host;

    public RestAppender() {
    }

    @Override
    public void append(LOG log) {
        LogModel.addLog(log);
        NetworkSchedulerJobBuilder.scheduleJob(AppContext.getContext());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
