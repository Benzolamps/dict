package com.benzolamps.dict.dao.base;

import org.intellij.lang.annotations.Language;

import java.io.InputStream;

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
     * 获取MySQL所在路径
     * @return MySQL所在路径
     */
    String getMysqlBaseDir();

    /**
     * clear
     */
    void clear();

    /**
     * 获取数据库总大小
     */
    long dataSize();

    /**
     * 执行SQL脚本
     * @param sql SQL
     */
    void executeSqlScript(@Language("MySQL") String sql);

    /**
     * 执行SQL脚本
     * @param sqlStream SQL输入流
     */
    void executeSqlScript(InputStream sqlStream);
}
