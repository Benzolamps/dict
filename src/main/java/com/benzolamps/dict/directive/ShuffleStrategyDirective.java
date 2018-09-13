package com.benzolamps.dict.directive;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.component.IShuffleStrategy;
import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.util.DictObject;
import freemarker.core.Environment;
import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;
import lombok.var;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 乱序方案遍历Freemarker指令
 */
@Component
public class ShuffleStrategyDirective implements TemplateDirectiveModel {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Assert.isTrue(params.containsKey("shuffle_strategy_setup"), "shuffle_strategy_setup未指定");
        IShuffleStrategySetup shuffleStrategySetup =
            DictObject.safeCast(DeepUnwrap.unwrap((TemplateModel) params.get("shuffle_strategy_setup")), IShuffleStrategySetup.class);
        Assert.notNull(shuffleStrategySetup, "shuffle_strategy_setup不能为null");
        List<BaseElement> elements = DictObject.safeCast(DeepUnwrap.unwrap((TemplateModel) params.get("elements")), List.class);
        Assert.notNull(elements, "elements不能为null");
        IShuffleStrategy shuffleStrategy = shuffleStrategySetup.setup(elements.size(), elements.hashCode());
        int elementIndex = 0;
        var oldVisible = env.getVariable("visible");
        var oldElement = env.getVariable("element");
        var oldIndex = env.getVariable("index");
        var oldElementIndex = env.getVariable("element_index");
        var oldHasNext = env.getVariable("has_next");
        while (shuffleStrategy.hasNext()) {
            ObjectWrapper objectWrapper = new DefaultObjectWrapperBuilder(Configuration.getVersion()).build();
            int index = shuffleStrategy.next();
            BaseElement element = elements.get(index);
            boolean visible = shuffleStrategy.visible();
            boolean hasNext = shuffleStrategy.hasNext();
            env.setVariable("visible", objectWrapper.wrap(visible));
            env.setVariable("element", objectWrapper.wrap(element));
            env.setVariable("index", objectWrapper.wrap(index));
            env.setVariable("element_index", objectWrapper.wrap(elementIndex++));
            env.setVariable("has_next", objectWrapper.wrap(hasNext));
            body.render(env.getOut());
        }
        env.setVariable("visible", oldVisible);
        env.setVariable("element", oldElement);
        env.setVariable("index", oldIndex);
        env.setVariable("element_index", oldElementIndex);
        env.setVariable("has_next", oldHasNext);
    }
}
