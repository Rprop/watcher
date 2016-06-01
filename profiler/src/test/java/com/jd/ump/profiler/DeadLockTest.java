package com.jd.ump.profiler;

import com.jd.ump.log4j.Logger;
import com.jd.ump.profiler.proxy.Profiler;
import com.jd.ump.profiler.util.CustomLogFactory;

public class DeadLockTest {

    public static void main(String[] args) throws InterruptedException {
        CallerInfo cc = Profiler.registerInfo("test", true, true);
        new Thread(new Runnable() {
            public void run() {
                CustomLogFactory.getLogger("tpLogger").info(new DeadLockTest());
            }
        }).start();

        Profiler.registerInfoEnd(cc);

        DeadLockingObject.getInstance();

        Thread.sleep(200L);
    }

    public String toString() {
        /* now we are inside log4j, try to create a DeadLockingObject */
        DeadLockingObject.getInstance();
        return "Created DeadlockObject, returning string";
    }
}

final class DeadLockingObject {
    private static final Logger LOG = CustomLogFactory.getLogger("tpLogger");
    private static DeadLockingObject singleton = new DeadLockingObject();

    private DeadLockingObject() {
    }

    public synchronized static DeadLockingObject getInstance() {
        try {
            // to make the deadlock occur
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        LOG.info("Returning nice singleton");
        return singleton;

    }
}