<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->

<@nothing><script type="text/javascript"></@nothing>
<#assign values>
  [
    <#list page.content as word>
      {
        'id': ${word.id},
        'prototype': '${word.prototype}',
        'britishPronunciation': '${word.britishPronunciation}',
        'americanPronunciation': '${word.americanPronunciation}',
        'clazzes': [<#list word.clazzes as clazz>'${clazz.name}'<#sep>, </#list>],
        'definition': '${word.definition}'
      }
    </#list>
  ]
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
      'html': 'hhh',
      'handler': 'console.log(this);',
      'needSelected': true
    }
  ]
  page_enabled=true
/>
