package com.huawei.esight.service.listener;

import java.io.File;

/**
 * 一个可以过滤传统网元包mo_typ目录的监听器
 * 用于在文件清洗的时候决定是否保留mo_type目录下的文件或者文件夹，
 * 例如配置管理云化网元包只需要保留mo_type目录下的
 * conffilemgr包下的所有文件即可
 *
 * 默认情况下在MomgrRebuilder中不设置此监听器，将默认实现
 * 配置管理的清洗逻辑
 *
 * 可自定义哪些文件夹需要被保留
 */
public interface OnMoFeaturePackageScannedListener {
    boolean shouldSurvival(File file);

}
