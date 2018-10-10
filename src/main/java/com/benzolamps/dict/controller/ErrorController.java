package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.vo.ErrorVo;
import com.benzolamps.dict.util.DictObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 错误Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-5 21:34:43
 */
@RestController
@Slf4j
public class ErrorController extends BaseController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String ERROR_PATH = "/error";

    @Resource
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = "/error", produces = "text/html")
    @NavigationView
    protected ModelAndView errorHtml(HttpServletRequest request) {
        Map<String, Object> model = getErrorAttributes(request, getTraceParameter(request));
        return new ModelAndView("view/default/error", model);
    }

    @RequestMapping(value = "/error", produces = "application/json")
    @ResponseBody
    protected ResponseEntity<ErrorVo> error(HttpServletRequest request) {
        ErrorVo errorVo = convertToErrorVo(getErrorAttributes(request, getTraceParameter(request)));
        return new ResponseEntity<>(errorVo, getStatus(request));
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private ErrorVo convertToErrorVo(Map<String, Object> attributes) {
        Date timestamp = DictObject.ofObject(attributes.get("timestamp"), Date.class);
        Integer status = DictObject.ofObject(attributes.get("status"), int.class);
        String error = DictObject.ofObject(attributes.get("error"), String.class);
        String exception =  DictObject.ofObject(attributes.get("exception"), String.class);
        String message = DictObject.ofObject(attributes.get("message"), String.class);
        String trace = DictObject.ofObject(attributes.get("trace"), String.class);
        String path = DictObject.ofObject(attributes.get("path"), String.class);
        return new ErrorVo(timestamp, status, error, exception, message, trace, path);
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        return !"false".equalsIgnoreCase(request.getParameter("trace"));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), includeStackTrace);
        if (Objects.equals(NOT_FOUND, attributes.get("status"))) {
            attributes.put("message", "请求的路径不存在");
        }
        StringJoiner sj = new StringJoiner("\n");
        Stream.of("timestamp", "status", "error", "exception", "message", "path", "trace")
            .filter(attributes::containsKey)
            .map(key -> key + ": " + Objects.toString(attributes.get(key)))
            .forEachOrdered(sj::add);
        logger.error(sj.toString());
        return attributes;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (IllegalArgumentException | NullPointerException e) {
            return INTERNAL_SERVER_ERROR;
        }
    }
}