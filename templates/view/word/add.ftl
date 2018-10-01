<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->
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
<@data_add
  id='words'
  fields=fields?eval
  messages={
      'prototype': {
      'remote': '该单词已存在'
    }
  }
/>