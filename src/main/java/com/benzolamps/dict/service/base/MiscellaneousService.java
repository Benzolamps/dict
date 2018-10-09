package com.benzolamps.dict.service.base;

import java.io.IOException;
import java.io.OutputStream;

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

    void backup(OutputStream outputStream) throws IOException;
}
