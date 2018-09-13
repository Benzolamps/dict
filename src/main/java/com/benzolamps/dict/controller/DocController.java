package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.controller.vo.DocExportVo;
import com.benzolamps.dict.service.base.DocSolutionService;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import com.benzolamps.dict.util.Constant;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    @RequestMapping(value = "/export.txt", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView export(DocExportVo docExportVo) {
        ModelAndView mv = new ModelAndView();
        Assert.notNull(docExportVo, "doc export vo不能为null");
        Assert.notEmpty(docExportVo.getContent(), "content不能为null或空");
        mv.addObject("content", docExportVo.getContent());
        DocSolution docSolution = docSolutionService.find(docExportVo.getDocSolutionId());
        Assert.notNull(docSolution, "doc solution不能为null");
        if (StringUtils.isEmpty(docExportVo.getTitle())) {
            mv.addObject("title", docSolution.getName());
        } else {
            mv.addObject("title", docExportVo.getTitle());
        }
        if (docSolution.getNeedHeader()) {
            mv.setViewName("doc/common/header");
            mv.addObject("content_path", docSolution.getPath());
        } else {
            mv.setViewName(docSolution.getPath());
        }
        if (docSolution.getNeedShuffle()) {
            IShuffleStrategySetup setup = shuffleSolutionService.getSolutionInstanceAt(docExportVo.getShuffleSolutionId());
            Assert.notNull(setup, "shuffle solution不能为null");
            mv.addObject("shuffleStrategySetup", setup);
        }
        docSolution.getProperties().forEach(mv.getModelMap()::putIfAbsent);
        docSolutionService.getBaseProperties().forEach(mv.getModelMap()::putIfAbsent);
        return mv;
    }

    @PostMapping(value = "/download.doc")
    @ResponseBody
    protected String download(String fileName, String content, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("Content-disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8") + ".doc");
        return content.replaceAll(Constant.HTML_COMPRESS_PATTERN, "").replaceAll("[\\s ]+", " ").replaceAll("> <", "><");
    }
}
