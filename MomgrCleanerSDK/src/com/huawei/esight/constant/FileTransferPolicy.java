package com.huawei.esight.constant;


/**
 * 文件复制策略
 */
public enum FileTransferPolicy {
    /**
     * 字节流
     */
    BYTE("byte"),
    /**
     * buffer
     */
    BUFFER("buffer"),
    /**
     * channel
     */
    CHANNEL("channel"),
    /**
     * nio
     */
    NIO("nio"),
    ;
    String value;

    FileTransferPolicy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }}
