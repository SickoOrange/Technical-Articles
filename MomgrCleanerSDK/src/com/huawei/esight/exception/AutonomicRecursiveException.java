package com.huawei.esight.exception;


/**
 * 一个自定义的异常，用于主动终止递归，防止无意义的递归，影响性能
 */
public class AutonomicRecursiveException extends Exception {
    public AutonomicRecursiveException(String message) {
        super(message);
    }
}
