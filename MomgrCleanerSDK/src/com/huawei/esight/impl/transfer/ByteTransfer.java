package com.huawei.esight.impl.transfer;

import com.huawei.esight.service.IFileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteTransfer implements IFileTransfer {
    @Override
    public void transfer(File srcPath, File destPath) throws IOException {
        FileInputStream fis = new FileInputStream(srcPath);
        FileOutputStream fos = new FileOutputStream(destPath);

        //3、声明字节数组，每次按1K读取，按1K输出
        byte[] bytes = new byte[1024];

        int temp = 0;
        while ((temp = fis.read(bytes)) != -1) {
            fos.write(bytes, 0, temp);
        }
        fos.flush();

        //5、关闭流
        fis.close();
        fos.close();
    }

    @Override
    public boolean filter(String filePath) {
        return false;
    }
}
