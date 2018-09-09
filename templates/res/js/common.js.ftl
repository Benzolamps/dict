/**
 * common.js (全局通用js)
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 21:00:19
 */

/**
 * 全局变量
 * @type {Object}
 */
window.dict = {};

/* ajax请求设为同步 */
$.ajaxSetup({async: false});

/* 通过ajax加载一个js文件 */
$.getScript('${base_url}/res/js/lang-utils.js');
$.getScript('${base_url}/res/js/web-utils.js');
$.getScript('${base_url}/res/js/dom-utils.js');
$.getScript('${base_url}/res/js/dynamic-form.js');
$.getScript('${base_url}/js/md5.js');

Array.prototype.toString = function () {
    return this.join('，');
};

!function () {
    this.models = ['element', 'layer', 'form', 'table', 'code', 'laypage'];
    this.loadLayui = function () {
        $.each(models, function (index, value) {
            window[value] || (window[value] = layui[value]);
        });

        layer && layer.config({
            title: false,
            resize: false,
            move: false,
            closeBtn: 2,
            yes: function (index) {
                layer.close(index);
            }
        });
    };
    layui.use(models, function () {
        loadLayui();
    });
    this.loadLayui();
}();

$.validator.addMethod('func', function(value, element, param) {
    return eval('(' + param + ').call(this, value, element, param)');
}, "输入错误");

$.validator.addMethod('constant', function(value, element, param) {
    return value == param;
}, "输入错误");

$.validator.methods.remote = function(value, element, param) {
    return $.validator.methods.constant.call(this, value, element, param.ignore) ||
        dict.loadText({
            url: param.url,
            type: 'get',
            cache: true,
            data: new (function () {
                this[element.name] = value;
            }),
            async: false
        }) == 'true'
};



