<div class="layui-tab layui-tab-card">
  <ul class="layui-tab-title">
    <li class="shutdown">关闭系统服务</li>
    <li class="clean">清理缓存</li>
    <li class="backup">数据库备份</li>
    <li class="sql">执行SQL脚本</li>
    <li class="update">系统升级</li>
    <li class="lan">局域网操作</li>
  </ul>
  <div class="layui-tab-content">
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/shutdown.html', loading: false}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/clean.html', loading: false}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/backup.html', loading: false}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/sql.html', loading: false}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/update.html', loading: false}));
      </script>
    </div>
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/lan.html', loading: false}));
      </script>
    </div>
  </div>
</div>

<script type="text/javascript">

  var updateButton = $('.update-button');
  var updateContent = $('.update-content');

  var callback = new function () {
    this.onHasNew = function (data) {
      data = JSON.parse(data).data;
      var newVersionName = data.newVersionName;
      updateButton.show();
      updateContent.html('检测到新版本：' + newVersionName);
    };

    this.onAlreadyNew = function () {
      updateButton.show();
      updateButton.children().eq(0).hide();
      updateContent.html('当前已是最新版本，无需更新！');
    };

    this.onDownloading = function () {
      updateButton.hide();
      updateContent.html('正在下载新版本！');
    };

    this.onDownloaded = function (data) {
      updateButton.hide();
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      updateContent.html(dict.format('下载完成！<br>共下载 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒！', total, totalSize, deltaTime));
    };

    this.onInstalled = function (data) {
      updateButton.hide();
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      updateContent.html(dict.format('安装完成！<br>共安装 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒！', total, totalSize, deltaTime));
    };

    this.onFailed = function () {
      updateButton.show();
      updateButton.children().eq(0).html('<i class=\"fa fa-puzzle-piece" style="font-size: 20px;"></i> &nbsp; 重试');
      updateContent.html('更新失败！');
    };
  };

  $('.clean,.shutdown,.backup,.sql,.lan').click(function () {
    parent.dict.updateSocket.initCallback();
    $.each(callback, function (key, value) {
      parent.dict.updateSocket[key] = dict.extendsFunction(parent.dict.updateSocket[key], value);
    });
  });

  $('.update').click(function () {
    $.each(callback, function (key, value) {
      parent.dict.updateSocket[key] = value;
    });
  });
  var hash = location.hash.slice(1);
  hash || (hash = 'clean');
  $('.' + hash).trigger('click');
</script>
