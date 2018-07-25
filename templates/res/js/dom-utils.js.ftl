/**
 * dom-utils.js (DOM操作)
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 21:05:27
 */
(function ($, dict) {
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
    }

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
    }
})(jQuery, dict);