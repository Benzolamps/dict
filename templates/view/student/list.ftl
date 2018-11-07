<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Student>" -->
<#-- @ftlvariable name="clazzes" type="java.util.Collection<com.benzolamps.dict.bean.Clazz>" -->
<@nothing><script type="text/javascript"></@nothing>
<#assign search>
  [
    {'name': 'number', 'display': '学号', 'type': 'integer'},
    {'name': 'name', 'display': '姓名', 'type': 'string'},
    {
      'name': 'clazz',
      'display': '班级',
      'type': 'string',
      'options': [
        <#list clazzes as clazz>
          {'id': ${clazz.id}, 'value': '${clazz.name}'}<#sep>,
        </#list>
      ]
    }
  ]
</#assign>
<@nothing>;</@nothing>

<#assign add_to_word_group>
  if (data.length > 100) {
    parent.layer.alert('一次最多只能添加100个学生！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '添加到单词分组',
      content: (function () {
        var baseUrl = '${base_url}/student/add_to_word_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'studentId=' + item.id;
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

<#assign add_to_phrase_group>
  if (data.length > 100) {
    parent.layer.alert('一次最多只能添加100个学生！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '添加到短语分组',
      content: (function () {
        var baseUrl = '${base_url}/student/add_to_phrase_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'studentId=' + item.id;
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

<#assign import_word_process>
  dict.uploadFile({
    action: '${base_url}/word_group/import.json',
    multiple: true,
    accept: 'image/*',
    success: function (delta) {
      location.reload(true);
      parent.layer.alert('导入单词学习进度成功！<br>用时 ' + delta + ' 秒！', {icon: 1});
    }
  });
</#assign>

<#assign import_phrase_process>
  dict.uploadFile({
    action: '${base_url}/phrase_group/import.json',
    multiple: true,
    accept: 'image/*',
    success: function (delta) {
      location.reload(true);
      parent.layer.alert('导入短语学习进度成功！<br>用时 ' + delta + ' 秒！', {icon: 1});
    }
  });
</#assign>

<@nothing></script></@nothing>

<@data_list
  id='students'
  name='学生'
  fields=[
    {'field': 'number', 'title': '学号', 'sort': true},
    {'field': 'name', 'title': '姓名', 'sort': true},
    {'field': 'clazz', 'title': '班级', 'sort': true},
    {'field': 'progress', 'title': '学习进度 （已掌握／未掌握）', 'sort': true, 'width': 250}
  ]
  page=page
  add='${base_url}/student/add.html'
  edit='${base_url}/student/edit.html'
  delete='${base_url}/student/delete.json'
  head_toolbar=[
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 添加到单词分组',
      'handler': add_to_word_group,
      'needSelected': true
    },
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 添加到短语分组',
      'handler': add_to_phrase_group,
      'needSelected': true
    },
    {
      'html': '<i class="fa fa-download" style="font-size: 20px;"></i> &nbsp; 导入单词学习进度',
      'handler': import_word_process
    },
    {
      'html': '<i class="fa fa-download" style="font-size: 20px;"></i> &nbsp; 导入短语学习进度',
      'handler': import_phrase_process
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
