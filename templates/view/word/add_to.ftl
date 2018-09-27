<#-- @ftlvariable name="words" type="java.util.Collection<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="groups" type="java.util.Collection<com.benzolamps.dict.bean.Group>" -->
<div class="layui-card">
  <div class="layui-card-body">
    <form id="words-add-to" class="layui-form" method="post" action="${base_url}/word_group/add_words.json">
      <#list words as word>
        <input multiple-select="1" type="hidden" name="wordId" value="${word.id}">
      </#list>
      <table class="layui-table">
      </table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>

  <script>
    var $form = $('#words-add-to');
    var $table = $('#words-add-to table');
    var value = [
      {
        name: 'groupId',
        display: '单词分组',
        notEmpty: true,
        type: 'integer',
        multiple: true,
        options: [
          <#list groups as group>{id: ${group.id}, value: '${group.name}'}, </#list>
        ]
      }
    ];
    dict.dynamicForm($table, value, '', {}, {}, {}, function () {
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