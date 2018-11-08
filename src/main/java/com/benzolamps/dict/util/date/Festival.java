package com.benzolamps.dict.util.date;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.util.*;

import static com.benzolamps.dict.util.Constant.DATE_FORMAT;
import static java.util.Calendar.*;

/**
 * 节日
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-8 10:07:17
 */
@SuppressWarnings({"MagicConstant", "SpellCheckingInspection", "RedundantIfStatement"})
public class Festival {

    private String name;

    /* 公历节日 */
    private static List<Festival> finalFestival;

    /* 农历节日 */
    private static List<Festival> lunarFestival;

    private Calendar calendar;

    private Festival(Date date, String name) {
        this.name = name;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(date);
    }

    static {
        finalFestival();
        lunarFestival();
    }

    @SneakyThrows(ParseException.class)
    private static void finalFestival() {
        finalFestival = Arrays.asList(
            /* 元旦 (1月1日) */
            new Festival(DATE_FORMAT.parse("1900-1-1"), "元旦"),
            /* 情人节 (2月14日) */
            new Festival(DATE_FORMAT.parse("1900-2-14"), "情人节"),
            /* 妇女节 (3月8日) */
            new Festival(DATE_FORMAT.parse("1900-3-8"), "妇女节"),
            /* 植树节 (3月12日) */
            new Festival(DATE_FORMAT.parse("1900-3-12"), "植树节"),
            /* 愚人节 (4月1日) */
            new Festival(DATE_FORMAT.parse("1900-4-1"), "愚人节"),
            /* 劳动节 (5月1日) */
            new Festival(DATE_FORMAT.parse("1900-5-1"), "劳动节"),
            /* 青年节 (5月4日) */
            new Festival(DATE_FORMAT.parse("1900-5-4"), "青年节"),
            /* 儿童节 (6月1日) */
            new Festival(DATE_FORMAT.parse("1900-6-1"), "儿童节"),
            /* 建党节 (7月1日) */
            new Festival(DATE_FORMAT.parse("1900-7-1"), "建党节"),
            /* 建军节 (8月1日) */
            new Festival(DATE_FORMAT.parse("1900-8-1"), "建军节"),
            /* 教师节 (9月10日) */
            new Festival(DATE_FORMAT.parse("1900-9-10"), "教师节"),
            /* 国庆节 (10月1日) */
            new Festival(DATE_FORMAT.parse("1900-10-1"), "国庆节"),
            /* 万圣节 (11月1日) */
            new Festival(DATE_FORMAT.parse("1900-11-1"), "万圣节"),
            /* 万灵节 (11月2日) */
            new Festival(DATE_FORMAT.parse("1900-11-2"), "万灵节"),
            /* 光棍节 (11月11日) */
            new Festival(DATE_FORMAT.parse("1900-11-11"), "光棍节"),
            /* 平安夜 (12月24日) */
            new Festival(DATE_FORMAT.parse("1900-12-24"), "平安夜"),
            /* 圣诞节 (12月25日) */
            new Festival(DATE_FORMAT.parse("1900-12-25"), "圣诞节")
        );
    }

    /* 母亲节，5月第2个星期六 */
    private static boolean isMotherDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(MONTH) != MAY) return false;
        if (calendar.get(DAY_OF_WEEK) != SATURDAY) return false;
        if (calendar.get(WEEK_OF_MONTH) != 2) return false;
        return true;
    }

    /* 父亲节，6月第3个星期日 */
    private static boolean isFatherDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(MONTH) != JUNE) return false;
        if (calendar.get(DAY_OF_WEEK) != SUNDAY) return false;
        if (calendar.get(WEEK_OF_MONTH) != 3) return false;
        return true;
    }

    /* 感恩节，11月第4个星期五 */
    private static boolean isThanksDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(MONTH) != NOVEMBER) return false;
        if (calendar.get(DAY_OF_WEEK) != FRIDAY) return false;
        if (calendar.get(WEEK_OF_MONTH) != 4) return false;
        return true;
    }

    @SneakyThrows(ParseException.class)
    private static void lunarFestival() {
        lunarFestival = Arrays.asList(
            /* 春节 (正月初一) */
            new Festival(DATE_FORMAT.parse("1900-1-1"), "春节"),
            /* 元宵节(正月十五) */
            new Festival(DATE_FORMAT.parse("1900-1-15"), "元宵节"),
            /* 龙抬头(二月初二) */
            new Festival(DATE_FORMAT.parse("1900-2-2"), "龙抬头"),
            /* 端午节(五月初五) */
            new Festival(DATE_FORMAT.parse("1900-5-5"), "端午节"),
            /* 七夕(七月初七) */
            new Festival(DATE_FORMAT.parse("1900-7-7"), "七夕"),
            /* 中元节(七月十五) */
            new Festival(DATE_FORMAT.parse("1900-7-15"), "中元节"),
            /* 中秋节(八月十五) */
            new Festival(DATE_FORMAT.parse("1900-8-15"), "中秋节"),
            /* 重阳节(九月初九) */
            new Festival(DATE_FORMAT.parse("1900-9-9"), "重阳节"),
            /* 腊八(腊月初八) */
            new Festival(DATE_FORMAT.parse("1900-12-8"), "腊八")
        );
    }

    /* 除夕，大月腊月三十，小月腊月廿九 */
    private static boolean isChuxi(Date date) {
        Lunar lunarDate = new Lunar(date);
        if (lunarDate.getMonth() != 12) return false;
        if (lunarDate.isLeap()) return false;
        if (lunarDate.getDay() < 29) return false;
        if (lunarDate.isBigMonth() && lunarDate.getDay() == 29) return false;
        return true;
    }

    /**
     * 获取一个日期的节日
     * @param date 日期
     * @return 节日数组
     */
    public static String[] getFestival(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        List<String> f = new ArrayList<>();
        for (Festival a : finalFestival) {
            Calendar c = a.calendar;
            if (calendar.get(MONTH) == c.get(MONTH) && calendar.get(DATE) == c.get(DATE))
                f.add(a.name);
        }
        for (Festival a : lunarFestival) {
            Calendar c = a.calendar;
            Lunar l = new Lunar(date);
            if (c.get(MONTH) + 1 == l.getMonth() && c.get(DATE) == l.getDay() && !l.isLeap())
                f.add(a.name);
        }
        if (isMotherDay(date)) f.add("母亲节");
        if (isFatherDay(date)) f.add("父亲节");
        if (isThanksDay(date)) f.add("感恩节");
        if (isChuxi(date)) f.add("除夕");
        return f.toArray(new String[0]);
    }
}
