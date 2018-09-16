<div class="layui-tab layui-tab-card">
  <ul class="layui-tab-title">
    <li class="shutdown">关闭系统服务</li>
    <li class="clean">清理缓存</li>
    <li class="update">系统升级</li>
  </ul>
  <div class="layui-tab-content">
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/shutdown.html'}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/clean.html'}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/update.html'}));
      </script>
    </div>
  </div>
</div>

<script type="text/javascript">
  $('.clean,.shutdown').click(function () {
    parent.dict.updateSocket.initCallback();
  });

  $('.update').click(function () {
    parent.dict.updateSocket.onHasNew = function (data) {
      data = JSON.parse(data).data;
      var newVersionName = data.newVersionName;
      $('.update-button').show();
      $('.update-content').html('检测到新版本：' + newVersionName);
    };

    parent.dict.updateSocket.onAlreadyNew = function () {
      $('.update-button').show();
      $('.update-button').children(0).hide();
      $('.update-content').html('当前已是最新版本，无需更新！');
    };

    parent.dict.updateSocket.onDownloading = function () {
      $('.update-button').hide();
      $('.update-content').html('正在下载新版本！');
    };

    parent.dict.updateSocket.onDownloaded = function (data) {
      $('.update-button').hide();
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      $('.update-content').html(dict.format('下载完成！<br>共更新 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒！', total, totalSize, deltaTime));
    };

    parent.dict.updateSocket.onInstalled = function (data) {
      $('.update-button').hide();
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      $('.update-content').html(dict.format('安装完成！<br>共更新 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒！', total, totalSize, deltaTime));
    };

    parent.dict.updateSocket.onFailed = function () {
      $('.update-button').show();
      $('.update-button').children().eq(0).html('<i class=\"fa fa-puzzle-piece" style="font-size: 20px;"></i> &nbsp; 重试');
      $('.update-content').html('更新失败！');
    };
  });
  var hash = location.hash.slice(1);
  hash || (hash = 'clean');
  $('.' + hash).trigger('click');
</script>
