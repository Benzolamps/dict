<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Group>" -->
<#list page.searches as search>
  <#if search.field == 'student' && search.value??><#assign student = search.value/></#if>
</#list>

<@nothing><script type="text/javascript"></@nothing>
<#assign search>
  [
    {'name': 'name', 'display': '名称', 'type': 'string'},
    {'name': 'description', 'display': '描述', 'type': 'string'},
    {
      'name': 'frequencyGenerated',
      'display': '类型',
      'type': 'string',
      'options': [
        {'id': 0, 'value': '非词频分组'},
        {'id': 1, 'value': '词频分组'}
      ]
    },
    {
      'name': 'status',
      'display': '状态',
      'type': 'string',
      'options': [
        {'id': 0, 'value': '正常'},
        {'id': 1, 'value': '评分中'},
        {'id': 2, 'value': '已完成'}
      ]
    },
    {'name': 'createDate', 'display': '创建时间', 'type': 'datetime', 'range': true},
    {
      'name': 'order',
      'display': '排序',
      'type': 'string',
      'options': [
        {'id': 'studentsCount asc', 'value': '学生数 ↑'},
        {'id': 'phrasesCount asc', 'value': '短语数 ↑'},
        {'id': 'scoreCount asc', 'value': '已考核次数 ↑'},
        {'id': 'studentsCount desc', 'value': '学生数 ↓'},
        {'id': 'phrasesCount desc', 'value': '短语数 ↓'},
        {'id': 'scoreCount desc', 'value': '已考核次数 ↓'}
      ]
    },
    {'name': 'studentsCount', 'display': '学生数', 'type': 'string', 'options': <@switch100/>},
    {'name': 'phrasesCount', 'display': '短语数', 'type': 'string', 'options': <@switch1000 gt1=false eq1=false/>},
    {'name': 'scoreCount', 'display': '已考核次数', 'type': 'string', 'options': <@switch10/>},
    {'name': 'studentNumber', 'display': '学生学号', 'type': 'integer'}
    <#if student??>
      , {'name': 'studentName', 'display': '学生姓名', 'type': 'string', 'readonly': true}
    </#if>
  ]
</#assign>

<#assign import_phrase_process>
  dict.uploadFile({
    action: 'import.json',
    multiple: true,
    accept: 'image/*',
    success: function (data, delta) {
      location.reload(true);
      parent.layer.alert('导入短语学习进度成功！<br>用时 ' + delta + ' 秒！', {icon: 1});
    }
  });
</#assign>

<#assign export>
  parent.layer.open({
    type: 2,
    title: '导出短语',
    content: '${base_url}/phrase/export.html',
    area: ['400px', '400px'],
    cancel: function () {
      delete parent.exportData;
    },
    end: function () {
      if (!parent.exportData) return false;
      var param = {};
      param.groupIds = data.map(function (item) {
        return item.id;
      });
      param.title = parent.exportData.title;
      param.docSolutionId = parent.exportData.docSolution;
      param.shuffleSolutionId = parent.exportData.shuffleSolution;
      dict.loadText({
        url: '${base_url}/phrase/export_save.json',
        type: 'post',
        data: param,
        dataType: 'json',
        requestBody: true,
        success: function (result, status, request) {
          parent.layer.alert('导出成功！', {
            icon: 1,
            end: function () {
              dict.postHref('${base_url}/doc/download', {
                fileName: param.title,
                token: result.data
              });
            }
          });
        },
        error: function (result, status, request) {
          parent.layer.alert(result.message, {
            icon: 2,
            title: result.status
          });
        }
      });
    }
  });
</#assign>
<@nothing></script></@nothing>

<@data_list
  id='phrase-groups'
  name='短语分组'
  fields=[
    {'field': 'name', 'title': '名称', 'sort': true, 'minWidth': 120},
    {'field': 'description', 'title': '描述', 'sort': true, 'minWidth': 120},
    {'field': 'createDate', 'title': '创建时间', 'sort': true, 'minWidth': 120},
    {'field': 'status', 'title': '状态', 'sort': true, 'minWidth': 10},
    {'title': '学生数／短语数／已考核次数', 'format': '{{d.studentsOriented}}／{{d.phrases}}／{{d.scoreCount}}', 'minWidth': 120}
  ]
  page=page
  add='${base_url}/phrase_group/add.html'
  edit='${base_url}/phrase_group/edit.html'
  delete='${base_url}/phrase_group/delete.json'
  window_width=400
  window_height=400
  head_toolbar=[
    {
      'html': '<i class="fa fa-plus" style="font-size: 20px;"></i> &nbsp; 添加短语词频分组',
      'handler': 'parent.layer.open({type: 2, title: \'添加短语词频分组\', content: \'${base_url}/phrase_group/add_frequency_group.html\', area: [\'800px\', \'600px\']});'
    },
    {
      'html': '导入短语学习进度',
      'handler': import_phrase_process
    },
    {
      'html': '导出短语',
      'handler': export,
      'needSelected': true
    }
  ]
  toolbar=[
    {
      'html': '<i class="fa fa-map-o" style="font-size: 20px;"></i> &nbsp; 详情',
      'handler': 'location.href = "detail.html?id=" + data.id;'
    }
  ]
  page_enabled=true
  search=search?eval
/>
