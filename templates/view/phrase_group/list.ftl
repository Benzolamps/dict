<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Group>" -->

<#list page.searches as search>
  <#if search.field == 'studentId'><#assign student_id = search.value/></#if>
</#list>

<#assign search>
  [
    {'name': 'name', 'display': '名称', 'type': 'string'},
    {'name': 'description', 'display': '描述', 'type': 'string'},
    {
      'name': 'status',
      'display': '状态',
      'type': 'string',
      'options': [
        {'id': 0, 'value': '正常'},
        {'id': 1, 'value': '评分中'},
        {'id': 2, 'value': '已完成'}
      ]
    }
  <#if student_id??>
      , {'name': 'studentId', 'display': '学生学号', 'type': 'integer', 'readonly': true}
      , {'name': 'studentName', 'display': '学生姓名', 'type': 'string', 'readonly': true}
  </#if>
  ]
</#assign>

<@data_list
  id='phrase-groups'
  name='短语分组'
  fields=[
    {'field': 'name', 'title': '名称', 'sort': true, 'minWidth': 120},
    {'field': 'description', 'title': '描述', 'sort': true, 'minWidth': 120},
    {'field': 'status', 'title': '状态', 'sort': true, 'minWidth': 10},
    {'field': 'scoreCount', 'title': '已考核次数', 'sort': true, 'minWidth': 120}
  ]
  page=page
  add='${base_url}/phrase_group/add.html'
  edit='${base_url}/phrase_group/edit.html'
  delete='${base_url}/phrase_group/delete.json'
  toolbar=[
    {
      'html': '<i class="fa fa-map-o" style="font-size: 20px;"></i> &nbsp; 详情',
      'handler': 'location.href = "detail.html?id=" + data.id;'
    }
  ]
  page_enabled=true
  search=search?eval
/>
