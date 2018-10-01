<#-- @ftlvariable name="clazzes" type="java.util.Collection<com.benzolamps.dict.bean.Clazz>" -->
<#assign fields>
  [
    <#list get_dict_property('com.benzolamps.dict.controller.vo.StudentVo') as field>
      <#-- @ftlvariable name="field" type="com.benzolamps.dict.controller.vo.DictPropertyInfoVo" -->
      <#if field.name == 'clazz'>
        {
          'name': '${field.name}',
          'type': 'string',
          'options': [
            <#list clazzes as clazz>
              {'id': ${clazz.id}, 'value': '${clazz.name}'}<#sep>,
            </#list>
          ],
          'display': '${field.display}',
          'description': '${field.description}',
          'notEmpty': '${field.notEmpty?c}'
        }
      <#else>
        <@json_dump obj=field/>
      </#if>
      <#sep>,
    </#list>
  ]
</#assign>

<@data_add
  id='students'
  fields=fields?eval
  messages={
    'number': {
      'remote': '学号已存在'
    }
  }
/>