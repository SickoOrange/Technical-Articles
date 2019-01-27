package com.huawei.esight;

import com.huawei.esight.constant.FileTransferPolicy;
import com.huawei.esight.impl.OnConfigFileScannedListenerImpl;
import com.huawei.esight.impl.OnMoFeaturePackageScannedListenerImpl;
import com.huawei.esight.rebuilder.MomgrRebuilder;
import com.huawei.esight.service.listener.OnTasksFinishedListener;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        long startTime = System.currentTimeMillis();
        MomgrRebuilder reBuilder = MomgrRebuilder.Builder()
                //必选
                //网元包根目录
                .setMomgrBase("/Users/yayin/Desktop/momgr")
                //可选
                //监听配置文件，当你需要修改xml文件的内容可以实现这个监听器
                .setOnConfigFileScannedListener(new OnConfigFileScannedListenerImpl())
                //必选
                //监听Mo_feature目录，当你需要过滤mo_feature下不需要的文件或文件夹 可以实现这个监听器
                .setOnMoFeaturepackageScannedListener(new OnMoFeaturePackageScannedListenerImpl())
                //可选
                //是否开启多线程（优化清洗速度）默认单线程
                .enableThreadPool(true)
                //可选
                //选择文件复制策略 （优化清洗速度）
                .setTransferPolicy(FileTransferPolicy.BYTE)
                //可选，当线程池所有任务完成，回掉此接口
                .setOnTaskFinishedListener(() -> {
                    long finishedTime = System.currentTimeMillis();
                    System.out.println((finishedTime - startTime)+" ms");
                })
                .builder();
        //执行清洗
        reBuilder.rebuild();

    }
}
