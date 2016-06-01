package com.whh.watcher.spi;

import com.whh.watcher.domain.Event;
import io.netty.buffer.ByteBuf;

import java.util.List;

public interface MessageCodec {
    /**
     * 将事件消息解码
     * @param buf
     * @return
     */
    public List<Event> decode(byte[] buf);

    /**
     * 将事件消息解码
     * @param buf
     * @param events
     */
    public void decode(ByteBuf buf, List<Event> events);

    /**
     * 将事件消息编码
     * @param events 事件
     */
    public byte[] encode(List<Event> events);
}
