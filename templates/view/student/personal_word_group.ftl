<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="words" type="java.util.Collection<com.benzolamps.dict.bean.Word>" -->
<div class="layui-card">
  <div class="layui-card-body">
    <form id="personal-word-group" class="layui-form" method="post" action="${base_url}/student/personal_word_group_save.json">
      <#list words as word>
        <input multiple-select="1" type="hidden" name="wordId" value="${word.id}">
      </#list>
      <input type="hidden" name="studentId" value="${student.id}">
      <table class="layui-table">
      </table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>

  <script>
    var $form = $('#personal-word-group');
    var $table = $('#personal-word-group table');
    var value = <@json_dump obj=get_dict_property('com.benzolamps.dict.bean.Group')/>;
    dict.dynamicForm($table, value, '', {}, {}, {
      name: {
        remote: '名称已存在'
      }
    }, function () {
      dict.loadText({
        url: $form.attr('action'),
        type: $form.attr('method'),
        data: dict.generateObject(dict.serializeObject($form)),
        success: function (result, status, request) {
          !function (parent) {
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
            parent.layer.alert('操作成功！', {icon: 1});
          }(parent);
        },
        error: function (result, status, request) {
          parent.layer.alert(result.message, {
            icon: 2,
            title: result.status
          });
        }
      });
      return false;
    });
  </script>
</div>