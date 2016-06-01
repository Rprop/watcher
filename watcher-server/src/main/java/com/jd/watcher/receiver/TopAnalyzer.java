package com.jd.watcher.receiver;

import com.alibaba.fastjson.JSON;
import com.jd.watcher.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class TopAnalyzer extends AbstractMessageAnalyzer<Event> {
    private static final Logger logger = LoggerFactory.getLogger(TopAnalyzer.class);
    private Integer countEvent = 0;

    @Override
    public void doCheckpoint(boolean atEnd) {

    }

    @Override
    public Event getReport(String domain) {
        return null;
    }

    @Override
    protected void loadReports() {

    }

    @Override
    protected void process(Event event) {
        countEvent++;
        if (countEvent % 10000 == 1) {
            logger.info("接收的数量：{}", countEvent);
        }
//        logger.info("TopAnalyzer---:{}", event.toJsonString());
    }
}
