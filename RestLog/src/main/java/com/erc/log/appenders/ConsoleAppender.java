package com.erc.log.appenders;

import com.erc.log.containers.LOG;
import com.erc.log.helpers.DateHelper;
import com.erc.log.helpers.StringUtil;
import com.erc.log.model.LogModel;

public class ConsoleAppender extends BaseAppender {

    public ConsoleAppender() {
    }

    @Override
    public void append(LOG log) {
        System.out.println(StringUtil.format("{0} {1} {2} {3}",
                DateHelper.getDateWithFormat(log.date, DateHelper.FORMAT),
                log.packageName,
                log.tag,
                log.message));
    }
}
