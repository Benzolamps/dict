package com.benzolamps.dict.service.base;

/**
 * 杂项Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:24:31
 */
public interface MiscellaneousService {

    /**
     * 获取SQLite版本
     * @return SQLite版本
     */
    String getSQLiteVersion();

    /**
     * 获取数据库文件的大小
     * @return 数据库文件的大小
     */
    String databaseFileSize();

    /**
     * vacuum
     */
    void clean();
}
