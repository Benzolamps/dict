<#-- @ftlvariable name="size" type="java.lang.String" -->
<div class="layui-card">
  <div class="layui-card-body">
    <blockquote class="layui-elem-quote">
      当前数据库文件大小：${size}&nbsp;&nbsp;&nbsp;&nbsp;
      <button id="vacuum" class="layui-btn layui-btn-normal layui-btn-sm">
        <i class="layui-icon" style="font-size: 20px;">&#xe666;</i> 清理缓存
      </button>
    </blockquote>
  </div>
</div>

<script type="text/javascript">
  $('#vacuum').click(function () {
    dict.loadText({
      url: 'vacuum.json',
      type: 'get',
      success: function (result, status, request) {
        parent.layer.alert('操作成功', {
          icon: 1,
          title: '操作成功',
          yes: function (index) {
            parent.layer.close(index);
            parent.$('iframe')[0].contentWindow.dict.reload(true);
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
</script>