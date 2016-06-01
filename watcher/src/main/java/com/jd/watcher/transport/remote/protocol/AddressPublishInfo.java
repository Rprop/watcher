package com.jd.watcher.transport.remote.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 获取要发送的主机
 *
 * @since 2013-7-24
 */
public class AddressPublishInfo {
    private List<String> addressList = new ArrayList<String>();
    private AtomicInteger sendWhichQueue = new AtomicInteger(0);

    public AddressPublishInfo(List<String> addressList) {
        this.addressList = addressList;
    }


    public boolean ok() {
        return null != this.addressList && !this.addressList.isEmpty();
    }

    public String selectOneAddress(final String lastBrokerName) {
        if (lastBrokerName != null) {
            int index = this.sendWhichQueue.getAndIncrement();
            for (int i = 0; i < this.addressList.size(); i++) {
                int pos = Math.abs(index++) % this.addressList.size();
                String address = this.addressList.get(pos);
                if (!address.equals(lastBrokerName)) {
                    return address;
                }
            }

            return null;
        } else {
            int index = this.sendWhichQueue.getAndIncrement();
            int pos = Math.abs(index) % this.addressList.size();
            return this.addressList.get(pos);
        }
    }

    @Override
    public String toString() {
        return "AddressPublishInfo{" +
                "addressList=" + addressList +
                ", sendWhichQueue=" + sendWhichQueue +
                '}';
    }
}
