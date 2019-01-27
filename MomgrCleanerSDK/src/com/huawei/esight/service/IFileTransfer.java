package com.huawei.esight.service;

import java.io.File;
import java.io.IOException;

public interface IFileTransfer {

    /**
     * 将对应目录的所有子目录跟子文件递归的复制到指定的路径
     * @param srcPath 源文件目录
     * @param destPath 指定目录
     * @throws IOException IO 异常
     */
    public void transfer(File srcPath, File destPath) throws IOException;


    /**
     * 当前文件路径
     * @param filePath 当前文件路径
     * @return 当前文件是否符合所指定的过滤规则
     */
    public boolean filter(String filePath);
}
