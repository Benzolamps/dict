<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Library>" -->
<@data_list
  id='libraries'
  name='词库'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'name', 'title': '名称', 'sort': true, 'minWidth': 120},
    {'field': 'description', 'title': '描述', 'sort': true, 'minWidth': 150},
    {'field': 'words', 'title': '单词数', 'sort': true, 'minWidth': 120},
    {'field': 'phrases', 'title': '短语数', 'sort': true, 'minWidth': 120}
  ]
  page=page
  add='${base_url}/library/add.html'
  edit='${base_url}/library/edit.html'
  delete='${base_url}/library/delete.json'
  page_enabled=true
  delete_confirm='删除词库将会删除其中的所有单词和短语，确定要继续吗？'
  search=[
    {
      'name': 'name',
      'display': '名称',
      'type': 'string'
    },
    {
      'name': 'description',
      'display': '描述',
      'type': 'string'
    }
  ]
/>
