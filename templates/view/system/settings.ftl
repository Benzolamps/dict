<div class="layui-tab layui-tab-card">
  <ul class="layui-tab-title">
    <li class="shutdown">关闭系统服务</li>
    <li class="clean">清理缓存</li>
    <li class="update">系统升级</li>
  </ul>
  <div class="layui-tab-content">
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        <@update_socket/>
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
    <@update_socket/>
  });
  $('.update').click(function () {
    parent.dict.updateSocket.onHasNew = function () {
      $('.update-content').show();
    };

    parent.dict.updateSocket.onAlreadyNew = function () {
      $('.update-content').hide();
      $('.dict-blockquote').html('当前已是最新版本，无需更新！');
    };

    parent.dict.updateSocket.onDownloading = function () {
      $('.update-content').hide();
      $('.dict-blockquote').html('正在下载新版本！');
    };

    parent.dict.updateSocket.onCopying = function () {
      $('.update-content').hide();
      $('.dict-blockquote').html('正在复制文件！');
    };

    parent.dict.updateSocket.onDeleting = function () {
      $('.update-content').hide();
      $('.dict-blockquote').html('正在删除旧版本！');
    };

    parent.dict.updateSocket.onFinished = function () {
      $('.update-content').hide();
      $('.dict-blockquote').html('更新已完成！');
    };

    parent.dict.updateSocket.onFailed = function () {
      $('.update-content').hide();
      $('.dict-blockquote').html('更新失败！');
    };
  });
  var hash = location.hash.slice(1);
  hash || (hash = 'clean');
  $('.' + hash).trigger('click');
</script>
