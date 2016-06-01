package com.jd.watcher.test.watcher;

import com.jd.watcher.Watcher;
import com.jd.watcher.domain.WatchEvent;

import java.text.MessageFormat;
import java.util.Date;

public class DefaultPathBuilder {

    public String getLogviewPath(Date timestamp, String name) {
        MessageFormat format = new MessageFormat("{0,date,yyyyMMdd}/{0,date,HH}/{1}");

        return format.format(new Object[]{timestamp, name});
    }

    public String getReportPath(String name, Date timestamp, int index) {
        MessageFormat format = new MessageFormat("{0,date,yyyyMMdd}/{0,date,HH}/{1}/report-{2}");

        return format.format(new Object[]{timestamp, index, name});
    }

    public static void main(String[] args) {
        DefaultPathBuilder builder = new DefaultPathBuilder();
        String path = builder.getReportPath("app", new Date(), 100);
        System.out.println(path);

        String path2 = builder.getLogviewPath(new Date(), "App");
        System.out.println(path2);
    }
}
