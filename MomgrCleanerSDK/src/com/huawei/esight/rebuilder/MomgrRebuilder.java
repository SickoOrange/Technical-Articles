package com.huawei.esight.rebuilder;

import com.huawei.esight.exception.ExceptionFactory;
import com.huawei.esight.constant.BaseFolderStatus;
import com.huawei.esight.constant.Constant;
import com.huawei.esight.constant.FileTransferPolicy;
import com.huawei.esight.manager.TransferManager;
import com.huawei.esight.service.listener.OnConfigFileScannedListener;
import com.huawei.esight.service.listener.OnMoFeaturePackageScannedListener;
import com.huawei.esight.service.listener.OnTasksFinishedListener;

import java.io.File;

public class MomgrRebuilder {
    private String momgrBase;
    private OnConfigFileScannedListener onConfigFileScannedListener;
    private OnMoFeaturePackageScannedListener onMoFeaturePackageScannedListener;
    private boolean enableThreadPool;
    private FileTransferPolicy transferPolicy;
    private OnTasksFinishedListener onTasksFinishedListener;

    public MomgrRebuilder(MomgrBuilder builder) {
        this.momgrBase = builder.momgrBase;
        this.onConfigFileScannedListener = builder.onConfigFileScannedListener;
        this.onMoFeaturePackageScannedListener = builder.onMoFeaturePackageScannedListener;
        this.enableThreadPool = builder.enableThreadPool;
        this.transferPolicy = builder.transferPolicy;
        this.onTasksFinishedListener = builder.onTasksFinishedListener;

    }

    public static MomgrBuilder Builder() {
        return new MomgrBuilder();
    }

    public void rebuild() {

        //根目录参数不存在
        if (this.getMomgrBase() == null || "".equals(this.getMomgrBase())) {
            throw ExceptionFactory.getIllegalArgumentException(BaseFolderStatus.NULL);
        }

        //根目录不存在
        File baseFile = new File(momgrBase);
        if (!baseFile.exists()) {
            throw ExceptionFactory.getIllegalArgumentException(BaseFolderStatus.NON_EXIT);
        }

        //根目录非法
        if (!Constant.BASE_NAME.equals(baseFile.getName())) {
            throw ExceptionFactory.getIllegalArgumentException(BaseFolderStatus.ILLEGAL);
        }
        //根目录为空
        if (baseFile.listFiles() == null) {
            throw ExceptionFactory.getIllegalArgumentException(BaseFolderStatus.EMPTY);
        }

        //根目录是一个文件
        if (baseFile.isFile()) {
            throw ExceptionFactory.getIllegalArgumentException(BaseFolderStatus.FILE);
        }
        TransferManager transferManager = new TransferManager(this);
        transferManager.setOnMoFeaturePackageScannedListener(getOnMoFeaturePackageScannedListener());
        transferManager.doRebuild(baseFile);
    }


    public static class MomgrBuilder {

        private String momgrBase;
        private OnConfigFileScannedListener onConfigFileScannedListener;
        private OnMoFeaturePackageScannedListener onMoFeaturePackageScannedListener;
        private boolean enableThreadPool;
        private FileTransferPolicy transferPolicy;
        private OnTasksFinishedListener onTasksFinishedListener;

        public MomgrBuilder setMomgrBase(String momgrBase) {
            this.momgrBase = momgrBase;
            return this;
        }

        public MomgrBuilder setOnConfigFileScannedListener(OnConfigFileScannedListener onConfigFileScannedListener) {
            this.onConfigFileScannedListener = onConfigFileScannedListener;
            return this;
        }

        public MomgrBuilder setOnMoFeaturepackageScannedListener(OnMoFeaturePackageScannedListener onMoFeaturePackageScannedListener) {
            this.onMoFeaturePackageScannedListener = onMoFeaturePackageScannedListener;
            return this;
        }

        public MomgrRebuilder builder() {
            return new MomgrRebuilder(this);
        }

        public MomgrBuilder enableThreadPool(boolean enableThreadPool) {
            this.enableThreadPool = enableThreadPool;
            return this;
        }

        public MomgrBuilder setTransferPolicy(FileTransferPolicy policy) {
            this.transferPolicy = policy;
            return this;
        }

        public MomgrBuilder setOnTaskFinishedListener(OnTasksFinishedListener onTasksFinishedListener) {
            this.onTasksFinishedListener = onTasksFinishedListener;
            return this;
        }
    }

    public String getMomgrBase() {
        return momgrBase;
    }

    public OnConfigFileScannedListener getOnConfigFileScannedListener() {
        return onConfigFileScannedListener;
    }

    public OnMoFeaturePackageScannedListener getOnMoFeaturePackageScannedListener() {
        return onMoFeaturePackageScannedListener;
    }

    public boolean isEnableThreadPool() {
        return enableThreadPool;
    }

    public FileTransferPolicy getTransferPolicy() {
        return transferPolicy;
    }

    public OnTasksFinishedListener getOnTasksFinishedListener() {
        return onTasksFinishedListener;
    }
}
