package com.jd.watcher.util.thread;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Dept：
 * User:wanghanghang
 * Date:2016/5/9
 * Version:1.0
 */
public class Threads {
    private static volatile Manager manager = new Manager();

    public static ThreadGroupManager forGroup() {
        return manager.getThreadGroupManager("Background");
    }

    public static ThreadGroupManager forGroup(String name) {
        return manager.getThreadGroupManager(name);
    }

    public static ThreadPoolManager forPool() {
        return manager.getThreadPoolManager();
    }

    public static void addListener(ThreadListener listener) {
        manager.addListener(listener);
    }

    public static void removeListener(ThreadListener listener) {
        manager.removeListener(listener);
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private ThreadGroup threadGroup;
        private String name;
        private AtomicInteger index = new AtomicInteger();
        private Thread.UncaughtExceptionHandler m_handler;

        public DefaultThreadFactory(String name) {
            threadGroup = new ThreadGroup(name);
            this.name = name;
        }

        public DefaultThreadFactory(ThreadGroup threadGroup) {
            this.threadGroup = threadGroup;
            name = threadGroup.getName();
        }

        public String getName() {
            return name;
        }

        @Override
        public Thread newThread(Runnable r) {
            int nextIndex = index.getAndIncrement(); // always increase by one
            String threadName;

            if (r instanceof Task) {
                threadName = name + "-" + ((Task) r).getName();
            } else {
                threadName = name + "-" + nextIndex;
            }

            return new RunnableThread(threadGroup, r, threadName, m_handler);
        }

        public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            m_handler = handler;
        }
    }

    static class Manager implements Thread.UncaughtExceptionHandler {
        private Map<String, ThreadGroupManager> groupManagers = new LinkedHashMap<String, ThreadGroupManager>();
        private List<ThreadListener> listeners = new ArrayList<ThreadListener>();
        private ThreadPoolManager threadPoolManager;

        public Manager() {
            Thread shutdownThread = new Thread() {
                @Override
                public void run() {
                    shutdownAll();
                }
            };

            threadPoolManager = new ThreadPoolManager(this);
            shutdownThread.setDaemon(true);
            Runtime.getRuntime().addShutdownHook(shutdownThread);
        }

        public void addListener(ThreadListener listener) {
            listeners.add(listener);
        }

        public ThreadGroupManager getThreadGroupManager(String name) {
            ThreadGroupManager groupManager = groupManagers.get(name);

            if (groupManager == null) {
                synchronized (this) {
                    groupManager = groupManagers.get(name);

                    if (groupManager != null && !groupManager.isActive()) {
                        groupManagers.remove(name);
                        groupManager = null;
                    }

                    if (groupManager == null) {
                        groupManager = new ThreadGroupManager(this, name);
                        groupManagers.put(name, groupManager);

                        onThreadGroupCreated(groupManager.getThreadGroup(), name);
                    }
                }
            }

            return groupManager;
        }

        public ThreadPoolManager getThreadPoolManager() {
            return threadPoolManager;
        }

        public void onThreadGroupCreated(ThreadGroup group, String name) {
            for (ThreadListener listener : listeners) {
                try {
                    listener.onThreadGroupCreated(group, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void onThreadPoolCreated(ExecutorService service, String name) {
            for (ThreadListener listener : listeners) {
                try {
                    listener.onThreadPoolCreated(service, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void onThreadStarting(Thread thread, String name) {
            for (ThreadListener listener : listeners) {
                try {
                    listener.onThreadStarting(thread, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void onThreadStopped(Thread thread, String name) {
            for (ThreadListener listener : listeners) {
                try {
                    listener.onThreadStopping(thread, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void removeListener(ThreadListener listener) {
            listeners.remove(listener);
        }

        public void shutdownAll() {
            for (ThreadGroupManager manager : groupManagers.values()) {
                manager.shutdown();
            }
            threadPoolManager.shutdownAll();
        }

        @Override
        public void uncaughtException(Thread thread, Throwable e) {
            for (ThreadListener listener : listeners) {
                boolean handled = listener.onUncaughtException(thread, e);

                if (handled) {
                    break;
                }
            }
        }
    }

    static class RunnableThread extends Thread {
        private Runnable target;

        public RunnableThread(ThreadGroup threadGroup, Runnable target, String name, UncaughtExceptionHandler handler) {
            super(threadGroup, target, name);

            this.target = target;

            setDaemon(true);
            setUncaughtExceptionHandler(handler);

            if (getPriority() != Thread.NORM_PRIORITY) {
                setPriority(Thread.NORM_PRIORITY);
            }
        }

        public Runnable getTarget() {
            return target;
        }

        @Override
        public void run() {
            manager.onThreadStarting(this, getName());
            super.run();
            manager.onThreadStopped(this, getName());
        }

        public void shutdown() {
            if (target instanceof Task) {
                System.out.println("shutdown task " + ((Task) target).getName());
                ((Task) target).shutdown();
            } else {
                interrupt();
            }
        }
    }

    public static class ThreadGroupManager {
        private DefaultThreadFactory factory;
        private ThreadGroup threadGroup;
        private boolean active;

        public ThreadGroupManager(Thread.UncaughtExceptionHandler handler, String name) {
            threadGroup = new ThreadGroup(name);
            factory = new DefaultThreadFactory(threadGroup);
            factory.setUncaughtExceptionHandler(handler);
            active = true;
        }

        public boolean isActive() {
            return active;
        }

        /**
         * 等待中止
         *
         * @param time
         * @param unit
         */
        public void awaitTermination(long time, TimeUnit unit) {
            long remaining = unit.toNanos(time);

            while (remaining > 0) {
                int len = threadGroup.activeCount();
                Thread[] activeThreads = new Thread[len];
                int num = threadGroup.enumerate(activeThreads);
                boolean anyAlive = false;

                for (int i = 0; i < num; i++) {
                    Thread thread = activeThreads[i];

                    if (thread.isAlive()) {
                        anyAlive = true;
                        break;
                    }
                }

                if (anyAlive) {
                    long slice = 1000 * 1000L;

                    // wait for 1 ms
                    LockSupport.parkNanos(slice);
                    remaining -= slice;
                } else {
                    break;
                }
            }
        }

        public ThreadGroup getThreadGroup() {
            return threadGroup;
        }

        public ThreadGroupManager nonDaemon() {
            return this;
        }

        public void shutdown() {
            int len = threadGroup.activeCount();
            Thread[] activeThreads = new Thread[len];
            int num = threadGroup.enumerate(activeThreads);

            for (int i = 0; i < num; i++) {
                Thread thread = activeThreads[i];

                if (thread instanceof RunnableThread) {
                    ((RunnableThread) thread).shutdown();
                } else if (thread.isAlive()) {
                    thread.interrupt();
                }
            }

            active = false;
        }

        public Thread start(Runnable runnable) {
            return start(runnable, true);
        }

        public Thread start(Runnable runnable, boolean daemon) {
            Thread thread = factory.newThread(runnable);
            thread.setDaemon(daemon);
            thread.start();
            return thread;
        }
    }

    public static class ThreadPoolManager {
        private Thread.UncaughtExceptionHandler m_handler;

        private Map<String, ExecutorService> m_services = new LinkedHashMap<String, ExecutorService>();

        public ThreadPoolManager(Thread.UncaughtExceptionHandler handler) {
            m_handler = handler;
        }

        public ExecutorService getCachedThreadPool(String name) {
            ExecutorService service = m_services.get(name);

            if (service != null && service.isShutdown()) {
                m_services.remove(name);
                service = null;
            }

            if (service == null) {
                synchronized (this) {
                    service = m_services.get(name);

                    if (service == null) {
                        DefaultThreadFactory factory = newThreadFactory(name);
                        service = Executors.newCachedThreadPool(factory);

                        m_services.put(name, service);
                        manager.onThreadPoolCreated(service, factory.getName());
                    }
                }
            }

            return service;
        }

        public ExecutorService getFixedThreadPool(String name, int nThreads) {
            ExecutorService service = m_services.get(name);

            if (service != null && service.isShutdown()) {
                m_services.remove(name);
                service = null;
            }

            if (service == null) {
                synchronized (this) {
                    service = m_services.get(name);

                    if (service == null) {
                        DefaultThreadFactory factory = newThreadFactory(name);
                        service = Executors.newFixedThreadPool(nThreads, factory);

                        m_services.put(name, service);
                        manager.onThreadPoolCreated(service, factory.getName());
                    }
                }
            }

            return service;
        }

        DefaultThreadFactory newThreadFactory(String name) {
            DefaultThreadFactory factory = new DefaultThreadFactory(name);

            factory.setUncaughtExceptionHandler(m_handler);
            return factory;
        }

        public void shutdownAll() {
            for (ExecutorService service : m_services.values()) {
                service.shutdown();
            }

            boolean allTerminated = false;
            int count = 100;

            while (!allTerminated && count-- > 0) {
                try {
                    for (ExecutorService service : m_services.values()) {
                        if (!service.isTerminated()) {
                            service.awaitTermination(10, TimeUnit.MILLISECONDS);
                        }
                    }

                    allTerminated = true;
                } catch (InterruptedException e) {
                    // ignore it
                }
            }
        }
    }
}
