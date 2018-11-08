package com.benzolamps.dict.util.date;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Calendar.*;

/**
 * 二十四节气
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-8 10:08:09
 */
@SuppressWarnings("MagicConstant")
public class SolarTerm {
    private static final double D = 0.2422;
    /* +1偏移 */
    private final static Map<String, Integer[]> INCREASE_OFFSET_MAP = new HashMap<>();
    private final static Map<String, Integer[]> DECREASE_OFFSET_MAP = new HashMap<>();

    /* 24节气 **/
    @SuppressWarnings("SpellCheckingInspection")
    private enum SolarTermsEnum {
        LICHUN, // 立春
        YUSHUI, // 雨水
        JINGZHE, // 惊蛰
        CHUNFEN, // 春分
        QINGMING, // 清明
        GUYU, // 谷雨
        LIXIA, // 立夏
        XIAOMAN, // 小满
        MANGZHONG, // 芒种
        XIAZHI, // 夏至
        XIAOSHU, // 小暑
        DASHU, // 大暑
        LIQIU, // 立秋
        CHUSHU, // 处暑
        BAILU, // 白露
        QIUFEN, // 秋分
        HANLU, // 寒露
        SHUANGJIANG, // 霜降
        LIDONG, // 立冬
        XIAOXUE, // 小雪
        DAXUE, // 大雪
        DONGZHI, // 冬至
        XIAOHAN, // 小寒
        DAHAN // 大寒
    }

    static {
        DECREASE_OFFSET_MAP.put(SolarTermsEnum.YUSHUI.name(), new Integer[] { 2026 }); // 雨水
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.CHUNFEN.name(), new Integer[] { 2084 }); // 春分
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.XIAOMAN.name(), new Integer[] { 2008 }); // 小满
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.MANGZHONG.name(), new Integer[] { 1902 }); // 芒种
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.XIAZHI.name(), new Integer[] { 1928 }); // 夏至
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.XIAOSHU.name(), new Integer[] { 1925, 2016 }); // 小暑
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.DASHU.name(), new Integer[] { 1922 }); // 大暑
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.LIQIU.name(), new Integer[] { 2002 }); // 立秋
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.BAILU.name(), new Integer[] { 1927 }); // 白露
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.QIUFEN.name(), new Integer[] { 1942 }); // 秋分
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.SHUANGJIANG.name(), new Integer[] { 2089 }); // 霜降
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.LIDONG.name(), new Integer[] { 2089 }); // 立冬
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.XIAOXUE.name(), new Integer[] { 1978 }); // 小雪
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.DAXUE.name(), new Integer[] { 1954 }); // 大雪
        DECREASE_OFFSET_MAP.put(SolarTermsEnum.DONGZHI.name(), new Integer[] { 1918, 2021 }); // 冬至
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.XIAOHAN.name(), new Integer[] { 1982 }); // 小寒
        DECREASE_OFFSET_MAP.put(SolarTermsEnum.XIAOHAN.name(), new Integer[] { 2019 }); // 小寒
        INCREASE_OFFSET_MAP.put(SolarTermsEnum.DAHAN.name(), new Integer[] { 2082 }); // 大寒
    }

    /* 定义一个二维数组，第一维数组存储的是20世纪的节气C值，第二维数组存储的是21世纪的节气C值,0到23个，依次代表立春、雨水...大寒节气的C值 */
    private static final double[][] CENTURY_ARRAY = {
        {
            4.6295, 19.4599, 6.3826, 21.4155, 5.59, 20.888, 6.318, 21.86, 6.5, 22.2,
            7.928, 23.65, 8.35, 23.95, 8.44, 23.822, 9.098, 24.218, 8.218, 23.08,
            7.9, 22.6, 6.11, 20.84
        },
        {
            3.87, 18.73, 5.63, 20.646, 4.81, 20.1, 5.52, 21.04, 5.678, 21.37,
            7.108, 22.83, 7.5, 23.13, 7.646, 23.042, 8.318, 23.438, 7.438, 22.36,
            7.18, 21.94, 5.4055, 20.12
        }
    };

    /**
     *
     * @param year 年份
     * @param name 节气的名称
     * @return 返回节气是相应月份的第几天
     */
    private static int getSolarTermNum(int year, String name) {
        double centuryValue;// 节气的世纪值，每个节气的每个世纪值都不同
        name = name.trim().toUpperCase();
        int ordinal = SolarTermsEnum.valueOf(name).ordinal();
        int centuryIndex;
        if (year >= 1901 && year <= 2000) {// 20世纪
            centuryIndex = 0;
        } else if (year >= 2001 && year <= 2100) {// 21世纪
            centuryIndex = 1;
        } else {
            throw new RuntimeException("不支持此年份：" + year + "，目前只支持1901年到2100年的时间范围");
        }
        centuryValue = CENTURY_ARRAY[centuryIndex][ordinal];
        int dateNum;
        /*
         * 计算 num =[Y*D+C]-L这是传说中的寿星通用公式 公式解读：年数的后2位乘0.2422加C(即：centuryValue)取整数后，减闰年数
         */
        int y = year % 100; // 步骤1:取年分的后两位数
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) { // 闰年
            if (ordinal == SolarTermsEnum.XIAOHAN.ordinal() || ordinal == SolarTermsEnum.DAHAN.ordinal()
                    || ordinal == SolarTermsEnum.LICHUN.ordinal() || ordinal == SolarTermsEnum.YUSHUI.ordinal()) {
                // 注意：凡闰年3月1日前闰年数要减一，即：L=[(Y-1)/4],因为小寒、大寒、立春、雨水这两个节气都小于3月1日,所以 y = y-1
                y = y - 1; // 步骤2
            }
        }
        dateNum = (int) (y * D + centuryValue) - (y / 4); // 步骤3，使用公式[Y*D+C]-L计算
        dateNum += specialYearOffset(year, name); // 步骤4，加上特殊的年分的节气偏移量
        return dateNum;
    }

    /**
     * 特例,特殊的年分的节气偏移量,由于公式并不完善，所以算出的个别节气的第几天数并不准确，在此返回其偏移量
     * @param year 年份
     * @param name 节气名称
     * @return 返回其偏移量
     */
    private static int specialYearOffset(int year, String name) {
        int offset = 0;
        offset += getOffset(DECREASE_OFFSET_MAP, year, name, -1);
        offset += getOffset(INCREASE_OFFSET_MAP, year, name, 1);

        return offset;
    }

    private static int getOffset(Map<String, Integer[]> map, int year, String name, int offset) {
        int off = 0;
        Integer[] years = map.get(name);
        if (null != years) {
            for (int i : years) {
                if (i == year) {
                    off = offset;
                    break;
                }
            }
        }
        return off;
    }

    public static String getSolarTerm(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(YEAR) >= 1901 && calendar.get(YEAR) <= 2100) {
            switch (calendar.get(MONTH) + 1) {
                case 1: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.XIAOHAN.name()) == calendar.get(DATE))
                        return "小寒";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.DAHAN.name()) == calendar.get(DATE))
                        return "大寒";
                    break;
                }

                case 2: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.LICHUN.name()) == calendar.get(DATE))
                        return "立春";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.YUSHUI.name()) == calendar.get(DATE))
                        return "雨水";
                    break;
                }

                case 3: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.JINGZHE.name()) == calendar.get(DATE))
                        return "惊蛰";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.CHUNFEN.name()) == calendar.get(DATE))
                        return "春分";
                    break;
                }

                case 4: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.QINGMING.name()) == calendar.get(DATE))
                        return "清明";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.GUYU.name()) == calendar.get(DATE))
                        return "谷雨";
                    break;
                }

                case 5: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.LIXIA.name()) == calendar.get(DATE))
                        return "立夏";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.XIAOMAN.name()) == calendar.get(DATE))
                        return "小满";
                    break;
                }

                case 6: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.MANGZHONG.name()) == calendar.get(DATE))
                        return "芒种";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.XIAZHI.name()) == calendar.get(DATE))
                        return "夏至";
                    break;
                }

                case 7: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.XIAOSHU.name()) == calendar.get(DATE))
                        return "小暑";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.DASHU.name()) == calendar.get(DATE))
                        return "大暑";
                    break;
                }

                case 8: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.LIQIU.name()) == calendar.get(DATE))
                        return "立秋";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.CHUSHU.name()) == calendar.get(DATE))
                        return "处暑";
                    break;
                }

                case 9: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.BAILU.name()) == calendar.get(DATE))
                        return "白露";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.QIUFEN.name()) == calendar.get(DATE))
                        return "秋分";
                    break;
                }

                case 10: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.HANLU.name()) == calendar.get(DATE))
                        return "寒露";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.SHUANGJIANG.name()) == calendar.get(DATE))
                        return "霜降";
                    break;
                }

                case 11: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.LIDONG.name()) == calendar.get(DATE))
                        return "立冬";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.XIAOXUE.name()) == calendar.get(DATE))
                        return "小雪";
                    break;
                }

                case 12: {
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.DAXUE.name()) == calendar.get(DATE))
                        return "大雪";
                    if (getSolarTermNum(calendar.get(YEAR), SolarTermsEnum.DONGZHI.name()) == calendar.get(DATE))
                        return "冬至";
                }
            }
        }
        return null;
    }
}
