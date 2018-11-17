<#-- @ftlvariable name="phraseGroup" type="com.benzolamps.dict.bean.Group" -->
<#-- @ftlvariable name="phrases" type="java.util.Collection<com.benzolamps.dict.bean.Phrase>" -->
<#-- @ftlvariable name="students" type="java.util.Collection<com.benzolamps.dict.bean.Student>" -->
<div class="layui-card">
  <div class="layui-card-body">
    <form id="extract-derive-group" class="layui-form" method="post" onsubmit="return false;" action="${base_url}/phrase_group/extract_derive_group_save.json">
      <#if phrases??>
        <#list phrases as phrase>
          <input multiple-select="1" type="hidden" name="phraseId" value="${phrase.id}">
        </#list>
      </#if>
      <#if students??>
        <#list students as student>
          <input multiple-select="1" type="hidden" name="studentId" value="${student.id}">
        </#list>
      </#if>
      <#if phraseGroup??>
        <input type="hidden" name="groupId" value="${phraseGroup.id}">
      </#if>
      <table class="layui-table">
      </table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>

  <script>
    var $form = $('#extract-derive-group');
    var $table = $('#extract-derive-group table');
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