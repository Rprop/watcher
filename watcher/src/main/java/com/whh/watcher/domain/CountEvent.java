package com.whh.watcher.domain;

import com.alibaba.fastjson.JSON;
import com.whh.watcher.channel.MergeAble;
import com.whh.watcher.domain.enums.CountTypeEnum;
import com.whh.watcher.domain.enums.EventTypeEnum;
import com.whh.watcher.channel.EventChannel;

import java.math.BigDecimal;

/**
 * Dept：type类型及存值方式:
 * *************** avg时，counter为总请求个数，data为总请求值
 * *************** total时，同avg
 * *************** mutAvg时，counter为空，data格式为a=15.3&b=6.5&a=12
 * User:wanghanghang
 * Date:2016/4/28
 * Version:1.0
 */
public class CountEvent extends Event implements MergeAble {
    private static final String connectOpen = "&";
    private Integer counter = 1;
    private String data;
    private CountTypeEnum type;

    public CountEvent() {
    }

    public CountEvent(String key, CountTypeEnum type, String data) {
        this.key = key;
        this.event = EventTypeEnum.count;
        this.data = data;
        this.type = type;
    }


    @Override
    public void end() {
        EventChannel.collectEvent(this);
    }

    /**
     * 事件合并，减少网络请求量。
     * <p/>
     * ********外部要保证key和type是相同的*******
     *
     * @param other 要合并的对象
     */
    public void mergeEvent(CountEvent other) {

    }

    public Integer getCounter() {
        return counter;
    }

    public String getData() {
        return data;
    }

    public CountTypeEnum getType() {
        return type;
    }

    @Override
    public String toJsonString() {
        return "C" + JSON.toJSONString(this);
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public String mergeKey() {
        return this.getKey();
    }

    @Override
    public void merge(Event event) {
        if (event instanceof CountEvent) {
            CountEvent other = (CountEvent) event;
            if (type == CountTypeEnum.multiAvg) {//多条件
                counter++;
                this.data = new StringBuffer(data).append(connectOpen).append(other.getData()).toString();
            } else if (type == CountTypeEnum.avg || type == CountTypeEnum.total) {
                counter++;
                this.data = new BigDecimal(data).add(new BigDecimal(other.getData())).toString();
            }
        }
    }
}
