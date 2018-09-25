<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Group>" -->
<@data_list
  id='word-groups'
  name='单词分组'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'name', 'title': '名称', 'sort': true, 'minWidth': 120},
    {'field': 'description', 'title': '描述', 'sort': true, 'minWidth': 120},
    {'field': 'status', 'title': '状态', 'sort': true, 'minWidth': 10},
    {'field': 'studentsOriented', 'title': '分组中的的学生数', 'sort': true, 'minWidth': 150},
    {'field': 'studentsScored', 'title': '已评分的学生数', 'sort': true, 'minWidth': 150},
    {'field': 'words', 'title': '单词数', 'sort': true, 'minWidth': 10}
  ]
  page=page
  add='${base_url}/word_group/add.html'
  edit='${base_url}/word_group/edit.html'
  delete='${base_url}/word_group/delete.json'
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
