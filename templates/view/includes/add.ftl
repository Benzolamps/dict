<#-- @ftlvariable name="id" type="java.lang.String" -->
<#-- @ftlvariable name="fields" type="java.util.List<com.benzolamps.dict.controller.vo.DictPropertyInfoVo>" -->
<#-- @ftlvariable name="values" type="java.util.Map" -->
<#-- @ftlvariable name="rules" type="java.util.Map" -->
<#-- @ftlvariable name="messages" type="java.util.Map" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="save" type="java.lang.String" -->
<#-- @ftlvariable name="submit_handler" type="java.lang.String" -->
<div class="layui-card">
  <div class="layui-card-header">${title}</div>
  <div class="layui-card-body">
    <form class="layui-form" id="${id}" method="post" action="${save}">
      <table class="layui-table"></table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
</div>
<script type="text/javascript" src="${base_url}/res/js/dynamic-form.js"></script>
<script type="text/javascript">
  layui.use('form', function() {
    var form = layui.form;
    var $form = $('#${id}');
    var $table = $('#${id} table');
    var $tbody = $(document.createElement('tbody'));
    $table.append($tbody);
    dict.dynamicForm($tbody, <@json_dump obj = fields/>, '', {}, <@json_dump obj = rules/>, <@json_dump obj = messages/>, function () {
      ${submit_handler}
    });

    var append = function () {
      var $tbody = $(document.createElement('tbody'));
      $table.append($tbody);
      return $tbody;
    };

    var submit = function () {
      document.getElementById('${id}').submit();
    };

    <#list fields as field>
      <#-- @ftlvariable name="field.handler" type="java.lang.String" -->
      <#if field.options?? && field.handler??>
        form.on('select(${field.name})', function (data) {
          ${field.handler}
        });
      <#elseif field.type?? && field.type == 'boolean'>
        form.on('switch(${field.name})', function (data) {
          ${field.handler}
        });
      </#if>
    </#list>
  });
</script>