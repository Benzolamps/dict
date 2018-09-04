<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->

<@nothing><script type="text/javascript"></@nothing>
<#assign values>
  [
    <#list page.content as word>
      {
        'id': ${word.id},
        'prototype': <@json_dump obj=word.prototype/>,
        'britishPronunciation': <@json_dump obj=word.britishPronunciation/>,
        'americanPronunciation': <@json_dump obj=word.americanPronunciation/>,
        'clazzes': [<#list word.clazzes as clazz><@json_dump obj=clazz.name/><#sep>, </#list>],
        'definition': <@json_dump obj=word.definition/>
      }
    </#list>
  ]
</#assign>

<#assign file_upload>
  var button = $(document.createElement('button'))
    .css('display', 'hidden');
  layui.upload.render({
    elem: button,
    url: '${base_url}/word/import.json',
    accept: 'file',
    acceptMime: 'application/x-xls',
    exts: 'xls|xlsx',
    done: function (res) {
      console.log(arguments);
      parent.$('iframe')[0].contentWindow.location.reload(true);
    },
    error: function () {
      console.log(arguments)
      parent.layer.alert('错误', {
        icon: 2,
        title: '错误'
      });
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
