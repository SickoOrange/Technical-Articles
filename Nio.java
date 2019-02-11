package com.huawei.esight.impl.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Nio {

    //https://blog.csdn.net/coslay/article/details/42062571
    public static final String src = "/Users/yayin/Downloads/Spring Boot笔记.docx";
    public static final String dest = "/Users/yayin/Desktop/Spring Boot笔记.docx";

    public static void main(String[] args) throws IOException {
        nioCopyTest1();
        nioCopyTest2();
        nioCopyTest3();
    }

    public static void delete() {
        File file = new File(dest);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 通道之间的数据传输(直接缓冲区的模式)
     * //直接缓冲不需要文件的复制拷贝 所以大大增加效率 其中第三种方法通道之间的文件传输 速度最快了直接在通道中完成
     */
    private static void nioCopyTest3() throws IOException {
        long startTime = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get(src), StandardOpenOption.READ);

        FileChannel outChennel = FileChannel.open(Paths.get(dest), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        outChennel.transferFrom(inChannel, 0, inChannel.size());

        long end = System.currentTimeMillis();
        System.out.println("nioCopyTest3耗费时间:" + (end - startTime));
        delete();
    }

    /**
     * 使用直接缓冲区完成文件的复制(内存映射文件)
     */
    private static void nioCopyTest2() throws IOException {
        long startTime = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get(src), StandardOpenOption.READ);

        FileChannel outChennel = FileChannel.open(Paths.get(dest), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        //内存映射文件(什么模式 从哪开始 到哪结束)
        MappedByteBuffer inMappeBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappeBuf = outChennel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接都缓冲区进行数据的读写操作
        byte[] dst = new byte[inMappeBuf.limit()];
        inMappeBuf.get(dst);
        outMappeBuf.put(dst);

        inChannel.close();
        outChennel.close();
        long end = System.currentTimeMillis();
        System.out.println("nioCopyTest2耗费时间:" + (end - startTime));
        delete();
    }


    /**
     * 非直接缓冲区 文件的复制
     *
     * @throws IOException
     */
    private static void nioCopyTest1() throws IOException {
        long startTime = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(new File(src));
        FileOutputStream fileOutputStream = new FileOutputStream(dest);

        //获取通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChanel = fileOutputStream.getChannel();

        //分配缓冲区的大小
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //将通道中的数据存入缓冲区
        while (inChannel.read(buf) != -1) {
            buf.flip();//切换读取数据的模式
            outChanel.write(buf);
            buf.clear();
        }
        outChanel.close();
        inChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("nioCopyTest1耗费时间:" + (end - startTime));
        delete();
    }
}