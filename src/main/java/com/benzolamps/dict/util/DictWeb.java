package com.benzolamps.dict.util;

/**
 * Web工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-8 00:19:37
 */
public interface DictWeb {

    /**
     * 将URL后缀转换为content type
     * @param extension 后缀
     * @return content type
     */
    static String convertContentType(String extension) {
        switch (extension.toLowerCase()) {
            case "css":
                return "text/css";
            case "js":
                return "text/javascript";
            case "json":
                return "application/json";
            case "html": case "htm":
                return "text/html";
            case "txt":
                return "text/plain";
            case "xml": case "dtd": case "xsd":
                return "text/xml";
            case "doc": case "docx":
                return "application/msword";
            case "xls": case "xlsx":
                return "application/x-xls";
            default:
                return "text/html";
        }
    }
}
