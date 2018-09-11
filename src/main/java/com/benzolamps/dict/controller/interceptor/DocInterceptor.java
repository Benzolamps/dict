package com.benzolamps.dict.controller.interceptor;

import com.benzolamps.dict.bean.DocSolution;
import com.benzolamps.dict.component.IShuffleStrategy;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.service.base.DocSolutionService;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import com.benzolamps.dict.util.DictObject;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Word文档拦截器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 22:58:43
 */
@SuppressWarnings("SuspiciousMethodCalls")
@Interceptor
public class DocInterceptor extends BaseInterceptor {

    @Resource
    private DocSolutionService docSolutionService;

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HandlerMethod method = (HandlerMethod) handler;
        if (!method.getMethod().isAnnotationPresent(DocView.class)) return;
        if (modelAndView != null) {
            if (modelAndView.getViewName().startsWith("redirect:")) return;
            ModelMap modelMap = modelAndView.getModelMap();
            DocSolution docSolution = (DocSolution) modelMap.get("docSolution");
            modelMap.remove(docSolution);
            modelMap.addAttribute("title", docSolution.getName());
            modelMap.addAttribute("content_path", modelAndView.getViewName());
            if (docSolution.getNeedHeader()) {
                modelAndView.setViewName("doc/common/header");
            }
            if (docSolution.getNeedShuffle()) {
                Integer id = DictObject.ofObject(request.getParameter("shuffleSolutionId"), Integer.class);
                Assert.notNull(id, "shuffle solution id不能为null");
                IShuffleStrategySetup setup = shuffleSolutionService.getSolutionInstanceAt(id);
                List content = (List) modelMap.get("content");
                Assert.notEmpty(content, "content不能为null或空");
                IShuffleStrategy shuffleStrategy = setup.setup(content.size(), content.hashCode());
                modelMap.addAttribute("shuffleStrategy", shuffleStrategy);
            }
            docSolution.getProperties().forEach(modelMap::putIfAbsent);
            docSolutionService.getBaseProperties().forEach(modelMap::putIfAbsent);
        }
    }
}
