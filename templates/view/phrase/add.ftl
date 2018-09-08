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

<@nothing>;</@nothing>
<@nothing></script></@nothing>

<@data_add
  id='phrases'
  title='添加短语'
  fields=get_dict_property('com.benzolamps.dict.controller.vo.PhraseVo')
  submit_handler=submit_handler
  error_handler=error_handler
/>