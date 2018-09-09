<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->
<@nothing><script type="text/javascript"></@nothing>
<#assign fields>
  [
    <#list get_dict_property('com.benzolamps.dict.controller.vo.WordVo') as field>
      <#-- @ftlvariable name="field" type="com.benzolamps.dict.controller.vo.DictPropertyInfoVo" -->
      <#if field.name == 'clazzes'>
        {
          'name': '${field.name}',
          'type': 'string',
          'options': [
            <#list wordClazzes as wordClazz>
              {'id': ${wordClazz.id}, 'value': '${wordClazz.name}'}<#sep>,
            </#list>
          ],
          'multiple': true,
          'display': '${field.display}',
          'description': '${field.description}',
          'notEmpty': ${field.notEmpty???c}
        }
      <#else>
        <@json_dump obj=field/>
      </#if>
      <#sep>,
    </#list>
  ]
</#assign>
<@nothing></script></@nothing>

<@data_add
  id='words'
  title='添加单词'
  fields=fields?eval
/>