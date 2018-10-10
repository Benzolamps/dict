package com.benzolamps.dict.directive;

import com.benzolamps.dict.service.base.LibraryService;
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
    private UserService userService;

    @Resource
    private LibraryService libraryService;

    /** @return 查看当前用户 */
    @Bean
    protected TemplateMethodModelEx currentUserMethod() {
        return arguments -> userService.getCurrent();
    }

    /** @return 查看当前词库 */
    @Bean
    protected TemplateMethodModelEx currentLibraryMethod() {
        return arguments -> libraryService.getCurrent();
    }

    /** @return 查看所有词库 */
    @Bean
    protected TemplateMethodModelEx allLibrariesMethod() {
        return arguments -> libraryService.findAll();
    }
}
