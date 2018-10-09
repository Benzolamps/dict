package com.benzolamps.dict.util;

import com.benzolamps.dict.controller.util.Constant;
import org.springframework.util.Assert;

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
        Assert.hasText(extension, "extension不能为null或空");
        switch (extension.toLowerCase()) {
            case "css":
                return Constant.CSS;
            case "js":
                return Constant.JS;
            case "json":
                return Constant.JSON;
            case "html": case "htm":
                return Constant.HTML;
            case "txt":
                return Constant.TXT;
            case "xml": case "dtd": case "xsd":
                return Constant.XML;
            case "doc": case "docx":
                return Constant.DOC;
            case "xls": case "xlsx":
                return Constant.XLS;
            case "zip": case "jar": case "war":
                return Constant.ZIP;
            default:
                return Constant.TXT;
        }
    }
}
