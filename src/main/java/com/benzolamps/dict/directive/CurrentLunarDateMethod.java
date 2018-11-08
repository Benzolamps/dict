package com.benzolamps.dict.directive;

import com.benzolamps.dict.util.date.Festival;
import com.benzolamps.dict.util.date.Lunar;
import com.benzolamps.dict.util.date.SolarTerm;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * 获取当前农历日期的Freemarker方法
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-8-24 19:48:14
 */
@Component
public class CurrentLunarDateMethod implements TemplateMethodModelEx {

    private String lunarDateStr;

    @PostConstruct
    @Scheduled(cron = "00 00 00 * * ?")
    private void update() {
        Date date = new Date();
        String[] festivals = Festival.getFestival(date);
        String solarTerm = SolarTerm.getSolarTerm(date);
        Lunar lunar = new Lunar(date);
        lunarDateStr = lunar + (festivals.length > 0 ? " " + String.join(" ", festivals) : "") + (solarTerm != null ? " " + solarTerm : "");
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) {
        return lunarDateStr;
    }
}
