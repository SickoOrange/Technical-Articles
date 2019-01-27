package com.huawei.esight.task;

import com.huawei.esight.constant.FileTransferPolicy;
import com.huawei.esight.impl.transfer.TransferHandlerUtils;
import com.huawei.esight.service.IFileTransfer;

import java.io.File;
import java.io.IOException;


/**
 * 文件复制任务包装类
 */
public class TransferTask implements Runnable {

    private File src;
    private File dest;
    private IFileTransfer fileTransfer;


    public TransferTask(File src, File dest, FileTransferPolicy transferPolicy) {
        this.src = src;
        this.dest = dest;
        fileTransfer = TransferHandlerUtils.gethandler(transferPolicy);
    }


    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()
                    + " process task: " + dest.getAbsolutePath());
            fileTransfer.transfer(src, dest);
        } catch (IOException e) {
            System.out.println("an Error occurred while transfer the file: " + src.getAbsolutePath());
            e.printStackTrace();
        }

    }
}
