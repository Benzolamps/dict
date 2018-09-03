<#-- @ftlvariable name="word" type="com.benzolamps.dict.bean.Word" -->
<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->
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
      title: result.status
    });
  }(result, status, request);
</#assign>

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
<@nothing>;</@nothing>
<@nothing></script></@nothing>

<@data_edit
  id='words'
  title='修改单词'
  fields=fields?eval
  values=word
  submit_handler=submit_handler
  error_handler=error_handler
/>
