/**
 * 系统更新Web Socket
 */
dict.updateSocket = new function () {
    var socket = new SockJS('${base_url}/websocket');
    var stompClient = Stomp.over(socket);
    var that = this;
    that.connect = function () {
        stompClient.connect({}, function (frame) {
            setTimeout(function () {
                stompClient.subscribe('/version/has_new', function (data) {
                    'onHasNew' in that && that.onHasNew(data.body);
                });
                stompClient.subscribe('/version/already_new', function (data) {
                    'onAlreadyNew' in that && that.onAlreadyNew(data.body);
                });
                stompClient.subscribe('/version/downloading', function (data) {
                    'onDownloading' in that && that.onDownloading(data.body);
                });
                stompClient.subscribe('/version/copying', function (data) {
                    'onCopying' in that && that.onCopying(data.body);
                });
                stompClient.subscribe('/version/deleting', function (data) {
                    'onDeleting' in that && that.onDeleting(data.body);
                });
                stompClient.subscribe('/version/finished', function (data) {
                    'onFinished' in that && that.onFinished(data.body);
                    dict.loadText({url: '${base_url}/system/reset_status.json'});
                });
                stompClient.subscribe('/version/failed', function (data) {
                    'onFailed' in that && that.onFailed(data.body);
                    dict.loadText({url: '${base_url}/system/reset_status.json'});
                });

                dict.loadText({url: '${base_url}/system/check_new_version.json'});
                dict.loadText({url: '${base_url}/system/check_update_finished.json'});
                dict.loadText({url: '${base_url}/system/check_update_failed.json'});
            }, 2000);
        });
    };
};
