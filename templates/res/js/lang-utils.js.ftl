/**
 * lang-utils.js
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 22:00:21
 */

/** nothing function */
dict.nothing = function () {
  return !1;
};

/**
 * 自定义断言
 * @param value {*} 验证的值
 * @param [msg] {string} 错误消息
 * @throws Error
 */
dict.assert = function (value, msg) {
  if (!value) throw Error(msg ? msg : '断言失败');
};

/**
 * 格式化字符串
 * @param source {string} 原字符串
 * @param params {*} 参数
 * @return {string}
 */
dict.format = function (source, params) {
  if (arguments.length < 2) return source;
  if (arguments.length > 2 || (null == params || params.constructor != Array)) {
    params = $.makeArray(arguments).slice(1);
  }
  $.each(params, function (i, n) {
    source = source.replace(new RegExp('\\{' + i + '\\}', 'g'), n);
  });
  return source;
};

/**
 * 用引号包裹字符串
 * @param str {string} 字符串
 * @return {string}
 */
dict.quote = function (str) {
  return '"' + str + '"';
};

/**
 * 用单引号包裹字符串
 * @param str {string} 字符串
 * @return {string}
 */
dict.singleQuote = function (str) {
  return "'" + str + "'";
};

/**
 * 将有点分隔的key的对象转换为对象
 * @param obj {Object} 原对象
 * @return {Object} 新对象
 */
dict.generateObject = function (obj) {
  dict.assert(obj, 'obj不能为空');
  var returnObj = {};
  for (var key in obj) {
    var variable = 'returnObj';
    var layers = key.split('.');
    $.each(layers, function (index, item) {
      dict.assert(/^[$_a-z][$_a-z0-9]*$/i.test(item), '标识符不符合规范');
      variable += '.' + item;
      if (index == layers.length - 1) {
        eval(dict.format('{0} = {1};', variable, 'obj[key]'));
      } else if (!eval(variable)) {
        eval(dict.format('{0} = {1};', variable, '{}'));
      }
    });
  }
  return returnObj;
};

/**
 * 字符串缩略
 * @param str {String} 字符串
 * @param width {int} 长度
 * @param ellipsis {String}
 * @return {String}
 */
dict.abbreviate = function (str, width, ellipsis) {
  width = parseInt(width);
  if (str == null || str.length <= 0 || width == null || isNaN(width) || isFinite(width)) return str;
  if (ellipsis == null) ellipsis = '';
  var chars = str.split('');
  var returnValue;
  for (var i = 0; i < chars.length; i++) {
    if (width > 0) {
      if (${regexp(constant('CHINESE_PATTERN'))}.
      test(chars[i])
    )
      width -= 2;
    else
      width--;
      returnValue += chars[i];
    } else {
      break;
    }

  }
};

/**
 * 加密字符串
 * @param str 字符串
 * @return {String}
 */
dict.encrypt = function (str) {
  dict.assert(str, "str不能为null");

  for (var i = 0; i < 6; i++) {
    str = hex_md5(str);
  }
  return str;
};

/**
 * 合并两个方法, 先执行func1, 再执行fun2, fun2的返回值作为整体的返回值
 * @param func1 {Function}
 * @param func2 {Function}
 * @returns {function(): *}
 */
dict.extendsFunction = function (func1, func2) {
  if (!func1 || 'function' != typeof func1) {
    func1 = dict.nothing;
  }
  if (!func2 || 'function' != typeof func2) {
    func2 = dict.nothing;
  }

  var extended = function () {
    var returnValue;
    for (var i = 0; i < extended.callArray.length; i++) {
      returnValue = extended.callArray[i].apply(this, arguments);
    }
    return returnValue;
  };

  extended.callArray = [];

  if (func1.callArray) {
    for (var i = 0; i < func1.callArray.length; i++) {
      extended.callArray.push(func1.callArray[i]);
    }
  } else {
    extended.callArray.push(func1);
  }

  if (func2.callArray) {
    for (var i = 0; i < func2.callArray.length; i++) {
      extended.callArray.push(func2.callArray[i]);
    }
  } else {
    extended.callArray.push(func2);
  }

  return extended;
};

/**
 * 日期转字符串
 *  @param date {Date} 日期
 *  @returns {string}
 */
dict.dateToStr = function (date) {
  dict.assert(date.constructor === Date, "date不是日期类型");
  var str = date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日 ';
  switch (date.getDay()) {
    case 0: str += '星期日'; break;
    case 1: str += '星期一'; break;
    case 2: str += '星期二'; break;
    case 3: str += '星期三'; break;
    case 4: str += '星期四'; break;
    case 5: str += '星期五'; break;
    case 6: str += '星期六'; break;
  }
  str += ' ' + date.getHours() + ':' +
    (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':' +
    (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());

  return str;
};