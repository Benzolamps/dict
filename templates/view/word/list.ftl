<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->
<#list page.searches as search>
  <#if search.field == 'student' && search.value??><#assign student = search.value/></#if>
</#list>
<@nothing><script type="text/javascript"></@nothing>

<#assign file_upload>
   dict.uploadFile({
     action: 'import.json',
     accept: 'application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
     success: function (data, delta) {
       location.reload(true);
       parent.layer.alert('导入单词成功！<br>共导入' + data.data + '个单词！<br>用时 ' + delta + ' 秒！', {icon: 1});
     }
   });
</#assign>

<#assign fields>
  [
    {'field': 'prototype', 'title': '单词', 'sort': true, 'minWidth': 120},
    <#--{'field': 'britishPronunciation', 'title': '英式发音', 'sort': true, 'minWidth': 120},-->
    <#--{'field': 'americanPronunciation', 'title': '美式发音', 'sort': true, 'minWidth': 120},-->
    {'field': 'definition', 'title': '词义', 'sort': true, 'minWidth': 150},
    {'field': 'clazzes', 'title': '词性', 'minWidth': 150},
    {'field': 'frequency', 'title': '词频', 'sort': true, 'minWidth': 150}
    <#if !student??>
      , {'field': 'masteredStudents', 'title': '已掌握', 'sort': true}
      , {'field': 'failedStudents', 'title': '未掌握', 'sort': true}
    </#if>
  ]
</#assign>

<#assign search>
  [
    {
      'name': 'prototype',
      'display': '单词',
      'type': 'string'
    },
    {
      'name': 'clazzes',
      'display': '词性',
      'type': 'string',
      'options': [
        <#list wordClazzes as wordClazz>
          {'id': ${wordClazz.id}, 'value': '${wordClazz.name}'}<#sep>,
        </#list>
      ]
    },
    {
      'name': 'frequency',
      'display': '词频',
      'type': 'string',
      'options': [
        {'id': 0, 'value': '0'},
        {'id': 1, 'value': '1～10'},
        {'id': 2, 'value': '11～20'},
        {'id': 3, 'value': '21～30'},
        {'id': 4, 'value': '31～40'},
        {'id': 5, 'value': '41～50'},
        {'id': 6, 'value': '51～60'},
        {'id': 7, 'value': '61～70'},
        {'id': 8, 'value': '71～80'},
        {'id': 9, 'value': '81～90'},
        {'id': 10, 'value': '91～100'},
        {'id': 11, 'value': '101～200'},
        {'id': 12, 'value': '201～500'},
        {'id': 13, 'value': '501～1000'},
        {'id': 14, 'value': '1000 以上'},
        {'id': 15, 'value': '非 0'}
      ]
    },
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
    }
  ]
</#assign>
<@nothing>;</@nothing>

<#assign head_toolbar>
  [
    {
      'html': '<i class="fa fa-download" style="font-size: 20px;"></i> &nbsp; 导入单词',
      'handler': file_upload
    },
    {
      'html': '<i class="fa fa-upload" style="font-size: 20px;"></i> &nbsp; 导出单词',
      'handler': file_export(false)
    },
    {
      'html': '<i class="fa fa-upload" style="font-size: 20px;"></i> &nbsp; 导出全部单词',
      'handler': file_export(true)
    },
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 添加到分组',
      'handler': add_to,
      'needSelected': true
    }
    <#if student_id??>
      , {
        'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 创建专属分组',
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
      title: '导出单词',
      content: '${base_url}/word/export.html',
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
    parent.layer.alert('一次最多只能添加 500 个单词！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '添加到单词分组',
      content: (function () {
        var baseUrl = '${base_url}/word/add_to.html?';
        $.each(data, function (index, item) {
          baseUrl += 'wordId=' + item.id;
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
    parent.layer.alert('一次最多只能添加 500 个单词！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '创建专属分组',
      content: (function () {
        var baseUrl = '${base_url}/student/personal_word_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'wordId=' + item.id + '&';
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
  id='words'
  name='单词'
  fields=fields?eval
  page=page
  add='${base_url}/word/add.html'
  edit='${base_url}/word/edit.html'
  delete='${base_url}/word/delete.json'
  head_toolbar=head_toolbar?eval
  page_enabled=true
  search=search?eval
/>
