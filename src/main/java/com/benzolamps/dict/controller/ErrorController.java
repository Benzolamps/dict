package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.interceptor.NavigationView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 错误控制器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-5 21:34:43
 */
@Controller
@Slf4j
public class ErrorController extends BaseController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String ERROR_PATH = "/error";

    @Resource
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = "/error", produces = "text/html")
    @NavigationView
    protected ModelAndView errorHtml(HttpServletRequest request) {
        getErrorAttributes(request, getTraceParameter(request));
        return new ModelAndView("view/default/error", getErrorAttributes(request, getTraceParameter(request)));
    }

    @RequestMapping(value = "/error", produces = "application/json")
    @ResponseBody
    protected ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        return new ResponseEntity<>(getErrorAttributes(request, getTraceParameter(request)), getStatus(request));
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        return !"false".equalsIgnoreCase(request.getParameter("trace"));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        return errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), includeStackTrace);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (IllegalArgumentException | NullPointerException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}