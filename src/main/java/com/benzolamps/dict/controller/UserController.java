package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.service.base.UserService;
import com.benzolamps.dict.util.Constant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
     * @param response HttpServletResponse
     * @param session HttpSession
     * @throws IOException IOException
     * @throws TemplateException TemplateException
     */
    @RequestMapping(value = "/login.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected void login(HttpServletResponse response, HttpSession session) throws IOException, TemplateException {
        if (session.getAttribute("currentUser") != null) {
            response.sendRedirect(baseUrl + "/");
        } else {
            Template template = configuration.getTemplate("static/login.html");
            StringWriter stringWriter = new StringWriter();
            template.process(null, stringWriter);
            String html = stringWriter.toString().replaceAll(Constant.HTML_COMPRESS_PATTERN, "");
            stringWriter.close();
            response.getWriter().print(html);
        }
    }

    /**
     * 验证用户名跟密码
     * @param user 用户
     * @return 验证结果
     */
    @PostMapping("/verify.json")
    @ResponseBody
    protected boolean verify(User user, HttpSession session) {
        boolean valid = userService.verifyUser(user);
        if (valid && session.getAttribute("currentUser") == null) {
            user = userService.findByUsername(user.getUsername());
            session.setAttribute("currentUser", user);
        }
        return valid;
    }

    /**
     * 登出
     * @param session HttpSession
     * @return 登出成功
     */
    @PostMapping("/logout.json")
    @ResponseBody
    protected boolean logout(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            session.removeAttribute("currentUser");
        }
        return true;
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
