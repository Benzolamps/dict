<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->

<@nothing><script type="text/javascript"></@nothing>
<#assign values>
  [
    <#list page.content as word>
      {
        'id': ${word.id},
        'prototype': "${word.prototype?json_string}",
        'britishPronunciation': "${word.britishPronunciation?json_string}",
        'americanPronunciation': "${word.americanPronunciation?json_string}",
        'clazzes': [<#list word.clazzes as clazz>"${clazz.name?json_string}"<#sep>, </#list>],
        'definition': "${word.definition?json_string}"
      }
    </#list>
  ]
</#assign>

<#assign file_upload>
  var button = $(document.createElement('button'))
    .css('display', 'hidden');
  var upload = layui.upload;

  //执行实例
  var uploadInst = upload.render({
    elem: button,
    url: '${base_url}/word/import.json',
    accept: 'file',
    acceptMime: 'application/x-xls',
    exts: 'xls|xlsx',
    done: function (res) {
      //上传完毕回调
    },
    error: function () {
      //请求异常回调
    }
  });

  button.trigger('click');
</#assign>
<@nothing></script></@nothing>
<@data_list
  id='words'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'prototype', 'title': '单词原形', 'sort': true},
    {'field': 'britishPronunciation', 'title': '英式发音', 'sort': true},
    {'field': 'americanPronunciation', 'title': '英式发音', 'sort': true},
    {'field': 'clazzes', 'title': '词性', 'sort': true},
    {'field': 'definition', 'title': '词义', 'sort': true}
  ]
  values=values?eval
  add='${base_url}/word/add.html'
  edit='${base_url}/word/edit.html'
  delete='${base_url}/word/delete.json'
  head_toolbar=[
    {
      'html': '<i class="layui-icon" style="font-size: 20px;">&#xe60a;</i> 导入单词',
      'handler': file_upload
    }
  ]
  page_enabled=true
/>
