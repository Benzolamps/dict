package com.benzolamps.dict.util;

import com.benzolamps.dict.component.CellFormat;
import com.benzolamps.dict.component.DetectColumnNum;
import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.exception.ExcelFormatException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
}
