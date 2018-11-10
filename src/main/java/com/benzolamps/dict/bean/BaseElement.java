package com.benzolamps.dict.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 单词或短语类的基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-6-30 23:05:02
 */
@MappedSuperclass
@Setter
@Getter
@JsonIgnoreProperties({"createDate", "modifyDate", "version", "remark"})
public abstract class BaseElement extends BaseEntity {

    private static final long serialVersionUID = -3019993342620190686L;

    /** 索引 */
    @Column(nullable = false, name = "indexes")
    protected Integer index;

    /** 原形 */
    @Convert(converter = FullWidthToHalfWidthConverter.class)
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String prototype;

    /** 词义转换 */
    @Converter
    public static class DefinitionConverter extends HalfWidthToFullWidthConverter {
        @Override
        public String convertToDatabaseColumn(String value) {
            return super.convertToDatabaseColumn(value)
                .replaceAll("[，]+", "，")
                .replaceAll("[；]+", "；")
                .replaceAll("(^；+)|(；+$)|(^，+)|(，+$)", "");
        }

        @Override
        public String convertToEntityAttribute(String value) {
            return super.convertToDatabaseColumn(value)
                .replaceAll("[，]+", "，")
                .replaceAll("[；]+", "；")
                .replaceAll("(^；+)|(；+$)|(^，+)|(，+$)", "");
        }
    }

    /** 词义 */
    @Convert(converter = DefinitionConverter.class)
    @Column(nullable = false)
    @NotEmpty
    @Length(max = 255)
    private String definition;

    /** 词库 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "library", nullable = false, updatable = false)
    private Library library;

    /** 词频 */
    @Column(nullable = false, insertable = false)
    @Min(0)
    @ColumnDefault("0")
    private Integer frequency;

    /** 词频信息 */
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = FrequencyInfo.FrequencyInfoConverter.class)
    @Basic(fetch = FetchType.LAZY)
    @Column(insertable = false, columnDefinition = "longtext")
    private List<FrequencyInfo> frequencyInfo;
}
