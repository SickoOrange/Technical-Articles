package com.huawei.esight.impl.handler;

import com.huawei.esight.service.TaskHandler;
import com.huawei.esight.service.listener.OnTasksFinishedListener;
import com.huawei.esight.task.TransferTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程任务处理器
 */
public class MultiThreadHandler implements TaskHandler {

    private static MultiThreadHandler multiThreadHandler = null;

    private OnTasksFinishedListener onTasksFinishedListener;

    private ExecutorService executorService;

    private List<Future<String>> resultSet = new ArrayList<>();


    private MultiThreadHandler() {
        //初始化线程池，线程数量由JVM决定
        executorService = Executors.newCachedThreadPool(new DefaultThreadFactory());
    }


    /**
     * 获取单例
     *
     * @return 单例
     */
    public static MultiThreadHandler getInstance() {
        if (multiThreadHandler == null) {
            multiThreadHandler = new MultiThreadHandler();
        }
        return multiThreadHandler;
    }


    @Override
    public void execute(List<TransferTask> tasks) {

        for (TransferTask transferTask : tasks) {
            executorService.execute(transferTask);
        }
        //提交完 线程池不再接受先来的任务！
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                //线程池任务结束监听器！
                if (onTasksFinishedListener!=null){
                    onTasksFinishedListener.tasksCompleted();
                }
                break;
            }
        }

    }

    public void setOnTasksFinishedListener(OnTasksFinishedListener onTasksFinishedListener) {
        this.onTasksFinishedListener = onTasksFinishedListener;
    }
}
