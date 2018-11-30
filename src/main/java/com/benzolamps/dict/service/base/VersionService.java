package com.benzolamps.dict.service.base;

import java.util.function.Consumer;

/**
 * 版本Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-14 21:13:30
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface VersionService {

    /**
     * 开始更新
     */
    void update();

    enum Status {
        /** 已是最新 */
        ALREADY_NEW,

        /** 有新版本 */
        HAS_NEW,

        /** 正在下载 */
        DOWNLOADING,

        /** 下载完成 */
        DOWNLOADED,

        /** 安装完成 */
        INSTALLED,

        /** 更新失败 */
        FAILED
    }

    /**
     * 获取当前状态
     * @return 状态
     */
    Status getStatus();

    /**
     * 获取总大小
     * @return 总大小
     */
    String getTotalSize();

    /**
     * 获取总时长
     * @return 总时长
     */
    long getDeltaTime();

    /**
     * 获取新版本号
     * @return 版本号
     */
    String getNewVersionName();

    /**
     * 获取文件总数
     * @return 文件总数
     */
    int getTotal();

    /**
     * 设置状态回调
     * @param callback 回调
     */
    void setStatusCallback(Consumer<Status> callback);

    /**
     * 死
     */
    void die();

    /**
     * @return 死了吗
     */
    boolean isDead();
}
