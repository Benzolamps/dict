package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DocExportVo;
import com.benzolamps.dict.service.base.DocSolutionService;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import com.benzolamps.dict.service.base.StudentService;
import com.benzolamps.dict.util.DictFile;
import com.benzolamps.dict.util.lambda.Action1;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.zip.ZipOutputStream;

import static com.benzolamps.dict.bean.Group.Type.WORD;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Word文档界面
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-12 13:24:22
 */
@Slf4j
@RestController
@RequestMapping("doc")
public class DocController extends BaseController {

    @Resource
    private DocSolutionService docSolutionService;

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    @Resource
    private freemarker.template.Configuration configuration;

    @Resource
    private StudentService studentService;

    @Resource
    private UnaryOperator<String> compress;

    /**
     * 导出专属文档
     * @param docExportVo DocExportVo
     * @return 操作成功
     */
    @RequestMapping(value = "export_personal.json")
    protected BaseVo exportPersonal(DocExportVo docExportVo) throws IOException, TemplateException {
        Assert.notNull(docExportVo, "doc export vo不能为null");
        Set<DictFile.ZipItem> zipItems = new HashSet<>();
        for (Group group : docExportVo.getGroups()) {
            boolean isWord = WORD.equals(group.getType());
            Set<Student> students = group.getStudentsOriented();
            Set<? extends BaseElement> elements = isWord ? group.getWords() : group.getPhrases();
            Assert.notEmpty(students, "分组" + group.getName() + "中没有学生");
            Assert.notEmpty(elements, "分组" + group.getName() + "中没有" + (isWord ? "单词" : "短语"));
            docExportVo.setContent(elements);
            Map<String, Object> attributes = new HashMap<>();
            Template template = this.getTemplate(docExportVo, attributes);
            for (Student student : students) {
                attributes.put("student", studentService.find(student.getId()));
                attributes.put("groupId", group.getId());
                try (StringWriter stringWriter = new StringWriter()) {
                    template.process(attributes, stringWriter);
                    String content = compress.apply(stringWriter.toString());
                    String name = (String) attributes.get("title");
                    InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
                    zipItems.add(new DictFile.ZipItem(name + " - " + group.getName() + " - " + student.getNumber() + " - " + student.getName() + ".doc", inputStream));
                }
            }
        }
        Map<String, Object> exportAttribute = new HashMap<>();
        exportAttribute.put("ext", "zip");
        exportAttribute.put("consumer", (Action1<OutputStream>) outputStream -> {
            try (ZipOutputStream zos = DictFile.zip(zipItems, outputStream, "")) {
                logger.trace(zos.toString());
            }
        });
        String token = UUID.randomUUID().toString().replace("-", "");
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(token, exportAttribute, RequestAttributes.SCOPE_SESSION);
        return wrapperData(token);
    }

    /**
     * 导出默认文档
     * @param docExportVo DocExportVo
     * @return 操作成功
     */
    @RequestMapping(value = "export_default.json", method = {GET, POST})
    @ResponseBody
    protected BaseVo exportDefault(@ModelAttribute DocExportVo docExportVo) throws IOException, TemplateException {
        Assert.notNull(docExportVo, "doc export vo不能为null");
        Map<String, Object> attributes = new HashMap<>();
        Template template = this.getTemplate(docExportVo, attributes);
        try (StringWriter stringWriter = new StringWriter()) {
            template.process(attributes, stringWriter);
            String content = compress.apply(stringWriter.toString());
            Map<String, Object> exportAttribute = new HashMap<>();
            exportAttribute.put("ext", "doc");
            exportAttribute.put("consumer", (Action1<OutputStream>) outputStream -> outputStream.write(content.getBytes("UTF-8")));
            String token = UUID.randomUUID().toString().replace("-", "");
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            requestAttributes.setAttribute(token, exportAttribute, RequestAttributes.SCOPE_SESSION);
            return wrapperData(token);
        }
    }

    private Template getTemplate(DocExportVo docExportVo, Map<String, Object> attributes) throws IOException {
        Assert.notEmpty(docExportVo.getContent(), "content不能为null或空");
        attributes.put("content", docExportVo.getContent());
        DocSolution docSolution = docSolutionService.find(docExportVo.getDocSolutionId());
        Assert.notNull(docSolution, "doc solution不能为null");
        attributes.put("title", docExportVo.getTitle());
        if (docSolution.getNeedShuffle()) {
            IShuffleStrategySetup setup = shuffleSolutionService.getSolutionInstanceAt(docExportVo.getShuffleSolutionId());
            Assert.notNull(setup, "shuffle solution不能为null");
            attributes.put("shuffleStrategySetup", setup);
        }
        docSolution.getProperties().forEach(attributes::putIfAbsent);
        docSolutionService.getBaseProperties().forEach(attributes::putIfAbsent);
        Template template;
        if (docSolution.getNeedHeader()) {
            attributes.put("content_path", docSolution.getPath());
            template = configuration.getTemplate("doc/common/header.ftl");

        } else {
            template = configuration.getTemplate(docSolution.getPath());
        }
        return template;
    }

    /**
     * 下载
     * @param fileName 文件名
     * @param token token
     * @param response HttpServletResponse
     */
    @SuppressWarnings("unchecked")
    @PostMapping("download")
    @ResponseBody
    protected void download(String fileName, String token, HttpServletResponse response) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Map<String, Object> exportAttribute = (Map<String, Object>) requestAttributes.getAttribute(token, RequestAttributes.SCOPE_SESSION);
        Assert.notNull(exportAttribute, "导出失败！");
        super.download(fileName + "." + exportAttribute.get("ext"));
        try (OutputStream outputStream = response.getOutputStream()) {
            ((Consumer<OutputStream>) exportAttribute.get("consumer")).accept(outputStream);
        }
        requestAttributes.removeAttribute(token, RequestAttributes.SCOPE_SESSION);
    }
}
