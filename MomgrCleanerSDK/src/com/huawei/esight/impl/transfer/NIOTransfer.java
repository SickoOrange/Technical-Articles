package com.huawei.esight.impl.transfer;

import com.huawei.esight.service.IFileTransfer;

import java.io.File;
import java.io.IOException;

/**
 * 使用NIO 非阻塞方式来复制文件
 */
public class NIOTransfer implements IFileTransfer {


    @Override
    public void transfer(File srcPath, File destPath) throws IOException {

    }

    @Override
    public boolean filter(String filePath) {
        return false;
    }
}
