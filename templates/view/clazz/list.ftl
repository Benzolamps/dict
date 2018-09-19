<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Library>" -->
<@data_list
  id='clazzes'
  name='班级'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'name', 'title': '名称', 'sort': true},
    {'field': 'description', 'title': '描述', 'sort': true},
    {'field': 'studentsCount', 'title': '学生数', 'sort': true}
  ]
  page=page
  add='${base_url}/clazz/add.html'
  edit='${base_url}/clazz/edit.html'
  delete='${base_url}/clazz/delete.json'
  page_enabled=true
  delete_confirm='删除班级将会删除其中的所有学生，确定要继续吗？'
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
