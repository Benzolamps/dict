<#-- @ftlvariable name="group" type="com.benzolamps.dict.bean.Group" -->
<#-- @ftlvariable name="students" type="java.util.List<com.benzolamps.dict.controller.vo.ClazzStudentTreeVo>" -->
<blockquote class="layui-elem-quote" style="margin-top: 10px;">
  ${group.name}&nbsp;&nbsp;&nbsp;&nbsp;
  <button class="layui-btn layui-btn-normal layui-btn-sm" onclick="history.back();">
    <i class="fa fa-arrow-circle-left" style="font-size: 20px;"></i> &nbsp; 返回
  </button>
  <button class="layui-btn layui-btn-warm layui-btn-sm" onclick="location.reload(true);">
    <i class="fa fa-refresh" style="font-size: 20px;"></i> &nbsp; 刷新
  </button>
  <button id="export" class="layui-btn layui-btn-primary layui-btn-sm">
    <i class="fa fa-upload" style="font-size: 20px;"></i> &nbsp; 导出单词
  </button>
  <button id="score" class="layui-btn layui-btn-primary layui-btn-sm"<#if group.status == 'COMPLETED'> style="display: none"</#if>>
    去评分
  </button>
  <button id="finish"  class="layui-btn layui-btn-primary layui-btn-sm"<#if group.status != 'SCORING'> style="display: none"</#if>>
    结束评分
  </button>
  <button id="complete" class="layui-btn layui-btn-primary layui-btn-sm"<#if group.status != 'COMPLETED'> style="display: none"</#if>>
    完成评分
  </button>
</blockquote>
<div class="layui-row">
  <div class="layui-col-xs6">
    <div class="layui-card">
      <div class="layui-card-body">
        <table class="layui-table">
          <colgroup>
            <col width="30">
            <col width="70">
          </colgroup>
          <thead></thead>
          <tbody>
            <tr>
              <th title="状态">状态</th>
              <td>${group.status}</td>
            </tr>
            <tr>
              <th title="单词数">单词数</th>
              <td>${group.wordsCount}</td>
            </tr>
            <tr>
              <th title="已考核次数">已考核次数</th>
              <td class="date-time-text">0</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="layui-col-xs6">
    <div class="layui-card">
      <div class="layui-card-body">
        <table class="layui-table">
          <thead></thead>
          <tbody>
            <tr>
              <th title="描述">描述</th>
              <td>${group.description!''}</td>
            </tr>
            <tr>
              <th title="学生数">学生数</th>
              <td>${group.studentsOrientedCount}</td>
            </tr>
            <tr>
              <th title="已考核学生数">已评分学生数</th>
              <td>${group.studentsScoredCount}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
<div class="layui-row" style="margin-top: 20px">
  <div class="layui-col-xs4">
    <div class="layui-card" style="height: 500px; overflow: auto">
      <div class="layui-card-body">
        <button class="layui-btn layui-btn-primary layui-btn-xs">全选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs">全不选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs">反选</button>
        <button class="layui-btn layui-btn-danger layui-btn-xs">删除</button>
        <div class="ztree" id="words-tree"></div>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card" style="height: 500px; overflow: auto">
      <div class="layui-card-body">
        <blockquote class="layui-elem-quote" style="margin-top: 10px;">
          结束评分后可查看该分组单词的考核情况！
        </blockquote>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card" style="height: 500px; overflow: auto">
      <div class="layui-card-body">
        <button class="layui-btn layui-btn-primary layui-btn-xs">全选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs">全不选</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs">反选</button>
        <button class="layui-btn layui-btn-danger layui-btn-xs">删除</button>
        <div class="ztree" id="students-tree"></div>
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

  var wordsNode = [
    {
      treeId: 'words',
      name: '单词',
      open: 'true',
      children: [
        <#list group.words as word>
          {id: '${word.id}', name: '${word.prototype} (${word.definition})'},
        </#list>
      ]
    }
  ];

  $.fn.zTree.init($('#words-tree'), setting, wordsNode);

  var studentsNode = [
    {
      name: '学生',
      open: 'true',
      children: [
        <#list students as clazz>
          {
            id: '${clazz.id}',
            name: '${clazz.name}<#if clazz.description??> (${clazz.description})</#if>',
            children: [
              <#list clazz.students as student>
                {
                  id: '#{student.id}',
                  name: '${student.name} (${student.number})<#if student.description??> (${student.description})</#if>'
                },
              </#list>
            ]
          },
        </#list>
      ]
    }
  ];

  $.fn.zTree.init($('#students-tree'), setting, studentsNode);

  $('#export').click(function () {
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
        data.groupId = ${group.id};
        data.title = parent.exportData.title;
        data.docSolutionId = parent.exportData.docSolution;
        data.shuffleSolutionId = parent.exportData.shuffleSolution;
        dict.loadText({
          url: '${base_url}/word/export_save.json',
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
  });
</script>