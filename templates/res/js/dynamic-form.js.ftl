/**
 * Ajax动态加载表单项
 * @param selector {*} 选择器
 * @param [fields] {Object} 字段
 * @param [prefix] {string} 前缀
 * @param initValues {Object} 初始化值
 * @param extendedRules {Object} 验证规则
 * @param extendedMessages {Object} 错误提示
 * @param submitHandler {Function} 提交拦截器
 */
dict.dynamicForm = function (selector, fields, prefix, initValues, extendedRules, extendedMessages, submitHandler) {

    initValues || (initValues = {});

    var $form;

    /* 验证是否有表单包裹 */
    var verifyOrAppend = function (selector) {
        var name = dict.$name(selector);
        var $selector = $(selector);

        var parentsUntilForm = $selector.parentsUntil('form');

        if (parentsUntilForm.length == 0) {
            $form = $selector.parent();
        } else {
            $form = parentsUntilForm.last().parent();
        }

        dict.assert($form.is('form'), "未找到form");

        /* 根据选择器的元素类型生成表格 */
        var $table;
        if (['table', 'tbody', 'thead', 'tfoot'].indexOf(name) > -1) {
            $table = $selector;
        } else {
            $table = $(document.createElement('table'));
            $table.addClass('layui-table');
            $selector.append($table);
        }
        return $table;
    }

    var $table = verifyOrAppend(selector);

    $table.empty();

    /* 根据属性类型生成不同的表单元素 */
    var formComponent = function (property) {
        dict.assert(property, 'property不能为空');
        dict.assert(property.type, 'property.type不能为空');
        dict.assert(property.name, 'property.name不能为空');

        var $component;

        if (property.readonly) {
            $component = $(document.createElement('span')).text(property.value);
        } else if (property.options) {
            if (!property.multiple) {
                $component = $(document.createElement('select'))
                    .attr('name', property.name)
                    .addClass('layui-input')
                    .attr('required', property.notEmpty)
                    .attr('lay-filter', property.name)
                    .append($(document.createElement('option'))
                        .val('')
                        .text('请选择' + property.display)
                        .addClass('layui-input')
                    );
                for (var i = 0; i < property.options.length; i++) {
                    var option = property.options[i];
                    var $option = $(document.createElement('option'))
                        .val(option.id)
                        .text(option.value)
                        .attr('selected', option.id == property.value)
                        .addClass('layui-input');
                    $component.append($option);
                }
            } else {
                $component = $(document.createElement('div'));
                for (var i = 0; i < property.options.length; i++) {
                    var option = property.options[i];
                    var $option = $(document.createElement('div'))
                        .css('padding', '5px')
                        .css('display', 'inline-block')
                        .append($(document.createElement('input'))
                            .attr('name', property.name)
                            .attr('type', 'checkbox')
                            .attr('title', option.value)
                            .attr('checked', property.value != null && property.value.indexOf(option.id) > -1)
                            .attr('lay-skin', 'primary')
                            .attr('option-id', option.id)
                            .addClass('layui-input')
                            .addClass('dict-checkbox')
                        );
                    $component.append($option);
                }
            }
        } else if (property.textArea) {
            $component = $(document.createElement('textarea'))
                .attr('name', property.name)
                .attr('placeholder', '请输入' + property.display)
                .attr('autocomplete', 'off')
                .attr('value', property.value)
                .attr('required', property.notEmpty)
                .attr('maxlength', property.maxLength)
                .addClass('layui-input')
                .addClass('layui-textarea');
        } else {
            switch (property.type) {
                case 'boolean': {
                    $component = $(document.createElement('input'))
                        .attr('type', 'checkbox')
                        .attr('name', property.name)
                        .attr('lay-skin', 'switch')
                        .attr('lay-text', '开|关')
                        .attr('lay-filter', property.name)
                        .attr('checked', property.value === 'true' || property.value === true)
                        .attr('required', property.notEmpty)
                        .addClass('layui-input')
                        .addClass('dict-switch');
                    break;
                }
                case 'string': {
                    $component = $(document.createElement('input'))
                        .attr('type', 'text')
                        .attr('name', property.name)
                        .attr('placeholder', '请输入' + property.display)
                        .attr('autocomplete', 'off')
                        .attr('maxlength', property.maxLength)
                        .attr('value', property.value)
                        .attr('required', property.notEmpty)
                        .addClass('layui-input');
                    break;
                }
                case 'integer': {
                    $component = $(document.createElement('input'))
                        .attr('type', 'text')
                        .attr('name', property.name)
                        .attr('placeholder', '请输入' + property.display)
                        .attr('maxlength', property.max ? String(property.max).length : 11)
                        .attr('autocomplete', 'off')
                        .attr('value', property.value)
                        .attr('required', property.notEmpty)
                        .addClass('layui-input');
                    break;
                }
                case 'float': {
                    $component = $(document.createElement('input'))
                        .attr('type', 'text')
                        .attr('name', property.name)
                        .attr('placeholder', '请输入' + property.display)
                        .attr('autocomplete', 'off')
                        .attr('value', property.value)
                        .attr('required', property.notEmpty)
                        .addClass('layui-input');
                    break;
                }
                case 'date': {
                    $component = $(document.createElement('input'))
                        .attr('type', 'text')
                        .attr('name', property.name)
                        .attr('placeholder', '请输入' + property.display)
                        .attr('autocomplete', 'off')
                        .attr('value', property.value)
                        .attr('required', property.notEmpty)
                        .addClass('layui-input');
                    break;
                }
            }
        }
        return $component;
    }

    var id;

    for (var i = 0; i < fields.length; i++) {
        fields[i].value = initValues[fields[i].name] != null ? initValues[fields[i].name] : fields[i].defaultValue;
        prefix && (fields[i].name = prefix + fields[i].name);
        if (!id && fields[i].id) {
            var $input = $(document.createElement('input'))
                .attr('name', fields[i].name)
                .attr('type', 'hidden')
                .val(fields[i].value);
            $table.append($input);
            id = true;
            continue;
        }
        var $tr = $(document.createElement('tr'));
        $table.append($tr);
        var $th = $(document.createElement('th'));
        $th.html(fields[i].display + (fields[i].notEmpty ? '<span class="required-field">&nbsp;*&nbsp;</span>' : ''));
        $th.attr('title', fields[i].description);
        $tr.append($th);
        var $td = $(document.createElement('td'));
        var $input = formComponent(fields[i]);
        $td.append($input);
        $tr.append($td);
        $table.append($tr);
    }

    form.render();

    $form.find('select').each(function () {
       if ($(this).attr('required')) {
           $(this).next().find('input').attr('required', true);
           $(this).next().find('input').attr('name', $(this).attr('name'));
           $(this).remove();
       }
    });

    var rules = {};
    var messages = {};
    for (var i = 0; i < fields.length; i++) {

        var itemRules = {};
        var itemMessages = {};
        if (fields[i].notEmpty) {
            itemRules.required = true;
            itemMessages.required = fields[i].display + '不能为空';
        }

        if (fields[i].remote) {
            itemRules.remote = fields[i].remote;
            itemMessages.remote = fields[i].display + '远程验证失败';
        }

        if (fields[i].email) {
            itemRules.email = true;
            itemMessages.email = fields[i].display + '格式不正确';
        }

        if (fields[i].url) {
            itemRules.url = true;
            itemMessages.url = fields[i].display + '格式不正确';
        }

        if (fields[i].type == 'float') {
            itemRules.number = true;
            itemMessages.number = fields[i].display + '必须是有效的数字';
        }

        if (fields[i].type == 'integer') {
            itemRules.integer = true;
            itemMessages.integer = fields[i].display + '必须是有效的整数';
        }

        if (fields[i].max != null) {
            itemRules.max = fields[i].max;
            itemMessages.max = fields[i].display + '不能大于 ' + fields[i].max;
        }

        if (fields[i].min != null) {
            itemRules.min = fields[i].min;
            itemMessages.min = fields[i].display + '不能小于 ' + fields[i].min;
        }

        if (fields[i].maxLength != null) {
            itemRules.maxlength = fields[i].maxLength;
            itemMessages.maxlength = fields[i].display + '不能多于 ' + fields[i].maxLength + ' 个字符';
        }

        if (fields[i].minLength != null) {
            itemRules.minlength = fields[i].minLength;
            itemMessages.minlength = fields[i].display + '不能少于 ' + fields[i].minLength + ' 个字符';
        }

        if (fields[i].pattern) {
            itemRules.pattern = fields[i].pattern;
            itemMessages.pattern = fields[i].display + '格式不正确';
        }

        rules[fields[i].name] = itemRules;

        messages[fields[i].name] = itemMessages;
    }
    dict.validateForm($form, $.extend(true, rules, extendedRules), $.extend(true, messages, extendedMessages), submitHandler);
};

/**
 * 验证表单
 * @param selector {*} 选择器
 * @param [rules] {Object} 验证规则
 * @param [messages] {Object} 错误消息
 * @param submitHandler {Function} 提交拦截器
 */
dict.validateForm = function (selector, rules, messages, submitHandler) {
    var $form = $(selector);
    $form.find('select').each(function () {
        var $select = $(this);
        if ($select.attr('required')) {
            var $input = $select.next().find('input');
            $input.attr('required', true);
            $input.attr('name', $select.attr('name'));
            $select.remove();
        }
    });
    var validator = $form.validate();
    rules = $.extend(true, validator.dicRules, rules);
    messages = $.extend(true, validator.dictMessages, messages);
    submitHandler = dict.extendsFunction(validator.dictSubmitHandler, submitHandler);
    validator.destroy();
    validator = $form.validate({
        rules: rules,
        messages: messages,
        errorPlacement: dict.nothing,
        showErrors: function(errorMap, errorList) {
            if (errorList.length > 0) {
                var error = errorList[0].message;
                var element = $(errorList[0].element);
                $('html,body').animate({scrollTop: $(element).offset().top - 15}, 'fast', function () {
                    layer.tips(error, element, {
                        anim: 6,
                        success: function () {
                            setTimeout(function () {
                                element.css('border-color', '#FF0033');
                            }, 200);
                        },
                        end: function () {
                            element.css('border-color', '');
                        }
                    });
                });
            }
        },
        focusInvalid: false,
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        submitHandler: function () {
            $form.find('input[type=checkbox].dict-switch').each(function () {
                var $next = $(this).next();
                if ($next.is('.layui-form-checked')) {
                    $(this).val('true');
                } else {
                    $(this).val('false');
                }
            });
            $form.find('input[type=checkbox].dict-checkbox').each(function () {
                var $next = $(this).next();
                if ($next.is('.layui-form-checked')) {
                    $(this).val($(this).attr('option-id'));
                } else {
                    $(this).val(null);
                }
            });
            return submitHandler();
        }
    })
    validator.dictRules = rules;
    validator.dictMessages = messages;
    validator.dictSubmitHandler = submitHandler;
};