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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

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
            if (real != acquired)
                throw new ExcelFormatException("错误的列数, 应该为 " + acquired + " , 得到 " + real, row.getRowNum(), 0);
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
            if (excelHeader.notEmpty() && (value == null || value.toString().trim().isEmpty())) {
                throw new ExcelFormatException("单元格内容不能为空", row.getRowNum(), index);
            }

            /* 判断数字范围 */
            if (excelHeader.cellFormat() == CellFormat.INTEGER || excelHeader.cellFormat() == CellFormat.DOUBLE) {
                if (value != null) {
                    double dbValue = Number.class.cast(value).doubleValue();
                    if (dbValue < excelHeader.range().min() || dbValue > excelHeader.range().max()) {
                        throw new ExcelFormatException(
                            "数字范围应该介于 "
                                + excelHeader.range().min() + " 和 "
                                + excelHeader.range().max() + " 之间, 得到 " + value,
                            row.getRowNum(), index);
                    }
                }
            }

            if (excelHeader.cellFormat() == CellFormat.STRING) {
                value = value.toString().trim();
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
    static Workbook inputStreamToWorkbook(InputStream stream) {
        try {
            return new XSSFWorkbook(stream);
        } catch (OfficeXmlFileException | IOException ignored) {
            try {
                return new HSSFWorkbook(stream);
            } catch (Exception e) {
                throw new DictException("文件格式有错误: " + e.getMessage(), e);
            }
        }
    }
}
