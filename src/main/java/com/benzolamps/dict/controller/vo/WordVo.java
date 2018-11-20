package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.component.DictIgnore;
import com.benzolamps.dict.component.DictPropertyInfo;
import com.benzolamps.dict.component.DictRemote;
import com.benzolamps.dict.service.base.WordClazzService;
import com.benzolamps.dict.util.DictSpring;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 单词Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-2 23:09:08
 */
@Data
public class WordVo implements Serializable {

    private static final long serialVersionUID = -5091224507491296666L;

    /** id */
    @Id
    private Integer id;

    /** 单词原形 */
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "单词")
    @DictRemote("/word/prototype_not_exists.json")
    private String prototype;

    /** 美式发音 */
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "美式发音")
    private String americanPronunciation;

    /** 英式发音 */
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "英式发音")
    private String britishPronunciation;

    /** 词性 */
    @NotEmpty
    @DictPropertyInfo(display = "词性")
    private Object[] clazzes;

    /** 词义 */
    @NotEmpty
    @Length(max = 255)
    @DictPropertyInfo(display = "词义")
    private String definition;

    /** 索引 */
    @DictPropertyInfo(display = "索引")
    private Integer index;

    /** 词频 */
    @DictIgnore
    private Integer frequency;

    /** 已掌握该单词的学生数 */
    @DictIgnore
    @JsonProperty("masteredStudents")
    private Integer masteredStudentsCount;

    /** 未掌握该单词的学生数 */
    @DictIgnore
    @JsonProperty("failedStudents")
    private Integer failedStudentsCount;

    /**
     * 将WordVo转换为Word
     * @param wordVo wordVo
     * @return word
     */
    public static Word convertToWord(WordVo wordVo) {
        Assert.notNull(wordVo, "word vo不能为null");
        WordClazzService wordClazzService = DictSpring.getBean(WordClazzService.class);
        Word word = new Word();
        word.setId(wordVo.getId());
        word.setIndex(wordVo.getIndex());
        word.setPrototype(wordVo.getPrototype());
        word.setBritishPronunciation(wordVo.getBritishPronunciation());
        word.setAmericanPronunciation(wordVo.getAmericanPronunciation());
        word.setDefinition(wordVo.getDefinition());
        word.setClazzes(Stream.of(wordVo.getClazzes()).map(clazz -> {
            WordClazz wordClazz = wordClazzService.findByIdOrName(clazz.toString());
            if (wordClazz == null) {
                wordClazz = new WordClazz();
                wordClazz.setName(clazz.toString());
            }
            return wordClazz;
        }).collect(Collectors.toSet()));
        return word;
    }

    /**
     * 将Word转换为WordVo
     * @param word word
     * @return wordVo
     */
    public static WordVo convertFromWord(Word word) {
        Assert.notNull(word, "word不能为null");
        WordVo wordVo = new WordVo();
        wordVo.setId(word.getId());
        wordVo.setIndex(word.getIndex());
        wordVo.setDefinition(word.getDefinition());
        wordVo.setBritishPronunciation(word.getBritishPronunciation());
        wordVo.setAmericanPronunciation(word.getAmericanPronunciation());
        wordVo.setPrototype(word.getPrototype());
        wordVo.setFrequency(word.getFrequency());
        wordVo.setClazzes(word.getClazzes().stream().map(WordClazz::getId).toArray(Integer[]::new));
        wordVo.setMasteredStudentsCount(word.getMasteredStudentsCount());
        wordVo.setFailedStudentsCount(word.getFailedStudentsCount());
        return wordVo;
    }
}
