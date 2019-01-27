package com.huawei.esight.exception;

import com.huawei.esight.constant.BaseFolderStatus;

public class ExceptionFactory {

    // TODO: 2019-01-27 根据返回的跟目录异常生产对应的异常信息
    public static IllegalArgumentException getIllegalArgumentException(BaseFolderStatus exceptionType) {


        return new IllegalArgumentException();
    }
}
