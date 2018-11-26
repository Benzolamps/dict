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

<#include '/res/js/lang-utils.js.ftl'/>
<#include '/res/js/web-utils.js.ftl'/>
<#include '/res/js/dom-utils.js.ftl'/>
<#include '/res/js/dynamic-form.js.ftl'/>

Array.prototype.toString = function () {
  return this.join('，');
};

!function () {
  this.models = ['element', 'layer', 'form', 'table', 'code', 'laypage', 'laydate', 'slider'];
  this.loadLayui = function () {
    $.each(models, function (index, value) {
      window[value] || (window[value] = layui[value]);
    });

    layer && layer.config({
      title: '提示',
      resize: false,
      move: false,
      closeBtn: 2,
      skin: 'dict-skin'
    });

    setInterval(function () {
      layer && layer.config({anim: ((Math.random() * 5) | 0) + 1});
    }, 1);
  };
  this.loadLayui();
}();

$.validator.addMethod('func', function (value, element, param) {
  return eval('(' + param + ').call(this, value, element, param)');
}, "输入错误");

$.validator.addMethod('constant', function (value, element, param) {
  return value == param;
}, "输入错误");

$.validator.methods.remote = function (value, element, param) {
  if ('string' == typeof param) {
    param = {url: param};
  }
  return $.validator.methods.constant.call(this, value, element, param.ignore) ||
    dict.loadText({
      url: param.url,
      type: 'get',
      cache: true,
      async: false,
      loading: false,
      data: new (function () {
        this[element.name] = value;
      })
    }) != 'false'
};

