package com.jd.watcher.receiver;


import com.jd.watcher.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("messageHandler")
public class DefaultMessageHandler implements MessageHandler {
    @Resource
    private MessageConsumer consumer;
    private static Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);

    @Override
    public void handle(Event event) {
//        if (m_consumer == null) {
//            m_consumer = lookup(MessageConsumer.class);
//        }
        try {
            consumer.consume(event);
        } catch (Throwable e) {
            logger.error("Error when consuming message in " + consumer + "! tree: " + event, e);
        }
    }
}
