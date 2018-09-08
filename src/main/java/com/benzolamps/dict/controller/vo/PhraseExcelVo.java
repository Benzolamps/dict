package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.component.CellFormat;
import com.benzolamps.dict.component.DetectColumnNum;
import com.benzolamps.dict.component.ExcelHeader;
import lombok.Data;

/**
 * 短语Excel Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:09:08
 */
@Data
@DetectColumnNum(3)
public class PhraseExcelVo implements BaseElementVo<Phrase> {

    private static final long serialVersionUID = -2201903558371038672L;

    /** 索引 */
    @ExcelHeader(value = 0, cellFormat = CellFormat.INTEGER, notEmpty = true, range = @ExcelHeader.Range(min = 1))
    private Integer index;

    /** 短语原形 */
    @ExcelHeader(value = 1, notEmpty = true)
    private String prototype;

    /** 词义 */
    @ExcelHeader(value = 2, notEmpty = true)
    private String definition;

    @Override
    public Phrase convertToElement() {
        Phrase phrase = new Phrase();
        phrase.setPrototype(this.getPrototype());
        phrase.setDefinition(this.getDefinition());
        phrase.setIndex(this.getIndex());
        return phrase;
    }
}
