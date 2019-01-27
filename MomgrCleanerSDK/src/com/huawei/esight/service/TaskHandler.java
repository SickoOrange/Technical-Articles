package com.huawei.esight.service;

import com.huawei.esight.task.TransferTask;

import java.util.List;

public interface TaskHandler {

    void execute(List<TransferTask> tasks);
}
