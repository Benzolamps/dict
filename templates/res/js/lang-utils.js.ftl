/**
 * lang-utils.js
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 22:00:21
 */

/** nothing function */
dict.nothing = function () { };

/**
 * 自定义断言
 * @param value {*} 验证的值
 * @param [msg] {string} 错误消息
 * @throws Error
 */
dict.assert = function (value, msg) {
    if (!value) throw Error(msg ? msg : "Assertion Failed!");
};

dict.format = function (source, params) {
    if (arguments.length == 1)
        return function () {
            var args = $.makeArray(arguments);
            args.unshift(source);
            return $.format.apply(this, args);
        };
    if (arguments.length > 2 && params.constructor != Array) {
        params = $.makeArray(arguments).slice(1);
    }
    if (params.constructor != Array) {
        params = [params];
    }
    $.each(params, function (i, n) {
        source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
    });
    return source;
};

dict.quote = function (str) {
    return '"' + str + '"';
}

dict.singleQuote = function (str) {
    return '\'' + str + '\'';
}