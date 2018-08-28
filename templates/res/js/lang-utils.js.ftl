/**
 * lang-utils.js
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 22:00:21
 */

/** nothing function */
dict.nothing = function () {
};

/**
 * 自定义断言
 * @param value {*} 验证的值
 * @param [msg] {string} 错误消息
 * @throws Error
 */
dict.assert = function (value, msg) {
    if (!value) throw Error(msg ? msg : 'Assertion Failed!');
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
}

/**
 * 用单引号包裹字符串
 * @param str {string} 字符串
 * @return {string}
 */
dict.singleQuote = function (str) {
    return '\'' + str + '\'';
}

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
                eval(dict.format('{0} = {1};', variable, dict.singleQuote(obj[key])));
            } else if (!eval(variable)) {
                eval(dict.format('{0} = {1};', variable, '{}'));
            }
        });
    }
    return returnObj;
}

/**
 * 字符串缩略
 * @param str 字符串
 * @param width 长度
 * @param ellipsis
 */
dict.abbreviate = function (str, width, ellipsis) {
    width = parseInt(width);
    if (str == null || str.length <= 0 || width == null || isNaN(width) || isFinite(width)) return str;
    if (ellipsis == null) ellipsis = '';
    var chars = str.split('');
    var returnValue;
    for (var i = 0; i < chars.length; i++) {
        if (width > 0) {
            if (${regexp(constant('CHINESE_PATTERN'))}.test(chars[i])) width -= 2;
            else width--;
            returnValue += chars[i];
        } else {
            break;
        }

    }
}