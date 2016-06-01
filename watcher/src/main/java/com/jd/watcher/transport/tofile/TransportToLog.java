package com.jd.watcher.transport.tofile;

import com.jd.watcher.domain.Event;
import com.jd.watcher.spi.TransportService;
import com.jd.watcher.config.ConfigFactory;

import java.util.List;

/**
 * Dept：将性能统计原始数据写入日志文件
 * User:wanghanghang
 * Date:2016/5/3
 * Version:1.0
 */
public class TransportToLog implements TransportService {
    private final RollingFileWork work;
    private final static String separator = System.getProperty("line.separator", "/n");

    public TransportToLog() {
        this.work = new RollingFileWork(ConfigFactory.getSystemConfig().getTpLogFile(),//输出的文件目录
                ConfigFactory.getSystemConfig().getTpLogFileMaxSize(),//输出的文件大小
                ConfigFactory.getSystemConfig().getTpLogFileMaxNum());//输出的文件个数
    }

    @Override
    public void sendEvent(Event event) {
        if (event != null) {
            work.append(event.toJsonString() + separator);
        }
    }

    @Override
    public void sendEvent(List<Event> events) {
        if (events != null && !events.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (Event event : events) {
                if (event != null) {
                    stringBuffer.append(event.toJsonString()).append(separator);
                }
            }
            work.append(stringBuffer.toString());
        }
    }
}
