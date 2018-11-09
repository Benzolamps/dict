package com.benzolamps.dict.exception;

/**
 * 学习进度导入异常
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-7 14:50:38
 */
public class ProcessImportException extends DictException {

    private static final long serialVersionUID = -1415927407807314732L;

    /**
     * 构造器
     * @param name 文件名
     * @param cause 原因
     */
    public ProcessImportException(String name, String msg, Throwable cause) {
        super(
            "导入 " + name + " 时出现异常，" +
            cause.getClass().getName() +
            (msg != null ? "：" + msg : "") +
            (cause.getMessage() != null ? "：" + cause.getMessage() : ""),
            cause
        );
    }
}
