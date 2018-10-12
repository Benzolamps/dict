package com.benzolamps.dict.service.base;

import org.intellij.lang.annotations.Language;

import java.io.InputStream;

/**
 * 杂项Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 11:24:31
 */
public interface MiscellaneousService {

    /**
     * 获取MySQL版本
     * @return MySQL版本
     */
    @SuppressWarnings("unused")
    String getMysqlVersion();

    /**
     * 获取数据库文件的大小
     * @return 数据库文件的大小
     */
    String databaseFileSize();

    /**
     * clean
     */
    void clean();

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
