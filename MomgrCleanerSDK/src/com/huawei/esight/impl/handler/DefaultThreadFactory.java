package com.huawei.esight.impl.handler;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    DefaultThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();//取得线程组
        namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "- transfer thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        //设置当前线程组根通用线程名
        Thread worker = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (worker.isDaemon()) {
            worker.setDaemon(false);
        }
        if (worker.getPriority() != Thread.NORM_PRIORITY) {
            //使用正常优先级
            worker.setPriority(Thread.NORM_PRIORITY);
        }
        return worker;
    }
}
