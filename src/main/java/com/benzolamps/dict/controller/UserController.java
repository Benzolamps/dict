package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.User;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.service.base.UserService;
import com.benzolamps.dict.util.Constant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
     * @param response HttpServletResponse
     * @throws IOException IOException
     * @throws TemplateException TemplateException
     */
    @RequestMapping(value = "/login.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected void login(HttpServletResponse response) throws IOException, TemplateException {
        if (userService.getCurrent() != null) {
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
    protected boolean verify(@Validated(User.LoginGroup.class) User user) {
        boolean valid = userService.verifyUser(user);
        if (valid && userService.getCurrent() == null) {
            user = userService.findByUsername(user.getUsername());
            userService.setCurrent(user);
        }
        return valid;
    }

    /**
     * 注销登录
     * @return 注销登录成功
     */
    @PostMapping("/logout.json")
    @ResponseBody
    protected boolean logout() {
        if (userService.getCurrent() != null) {
            userService.setCurrent(null);
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

    /**
     * 验证密码
     * @param password 密码
     * @return 验证结果
     */
    @RequestMapping(value = "/check_password.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected boolean checkPassword(@RequestParam("oldPassword") String password) {
        User user = userService.getCurrent();
        if (user == null) {
            return false;
        } else {
            return userService.verifyUser(new User(user.getUsername(), password));
        }
    }

    /**
     * 修改密码界面
     * @return 界面
     */
    @WindowView
    @RequestMapping(value = "/edit_password.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView editPassword() {
        ModelAndView mv = new ModelAndView();
        User user = userService.getCurrent();
        if (user == null) {
            mv.setViewName("redirect:login.html");
        } else {
            mv.setViewName("view/user/password");
        }
        return mv;
    }

    /**
     * 保存修改密码
     * @param password 密码
     * @return 修改密码成功
     */
    @PostMapping("/save_password.json")
    @ResponseBody
    protected BaseVo savePassword(@RequestParam("newPassword") String password) {
        User user = userService.getCurrent();
        userService.savePassword(user, password);
        return wrapperMsg("success");
    }

    /**
     * 锁屏界面
     * @return 界面
     */
    @WindowView
    @RequestMapping(value = "/lock_screen.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView lockScreen() {
        ModelAndView mv = new ModelAndView();
        User user = userService.getCurrent();
        if (user == null) {
            mv.setViewName("redirect:login.html");
        } else {
            mv.setViewName("view/user/lock_screen");
        }
        return mv;
    }

    /**
     * 个人资料界面
     * @return 界面
     */
    @WindowView
    @RequestMapping(value = "/profile.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView profile() {
        ModelAndView mv = new ModelAndView();
        User user = userService.getCurrent();
        if (user == null) {
            mv.setViewName("redirect:login.html");
        } else {
            mv.setViewName("view/user/profile");
        }
        return mv;
    }

    /**
     * 更新个人资料
     * @param user 用户
     * @return 更新个人资料成功
     */
    @ResponseBody
    @PostMapping(value = "/update.json")
    protected BaseVo update(@RequestBody User user) {
        User current = userService.getCurrent();
        Assert.notNull(current, "还没有登录");
        Assert.isTrue(current.equals(user), "没有权限");
        userService.update(user, "password");
        return wrapperMsg("success");
    }
}
