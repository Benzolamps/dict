<#-- @ftlvariable name="phrase" type="com.benzolamps.dict.bean.Phrase" -->
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

<@data_edit
  id='phrase'
  title='修改短语'
  fields=get_dict_property('com.benzolamps.dict.controller.vo.PhraseVo')
  values=phrase
  submit_handler=submit_handler
  error_handler=error_handler
/>
