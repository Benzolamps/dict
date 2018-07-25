package com.benzolamps.dict.util;

public class DictString {


    /**
     * 将变量名转换为驼峰
     * @param origin 原形
     * @return 转换后
     */
    public static String toCamel(String origin) {
        StringBuilder sb = new StringBuilder();
        if (origin.contains("-") || origin.contains("_")) {
            origin = origin.toLowerCase();
            for (int i = 0; i < origin.length(); i++) {
                char chr = origin.charAt(i);
                if ((chr == '_' || chr == '-') && i + 1 < origin.length()) {
                    sb.append(Character.toUpperCase(origin.charAt(++i)));
                } else if (Character.isLetterOrDigit(chr)) {
                    if (sb.length() == 0) {
                        if (Character.isLetter(chr)) {
                            sb.append(chr);
                        }
                    } else {
                        sb.append(chr);
                    }
                }
            }
        } else {
            for (int i = 0; i < origin.length(); i++) {
                char chr = origin.charAt(i);
                if (Character.isLetterOrDigit(chr)) {
                    if (sb.length() == 0) {
                        if (Character.isLetter(chr)) {
                            sb.append(Character.toLowerCase(chr));
                        }
                    } else {
                        sb.append(chr);
                    }
                }
            }
        }
        return sb.toString();
    }
}
