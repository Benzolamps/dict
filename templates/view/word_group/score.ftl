<#-- @ftlvariable name="group" type="com.benzolamps.dict.bean.Group" -->
<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="hasMore" type="boolean" -->
<#-- @ftlvariable name="masteredWords" type="java.util.Collection<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="failedWords" type="java.util.Collection<com.benzolamps.dict.bean.Word>" -->
<div class="layui-row">
  <div class="layui-col-xs5">
    <div class="layui-card" style="height: 500px;">
      <div class="layui-card-header">
        <div style="text-align: center">未掌握的单词</div>
      </div>
      <div class="layui-card-body">
        <button class="layui-btn layui-btn-primary layui-btn-xs select-all">全选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs select-none">全不选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs select-reverse">反选</button>
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
        ${student.name}<br>
        ${student.number}<br>
        ${student.description}<br>
        ${student.clazz.name}<br>
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
        <input id="submit" type="submit" value="<#if hasMore>下一个<#else>完成</#if>" class="layui-btn layui-btn-normal layui-btn-sm" style="margin-top: 200px; width: 100%">
      </div>
    </div>
  </div>
  <div class="layui-col-xs5">
    <div class="layui-card" style="height: 500px;">
      <div class="layui-card-header">
        <div style="text-align: center">已掌握的单词</div>
      </div>
      <div class="layui-card-body">
        <button class="layui-btn layui-btn-primary layui-btn-xs select-all">全选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs select-none">全不选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs select-reverse">反选</button>
        <div class="ztree" id="mastered-tree" style="height: 400px; overflow-x: hidden; overflow-y: auto; margin-top: 5px; background-color: #FFE6B0"></div>
      </div>
    </div>
  </div>
</div>
<link rel="stylesheet" type="text/css" href="${base_url}/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<script type="text/javascript" src="${base_url}/zTree_v3/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${base_url}/zTree_v3/js/jquery.ztree.excheck.js"></script>
<script>
  var setting = {
    check: {
      enable: true,
      chkStyle: "checkbox"
    },
    callback: {
      beforeClick: dict.nothing
    }
  };

  var masteredNode = [
    <#list masteredWords as word>
      {id: '${word.id}', name: '${word.prototype} (${word.definition})', index: ${word.index}},
    </#list>
  ];

  var failedNode = [
    <#list failedWords as word>
      {id: '${word.id}', name: '${word.prototype} (${word.definition})', index: ${word.index}},
    </#list>
  ];

  var masteredTree, failedTree;

  var init = function () {
    <@data_tree id='failed-tree' setting='setting' value='failedNode' variable='failedTree'/>
    <@data_tree id='mastered-tree' setting='setting' value='masteredNode' variable='masteredTree'/>
  }

  init();

  $('#move-right').click(function () {
    var nodes = failedTree.getCheckedNodes();
    $.each(nodes, function (index, item) {
      var node = failedNode.splice(item.getIndex(), 1)[0];
      masteredNode.push(node);
      masteredNode.sort(function (a, b) {
          return a.index - b.index;
      });
      init();
    })
  });

  $('#move-left').click(function () {
    var nodes = masteredTree.getCheckedNodes();
    $.each(nodes, function (index, item) {
      var node = masteredNode.splice(item.getIndex(), 1)[0];
      failedNode.push(node);
      failedNode.sort(function (a, b) {
        return a.index - b.index;
      });
      init();
    })
  });

  $('#submit').click(function () {
    var nodes = masteredTree.getNodes();
    console.log({
      groupId: ${group.id},
      studentId: ${student.id},
      wordId: nodes.map(function (item) {
        return item.id;
      })
    });
    dict.loadText({
      url: '${base_url}/word_group/score_save.json',
      type: 'post',
      data: {
        groupId: ${group.id},
        studentId: ${student.id},
        wordId: nodes.map(function (item) {
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
              dict.reload(true);
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
</script>