<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  <button id="shutdown" class="layui-btn layui-btn-danger layui-btn-sm">
    <i class="fa fa-power-off" style="font-size: 20px;"></i> &nbsp; 关闭系统服务
  </button>
</blockquote>

<script type="text/javascript">
  $('#shutdown').click(function () {
    parent.layer.confirm('确定要关闭系统服务？', {icon: 3}, function () {
      parent.layer.confirm('关闭系统服务后所有的功能将不可用，网页将无法访问，确定要继续吗？', {icon: 3}, function (index) {
        parent.layer.close(index);
        dict.loadText({
          url: 'shutdown.json',
          type: 'post',
          success: function (result, status, request) {
            parent.layer.alert('操作成功！', {
              icon: 1,
              end: function () {
                parent.location.reload(true);
              }
            });
          },
          error: function (result, status, request) {
            parent.layer.alert(result.message, {
              icon: 2,
              title: result.status
            });
          }
        });
      });
    });
  });
</script>