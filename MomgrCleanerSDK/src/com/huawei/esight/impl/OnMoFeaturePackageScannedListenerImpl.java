package com.huawei.esight.impl;

import com.huawei.esight.constant.Constant;
import com.huawei.esight.service.listener.OnMoFeaturePackageScannedListener;

import java.io.File;

/**
 * 配置文件管理云化下的默认实现的过滤器，只过滤父文件夹为mo_feature下所有文件夹不为configfilemgr的文件夹
 */
public class OnMoFeaturePackageScannedListenerImpl implements OnMoFeaturePackageScannedListener {
    @Override
    public boolean shouldSurvival(File file) {
        return Constant.CONF_FILE_MGR.equals(file.getName());
    }
}
