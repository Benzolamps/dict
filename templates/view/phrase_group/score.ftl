<#-- @ftlvariable name="group" type="com.benzolamps.dict.bean.Group" -->
<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="students" type="java.util.Collection<com.benzolamps.dict.bean.Student>" -->
<#-- @ftlvariable name="hasMore" type="boolean" -->
<#-- @ftlvariable name="masteredPhrases" type="java.util.Collection<com.benzolamps.dict.bean.Phrase>" -->
<#-- @ftlvariable name="failedPhrases" type="java.util.Collection<com.benzolamps.dict.bean.Phrase>" -->
<#-- @ftlvariable name="scored" type="boolean" -->
<div class="layui-row">
  <div class="layui-col-xs5">
    <div class="layui-card" style="height: 500px;">
      <div class="layui-card-header">
        <div style="text-align: center">未掌握的短语（<span id="failed-phrase-count">${failedPhrases?size}</span>）</div>
      </div>
      <div class="layui-card-body">
        <#if !scored>
          <button class="layui-btn layui-btn-primary layui-btn-xs select-all">全选</button>
          <button class="layui-btn layui-btn-primary layui-btn-xs select-none">全不选</button>
          <button class="layui-btn layui-btn-primary layui-btn-xs select-reverse">反选</button>
        </#if>
        <div class="ztree" id="failed-tree" style="height: 400px; overflow-x: hidden; overflow-y: auto; margin-top: 5px; background-color: #FFE6B0"></div>
      </div>
    </div>
  </div>
  <div class="layui-col-xs2">
    <div class="layui-card" style="height: 500px;">
      <div class="layui-card-header">
        <div style="text-align: center">操作</div>
      </div>
      <br><br>
      <div class="layui-card-body" style="text-align: center">
        <#if !scored>
          <select id="change">
            <#list students as stu>
              <option value="${stu.id}"<#if stu == student> selected</#if>>${stu.name}</option>
            </#list>
          </select>
        <#else>
          ${student.name}<br>
        </#if>
        <br>
      ${student.number}<br>
      ${student.description!''}<br>
      ${student.clazz.name}<br>
        <#if !scored>
          <table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 10px">
            <tr>
              <td>
                <button id="move-right" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-radius" style="width: 50%; margin-bottom: 10px;">
                  <i class="fa fa-angle-double-right" style="font-size: 20px;"></i>
                </button>
              </td>
            </tr>
            <tr>
              <td>
                <button id="move-left" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-radius" style="width: 50%; margin-bottom: 10px;">
                  <i class="fa fa-angle-double-left" style="font-size: 20px;"></i>
                </button>
              </td>
            </tr>
          </table>
        </#if>
        <#if group.status != 'COMPLETED'>
          <br><br><input id="submit" type="button" value="完成" class="layui-btn layui-btn-normal layui-btn-sm" style="width: 100%">
        </#if>
        <#if !scored>
          <br><br><input id="jump" type="button" value="跳过" class="layui-btn layui-btn-danger layui-btn-sm" style="width: 100%">
          <br><br><input id="import" type="button" value="导入学习进度" class="layui-btn layui-btn-primary layui-btn-sm" style="width: 100%">
        <#else>
          <br><br><input id="extract-personal-group" type="button" value="创建专属分组" class="layui-btn layui-btn-warm layui-btn-sm" style="width: 100%">
        </#if>
      </div>
    </div>
  </div>
  <div class="layui-col-xs5">
    <div class="layui-card" style="height: 500px;">
      <div class="layui-card-header">
        <div style="text-align: center">已掌握的短语（<span id="mastered-phrase-count">${masteredPhrases?size}</span>）</div>
      </div>
      <div class="layui-card-body">
        <#if !scored>
          <button class="layui-btn layui-btn-primary layui-btn-xs select-all">全选</button>
          <button class="layui-btn layui-btn-primary layui-btn-xs select-none">全不选</button>
          <button class="layui-btn layui-btn-primary layui-btn-xs select-reverse">反选</button>
        </#if>
        <div class="ztree" id="mastered-tree" style="height: 400px; overflow-x: hidden; overflow-y: auto; margin-top: 5px; background-color: #FFE6B0"></div>
      </div>
    </div>
  </div>
</div>
<link rel="stylesheet" type="text/css" href="${base_url}/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<script type="text/javascript" src="${base_url}/zTree_v3/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${base_url}/zTree_v3/js/jquery.ztree.excheck.js"></script>
<script>
  <#escape x as x?js_string>
    var setting = {
      <#if !scored>
        check: {
          enable: true,
          chkStyle: 'checkbox'
        },
      </#if>
      callback: {
        beforeClick: dict.nothing
      }
    };

    var masteredNode = [
      <#list masteredPhrases as phrase>
        {id: '${phrase.id}', name: '${phrase.prototype} (${phrase.definition})', index: ${phrase.index}},
      </#list>
    ];

    var failedNode = [
      <#list failedPhrases as phrase>
        {id: '${phrase.id}', name: '${phrase.prototype} (${phrase.definition})', index: ${phrase.index}},
      </#list>
    ];

    var masteredTree, failedTree;

    var init = function () {
      <@data_tree id='failed-tree' setting='setting' value='failedNode' variable='failedTree'/>
      <@data_tree id='mastered-tree' setting='setting' value='masteredNode' variable='masteredTree'/>
    };

    init();

    $('#move-right').click(function () {
      var nodes = failedTree.getCheckedNodes();
      $.each(nodes.sort(function (a, b) {
        return b.getIndex() - a.getIndex();
      }), function (index, item) {
        var node = failedNode.splice(item.getIndex(), 1)[0];
        masteredNode.push(node);
      });
      $('#mastered-phrase-count').text(masteredNode.length);
      $('#failed-phrase-count').text(failedNode.length);
      masteredNode.sort(function (a, b) {
        return a.index - b.index;
      });
      init();
    });

    $('#move-left').click(function () {
      var nodes = masteredTree.getCheckedNodes();
      $.each(nodes.sort(function (a, b) {
        return b.getIndex() - a.getIndex();
      }), function (index, item) {
        var node = masteredNode.splice(item.getIndex(), 1)[0];
        failedNode.push(node);
      });
      $('#mastered-phrase-count').text(masteredNode.length);
      $('#failed-phrase-count').text(failedNode.length);
      failedNode.sort(function (a, b) {
        return a.index - b.index;
      });
      init();
    });

    $('#change').change(function () {
      location.replace(location.pathname + "?id=${group.id}&studentId=" + $(this).val());
    });

    $('#submit').click(function () {
      <#if !scored>
        parent.layer.confirm('确定当前学生已评分完毕？', {icon: 3, title: '提示'}, function (index) {
          parent.layer.close(index);
          var nodes = masteredTree.getNodes();
          dict.loadText({
            url: 'score_save.json',
            type: 'post',
            data: {
              groupId: ${group.id},
              studentId: ${student.id},
              phraseId: nodes.map(function (item) {
                return item.id;
              })
            },
            dataType: 'json',
            success: function (result, status, request) {
              parent.layer.alert('操作成功！', {
                icon: 1,
                end: function () {
                  parent.$('iframe')[0].contentWindow.dict.reload(true);
                  <#if hasMore>
                    location.replace(location.pathname + "?id=${group.id}");
                  <#else>
                    var layerIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(layerIndex);
                  </#if>
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
        });
      <#else>
        parent.$('iframe')[0].contentWindow.dict.reload(true);
        <#if hasMore>
          location.replace(location.pathname + "?id=${group.id}");
        <#else>
          var layerIndex = parent.layer.getFrameIndex(window.name);
          parent.layer.close(layerIndex);
        </#if>
      </#if>
    });

    <#if !scored>
      $('#jump').click(function () {
        parent.layer.confirm('确定要跳过当前学生的评分？', {icon: 3, title: '提示'}, function (index) {
          parent.layer.close(index);
          var nodes = masteredTree.getNodes();
          dict.loadText({
            url: 'jump.json',
            type: 'post',
            data: {
              groupId: ${group.id},
              studentId: ${student.id}
            },
            dataType: 'json',
            success: function (result, status, request) {
              parent.layer.alert('操作成功！', {
                icon: 1,
                end: function () {
                  parent.$('iframe')[0].contentWindow.dict.reload(true);
                  <#if hasMore>
                    location.replace(location.pathname + "?id=${group.id}");
                  <#else>
                    var layerIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(layerIndex);
                    parent.location.reload(true);
                  </#if>
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
        });
      });

      $('#import').click(function () {
        dict.uploadFile({
          action: 'import.json',
          data: {
            studentId: ${student.id},
            groupId: ${group.id}
          },
          multiple: true,
          accept: 'image/*',
          success: function (data, delta) {
            location.replace(location.pathname + "?id=${group.id}&studentId=${student.id}");
            parent.$('iframe')[0].contentWindow.dict.reload(true);
            parent.layer.alert('导入学习进度成功！<br>用时 ' + delta + ' 秒！', {icon: 1});
          }
        });
      });
    <#else>
       $('#extract-personal-group').click(function () {
         parent.layer.open({
           type: 2,
           title: '创建专属短语分组',
           content: '${base_url}/phrase_group/extract_personal_group.html?studentId=${student.id}&groupId=${group.id}',
           area: ['400px', '400px']
         });
       });
    </#if>
  </#escape>
</script>