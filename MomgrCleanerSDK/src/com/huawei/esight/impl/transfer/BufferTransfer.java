package com.huawei.esight.impl.transfer;

import com.huawei.esight.service.IFileTransfer;

import java.io.*;

public class BufferTransfer implements IFileTransfer {
    @Override
    public void transfer(File src, File dest) throws IOException {
        if (!filter(src.getCanonicalPath())) return;

        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileReader = new FileReader(src);
            fileWriter = new FileWriter(dest);
            bufferedReader = new BufferedReader(fileReader);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fileReader) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean filter(String filePath) {
        if (filePath == null || "".equals(filePath)) return false;
        if (filePath.contains("extension.xml")) return true;
        if (filePath.contains("mo_type")) return true;
        return filePath.contains("configfilemgr");

    }
}
