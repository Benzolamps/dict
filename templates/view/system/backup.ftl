<#-- @ftlvariable name="size" type="java.lang.String" -->
<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  <button id="backup" class="layui-btn layui-btn-normal layui-btn-sm" onclick="dict.postHref('backup.zip');">数据库备份</button>
  <button id="restore" class="layui-btn layui-btn-warm layui-btn-sm">数据库恢复</button>
</blockquote>
<script type="text/javascript">
  $('#restore').click(function () {
    dict.uploadFile({
      action: 'restore.json',
      accept: 'application/zip',
      success: function (data, delta) {
        parent.layer.close(loader);
        parent.layer.alert('备份已恢复！请重启系统服务！', {icon: 1});
      }
    });
  });
</script>