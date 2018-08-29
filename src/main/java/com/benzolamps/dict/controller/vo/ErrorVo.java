package com.benzolamps.dict.controller.vo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 错误Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-29 09:08:35
 */
@Getter
@Slf4j
public class ErrorVo extends BaseVo {

    /** 时间戳 */
    private final Date timestamp;

    /** 错误 */
    private final String error;

    /** 异常 */
    private final String exception;

    /** 异常栈信息 */
    private final String trace;

    /** 访问路径 */
    private final String path;

    /**
     * 构造器
     * @param timestamp 时间戳
     * @param status 状态码
     * @param error 错误
     * @param exception 异常
     * @param message 提示信息
     * @param trace 异常栈信息
     * @param path 访问路径
     */
    public ErrorVo(Date timestamp, Integer status, String error, String exception, String message, String trace, String path) {
        super(status, message);
        this.timestamp = timestamp;
        this.error = error;
        this.exception = exception;
        this.trace = trace;
        this.path = path;
        String log = new StringJoiner("\n")
            .add("timestamp: " + timestamp)
            .add("status: " + status)
            .add("error: " + error)
            .add("exception: " + exception)
            .add("message:" + message)
            // .add("trace: "+ trace)
            .add("path: " + path)
            .toString();
        logger.error(log);
    }
}
