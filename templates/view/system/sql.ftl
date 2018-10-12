<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  <button id="file-execute" class="layui-btn layui-btn-normal layui-btn-sm">执行SQL脚本文件</button>
  <form id="file-form" method="post" action="sql.json" enctype="multipart/form-data" style="display: none;">
    <input type="file" name="file" accept=".sql">
  </form>
</blockquote>
<div class="layui-row">
  <div class="layui-card" style="width: 800px;">
    <div class="layui-card-body">
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
        url: '${base_url}/system/sql.json',
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
    var $file = $('#file-form input');
    $file.trigger('click');
    $file.unbind('change');
    $file.change(function () {
      var loader = parent.layer.load();
      setTimeout(function () {
        console.log('kkk');
        $('#file-form').ajaxSubmit({
          success: function (result, status, request) {
            parent.layer.close(loader);
            parent.layer.alert('操作成功！', {icon: 1});
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