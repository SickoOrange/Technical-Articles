package com.huawei.esight.impl.handler;

import com.huawei.esight.constant.FileTransferPolicy;
import com.huawei.esight.service.IFileTransfer;
import com.huawei.esight.service.TaskHandler;
import com.huawei.esight.service.listener.OnTasksFinishedListener;
import com.huawei.esight.task.TransferTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 单线程任务处理器
 */
public class SingleThreadHandler implements TaskHandler {

    private static SingleThreadHandler singleThreadHandler = null;

    private OnTasksFinishedListener onTasksFinishedListener;

    private FileTransferPolicy transferPolicy;
    private IFileTransfer transferHandler;
    private ExecutorService executorService;


    private SingleThreadHandler() {
        executorService = Executors.newSingleThreadExecutor(new DefaultThreadFactory());
    }


    /**
     * 获取单例
     *
     * @return 单例
     */
    public static SingleThreadHandler getInstance() {
        if (singleThreadHandler == null) {
            singleThreadHandler = new SingleThreadHandler();
        }
        return singleThreadHandler;
    }

    @Override
    public void execute(List<TransferTask> tasks) {
        for (TransferTask transferTask : tasks) {
            executorService.execute(transferTask);
        }
        executorService.shutdown();

        while (true) {
            if (executorService.isTerminated()) {
                //线程池任务结束监听器！
                if (onTasksFinishedListener != null) {
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
