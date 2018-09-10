package com.benzolamps.dict.util;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * 用于操作文件的类
 * @author Benzolamps
 * @version 1.1.1
 * @datetime 2018-6-12 13:15:21
 */
@SuppressWarnings("unused")
@Component
public interface DictFile {

    /**
     * 深度列出目录所有文件
     * @param file 目录
     * @param filter 过滤器
     * @return 文件集合
     */
    static Set<File> deepListFiles(File file, Predicate<File> filter) {
        Assert.state(file != null && file.isDirectory(), "file不是目录");
        Stack<File> stack = new Stack<>();
        Set<File> files = new HashSet<>();
        stack.push(file);
        while (!stack.isEmpty()) {
            File path = stack.pop();
            for (File subFile : path.listFiles(f -> f.isDirectory() || filter == null || filter.test(f))) {
                if (subFile.isDirectory()) {
                    stack.push(subFile);
                } else {
                    files.add(subFile);
                }
            }
        }

        return files;
    }

    /**
     * 将long型转换为文件大小字符串
     * @param size 大小
     * @return 字符串
     */
    static String fileSizeStr(long size) {
        String[] unit = {"B", "KB", "MB", "GB", "TB", "PB"};
        int i = 0;
        double doubleSize = size;
        while (i < 5 && doubleSize >= 1024) {
            i++;
            doubleSize /= 1024.0;
        }
        return new BigDecimal(doubleSize).setScale(2, BigDecimal.ROUND_HALF_UP) + " " + unit[i];
    }
}
