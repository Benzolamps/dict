package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.DocSolution;
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
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;

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

    private Template getTemplate(DocExportVo docExportVo, ModelMap modelMap) throws IOException {
        Assert.notNull(docExportVo, "doc export vo不能为null");
        Assert.notEmpty(docExportVo.getContent(), "content不能为null或空");
        modelMap.addAttribute("content", docExportVo.getContent());
        DocSolution docSolution = docSolutionService.find(docExportVo.getDocSolutionId());
        Assert.notNull(docSolution, "doc solution不能为null");
        if (StringUtils.isEmpty(docExportVo.getTitle())) {
            modelMap.addAttribute("title", docSolution.getName());
        } else {
            modelMap.addAttribute("title", docExportVo.getTitle());
        }
        if (docSolution.getNeedShuffle()) {
            IShuffleStrategySetup setup = shuffleSolutionService.getSolutionInstanceAt(docExportVo.getShuffleSolutionId());
            Assert.notNull(setup, "shuffle solution不能为null");
            modelMap.addAttribute("shuffleStrategySetup", setup);
        }
        docSolution.getProperties().forEach(modelMap::putIfAbsent);
        docSolutionService.getBaseProperties().forEach(modelMap::putIfAbsent);
        Template template;
        if (docSolution.getNeedHeader()) {
            template = configuration.getTemplate("doc/common/header.ftl");
            modelMap.addAttribute("content_path", docSolution.getPath());
        } else {
            template = configuration.getTemplate(docSolution.getPath());
        }
        return template;
    }

    @RequestMapping(value = "export_personal.json")
    protected BaseVo exportPersonal(DocExportVo docExportVo, ModelMap modelMap) throws IOException, TemplateException {
        Assert.notNull(docExportVo, "doc export vo不能为null");
        Collection<Student> students = docExportVo.getStudents();
        Assert.notNull(students, "students不能为null");
        Template template = this.getTemplate(docExportVo, modelMap);
        Set<DictFile.ZipItem> zipItems = new HashSet<>();
        for (Student student : students) {
            modelMap.addAttribute("student", studentService.find(student.getId()));
            modelMap.addAttribute("groupId", docExportVo.getGroupId());
            try (StringWriter stringWriter = new StringWriter()) {
                template.process(modelMap, stringWriter);
                String content = stringWriter.toString().replaceAll("[\\t\\f\\u00a0 ]+", " ").replaceAll("[\\r\\n]+ ", "\r\n").trim();
                String name = (String) modelMap.get("title");
                InputStream inputStream = new ByteArrayInputStream(content.getBytes());
                zipItems.add(new DictFile.ZipItem(name + " - " + student.getId() + " - " + student.getName() + ".doc", inputStream));
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

    @RequestMapping(value = "export_default.json", method = {GET, POST})
    @ResponseBody
    protected BaseVo exportDefault(@ModelAttribute DocExportVo docExportVo, ModelMap modelMap) throws IOException, TemplateException {
        Assert.notNull(docExportVo, "doc export vo不能为null");
        Template template = this.getTemplate(docExportVo, modelMap);
        try (StringWriter stringWriter = new StringWriter()) {
            template.process(modelMap, stringWriter);
            String content = stringWriter.toString().replaceAll("[\\t\\f\\u00a0 ]+", " ").replaceAll("[\\r\\n]+ ", "\r\n").trim();
            Map<String, Object> exportAttribute = new HashMap<>();
            exportAttribute.put("ext", "doc");
            exportAttribute.put("consumer", (Action1<OutputStream>) outputStream -> outputStream.write(content.getBytes()));
            String token = UUID.randomUUID().toString().replace("-", "");
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            requestAttributes.setAttribute(token, exportAttribute, RequestAttributes.SCOPE_SESSION);
            return wrapperData(token);
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("download")
    @ResponseBody
    protected void download(String fileName, String token, HttpServletResponse response) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Map<String, Object> exportAttribute = (Map<String, Object>) requestAttributes.getAttribute(token, RequestAttributes.SCOPE_SESSION);
        Assert.notNull(exportAttribute, "导出失败！");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8") + "." + exportAttribute.get("ext"));
        try (OutputStream outputStream = response.getOutputStream()) {
            ((Consumer<OutputStream>) exportAttribute.get("consumer")).accept(outputStream);
        }
        requestAttributes.removeAttribute(token, RequestAttributes.SCOPE_SESSION);
    }
}
