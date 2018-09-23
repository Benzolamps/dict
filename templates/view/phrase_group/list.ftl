<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Group>" -->
<@data_list
  id='phrase-groups'
  name='短语分组'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'name', 'title': '名称', 'sort': true},
    {'field': 'description', 'title': '描述', 'sort': true},
    {'field': 'status', 'title': '状态', 'sort': true},
    {'field': 'studentsOriented', 'title': '分组中的的学生数', 'sort': true},
    {'field': 'studentsScored', 'title': '已评分的学生数', 'sort': true},
    {'field': 'phrases', 'title': '短语数', 'sort': true}
  ]
  page=page
  add='${base_url}/phrase_group/add.html'
  edit='${base_url}/phrase_group/edit.html'
  delete='${base_url}/phrase_group/delete.json'
  page_enabled=true
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
    },
    {
      'name': 'status',
      'display': '状态',
      'type': 'string',
      'options': [
        {
          'id': 0,
          'value': '正常'
        },
        {
          'id': 1,
          'value': '评分中'
        },
        {
          'id': 2,
          'value': '已完成'
        }
      ]
    }
  ]
/>
