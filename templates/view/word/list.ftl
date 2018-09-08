<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="wordClazzes" type="java.util.Collection<com.benzolamps.dict.bean.WordClazz>" -->

<form id="upload-form" method="post" action="import.json" enctype="multipart/form-data" style="display: none;">
  <input type="file" name="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
</form>

<@nothing><script type="text/javascript"></@nothing>
<#assign values>
  [
    <#list page.content as word>
      {
        'id': ${word.id},
        'prototype': <@json_dump obj=word.prototype/>,
        'britishPronunciation': <@json_dump obj=word.britishPronunciation/>,
        'americanPronunciation': <@json_dump obj=word.americanPronunciation/>,
        'clazzes': [<#list word.clazzes as clazz><@json_dump obj=clazz.name/><#sep>, </#list>],
        'definition': <@json_dump obj=word.definition/>
      }
    </#list>
  ]
</#assign>

<#assign search>
[
    {
      'name': 'prototype',
      'display': '单词原形',
      'type': 'string'
    },
      {
        'name': 'definition',
        'display': '词义',
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
          parent.layer.alert('导入单词成功, 共导入 ' + result.data + ' 个单词, 用时 ' + ((endTime - startTime) * 0.001).toFixed(3) + ' 秒', {
            icon: 1,
            title: '导入单词成功',
            yes: function (index) {
              parent.$('iframe')[0].contentWindow.location.reload(true);
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
    {'field': 'prototype', 'title': '单词原形', 'sort': true},
    {'field': 'britishPronunciation', 'title': '英式发音', 'sort': true},
    {'field': 'americanPronunciation', 'title': '英式发音', 'sort': true},
    {'field': 'clazzes', 'title': '词性', 'sort': true},
    {'field': 'definition', 'title': '词义', 'sort': true}
  ]
  page=page
  values=values?eval
  add='${base_url}/word/add.html'
  edit='${base_url}/word/edit.html'
  delete='${base_url}/word/delete.json'
  head_toolbar=[
    {
      'html': '<i class="layui-icon" style="font-size: 20px;">&#xe60a;</i> 导入单词',
      'handler': file_upload
    }
  ]
  page_enabled=true
  search=search?eval
/>
