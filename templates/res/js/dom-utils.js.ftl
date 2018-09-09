/**
 * dom-utils.js (DOM操作)
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 21:05:27
 */
!function ($, dict) {
    var defaultOptions = {
        data: {},
        interval: 1000,
        complete: dict.nothing()
    };

    /**
     * 实时加载一个url的内容到DOM元素
     * @param selector 选择器
     * @param options 选项
     */
    dict.rtText = function (selector, options) {
        var $element = $(selector);
        options = $.extend(defaultOptions, options);
        if (!options.url) return;
        var interval = parseInt(options.interval);
        setInterval(function () {
            $element.load(options.url, options.data, options.complete);
        }, interval);
    };

    /**
     * 实时加载当前时间到DOM元素
     * @param selector 选择器
     * @param options 选项
     */
    dict.rtDateTimeText = function (selector, options) {
        options || (options = {});
        options.url = '${base_url}/res/txt/current_time.txt';
        options.pattern || (options.pattern = 'yyyy年M月d日 H时mm分ss秒');
        options.data = {pattern: options.pattern};
        dict.rtText(selector, options);
    };

    /**
     * 获取元素名称
     * @param selector {*} 选择器
     * @returns {string} 名称
     */
    dict.$name = function (selector) {
        var ele = dict.$single(selector)[0];
        return ele.nodeName.toLowerCase();
    };

    /**
     * 获取元素的唯一一个
     * @param selector {*} 选择器
     * @returns jQuery元素
     */
    dict.$single = function (selector) {
        var $selector = $(selector);
        dict.assert($selector.length > 0, '元素为空!');
        dict.assert($selector.length <= 1, '元素不唯一!');
        return $selector.first();
    };

    /**
     * 将表单元素序列化成对象
     * @param selector {*} 选择器
     * @return {Object} 对象
     */
    dict.serializeObject = function (selector) {
        var $form = $(selector);
        var object = {};
        $.each($form.serializeArray(), function (index, item) {
            if (item.value == null || item.value == '') return;
            var value = object[item.name];
            if (value && $.isArray(value)) {
                value.push(item.value);
            } else if (value && !$.isArray(value)) {
                value = [value];
                value.push(item.value);
            } else {
                var $checkbox = $form.find('input[name="' + item.name + '"][type=checkbox]');
                if ($checkbox.is('.dict-switch')) {
                    object[item.name] = eval(item.value);
                } else if ($checkbox.is('.dict-checkbox')) {
                    object[item.name] = [item.value];
                } else {
                    object[item.name] = item.value;
                }
            }
        });
        console.log(object);
        return object;
    }

    /**
     * 重新加载页面
     * @param [forcedReload] {boolean}
     */
    dict.reload = location.reload;
}(jQuery, dict);