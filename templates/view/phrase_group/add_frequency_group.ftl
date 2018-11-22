<div class="layui-card">
  <div class="layui-card-body">
    <form id="add-frequency-group" class="layui-form" method="post">
      <table class="layui-table">
        <tbody class="properties"></tbody>
        <tbody>
        <tr>
          <th>内容</th>
          <td>
            <textarea name="content" rows="10" class="layui-input layui-textarea" style="resize: none; margin-top: 10px;" autocomplete="off"  placeholder="请输入内容"></textarea>
          </td>
        </tr>
        </tbody>
      </table>
      <input type="submit" value="选择文件" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
</div>

<script>
  var $form = $('#add-frequency-group');
  var $table = $('#add-frequency-group table .properties');
  var $contentSubmit = $form.find('input[type=submit]');
  var $content = $form.find('textarea[name=content]');
  $content.bind('change blur mouseleave keydown keyup keypress input', function () {
    var value = $(this).val();
    if (value != null && $.trim(value) != '') {
      $contentSubmit.val('提交文本');
    } else {
      $contentSubmit.val('选择文件');
    }
  });
  var messages = {
    name: {
      remote: '名称已存在'
    }
  };
  dict.dynamicForm($table, <@json_dump obj=get_dict_property('com.benzolamps.dict.bean.Group')/>, '', {}, {}, messages, function () {
    var name = $form.find('input[name=name]').val();
    var description = $form.find('input[name=description]').val();
    var content = $content.val();

    var successCallback = function (data) {
      !function (parent) {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
        if (data.data.hasExtraPhrases) {
          parent.layer.confirm('添加成功！是否要导出词库中没有的短语？', {
            icon: 3,
            end: function () {
              parent.$('iframe')[0].contentWindow.dict.reload(true);
            }
          }, function (index) {
            parent.layer.close(index);
            parent.dict.postHref('${base_url}/doc/download', {
              fileName: name,
              token: data.data.token
            });
          });
        } else {
          parent.layer.alert('添加成功！', {
            icon: 1,
            end: function () {
              parent.$('iframe')[0].contentWindow.dict.reload(true);
            }
          });
        }
      }(parent);
    };

    if ($.trim(content) != '') {
      dict.loadText({
        url: 'add_frequency_group_save.json',
        type: 'post',
        data: {
          name: name,
          description: description,
          content: content
        },
        dataType: 'json',
        success: successCallback,
        error: function (result, status, request) {
          parent.layer.alert(result.message, {
            icon: 2,
            title: result.status
          });
        }
      });
    } else {
      dict.uploadFile({
        action: 'add_frequency_group_save.json',
        data: {
          name: name,
          description: description
        },
        accept: '.doc, .docx, .txt',
        success: successCallback
      });
    }
    return false;
  });
</script>