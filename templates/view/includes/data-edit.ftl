<#macro data_edit id fields values={} rules={} messages={}
  title='' update='update.json' request_body=true
  submit_handler='return true;' error_handler='return true;' ready_handler='return true;'
>
  <#-- @ftlvariable name="id" type="java.lang.String" -->
  <#-- @ftlvariable name="fields" type="java.util.List<com.benzolamps.dict.controller.vo.DictPropertyInfoVo>" -->
  <#-- @ftlvariable name="values" type="java.util.Map" -->
  <#-- @ftlvariable name="rules" type="java.util.Map" -->
  <#-- @ftlvariable name="messages" type="java.util.Map" -->
  <#-- @ftlvariable name="title" type="java.lang.String" -->
  <#-- @ftlvariable name="update" type="java.lang.String" -->
  <#-- @ftlvariable name="submit_handler" type="java.lang.String" -->
  <#-- @ftlvariable name="error_handler" type="java.lang.String" -->
  <#-- @ftlvariable name="ready_handler" type="java.lang.String" -->
  <#-- @ftlvariable name="request_body" type="java.lang.Boolean" -->
  <div class="layui-card">
    <div class="layui-card-body">
      <form class="layui-form" id="${id}" method="post" action="${update}">
        <table class="layui-table"></table>
        <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
      </form>
    </div>
  </div>
  <script type="text/javascript">
    var $form = $('#${id}');
    var $table = $('#${id} table');
    var $tbody = $(document.createElement('tbody'));
    var fields = <@json_dump obj=fields/>;
    var layerIndex = parent.layer.getFrameIndex(window.name);
    <#if title != ''>parent.layer.title('${title}', layerIndex);</#if>
    $table.append($tbody);
    dict.dynamicForm($tbody, fields, '', <@json_dump obj=values/>, <@json_dump obj=rules/>, <@json_dump obj=messages/>, function () {
      dict.loadText({
        url: $form.attr('action'),
        type: $form.attr('method'),
        data: dict.generateObject(dict.serializeObject($form)),
        requestBody: ${request_body?c},
        success: function (result, status, request) {
          !function (parent) {
            parent.layer.close(layerIndex);
            parent.layer.alert('修改成功！', {
              icon: 1,
              end: function () {
                if ((function () {
                  ${submit_handler}
                })()) {
                  parent.$('iframe')[0].contentWindow.dict.reload(true);
                }
              }
            });
          }(parent);
        },
        error: function (result, status, request) {
          if ((function () {
            ${error_handler}
          })()) {
            parent.layer.alert(result.message, {
              icon: 2,
              title: result.status
            });
          }
        }
      });
      return false;
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

    for (var i = 0; i < fields.length; i++) !function (i) {
      if (fields[i].handler && fields[i].options) {
        form.on('select(' + fields[i].name + ')', function (data) {
          eval(fields[i].handler);
        });
      } else if (fields[i].type && fields[i].type == 'boolean') {
        form.on('switch(' + fields[i].name + ')', function (data) {
          eval(fields[i].handler);
        });
      }
    }(i);

    !function () {
      ${ready_handler}
    }();
  </script>
</#macro>