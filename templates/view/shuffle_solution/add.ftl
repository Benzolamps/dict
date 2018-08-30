<#-- @ftlvariable name="strategies" type="java.util.Collection<java.lang.String>" -->
<@nothing><script type="text/javascript"></@nothing>
<#assign submit_handler>
  !function (result, status, request) {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
    parent.$('iframe')[0].contentWindow.location.reload(true);
  }(result, status, request);
</#assign>

<#assign error_handler>
  !function (result, status, request) {
    parent.layer.alert(result.message, {
      icon: 2,
      title: result.status,
      yes: function (index) {
        parent.layer.close(index);
        parent.layer.close(parent.layer.getFrameIndex(window.name));
      },
      cancel: function (index) {
        parent.layer.close(index);
        parent.layer.close(parent.layer.getFrameIndex(window.name));
      }
    });
  }(result, status, request);
</#assign>

<#assign select_handler>
  !function (data) {
    if (data.value != null && data.value != '') {
      dict.loadText({
        url: 'property_info.json',
        data: {className: data.value},
        dataType: 'json',
        success: function (data) {
          dict.dynamicForm(append(1), data.data, 'properties.');
        }
      });
    } else {
      append(1).empty();
    }
  }(data);
</#assign>

<#assign fields>
  [
    <#list get_dict_property('com.benzolamps.dict.bean.ShuffleSolution') as field>
      <#-- @ftlvariable name="field" type="com.benzolamps.dict.controller.vo.DictPropertyInfoVo" -->
      <#if field.name == 'strategyClass'>
        {
          'name': '${field.name}',
          'type': '${field.type}',
          'options': <@json_dump obj=strategies/>,
          'display': '${field.display}',
          'description': '${field.description}',
          'notEmpty': ${field.notEmpty?c},
          'handler': "${select_handler}"
        }
      <#else>
        <@json_dump obj=field/>
      </#if>
      <#sep>,
    </#list>
  ]
</#assign>
<@nothing>;</@nothing>
<@nothing></script></@nothing>

<@data_add
  id='shuffle_solution'
  title='添加乱序方案'
  fields=fields?eval
  messages={
    'name': {'pattern': '乱序方案名称必须是汉字、字母、数字的组合'}
  }
  submit_handler=submit_handler
  error_handler=error_handler
/>