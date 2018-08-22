<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->
<@data_list
  id='shuffle-solutions'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'name', 'title': '随机方案名称', 'sort': true},
    {'field': 'strategyClass', 'title': '随机策略名称', 'sort': true},
    {'field': 'remark', 'title': '备注', 'sort': true}
  ]
  values=page.content
  add='${base_url}/shuffle_solution/add.html'
  edit='${base_url}/shuffle_solution/edit.html'
  delete='${base_url}/shuffle_solution/delete.json'
  page_enabled=false
/>
