<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->

<form id="upload-form" method="post" action="import.json" enctype="multipart/form-data" style="display: none;">
  <input type="file" name="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
</form>

<@nothing><script type="text/javascript"></@nothing>
<#assign values>
  [
    <#list page.content as phrase>
      {
        'id': ${phrase.id},
        'prototype': <@json_dump obj=phrase.prototype/>,
        'definition': <@json_dump obj=phrase.definition/>
      }
    </#list>
  ]
</#assign>

<#assign search>
  [
    {
      'name': 'prototype',
      'display': '短语原形',
      'type': 'string'
    },
    {
      'name': 'definition',
      'display': '词义',
      'type': 'string'
    }
  ]
</#assign>

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
          parent.layer.close(loader);
          parent.layer.alert('导入短语成功, 共导入 ' + result.data + ' 个短语, 用时 ' + ((endTime - startTime) * 0.001).toFixed(3) + ' 秒', {
            icon: 1,
            title: '导入短语成功',
            yes: function (index) {
              parent.$('iframe')[0].contentWindow.dict.reload(true);
              parent.layer.close(index);
            }
          });
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
<@nothing></script></@nothing>
<@data_list
  id='words'
  fields=[
    {'type': 'numbers'},
    {'type': 'checkbox'},
    {'field': 'prototype', 'title': '短语原形', 'sort': true},
    {'field': 'definition', 'title': '词义', 'sort': true}
  ]
  page=page
  values=values?eval
  add='${base_url}/phrase/add.html'
  edit='${base_url}/phrase/edit.html'
  delete='${base_url}/phrase/delete.json'
  head_toolbar=[
    {
      'html': '<i class="layui-icon" style="font-size: 20px;">&#xe60a;</i> 导入短语',
      'handler': file_upload
    }
  ]
  page_enabled=true
  search=search?eval
/>
