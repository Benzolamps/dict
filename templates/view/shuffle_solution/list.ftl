<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->
<@data_list
  id='shuffle-solutions'
  name='乱序方案'
  fields=[
    {'field': 'name', 'title': '随机方案名称', 'minWidth': 150},
    {'field': 'strategyClass', 'title': '随机策略名称', 'minWidth': 150},
    {'field': 'remark', 'title': '备注', 'minWidth': 150}
  ]
  page=page
  add='${base_url}/shuffle_solution/add.html'
  edit='${base_url}/shuffle_solution/edit.html'
  delete='${base_url}/shuffle_solution/delete.json'
  page_enabled=false
/>
