<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->
<@nothing><script type="text/javascript"></@nothing>

<#assign file_upload>
  dict.uploadFile({
    action: 'import.json',
    accept: 'application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    success: function (data, delta) {
      location.reload(true);
      parent.layer.alert('导入短语成功！<br>共导入' + data.data + '个短语！<br>用时 ' + delta + ' 秒！', {icon: 1});
    }
  });
</#assign>

<#list page.searches as search>
  <#if search.field == 'student' && search.value??><#assign student = search.value/></#if>
</#list>

<#assign fields>
  [
    {'field': 'prototype', 'title': '短语', 'sort': true},
    {'field': 'definition', 'title': '词义', 'sort': true},
    {'field': 'frequency', 'title': '词频', 'sort': true, 'minWidth': 150}
    <#if !student??>
      , {'field': 'masteredStudents', 'title': '已掌握人数', 'sort': true}
      , {'field': 'failedStudents', 'title': '未掌握人数', 'sort': true}
    </#if>
  ]
</#assign>
<@nothing>;</@nothing>
<#assign search>
  [
    {'name': 'prototype', 'display': '短语', 'type': 'string'},
    {'name': 'definition', 'display': '词义', 'type': 'string'},
    {'name': 'studentNumber', 'display': '学生学号', 'type': 'integer'},
    <#if student??>
      {'name': 'studentName', 'display': '学生姓名', 'type': 'string', 'readonly': true},
    </#if>
    {
      'name': 'isMastered',
      'display': '是否掌握',
      'type': 'string',
      'options': [
        {'id': 'true', 'value': '已掌握'},
        {'id': 'false', 'value': '未掌握'}
      ]
    },
    <#if !student??>
      {'name': 'masteredStudents', 'display': '已掌握人数', 'type': 'string', 'options': <@switch100 gt1=false eq1=false/>},
      {'name': 'failedStudents', 'display': '未掌握人数', 'type': 'string', 'options': <@switch100 gt1=false eq1=false/>},
    </#if>
    {'name': 'frequency', 'display': '词频', 'type': 'string', 'options': <@switch1000 gt1=false eq1=false/>}
  ]
</#assign>
<@nothing>;</@nothing>

<#assign head_toolbar>
  [
    {
      'html': '<i class="fa fa-download" style="font-size: 20px;"></i> &nbsp; 导入短语',
      'handler': file_upload
    },
    {
      'html': '<i class="fa fa-upload" style="font-size: 20px;"></i> &nbsp; 导出短语',
      'handler': file_export(false)
    },
    {
      'html': '<i class="fa fa-upload" style="font-size: 20px;"></i> &nbsp; 导出全部短语',
      'handler': file_export(true)
    },
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 添加到分组',
      'handler': add_to,
      'needSelected': true
    }
    <#if student??>
      , {
        'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 创建专属短语分组',
        'handler': create_personal,
        'needSelected': true
      }
    </#if>
  ]
</#assign>
<@nothing>;</@nothing>

<#function file_export pageDisabled>
  <#assign returnValue>
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
        var data = {};
        data.pageable = dict.loadFormData();
        data.pageable.pageDisabled = ${pageDisabled?c};
        data.title = parent.exportData.title;
        data.docSolutionId = parent.exportData.docSolution;
        data.shuffleSolutionId = parent.exportData.shuffleSolution;
        dict.loadText({
          url: 'export_save.json',
          type: 'post',
          data: data,
          dataType: 'json',
          requestBody: true,
          success: function (result, status, request) {
            parent.layer.alert('导出成功！', {
              icon: 1,
              end: function () {
                dict.postHref('${base_url}/doc/download', {
                  fileName: data.title,
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
  <#return returnValue/>
</#function>

<#assign add_to>
  if (data.length > 500) {
    parent.layer.alert('一次最多只能添加 500 个短语！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '添加到短语分组',
      content: (function () {
        var baseUrl = '${base_url}/phrase/add_to.html?';
        $.each(data, function (index, item) {
          baseUrl += 'phraseId=' + item.id;
          if (index < data.length - 1) {
            baseUrl += '&';
          }
        });
        return baseUrl;
      })(),
      area: ['800px', '600px']
    });
  }
</#assign>

<#assign create_personal>
  if (data.length > 500) {
    parent.layer.alert('一次最多只能添加 500 个短语！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '创建专属短语分组',
      content: (function () {
        var baseUrl = '${base_url}/phrase_group/extract_derive_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'phraseId=' + item.id + '&';
        });
        baseUrl += 'studentId=${(student.id)!}';
        return baseUrl;
      })(),
      area: ['400px', '400px']
    });
  }
</#assign>

<@nothing></script></@nothing>
<@data_list
  id='phrases'
  name='短语'
  fields=fields?eval
  page=page
  add='${base_url}/phrase/add.html'
  edit='${base_url}/phrase/edit.html'
  delete='${base_url}/phrase/delete.json'
  head_toolbar=head_toolbar?eval
  page_enabled=true
  search=search?eval
/>
