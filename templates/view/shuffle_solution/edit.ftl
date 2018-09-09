<#-- @ftlvariable name="strategies" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="solution" type="com.benzolamps.dict.bean.ShuffleSolution" -->
<script type="text/javascript">

  var showEdit = function (selector, className) {
    dict.loadText({
      url: 'property_info.json',
      data: {className: '${solution.strategyClass}'},
      dataType: 'json',
      success: function (data) {
        dict.dynamicForm(selector, data.data, 'properties.', <@json_dump obj = solution.properties/>);
      }
    });
  };

  <#assign ready_handler>
    <#if solution.strategyClass??>
      showEdit(append(1), '${solution.strategyClass}');
    </#if>
  </#assign>

  <#assign select_handler>
    !function (data) {
      if (data.value != null && data.value != '') {
        showEdit(append(1), data.value);
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
            'options': [
              <#list strategies as strategy>
                {'id': '${strategy}', 'value': '${strategy}'}<#sep>,
              </#list>
            ],
            'display': '${field.display}',
            'description': '${field.description}',
            'notEmpty': ${field.notEmpty???c},
            'handler': "${select_handler}"
          }
        <#else>
          <@json_dump obj=field/>
        </#if>
        <#sep>,
      </#list>
    ]
  </#assign>
</script>

<@data_edit
  id='shuffle-solutions'
  title='修改乱序方案'
  fields=fields?eval
  values=solution
  messages={
    'name': {'pattern': '乱序方案名称必须是汉字、字母、数字的组合'}
  }
  ready_handler=ready_handler
/>
