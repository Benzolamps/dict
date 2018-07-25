package com.benzolamps.dict.util;

/**
 * 资源类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 20:20:17
 */
public enum ResourceType {

    /** 传入的字符串当作ClassPath路径处理 */
    CLASS_PATH,

    /** 传入的字符串当作系统资源路径处理 */
    FILE_SYSTEM,

    /** 传入的字符串当作网络URL处理 */
    URL,

    /** 传入的字符串当作YAML文本处理 */
    STRING
}
