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

layui.use(['element', 'layer'], function () {
    window.element = layui.element;
    window.layer = layui.layer;
    layer.config({
        title: false,
        resize: false,
        move: false,
        closeBtn: 2,
        yes: function (index) {
            layer.close(index);
        }
    });
});

$.validator.addMethod('func', function(value, element, param) {
  return eval('(' + param + ')(value, element)');
}, "输入错误");