<#-- @ftlvariable name="size" type="java.lang.String" -->
<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  <button id="backup" class="layui-btn layui-btn-normal layui-btn-sm" onclick="location.href = 'backup.zip'">数据库备份</button>
  <button id="restore" class="layui-btn layui-btn-warm layui-btn-sm">数据库恢复</button>
</blockquote>
<form id="zip-form" method="post" action="restore.json" enctype="multipart/form-data" style="display: none;">
  <input type="file" name="file" accept="application/zip">
</form>
<script type="text/javascript">
  $('#restore').click(function () {
    var $file = $('#zip-form input');
    $file.trigger('click');
    $file.unbind('change');
    $file.change(function () {
      var loader = parent.layer.load();
      setTimeout(function () {
        var startTime = new Date().getTime();
        $('#zip-form').ajaxSubmit({
          success: function (result, status, request) {
            var endTime = new Date().getTime();
            var delta = ((endTime - startTime) * 0.001).toFixed(3);
            parent.layer.close(loader);
            parent.layer.alert('备份已恢复！用时 ' + delta + ' 秒！请重启系统服务！', {icon: 1});
          },
          error: function (request) {
            parent.layer.close(loader);
            var result = JSON.parse(request.responseText);
            parent.layer.alert(result.message, {
              icon: 2,
              anim: 6,
              title: result.status
            });
          }
        });
      }, 500);
    });
  });

</script>