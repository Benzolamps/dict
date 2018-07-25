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
     * 将表格中当前行生成指定对象
     * @param row 行
     * @param clazz 对象的类型
     * @param <T> 对象的类型
     * @return 对象
     */
    static <T> T excelRowToObject(Row row, Class<T> clazz) {
        DetectColumnNum detectColumnNum = clazz.getDeclaredAnnotation(DetectColumnNum.class);

        /* 检测总列数 */
        if (detectColumnNum != null) {
            int real = row.getLastCellNum();
            int acquired = detectColumnNum.value();
            if (real != acquired)
                throw new ExcelFormatException("错误的列数, 应该为 " + acquired + " , 得到 " + real, row.getRowNum(), 0);
        }

        /* 获取所有带有ExcelHeader注解的属性 */
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(ExcelHeader.class))
            .collect(Collectors.toList());

        /* 创建对象 */
        try {
            T obj = clazz.newInstance();
            fields.forEach(field -> {
                ExcelHeader excelHeader = field.getAnnotation(ExcelHeader.class);
                field.setAccessible(true);
                int index = excelHeader.value();
                Object value = null;

                try {
                    /* 判断类型 */
                    switch (excelHeader.cellFormat()) {
                        case STRING:
                            value = row.getCell(index).getStringCellValue();
                            break;
                        case INTEGER:
                            value = (int) row.getCell(index).getNumericCellValue();
                            break;
                        case DOUBLE:
                            value = row.getCell(index).getNumericCellValue();
                            break;
                        case DATE:
                            value = row.getCell(index).getDateCellValue();
                            break;
                        case BOOLEAN:
                            value = row.getCell(index).getBooleanCellValue();
                            break;
                    }
                } catch (Exception e) {
                    throw new ExcelFormatException(e.getMessage() + ", " + e.getClass().getName(), row.getRowNum(), index);
                }

                /* 判断非空 */
                if (excelHeader.notEmpty() && (value == null || value.toString().isEmpty())) {
                    throw new ExcelFormatException("单元格内容不能为空", row.getRowNum(), index);
                }

                /* 判断数字范围 */
                if (excelHeader.cellFormat() == CellFormat.INTEGER || excelHeader.cellFormat() == CellFormat.DOUBLE) {
                    double dbValue = Number.class.cast(value).doubleValue();
                    if (dbValue < excelHeader.range().min() || dbValue > excelHeader.range().max()) {
                        throw new ExcelFormatException(
                            "数字范围应该介于 "
                                + excelHeader.range().min() + " 和 "
                                + excelHeader.range().max() + " 之间, 得到 " + value,
                            row.getRowNum(), index);
                    }
                }

                /* 给属性赋值 */
                try {
                    field.set(obj, value);
                } catch (IllegalAccessException e) {
                    throw new DictException(e);
                }
            });
            return obj;
        } catch (ReflectiveOperationException e) {
            throw new DictException(e);
        }
    }

    /**
     * 将Excel文件转换为工作簿
     * @param file Excel文件
     * @return 工作簿
     */
    static Workbook fileToWorkbook(File file) {
        InputStream stream;
        try {
            stream = new FileInputStream(file);
            return new HSSFWorkbook(stream);
        } catch (FileNotFoundException e) {
            throw new DictException("找不到文件!", e);
        } catch (OfficeXmlFileException | IOException ignored) {
            try {
                stream = new FileInputStream(file);
                return new XSSFWorkbook(stream);
            }  catch (FileNotFoundException e) {
                throw new DictException("找不到文件!", e);
            } catch (Exception e) {
                throw new DictException("文件格式有错误! " + e.getMessage(), e);
            }
        }
    }

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
            for (File subfile : path.listFiles(f -> f.isDirectory() || filter == null || filter.test(f))) {
                if (subfile.isDirectory()) {
                    stack.push(subfile);
                } else {
                    files.add(subfile);
                }
            }
        }

        return files;
    }
}
