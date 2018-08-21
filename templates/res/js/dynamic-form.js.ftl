/**
 * Ajax动态加载表单项
 * @param selector {*} 选择器
 * @param url {string} URL
 * @param [data] {Object} 参数
 * @param [prefix] {string} 参数
 * @param initValues {Object}
 * @param [requestBody] {boolean} 是否是RequestBody
 */
dict.dynamicForm = function (selector, url, data, prefix, initValues, requestBody) {

    initValues || (initValues = {});

    var $form;

    /* 验证是否有表单包裹 */
    var verifyOrAppend = function (selector) {
        var name = dict.$name(selector);
        var $selector = $(selector);

        /* 获取表单 */
        $form = $selector.parentsUntil('form').last().parent();
        dict.assert($form.is('form'), "未找到form");

        /* 根据选择器的元素类型生成表格 */
        var $table;
        if (['table', 'tbody', 'thead', 'tfoot'].includes(name)) {
            $table = $selector;
        } else {
            $table = $(document.createElement('table'));
            $table.addClass('layui-table');
            $selector.append($table);
        }
        return $table;
    }

    /* 根据属性类型生成不同的表单元素 */
    var formComponent = function (property) {
        dict.assert(property, 'property不能为空');
        dict.assert(property.type, 'property.type不能为空');
        dict.assert(property.name, 'property.name不能为空');

        var $component;

        /* 是否是选择下拉框 */
        if (property.options) {
            $component = $(document.createElement('select'))
                .attr('name', property.name)
                .addClass('layui-input')
                .attr('required', property.notEmpty)
                .append($(document.createElement('option'))
                    .val('')
                    .text('请选择' + property.display)
                    .addClass('layui-input')
            );
            for (var i = 0; i < property.options.length; i++) {
                var option = property.options[i];
                var $option = $(document.createElement('option'))
                    .attr('value', option)
                    .text(option)
                    .attr('selected', property.options[i] == property.value)
                    .addClass('layui-input');
                $component.append($option);
            }
        } else {
            switch (property.type) {
                case 'boolean': {
                    $component = $(document.createElement('input'))
                        .attr('type', 'checkbox')
                        .attr('name', property.name)
                        .attr('lay-skin', 'switch')
                        .attr('lay-text', '开|关')
                        .attr('checked', property.value === 'true' || property.value === true)
                        .attr('required', property.notEmpty)
                        .addClass('layui-input');
                    break;
                }
                case 'string': {
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
                case 'integer': {
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

    dict.loadText({
        url: url,
        data: data,
        dataType: 'json',
        requestBody: requestBody,
        success: function (data) {
            var $table = verifyOrAppend(selector);
            $table.empty();

            var property = data.data;

            for (var i = 0; i < data.count; i++) {
                property[i].value = initValues[property[i].name] != null ? initValues[property[i].name] : property[i].defaultValue;
                prefix && (property[i].name = prefix + property[i].name);
                var $tr = $(document.createElement('tr'));
                $table.append($tr);
                var $th = $(document.createElement('th'));
                $th.text(property[i].display);
                $th.attr('title', property[i].description);
                $tr.append($th);
                var $td = $(document.createElement('td'));
                var $input = formComponent(property[i]);
                $td.append($input);
                $tr.append($td);
                $table.append($tr);
            }

            layui.use('form', function() {
                layui.form.render();
            });

            $form.find('select').each(function () {
               if ($(this).attr('required')) {
                   $(this).next().find('input').attr('required', true);
                   $(this).next().find('input').attr('name', $(this).attr('name'));
                   $(this).remove();
               }
            });

            var rules = {};
            var messages = {};
            for (var i = 0; i < data.count; i++) {

                var itemRules = {};
                var itemMessages = {};
                if (property[i].notEmpty) {
                    itemRules.required = true;
                    itemMessages.required = property[i].display + '不能为空';
                }

                if (property[i].remote) {
                    itemRules.remote = property[i].remote;
                    itemMessages.remote = property[i].display + '远程验证失败';
                }

                if (property[i].email) {
                    itemRules.email = true;
                    itemMessages.email = property[i].display + '格式不正确';
                }

                if (property[i].url) {
                    itemRules.url = true;
                    itemMessages.email = property[i].display + '格式不正确';
                }

                if (property[i].type == 'float') {
                    itemRules.number = true;
                    itemMessages.email = property[i].display + '必须是有效的数字';
                }

                if (property[i].type == 'integer') {
                    itemRules.integer = true;
                    itemMessages.email = property[i].display + '必须是有效的整数';
                }

                if (property[i].max != null) {
                    itemRules.max = property[i].max;
                    itemMessages.max = property[i].display + '不能大于' + property[i].max;
                }

                if (property[i].min != null) {
                    itemRules.min = property[i].min;
                    itemMessages.min = property[i].display + '不能小于' + property[i].min;
                }

                if (property[i].minLength != null) {
                    itemRules.minlength = property[i].minLength;
                    itemMessages.minlength = property[i].display + '不能大于' + property[i].minLength;
                }

                if (property[i].maxLength != null) {
                    itemRules.maxlength = property[i].maxLength;
                    itemMessages.maxlength = property[i].display + '不能大于' + property[i].maxLength;
                }

                if (property[i].pattern) {
                    itemRules.pattern = property[i].pattern;
                    itemMessages.pattern = property[i].display + '格式不正确';
                }

                rules[property[i].name] = itemRules;

                messages[property[i].name] = itemMessages;
            }

            dict.validateForm($form, rules, messages);
        }
    });
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
    $form.validate({
        rules: rules,
        messages: messages,
        errorPlacement: dict.nothing,
        showErrors: function(errorMap, errorList) {
            if (errorList.length > 0) {
                var error = errorList[0].message;
                var element = $(errorList[0].element);
                layer.tips(error, element, {
                    anim: 6,
                    success: function () {
                        var anchor = $(document.createElement('a')).attr('name', element.attr('name'));
                        element.before(anchor);
                        location.replace('#' + element.attr('name'));
                        anchor.remove();
                        setTimeout(function () {
                            element.css('border-color', '#FF0033');
                        }, 200);
                    },
                    end: function () {
                        element.css('border-color', '');
                    }
                });
            }
        },
        focusInvalid: false,
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        submitHandler: submitHandler
    })
};