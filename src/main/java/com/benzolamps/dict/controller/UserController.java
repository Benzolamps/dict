package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.service.base.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

/**
 * 用户Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-30 21:44:45
 */
@RestController
@RequestMapping("user/")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private Configuration configuration;

    /**
     * 登录界面
     */
    @ResponseBody
    @RequestMapping(value = "/login.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected String login(HttpServletResponse response) throws IOException, TemplateException {
        Template template = configuration.getTemplate("static/login.html");
        StringWriter stringWriter = new StringWriter();
        template.process(null, stringWriter);
        return stringWriter.toString().replaceAll("^\\s+|\\s+$|\\n|\\r", "");
    }

    /**
     * 验证用户名跟密码
     * @param user 用户
     * @return 验证结果
     */
    @PostMapping("/verify.json")
    @ResponseBody
    protected boolean verify(User user) {
        return userService.verifyUser(user);
    }

    /**
     * 检测是否存在此用户
     * @param username 用户名
     * @return 检测结果
     */
    @RequestMapping(value = "/username_exists.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected boolean usernameExists(String username) {
        return userService.usernameExists(username);
    }
}
