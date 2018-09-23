package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.dao.core.Pageable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Word文档Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-12 13:37:46
 */
@Getter
@Setter
public class DocExportVo implements Serializable {

    private static final long serialVersionUID = 852337843770551706L;

    /** 分组id */
    private Integer groupId;

    /** pageable */
    private Pageable pageable;

    /** 标题 */
    @Length(max = 20)
    private String title;

    /** Word文档方案 */
    @NotNull
    private Integer docSolutionId;

    /** 乱序方案 */
    private Integer shuffleSolutionId;

    @JsonIgnore
    private List<? extends BaseElement> content;
}
