/**
 * 系统更新Web Socket
 */
dict.updateSocket = new function () {
  var socket = new SockJS('${base_url}/websocket');
  var stompClient = Stomp.over(socket);
  var that = this;

  /**
   * 连接
   */
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
        stompClient.subscribe('/version/downloaded', function (data) {
          'onDownloaded' in that && that.onDownloaded(data.body);
          dict.loadText({url: '${base_url}/system/die.json'});
        });
        stompClient.subscribe('/version/installed', function (data) {
          'onInstalled' in that && that.onInstalled(data.body);
          dict.loadText({url: '${base_url}/system/die.json'});
        });
        stompClient.subscribe('/version/failed', function (data) {
          'onFailed' in that && that.onFailed(data.body);
          dict.loadText({url: '${base_url}/system/die.json'});
        });

        dict.loadText({url: '${base_url}/system/check_new_version.json'});
        dict.loadText({url: '${base_url}/system/check_downloaded.json'});
        dict.loadText({url: '${base_url}/system/check_installed.json'});
        dict.loadText({url: '${base_url}/system/check_failed.json'});
      }, 2000);
    });
  };

  /**
   * 初始化回调
   */
  that.initCallback = function () {
    dict.updateSocket.onHasNew = function (data) {
      layer.confirm('有新版本, 是否更新？', {
        icon: 1,
        title: '提示',
        id: 'update'
      }, function (index) {
        var path = '${base_url}/system/settings.html';
        var iframeWindow = parent.$('#content-frame')[0].contentWindow;
        if (iframeWindow.location.href.indexOf(path) > -1) {
          iframeWindow.$('.update').trigger('click');
        } else {
          iframeWindow.location.href = path + '#update';
        }
        layer.close(index);
      });
    };

    dict.updateSocket.onDownloaded = function (data) {
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      layer.alert(dict.format('下载完成！<br>共下载 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒', total, totalSize, deltaTime), {icon: 1});
    }

    dict.updateSocket.onInstalled = function (data) {
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      layer.alert(dict.format('安装完成！<br>共更新 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒', total, totalSize, deltaTime), {icon: 1});
    }

    dict.updateSocket.onFailed = function (data) {
      layer.alert('更新失败！', {icon: 2});
    }
  }
};
