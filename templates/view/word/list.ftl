<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->

<form id="upload-form" method="post" action="import.json" enctype="multipart/form-data" style="display: none;">
  <input type="file" name="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
</form>

<@nothing><script type="text/javascript"></@nothing>

<#list page.searches as search>
  <#if search.field == 'studentId'><#assign student_id = search.value/></#if>
</#list>

<#assign fields>
  [
    {'field': 'prototype', 'title': '单词', 'sort': true, 'minWidth': 120},
    <#--{'field': 'britishPronunciation', 'title': '英式发音', 'sort': true, 'minWidth': 120},-->
    <#--{'field': 'americanPronunciation', 'title': '美式发音', 'sort': true, 'minWidth': 120},-->
    {'field': 'definition', 'title': '词义', 'sort': true, 'minWidth': 150},
    {'field': 'clazzes', 'title': '词性', 'minWidth': 150}
    <#if !student_id??>
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
    }
    <#if student_id??>
      , {'name': 'studentId', 'display': '学生学号', 'type': 'integer', 'readonly': true}
      , {'name': 'studentName', 'display': '学生姓名', 'type': 'string', 'readonly': true}
      , {
        'name': 'isMastered',
        'display': '是否掌握',
        'type': 'string',
        'readonly': true,
        'options': [
          {'id': 'true', 'value': '已掌握'},
          {'id': 'false', 'value': '未掌握'}
        ]
      }
    </#if>
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

<#assign file_upload>
  var $file = $('#upload-form input');
  $file.trigger('click');
  $file.unbind('change');
  $file.change(function () {
    var loader = parent.layer.load();
    setTimeout(function () {
      var startTime = new Date().getTime();
      $('#upload-form').ajaxSubmit({
        success: function (result, status, request) {
          var endTime = new Date().getTime();
          var delta = ((endTime - startTime) * 0.001).toFixed(3);
          parent.layer.close(loader);
          parent.layer.alert('导入单词成功！<br>共导入 ' + result.data + ' 个单词！<br>用时 ' + delta + ' 秒！', {icon: 1});
        },
        error: function (request) {
          parent.layer.close(loader);
          var result = JSON.parse(request.responseText);
          parent.layer.alert(result.message, {
            icon: 2,
            anim: 6,
            title: result.status
          });
        }
      });
    }, 500);
  });
</#assign>

<#function file_export pageDisabled>
  <#assign returnValue>
    parent.layer.open({
      type: 2,
      title: '导出单词',
      content: '${base_url}/word/export.html',
      area: ['800px', '600px'],
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
                dict.postHref('${base_url}/doc/download.doc', {
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
  if (data.length > 100) {
    parent.layer.alert('一次最多只能添加100个单词！', {icon: 2});
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
  if (data.length > 100) {
    parent.layer.alert('一次最多只能添加100个单词！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '创建专属分组',
      content: (function () {
        var baseUrl = '${base_url}/student/personal_word_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'wordId=' + item.id + '&';
        });
        baseUrl += 'studentId=${student_id!}';
        return baseUrl;
      })(),
      area: ['800px', '600px']
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
