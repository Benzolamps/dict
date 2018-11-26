<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  <button id="file-execute" class="layui-btn layui-btn-normal layui-btn-sm">执行SQL脚本文件</button>
</blockquote>
<div class="layui-row">
  <div class="layui-card" style="width: 800px;">
    <div class="layui-card-body" style="font-family: Consolas, Helvetica Neue, Helvetica, PingFang SC, Tahoma, Arial, sans-serif">
    <textarea id="sql" cols="120" rows="20" style="resize: none" autocomplete="off"></textarea>
    <input id="sql-execute" value="执行" type="button" class="layui-btn layui-btn-normal layui-btn-disabled" style="margin-top: 10px;">
    </div>
  </div>
</div>

<script type="text/javascript">
  var $sqlExecute = $('#sql-execute');
  var $sql = $('#sql');
  $sql.bind('change blur mouseleave keydown keyup keypress input', function () {
    var value = $(this).val();
    if (value != null && $.trim(value) != '') {
      $sqlExecute.removeClass('layui-btn-disabled');
    } else {
      $sqlExecute.addClass('layui-btn-disabled');
    }
  });

  $sqlExecute.click(function () {
    if (!$(this).is('.layui-btn-disabled')) {
      dict.loadText({
        url: 'sql.json',
        type: 'post',
        data: {
          type: 'sql',
          sql: $.trim($sql.val())
        },
        success: function (result, status, request) {
          parent.layer.alert('操作成功！', {icon: 1});
        },
        error: function (result, status, request) {
          parent.layer.alert(result.message, {
            icon: 2,
            title: result.status
          });
        }
      });
    }
  });

  $('#file-execute').click(function () {
    dict.uploadFile({
      action: 'sql.json',
      accept: '.sql, .txt',
      success: function (data, delta) {
        parent.layer.alert('操作成功！', {icon: 1});
      }
    });
  });
</script>