<#-- @ftlvariable name="size" type="java.lang.String" -->
<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  当前数据库文件大小：${size}&nbsp;&nbsp;&nbsp;&nbsp;
  <button id="clean" class="layui-btn layui-btn-normal layui-btn-sm">
    <i class="fa fa-rocket" style="font-size: 20px;"></i> &nbsp; 清理缓存
  </button>
</blockquote>

<script type="text/javascript">
  $('#clean').click(function () {
    dict.loadText({
      url: 'vacuum.json',
      type: 'get',
      success: function (result, status, request) {
        parent.layer.alert('操作成功', {
          icon: 1,
          title: '操作成功',
          end: function () {
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