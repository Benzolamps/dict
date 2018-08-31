/**
 * web-utils.js 页面
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-10 22:01:57
 */

/**
 * 页面post跳转
 * @param url {string} URL
 * @param data {object} 参数
 * @returns {HTMLFormElement} 表单
 */
dict.postHref = function (url, data) {
    var form = document.createElement('form');
    form.action = url;
    form.method = 'post';
    form.style.display = 'none';
    for (var key in data) {
        var element = document.createElement('textarea');
        element.name = key;
        element.value = data[key];
        form.appendChild(element);
    }
    document.body.appendChild(form);
    form.submit();
    return form;
};

!function ($, dict) {
    var defaultOptions = {
        async: false,
        cache: false,
        dataType: 'text',
        data: {},
        type: 'post',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        error: function (result, status, request) {
            console.log('请求失败');
            console.log(result);
            console.log(status);
            console.log(request);
            if (request.status == 403) {
                location.href = '${base_url}/user/login.html';
            }
        },
        success: function (result, status, request) {
            console.log('请求成功');
            console.log(result);
            console.log(status);
            console.log(request);
        }
    };

    /**
     * 自定义ajax方法
     * @param options {object} 选项
     * @returns {string} 响应的内容
     */
    dict.loadText = function (options) {
        dict.assert(options);
        dict.assert(options.url);

        var responseData = null;

        var choose = function (key) {
            return options[key] ? options[key] : defaultOptions[key];
        };

        $.ajax({
            url: options.url,
            async: choose('async'),
            cache: choose('cache'),
            type: choose('type'),
            dataType: choose('dataType'),
            traditional: true,
            contentType: options.requestBody ? 'application/json; charset=UTF-8' : choose('contentType'),

            data: (function () {
                var expected = choose('data');
                return options.requestBody ? JSON.stringify(expected) : expected;
            })(),

            error: function (request, status) {
                var result = JSON.parse(request.responseText);
                defaultOptions.error(result, status, request);
                options.error && options.error(result, status, request);
            },

            success: function (result, status, request) {
                responseData = result;
                defaultOptions.success(result, status, request);
                options.success && options.success(result, status, request);
            }
        });
        if (options.async) {
            console.warn('异步的请求不能通过返回值获取数据!');
        }
        return responseData;
    }
}(jQuery, dict);
