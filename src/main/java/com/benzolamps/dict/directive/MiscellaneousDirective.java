package com.benzolamps.dict.directive;

import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.service.base.UserService;
import com.benzolamps.dict.util.date.Festival;
import com.benzolamps.dict.util.date.Lunar;
import com.benzolamps.dict.util.date.SolarTerm;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

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

    /** @return 查看当前农历日期*/
    @Bean
    protected TemplateMethodModelEx currentLunarDateMethod() {
        return arguments -> {
            Date date = new Date();
            String[] festivals = Festival.getFestival(date);
            String solarTerm = SolarTerm.getSolarTerm(date);
            Lunar lunar = new Lunar(date);
            return lunar + (festivals.length > 0 ? " " + String.join(" ", festivals) : "") + (solarTerm != null ? " " + solarTerm : "");
        };
    }
}
