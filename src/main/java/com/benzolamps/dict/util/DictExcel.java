package com.benzolamps.dict.util;

import com.benzolamps.dict.component.DetectColumnNum;
import com.benzolamps.dict.component.ExcelHeader;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.exception.ExcelFormatException;
import lombok.SneakyThrows;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel工具类
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-9-29 17:27:05
 */
public interface DictExcel {

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
            if (real != acquired) throw new ExcelFormatException("错误的列数，应该为 " + acquired + "，得到 " + real, row.getRowNum(), 0);
        }

        /* 获取所有带有ExcelHeader注解的属性 */
        DictBean<T> dictBean = new DictBean<>(clazz);

        /* 创建对象 */
        Map<String, Object> properties = new LinkedHashMap<>();
        dictBean.getProperties().forEach(property -> {
            if (!property.hasAnnotation(ExcelHeader.class)) return;
            ExcelHeader excelHeader = property.getAnnotation(ExcelHeader.class);
            int index = excelHeader.value();
            Object value = null;
            try {
                Cell cell = row.getCell(index);
                /* 判断类型 */
                if (cell != null) switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        Assert.isTrue(!excelHeader.notEmpty(), "单元格内容不能为空");
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        value = cell.getErrorCellValue();
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        value = cell.getArrayFormulaRange().formatAsString();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (Date.class.isAssignableFrom(excelHeader.cellClass())) {
                            value = cell.getDateCellValue();
                        } else {
                            value = cell.getNumericCellValue();
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue().trim();
                        break;
                }
                value = DictObject.ofObject(value, excelHeader.cellClass());
                Assert.isTrue((value = DictObject.ofObject(value, excelHeader.cellClass())) != null || !excelHeader.notEmpty(), "单元格内容不能为空");
            } catch (Exception e) {
                throw new ExcelFormatException(e, row.getRowNum(), index);
            }

            /* 判断数字范围 */
            if (Number.class.isAssignableFrom(excelHeader.cellClass())) {
                if (value != null) {
                    double dbValue = Number.class.cast(value).doubleValue();
                    if (dbValue < excelHeader.range().min() || dbValue > excelHeader.range().max()) {
                        throw new ExcelFormatException(
                            "数字范围应该介于 " + excelHeader.range().min() + " 和 " + excelHeader.range().max() + " 之间, 得到 " + value,
                            row.getRowNum(),
                            index
                        );
                    }
                }
            }

            /* 给属性赋值 */
            properties.put(property.getName(), value);
        });
        return dictBean.createBean(properties);
    }

    /**
     * 将Excel输入流转换为工作簿
     * @return 工作簿
     */
    @SneakyThrows(IOException.class)
    static Workbook inputStreamToWorkbook(InputStream stream) {
        byte[] bytes = StreamUtils.copyToByteArray(stream);
        try {
            return new XSSFWorkbook(new ByteArrayInputStream(bytes));
        } catch (POIXMLException | OfficeXmlFileException | IOException ignored) {
            try {
                return new HSSFWorkbook(new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                throw new DictException("文件格式有错误：" + e.getMessage(), e);
            }
        }
    }
}
