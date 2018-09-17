package com.benzolamps.dict.exception;

/**
 * 自定义异常
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018年6月9日16:45:35
 */
@SuppressWarnings("unused")
public class DictException extends RuntimeException {

    private static final long serialVersionUID = 6844909847465796221L;

    public DictException() {
        super();
    }

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
