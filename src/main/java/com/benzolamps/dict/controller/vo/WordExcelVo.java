package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.component.DetectColumnNum;
import com.benzolamps.dict.component.ExcelHeader;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 单词Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:09:08
 */
@Data
@DetectColumnNum(6)
public class WordExcelVo implements BaseElementVo<Word> {

    private static final long serialVersionUID = -2201903558371038672L;

    /** 索引 */
    @ExcelHeader(value = 0, cellClass = int.class, range = @ExcelHeader.Range(min = 0))
    private Integer index;

    /** 单词原形 */
    @ExcelHeader(value = 1, notEmpty = true)
    private String prototype;

    /** 英式发音 */
    @NotEmpty
    @ExcelHeader(value = 2, notEmpty = true)
    private String britishPronunciation;

    /** 美式发音 */
    @ExcelHeader(value = 3, notEmpty = true)
    private String americanPronunciation;

    /** 词性 */
    @ExcelHeader(value = 4, notEmpty = true)
    private String clazzes;

    /** 词义 */
    @ExcelHeader(value = 5, notEmpty = true)
    private String definition;

    @Override
    public Word convertToElement() {
        Word word = new Word();
        word.setPrototype(this.getPrototype());
        word.setAmericanPronunciation(this.getAmericanPronunciation());
        word.setBritishPronunciation(this.getBritishPronunciation());
        word.setDefinition(this.getDefinition());
        word.setIndex(this.getIndex());
        String[] clazzes = this.getClazzes().split("[ \\s\\u00a0]*[,，;；][ \\s\\u00a0]*");
        Set<WordClazz> wordClazzes = new LinkedHashSet<>();
        for (int i = 0; i < clazzes.length; i++) {
            String clazz = clazzes[i];
            WordClazz wordClazz = new WordClazz();
            wordClazz.setName(clazz);
            wordClazz.setId(i);
            wordClazzes.add(wordClazz);
        }
        word.setClazzes(wordClazzes);
        return word;
    }
}
