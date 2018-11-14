/**
 * dom-utils.js (DOM操作)
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 21:05:27
 */
!function ($, dict) {
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
    dict.assert($selector.length > 0, '元素为空');
    dict.assert($selector.length <= 1, '元素不唯一');
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
    console.log($form.serializeArray());
    $.each($form.serializeArray(), function (index, item) {
      if (item.value == null || item.value == '') return;
      var value = object[item.name];
      if (value && $.isArray(value)) {
        value.push(item.value);
      } else if (value && !$.isArray(value)) {
      } else {
        var $component = $form.find('input[name="' + item.name + '"]');
        if ($component.is('[multiple-select=1]')) {
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
  dict.reload = function (forcedReload) {
    location.reload(forcedReload);
  };
}(jQuery, dict);
