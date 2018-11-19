package com.benzolamps.dict.util;

import lombok.SneakyThrows;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

import static com.benzolamps.dict.controller.util.Constant.*;

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
                return CSS;
            case "js":
                return JS;
            case "json":
                return JSON;
            case "html":
            case "htm":
                return HTML;
            case "txt":
                return TXT;
            case "xml":
            case "dtd":
            case "xsd":
                return XML;
            case "doc":
            case "docx":
                return DOC;
            case "xls":
            case "xlsx":
                return XLS;
            case "zip":
            case "jar":
            case "war":
                return ZIP;
            default:
                return TXT;
        }
    }

    /**
     * 下载
     * @param fileName 文件名
     */
    @SneakyThrows(IOException.class)
    static void download(String fileName) {
        getResponse().setHeader("Content-disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8"));
    }

    /**
     * 获取request
     * @return HttpServletRequest
     */
    static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();;
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取response
     * @return HttpServletResponse
     */
    static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();;
        return servletRequestAttributes.getResponse();
    }
}
