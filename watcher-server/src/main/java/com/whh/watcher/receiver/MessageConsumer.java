package com.whh.watcher.receiver;

import com.whh.watcher.domain.Event;

public interface MessageConsumer {
    /**
     * 本地处理一条消息
     *
     * @param tree
     */
    public void consume(Event tree);

    public void doCheckpoint();
}
