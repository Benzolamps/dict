package com.benzolamps.dict.exception;

import lombok.NoArgsConstructor;

/**
 * 自定义异常
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018年6月9日16:45:35
 */
@NoArgsConstructor
public class DictException extends RuntimeException {

    public DictException(String message) {
        super(message);
    }

    public DictException(String message, Throwable cause) {
        super(message, cause);
    }

    public DictException(Throwable cause) {
        super(cause);
    }
}
