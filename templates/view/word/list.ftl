<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->
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
values=page.content
add='${base_url}/word/add.html'
edit='${base_url}/word/edit.html'
delete='${base_url}/word/delete.json'
page_enabled=true
/>
