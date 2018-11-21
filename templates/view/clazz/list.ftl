<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Library>" -->
<@nothing><script type="text/javascript"></@nothing>

<#assign add_to_word_group>
  if (data.length > 500) {
    parent.layer.alert('一次最多只能添加 500 个班级！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '添加到单词分组',
      content: (function () {
        var baseUrl = '${base_url}/clazz/add_to_word_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'clazzId=' + item.id;
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
    parent.layer.alert('一次最多只能添加 500 个班级！', {icon: 2});
  } else {
    parent.layer.open({
      type: 2,
      title: '添加到短语分组',
      content: (function () {
        var baseUrl = '${base_url}/clazz/add_to_phrase_group.html?';
        $.each(data, function (index, item) {
          baseUrl += 'clazzId=' + item.id;
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
  id='clazzes'
  name='班级'
  fields=[
    {'field': 'name', 'title': '名称', 'sort': true},
    {'field': 'description', 'title': '描述', 'sort': true},
    {'field': 'students', 'title': '学生数', 'sort': true}
  ]
  page=page
  add='${base_url}/clazz/add.html'
  edit='${base_url}/clazz/edit.html'
  delete='${base_url}/clazz/delete.json'
  window_width=400
  window_height=400
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
      'html': '<i class="fa fa-graduation-cap" style="font-size: 20px;"></i> &nbsp; 查看学生',
      'handler': "
         var baseUrl = '${base_url}/student/list.html?' +
         encodeURIComponent(JSON.stringify({
          searches: [
            {field: 'clazz', value: data.id},
          ]
        }));
        console.log(baseUrl);
        location.href = baseUrl;
      "
    }
  ]
  page_enabled=true
  delete_confirm='删除班级将会删除其中的所有学生，确定要继续吗？'
  search=[
    {'name': 'name', 'display': '名称', 'type': 'string'},
    {'name': 'description', 'display': '描述', 'type': 'string'}
  ]
/>
