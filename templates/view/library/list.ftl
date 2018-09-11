<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Library>" -->
<@data_list
  id='libraries'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'name', 'title': '名称', 'sort': true},
    {'field': 'description', 'title': '描述', 'sort': true}
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