<div class="layui-tab layui-tab-card">
  <ul class="layui-tab-title">
    <li class="clean">清理缓存</li>
    <li class="update">系统升级</li>
    <li class="shutdown">关闭系统服务</li>
  </ul>
  <div class="layui-tab-content">
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
    <div class="layui-tab-item" style="min-height: 400px;">
      <script type="text/javascript">
        document.write(dict.loadText({url: '${base_url}/system/shutdown.html'}));
      </script>
    </div>
  </div>
</div>

<script type="text/javascript">
  var hash = location.hash.slice(1);
  hash || (hash = 'clean');
  $('.' + hash).trigger('click');
</script>