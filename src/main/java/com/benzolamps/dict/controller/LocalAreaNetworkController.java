package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.service.base.LocalAreaNetworkService;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 局域网操作Controller
 * @author Benzolamps
 * @version 2.2.1
 * @datetime 2018-10-9 13:00:44
 */
@Controller
@RequestMapping("lan")
@ResponseBody
@Conditional(LocalAreaNetworkController.LanCondition.class)
public class LocalAreaNetworkController extends BaseController {

    /** 仅限在Windows下执行 */
    public static class LanCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return System.getProperty("os.name").matches("(?i).*windows.*");
        }
    }

    @Resource
    private LocalAreaNetworkService localAreaNetworkService;

    /**
     * 打开防火墙
     * @return 操作成功
     */
    @GetMapping("open_firewall.json")
    protected BaseVo openFireWall() {
        localAreaNetworkService.openFireWall();
        return SUCCESS_VO;
    }

    /**
     * 添加入站规则
     * @return 操作成功
     */
    @GetMapping("add_rule.json")
    protected BaseVo addRule() {
        localAreaNetworkService.addRule();
        return SUCCESS_VO;
    }
}
