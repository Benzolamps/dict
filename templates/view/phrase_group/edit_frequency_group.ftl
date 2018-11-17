<#-- @ftlvariable name="phraseGroup" type="com.benzolamps.dict.bean.Group" -->
<div class="layui-card">
  <div class="layui-card-body">
    <form id="edit-frequency-group" class="layui-form" method="post" action="save.json">
      <table class="layui-table">
      </table>
      <input type="submit" value="选择文件" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
</div>

<script>
  var $form = $('#edit-frequency-group');
  var $table = $('#edit-frequency-group table');
  var messages = {
    name: {
      remote: '名称已存在'
    }
  };
  dict.dynamicForm($table, <@json_dump obj=get_dict_property('com.benzolamps.dict.bean.Group')/>, '', <@json_dump obj=phraseGroup/>, {}, messages, function () {
    var name = $form.find('input[name=name]').val(), description = $form.find('input[name=description]').val(), id = $form.find('input[name=id]').val();
    dict.uploadFile({
      action: 'edit_frequency_group_save.json',
      data: {
        id: id,
        name: name,
        description: description
      },
      accept: '.doc, .docx, .txt',
      success: function (data, delta) {
        !function (parent) {
          var index = parent.layer.getFrameIndex(window.name);
          parent.layer.close(index);
          if (data.data.hasExtraPhrases) {
            parent.layer.confirm('更新成功！是否要导出词库中没有的短语？', {
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
            parent.layer.alert('更新成功！', {
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