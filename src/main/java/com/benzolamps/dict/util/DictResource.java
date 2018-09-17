package com.benzolamps.dict.util;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * 资源工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-15 21:48:50
 */
@SuppressWarnings("unused")
@Slf4j
public final class DictResource {

    /**
     * 关闭可关闭资源
     * @param closeable 可关闭资源
     */
    public static void closeCloseable(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
    }

    /**
     * 关闭可关闭资源
     * @param closeable 可关闭资源
     */
    public static void closeCloseable(AutoCloseable closeable) {
        try {
            if (closeable != null) closeable.close();
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
    }

}
