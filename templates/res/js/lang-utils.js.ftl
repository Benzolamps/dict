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