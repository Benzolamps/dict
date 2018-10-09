package com.benzolamps.dict.service.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据库备份恢复Service接口
 * @author Benzolamps
 * @version 2.2.1
 * @datetime 2018-10-9 18:48:47
 */
public interface BackupService {

    /**
     * 备份数据库
     * @param outputStream 输出流
     * @throws IOException IOException
     */
    void backup(OutputStream outputStream) throws IOException;

    @SuppressWarnings("SpellCheckingInspection")
    void restore(InputStream inputStream) throws IOException;
}
