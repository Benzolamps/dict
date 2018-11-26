<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Student>" -->
<#-- @ftlvariable name="clazzes" type="java.util.Collection<com.benzolamps.dict.bean.Clazz>" -->
<@nothing><script type="text/javascript"></@nothing>
<#assign search>
  [
    {'name': 'number', 'display': '学号', 'type': 'integer'},
    {'name': 'name', 'display': '姓名', 'type': 'string'},
    {'name': 'masteredWords', 'display': '已掌握单词数', 'type': 'string', 'options': <@switch1000 gt1=false eq1=false/>},
    {'name': 'failedWords', 'display': '未掌握单词数', 'type': 'string', 'options': <@switch1000 gt1=false eq1=false/>},
    {'name': 'masteredPhrases', 'display': '已掌握短语数', 'type': 'string', 'options': <@switch1000 gt1=false eq1=false/>},
    {'name': 'failedPhrases', 'display': '未掌握短语数', 'type': 'string', 'options': <@switch1000 gt1=false eq1=false/>},
    {
      'name': 'order',
      'display': '排序',
      'type': 'string',
      'options': [
        {'id': 'masteredWords asc', 'value': '已掌握单词数 ↑'},
        {'id': 'failedWords asc', 'value': '未掌握单词数 ↑'},
        {'id': 'masteredPhrases asc', 'value': '已掌握短语数 ↑'},
        {'id': 'failedPhrases asc', 'value': '未掌握短语数 ↑'},
        {'id': 'masteredWords desc', 'value': '已掌握单词数 ↓'},
        {'id': 'failedWords desc', 'value': '未掌握单词数 ↓'},
        {'id': 'masteredPhrases desc', 'value': '已掌握短语数 ↓'},
        {'id': 'failedPhrases desc', 'value': '未掌握短语数 ↓'}
      ]
    },
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
<@nothing>;</@nothing>

<#assign add_to_word_group>
  if (data.length > 500) {
    parent.layer.alert('一次最多只能添加 500 个学生！', {icon: 2});
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
  if (data.length > 500) {
    parent.layer.alert('一次最多只能添加 500 个学生！', {icon: 2});
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

<#assign create_personal_word_groups>
  parent.layer.open({
    type: 2,
    title: '创建专属单词分组',
    content: (function () {
      var baseUrl = '${base_url}/word_group/extract_personal_group.html?';
      $.each(data, function (index, item) {
        baseUrl += 'studentId=' + item.id + '&';
      });
      return baseUrl;
    })(),
    area: ['400px', '400px']
  });
</#assign>

<#assign create_personal_phrase_groups>
  parent.layer.open({
    type: 2,
    title: '创建专属短语分组',
    content: (function () {
      var baseUrl = '${base_url}/phrase_group/extract_personal_group.html?';
      $.each(data, function (index, item) {
        baseUrl += 'studentId=' + item.id + '&';
      });
      return baseUrl;
    })(),
    area: ['400px', '400px']
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
    {'title': '学习进度 （已掌握／未掌握）', 'width': 250, 'format': '单词 {{d.masteredWords}}／{{d.failedWords}} 短语 {{d.masteredPhrases}}／{{d.failedPhrases}}'}
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
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 创建专属单词分组',
      'handler': create_personal_word_groups,
      'needSelected': true
    },
    {
      'html': '<i class="fa fa-paw" style="font-size: 20px;"></i> &nbsp; 创建专属短语分组',
      'handler': create_personal_phrase_groups,
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
