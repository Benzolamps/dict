package com.benzolamps.dict.directive;

import com.benzolamps.dict.service.base.MiscellaneousService;
import com.benzolamps.dict.service.base.UserService;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 杂项Freemarker指令
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-1 15:56:17
 */
@Component
@Lazy(false)
public class MiscellaneousDirective {

    @Resource
    private MiscellaneousService miscellaneousService;

    @Resource
    private UserService userService;

    @Bean
    protected TemplateMethodModelEx currentUserMethod() {
        return arguments -> {
            System.out.println(userService.getCurrent());
            return userService.getCurrent();
        };
    }
}
