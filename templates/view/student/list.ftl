<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Student>" -->
<#-- @ftlvariable name="clazzes" type="java.util.Collection<com.benzolamps.dict.bean.Clazz>" -->
<@nothing><script type="text/javascript"></@nothing>
<#assign search>
  [
    {'name': 'number', 'display': '学号', 'type': 'integer'},
    {'name': 'name', 'display': '姓名', 'type': 'string'},
    {'name': 'description', 'display': '描述', 'type': 'string'},
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
<@nothing></script></@nothing>

<@data_list
  id='students'
  name='学生'
  fields=[
    {'field': 'number', 'title': '学号', 'sort': true, 'minWidth': 120},
    {'field': 'name', 'title': '姓名', 'sort': true, 'minWidth': 120},
    {'field': 'description', 'title': '描述', 'sort': true, 'minWidth': 150},
    {'field': 'clazz', 'title': '班级', 'sort': true, 'minWidth': 150},
    {'field': 'progress', 'title': '学习进度 （已掌握／未掌握）', 'sort': true, 'minWidth': 240}
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
    }
  ]
  toolbar=[
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 添加到单词分组',
      'handler': '~function (data) {' + add_to_word_group + '}([data]);'
    },
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 添加到短语分组',
      'handler': '~function (data) {' + add_to_phrase_group + '}([data]);'
    }
  ]
  page_enabled=true
  search=search?eval
/>
