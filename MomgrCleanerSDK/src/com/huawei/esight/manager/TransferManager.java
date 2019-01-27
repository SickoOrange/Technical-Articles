package com.huawei.esight.manager;

import com.huawei.esight.exception.AutonomicRecursiveException;
import com.huawei.esight.constant.Constant;
import com.huawei.esight.impl.handler.MultiThreadHandler;
import com.huawei.esight.impl.handler.SingleThreadHandler;
import com.huawei.esight.service.listener.OnMoFeaturePackageScannedListener;
import com.huawei.esight.rebuilder.MomgrRebuilder;
import com.huawei.esight.task.TransferTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 网元包清理逻辑管理器
 * 主要负责文件夹的遍历跟线程任务的创建
 */
public class TransferManager {
    private File baseOMS;

    private MomgrRebuilder momgrRebuilder;

    private OnMoFeaturePackageScannedListener onMoFeaturePackageScannedListener;

    private List<TransferTask> tasks = new ArrayList<>();

    public TransferManager(MomgrRebuilder momgrRebuilder) {
        this.momgrRebuilder = momgrRebuilder;

    }


    public void doRebuild(File base) {
        File[] files = base.listFiles();
        if (files == null) return;
        for (File file : files) {
            //递归搜索momgr目录下所有的类型网元包中的oms文件夹
            recursiveSearch(file);
            try {
                Optional<File> destBaseOptional = generateDestBase(momgrRebuilder);
                if (destBaseOptional.isPresent()) {
                    //准备创建任务
                    prepareTransferTask(baseOMS, destBaseOptional.get());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //执行所有任务
        executeTasks();
    }

    private void executeTasks() {
        if (momgrRebuilder.isEnableThreadPool()) {
            //注册程池任务结束监听器
            MultiThreadHandler.getInstance().setOnTasksFinishedListener(momgrRebuilder.getOnTasksFinishedListener());
            //开启多线程任务
            MultiThreadHandler.getInstance().execute(tasks);
        } else {
            //注册程池任务结束监听器
            SingleThreadHandler.getInstance().setOnTasksFinishedListener(momgrRebuilder.getOnTasksFinishedListener());
            //使用单线程执行任务
            SingleThreadHandler.getInstance().execute(tasks);

        }
    }


    /**
     * 创建清洗目录的根目录
     *
     * @param momgrRebuilder 源信息
     * @return 返回一个包裹清洗目录文件的Optional对象
     */
    private Optional<File> generateDestBase(MomgrRebuilder momgrRebuilder) throws IOException {
        String momgrBase = momgrRebuilder.getMomgrBase();
        int index = momgrBase.lastIndexOf(File.separator);
        if (index != -1) {


            String momgrPrefix = momgrBase.substring(0, index);
            String momgr = momgrBase.substring(index + 1);
            String momgrType = baseOMS.getParentFile().getName();

            File destFile = new File(new File(momgrPrefix, Constant.DEST_BASE), momgr + File.separator + momgrType);
            if (!destFile.exists()) {
                if (!destFile.mkdirs()) {
                    System.out.println("create destination base folder failed " + destFile.getCanonicalPath());
                }
            }

            return Optional.of(destFile);
        }
        return Optional.empty();

    }


    /**
     * 递归的查找OMS文件夹所在的位置，传统的网元配置包的所有配置文件都保存在OMS文件夹下
     *
     * @param base 网元包根目录
     */
    private void recursiveSearch(File base) {

        File[] files = base.listFiles();
        // TODO: 2019-01-27 是否是无意义的判断
        if (files == null) {
            return;
        }
        try {
            for (File file : files) {
                if (file.getName().equals(Constant.OMS.toLowerCase())) {
                    baseOMS = file;
                    //主动结束递归，防止无意义的递归调用
                    throw new AutonomicRecursiveException("rebuilding " +base.getName()+
                            " terminate the meaningless recursive search");
                }
                recursiveSearch(file);
            }
        } catch (AutonomicRecursiveException e) {
            System.out.println(e.getMessage());
        }
    }


    private void prepareTransferTask(File baseOMS, File destBase) throws IOException {
        File[] files = baseOMS.listFiles();

        //只有在oms目录不为空的情况下才处理
        if (files == null || files.length <= 0) return;

        if (!destBase.exists() && !destBase.mkdirs()) {
            System.out.println("试图创建文件(夹)失败：" + destBase.getCanonicalPath());
        }

        for (File file : files) {
            if (!file.isDirectory()) {
                createTransferTask(file, new File(destBase, file.getName()));
            } else {
                if (!shouldSpecialProcess(file)) {
                    prepareTransferTask(file, new File(destBase, file.getName()));
                }
            }
        }

    }

    /**
     * 创建任务
     *
     * @param src  源文件
     * @param dest 目标文件
     */
    private void createTransferTask(File src, File dest) {
        TransferTask task = new TransferTask(src, dest, momgrRebuilder.getTransferPolicy());
        tasks.add(task);
    }

    private boolean shouldSpecialProcess(File file) {
        if (Constant.MO_FEARTURE.equals(file.getParentFile().getName())) {
            // 监听器优先级大于配置文件云化（配置云化只需要保留mo_feature下的conffilemgr包即可）
            if (onMoFeaturePackageScannedListener != null) {
                return !onMoFeaturePackageScannedListener.shouldSurvival(file);
            }else {
                //默认实现配置管理的清理
                return !Constant.CONF_FILE_MGR.equals(file.getName());
            }

        }
        return false;
    }


    public void setOnMoFeaturePackageScannedListener(OnMoFeaturePackageScannedListener
                                                             onMoFeaturePackageScannedListener) {
        if (onMoFeaturePackageScannedListener != null) {
            this.onMoFeaturePackageScannedListener = onMoFeaturePackageScannedListener;
        }
    }
}
