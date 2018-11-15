<div class="layui-card">
  <div class="layui-card-body">
    <form id="add-frequency-group" class="layui-form" method="post" action="save.json">
      <table class="layui-table">
      </table>
      <input type="submit" value="选择文件" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
</div>

<script>
  var $form = $('#add-frequency-group');
  var $table = $('#add-frequency-group table');
  var messages = {
    name: {
      remote: '名称已存在'
    }
  };
  dict.dynamicForm($table, <@json_dump obj=get_dict_property('com.benzolamps.dict.bean.Group')/>, '', {}, {}, messages, function () {
    var name = $form.find('input[name=name]').val(), description = $form.find('input[name=description]').val();
    dict.uploadFile({
      action: 'add_frequency_group_save.json',
      data: {
        name: name,
        description: description
      },
      accept: '.doc, .docx, .txt',
      success: function (data, delta) {
        !function (parent) {
          var index = parent.layer.getFrameIndex(window.name);
          parent.layer.close(index);
          if (data.data.hasExtraWords) {
            parent.layer.confirm('添加成功！是否要导出词库中没有的单词？', {
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
      }
    });
    return false;
  });
</script>