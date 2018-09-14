package com.benzolamps.dict.service.base;

/**
 * 版本Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-14 21:13:30
 */
public interface VersionService {

    void update();

    enum Status {
        ALREADY_NEW,
        HAS_NEW,
        DOWNLOADING,
        COPYING,
        DELETING,
        FINISHED,
        FAILED
    }

    Status getStatus();

    String getTotalSize();

    long getDeltaTime();

    String getNewVersionName();

    int getTotal();
}
