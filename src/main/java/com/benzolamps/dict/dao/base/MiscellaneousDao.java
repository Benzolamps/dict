package com.benzolamps.dict.dao.base;

/**
 * 杂项Dao接口
 * @author Benzolamps
 * @version 2.1.1
 */
public interface MiscellaneousDao {

    /**
     * 获取MySQL版本
     * @return MySQL版本
     */
    String getMysqlVersion();

    /**
     * clear
     */
    void clear();

    /** 获取数据库总大小 */
    long dataSize();
}
