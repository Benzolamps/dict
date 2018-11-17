<#-- @ftlvariable name="phraseGroup" type="com.benzolamps.dict.bean.Group" -->
<#-- @ftlvariable name="students" type="java.util.Collection<com.benzolamps.dict.bean.Student>" -->
<div class="layui-card">
  <div class="layui-card-body">
    <form id="extract-personal-group" class="layui-form" method="post" action="${base_url}/phrase_group/extract_personal_group_save.json">
      <#list students as student>
        <input multiple-select="1" type="hidden" name="studentId" value="${student.id}">
      </#list>
      <#if phraseGroup??>
        <input type="hidden" name="groupId" value="${phraseGroup.id}">
      </#if>
      <table class="layui-table">
      </table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>

  <script>
    var $form = $('#extract-personal-group');
    var $table = $('#extract-personal-group table');
    dict.dynamicForm($table, [
      {
        name: "name",
        type: "string",
        minLength: 0,
        maxLength: 20,
        notEmpty: true,
        display: "名称前缀"
      },
      {
        name: "description",
        type: "string",
        minLength: 0,
        maxLength: 50,
        display: "描述"
      }
    ], '', {}, {}, {}, function () {
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