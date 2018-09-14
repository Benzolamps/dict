package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DocExportVo;
import com.benzolamps.dict.service.base.DocSolutionService;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import com.benzolamps.dict.util.Constant;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Word文档界面
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-12 13:24:22
 */
@RestController
@RequestMapping("doc/")
public class DocController extends BaseController {

    @Resource
    private DocSolutionService docSolutionService;

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    @Resource
    private freemarker.template.Configuration configuration;

    @RequestMapping(value = "/export.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected BaseVo export(DocExportVo docExportVo, ModelMap modelMap) throws IOException, TemplateException {
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
        StringWriter stringWriter = new StringWriter();
        template.process(modelMap, stringWriter);
        String content = stringWriter.toString().replaceAll(Constant.HTML_COMPRESS_PATTERN, "").replaceAll("[\\s ]+", " ").replaceAll("> <", "><");
        String token = UUID.randomUUID().toString().replace("-", "");
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(token, content, RequestAttributes.SCOPE_SESSION);
        return wrapperData(token);
    }

    @PostMapping(value = "/download.doc")
    @ResponseBody
    protected String download(String fileName, String token, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("Content-disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8") + ".doc");
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String content = (String) requestAttributes.getAttribute(token, RequestAttributes.SCOPE_SESSION);
        requestAttributes.removeAttribute(token, RequestAttributes.SCOPE_SESSION);
        return content;
    }
}
