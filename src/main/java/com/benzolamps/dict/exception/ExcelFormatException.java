package com.benzolamps.dict.exception;

/**
 * Excel格式错误
 * @author Benzolamps
 * @version 1.1.6
 * @datetime 2018-7-1 21:12:35
 */
public class ExcelFormatException extends DictException {

    private static final long serialVersionUID = -120019519237553699L;

    /**
     * @param message 错误消息
     * @param rowNum 行号
     * @param columnNum 列号
     */
    public ExcelFormatException(String message, int rowNum, int columnNum) {
        super("导入失败，在第 " + (rowNum + 1) + " 行，第 " + (columnNum + 1) + " 列发生错误，" + message);
    }

    /**
     * @param cause 原因
     * @param rowNum 行号
     * @param columnNum 列号
     */
    public ExcelFormatException(Throwable cause, int rowNum, int columnNum) {
        super("导入失败，在第 " + (rowNum + 1) + " 行，第 " + (columnNum + 1) + " 列发生错误，" + cause.getClass().getName() + "：" + cause.getMessage(), cause);
    }
}
