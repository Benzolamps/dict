package com.benzolamps.dict.util.date;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.util.*;

import static com.benzolamps.dict.util.Constant.DATE_FORMAT;
import static java.util.Calendar.*;

/**
 * @author Benzolamps
 */
@SuppressWarnings("MagicConstant")
public class Festival {

    private String name;

    private static List<Festival> finalFestival, lunarFestival;

    private Calendar calendar;

    private Festival(Date date, String name) {
        this.name = name;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(date);
    }

    @SneakyThrows(ParseException.class)
    public static void finalFestival() { // 公历节日
        finalFestival = new ArrayList<>();
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-1-1"), "元旦")); // 元旦(1月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-2-14"), "情人节")); // 情人节(2月14日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-3-8"), "妇女节")); // 妇女节(3月8日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-3-12"), "植树节")); // 植树节(3月12日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-4-1"), "愚人节")); // 愚人节(4月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-5-1"), "劳动节")); // 劳动节(5月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-5-4"), "青年节")); // 青年节(5月4日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-6-1"), "儿童节")); // 儿童节(6月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-7-1"), "建党节")); // 建党节(7月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-8-1"), "建军节")); // 建军节(8月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-9-10"), "教师节")); // 教师节(9月10日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-10-1"), "国庆节")); // 国庆节(10月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-11-1"), "万圣节")); // 万圣节(11月1日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-11-2"), "万灵节")); // 万灵节(11月2日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-11-11"), "光棍节")); // 光棍节(11月11日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-12-24"), "平安夜")); // 平安夜(12月24日)
        finalFestival.add(new Festival(DATE_FORMAT.parse("1900-12-25"), "圣诞节")); // 圣诞节(12月25日)
    }

    public static boolean isMotherDay(Date date) { // 母亲节，5月第2个星期六
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(MONTH) != MAY)
            return false;

        if (calendar.get(DAY_OF_WEEK) != SATURDAY)
            return false;

        if (calendar.get(DATE) <= 7)
            return false;

        if (calendar.get(DATE) > 14)
            return false;

        return true;
    }

    public static boolean isFatherDay(Date date) { // 父亲节，6月第3个星期日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(MONTH) != JUNE)
            return false;

        if (calendar.get(DAY_OF_WEEK) != SUNDAY)
            return false;

        if (calendar.get(DATE) <= 14)
            return false;

        if (calendar.get(DATE) > 21)
            return false;

        return true;
    }

    public static boolean isThanksDay(Date date) { // 感恩节，11月第4个星期五
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(MONTH) != NOVEMBER)
            return false;

        if (calendar.get(DAY_OF_WEEK) != FRIDAY)
            return false;

        if (calendar.get(DATE) <= 21)
            return false;

        if (calendar.get(DATE) > 28)
            return false;

        return true;
    }

    @SneakyThrows(ParseException.class)
    public static void lunarFestival() {
        lunarFestival = new ArrayList<>();
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-1-1"), "春节")); // 春节(正月初一)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-1-15"), "元宵节")); // 元宵节(正月十五)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-2-2"), "龙抬头")); // 龙抬头(二月初二)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-5-5"), "端午节")); // 端午节(五月初五)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-7-7"), "七夕")); // 七夕(七月初七)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-7-15"), "中元节")); // 中元节(七月十五)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-8-15"), "中秋节")); // 中秋节(八月十五)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-9-9"), "重阳节")); // 重阳节(九月初九)
        lunarFestival.add(new Festival(DATE_FORMAT.parse("1900-12-8"), "腊八")); // 腊八(腊月初八)
    }

    @SneakyThrows(ParseException.class)
    public static boolean isChuxi(Date date) { // 除夕，大月腊月三十，小月腊月廿九
        Lunar lunarDate = new Lunar(date);
        if (lunarDate.month != 12)
            return false;

        if (lunarDate.isLeap)
            return false;

        if (lunarDate.day < 29)
            return false;

        if (lunarDate.isBigMonth() && lunarDate.day == 29)
            return false;

        return true;
    }

    @SneakyThrows(ParseException.class)
    public static String[] getFestival(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        finalFestival();
        lunarFestival();
        Vector<String> f = new Vector<>();

        for (Festival a : finalFestival) {
            Calendar c = a.calendar;
            if (calendar.get(MONTH) == c.get(MONTH) && calendar.get(DATE) == c.get(DATE))
                f.add(a.name);
        }

        for (Festival a : lunarFestival) {
            Calendar c = a.calendar;
            Lunar l = new Lunar(date);
            if (c.get(MONTH) + 1 == l.month && c.get(DATE) == l.day && !l.isLeap)
                f.add(a.name);
        }

        if (isMotherDay(date))
            f.add("母亲节");
        if (isFatherDay(date))
            f.add("父亲节");
        if (isThanksDay(date))
            f.add("感恩节");
        if (isChuxi(date))
            f.add("除夕");

        return f.toArray(new String[0]);
    }

}
