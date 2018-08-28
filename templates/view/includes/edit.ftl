<#-- @ftlvariable name="id" type="java.lang.String" -->
<#-- @ftlvariable name="fields" type="java.util.List<com.benzolamps.dict.controller.vo.DictPropertyInfoVo>" -->
<#-- @ftlvariable name="values" type="java.util.Map" -->
<#-- @ftlvariable name="rules" type="java.util.Map" -->
<#-- @ftlvariable name="messages" type="java.util.Map" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="update" type="java.lang.String" -->
<#-- @ftlvariable name="submit_handler" type="java.lang.String" -->
<#-- @ftlvariable name="ready_handler" type="java.lang.String" -->
<div class="layui-card">
  <div class="layui-card-header">${title}</div>
  <div class="layui-card-body">
    <form class="layui-form" id="${id}" method="post" action="${update}">
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
    var fields = <@json_dump obj=fields/>;
    $table.append($tbody);
    dict.dynamicForm($tbody, fields, '', <@json_dump obj=values/>, <@json_dump obj=rules/>, <@json_dump obj=messages/>, function () {
      ${submit_handler}
    });

    var append = function (index) {
      if ($table.find('tbody').length > index) {
          return $table.find('tbody').eq(index);
      } else {
        var $tbody = $(document.createElement('tbody'));
        $table.append($tbody);
        return $tbody;
      }
    };

    var submit = function () {
      document.getElementById('${id}').submit();
    };

    for (var i = 0; i < fields.length; i++) {
      (function (i) {
        if (fields[i].handler && fields[i].options) {
          form.on('select(' + fields[i].name + ')', function (data) {
            eval(fields[i].handler);
          });
        } else if (fields[i].type && fields[i].type == 'boolean') {
          form.on('switch(' + fields[i].name + ')', function (data) {
            eval(fields[i].handler);
          });
        }
      })(i);
    }

    ${ready_handler}
  });
</script>