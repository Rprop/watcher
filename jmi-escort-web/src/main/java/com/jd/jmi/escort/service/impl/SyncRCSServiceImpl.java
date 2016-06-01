package com.jd.jmi.escort.service.impl;

import com.alibaba.fastjson.JSON;
import com.jd.jmi.escort.common.constant.Constants;
import com.jd.jmi.escort.constants.MqConstants;
import com.jd.jmi.escort.service.SyncRCSService;
import com.jd.jmq.client.producer.MessageProducer;
import com.jd.jmq.common.exception.JMQException;
import com.jd.jmq.common.message.Message;
import com.jd.risk.riskservice.event.JmqData;
import com.jd.risk.riskservice.event.KeyTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author baozhaonasita
 * @version 1.0
 * @date 2016/3/4
 */
@Service("syncRCSService")
public class SyncRCSServiceImpl implements SyncRCSService {

    private static final Logger logger = LoggerFactory.getLogger(SyncRCSServiceImpl.class);

    @Resource
    private MessageProducer rcsProducer;

    @Resource
    private Map<Integer, String> orderTypetoRcsRiskEvent;

    private void syncRCS(JmqData jmqData) {
        List<JmqData> jmqDatas = new ArrayList<JmqData>();
        jmqDatas.add(jmqData);
        syncRCS(jmqDatas);
    }

    private void syncRCS(List<JmqData> jmqDataList) {
        String msg = null;
        try {
            List<Message> messages = new ArrayList<Message>();
            for (JmqData data : jmqDataList) {
                Message message = new Message(MqConstants.RCSJMQ_TOPIC, data.toMessage(), data.getSourceId());
                messages.add(message);
            }
            msg = JSON.toJSONString(jmqDataList);
            logger.info("同步RCS msg={}", msg);
            rcsProducer.send(messages);
        } catch (JMQException e) {
            logger.error("同步RCS mq消息发送失败," + msg, e);
        }
    }

    @Override
    public void syncAddBlackUser(String userPin, int orderType) {
        syncAddBlackUser(userPin,new int[]{orderType});
    }

    @Override
    public void syncAddBlackUser(String userPin, int[] orderTypes) {
        List<JmqData> jmqDataList = new ArrayList<JmqData>();
        for (int i = 0; i < orderTypes.length; i++) {
            JmqData jmq = new JmqData();
            jmq.setPin(userPin);
            jmq.setType(KeyTypeEnum.USER);
            jmq.setEvent(getAddBlackUserEvent(orderTypes[i]));
            jmq.setSourceType(Constants.RCS_JMQDATA_SOURCETYPE);
            jmq.setSourceDate(new Date());
            String uuid = UUID.randomUUID().toString();
            jmq.setSourceId(uuid);

            jmqDataList.add(jmq);
        }
        syncRCS(jmqDataList);
    }

    @Override
    public void syncDeleteBlackUser(String userPin, int orderType) {
        syncDeleteBlackUser(userPin,new int[]{orderType});
    }

    @Override
    public void syncDeleteBlackUser(String userPin, int[] orderTypes) {
        List<JmqData> jmqDataList = new ArrayList<JmqData>();
        for (int i = 0; i < orderTypes.length; i++) {
            JmqData jmq = new JmqData();
            jmq.setPin(userPin);
            jmq.setType(KeyTypeEnum.USER);
            jmq.setEvent(getDeleteBlackUserEvent(orderTypes[i]));
            jmq.setSourceType(Constants.RCS_JMQDATA_SOURCETYPE);
            jmq.setSourceDate(new Date());

            String uuid = UUID.randomUUID().toString();
            jmq.setSourceId(uuid);

            jmqDataList.add(jmq);
        }
        syncRCS(jmqDataList);
    }

    @Override
    public void syncAddWhiteUser(String userPin, int orderType) {
        syncAddWhiteUser(userPin, new int[]{orderType});
    }

    @Override
    public void syncAddWhiteUser(String userPin, int[] orderTypes) {
        List<JmqData> jmqDataList = new ArrayList<JmqData>();
        for (int i = 0; i < orderTypes.length; i++) {
            JmqData jmq = new JmqData();
            jmq.setPin(userPin);
            jmq.setType(KeyTypeEnum.USER);
            jmq.setEvent(getAddWhiteUserEvent(orderTypes[i]));
            jmq.setSourceType(Constants.RCS_JMQDATA_SOURCETYPE);
            jmq.setSourceDate(new Date());
            String uuid = UUID.randomUUID().toString();
            jmq.setSourceId(uuid);

            jmqDataList.add(jmq);
        }
        syncRCS(jmqDataList);
    }

    @Override
    public void syncDeleteWhiteUser(String userPin, int orderType) {
        syncDeleteWhiteUser(userPin,new int[]{orderType});
    }

    @Override
    public void syncDeleteWhiteUser(String userPin, int[] orderTypes) {
        List<JmqData> jmqDataList = new ArrayList<JmqData>();
        for (int i = 0; i < orderTypes.length; i++) {
            JmqData jmq = new JmqData();
            jmq.setPin(userPin);
            jmq.setType(KeyTypeEnum.USER);
            jmq.setEvent(getDeleteWhiteUserEvent(orderTypes[i]));
            jmq.setSourceType(Constants.RCS_JMQDATA_SOURCETYPE);
            jmq.setSourceDate(new Date());

            String uuid = UUID.randomUUID().toString();
            jmq.setSourceId(uuid);

            jmqDataList.add(jmq);
        }
        syncRCS(jmqDataList);
    }

    private String getAddBlackUserEvent(int orderType) {
        return "Add_" + orderTypetoRcsRiskEvent.get(orderType) + "_Black";
    }

    private String getDeleteBlackUserEvent(int orderType) {
        return "Delete_" + orderTypetoRcsRiskEvent.get(orderType) + "_Black";
    }

    private String getAddWhiteUserEvent(int orderType) {
        return "Add_" + orderTypetoRcsRiskEvent.get(orderType) + "_White";
    }

    private String getDeleteWhiteUserEvent(int orderType) {
        return "Delete_" + orderTypetoRcsRiskEvent.get(orderType) + "_White";
    }
}
