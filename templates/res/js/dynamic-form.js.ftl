/**
 * Ajax动态加载表单项
 * @param selector {*} 选择器
 * @param url {string} URL
 * @param [data] {Object} 参数
 * @param [requestBody] {boolean} 是否是RequestBody
 */
dict.dynamicForm = function (selector, url, data, requestBody) {

    var verifyOrAppend = function (selector) {
        var name = dict.$name(selector);
        var $selector = $(selector);
        var $table;
        if (name == 'table' || name == 'tbody' || name == 'thead' || name == 'tfoot') {
            $table = $selector;
        } else {
            $table = $(document.createElement('table'));
            $table.addClass('layui-table');
            $selector.append($table);
        }
        return $table;
    }

    var formComponent = function (property) {
        dict.assert(property, 'property不能为空');
        dict.assert(property.type, 'property.type不能为空');
        dict.assert(property.name, 'property.name不能为空');

        var $component;

        switch (property.type) {
            case 'boolean': {
                $component = $(document.createElement('input'))
                    .attr('type', 'checkbox')
                    .attr('checked', false);
                break;
            }
            case 'string': {
                $component = $(document.createElement('input'))
                    .attr('type', 'text')
                    .attr('name', property.name)
                    .attr('placeholder', property.description)
                    .addClass('layui-input');
                break;
            }
            case 'integer': {
                $component = $(document.createElement('input'))
                    .attr('type', 'text')
                    .attr('name', property.name)
                    .attr('placeholder', property.description)
                    .addClass('layui-input');
                break;
            }
            case 'float': {
                $component = $(document.createElement('input'))
                    .attr('type', 'text')
                    .attr('name', property.name)
                    .attr('placeholder', property.description)
                    .addClass('layui-input');
                break;
            }
            case 'date': {
                $component = $(document.createElement('input'))
                    .attr('type', 'text')
                    .attr('name', property.name)
                    .attr('placeholder', property.description)
                    .addClass('layui-input');
                break;
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
            for (var i = 0; i < data.count; i++) {
                var $tr = $(document.createElement('tr'));
                $table.append($tr);
                var $th = $(document.createElement('th'));
                $th.text(data.data[i].display);
                $th.attr('title', data.data[i].description);
                $tr.append($th);
                var $td = $(document.createElement('td'));
                var $input = formComponent(data.data[i]);
                console.log($input);
                $td.append($input);
                $tr.append($td);
                $table.append($tr);
                layui.use('form');
            }
        }
    });
};