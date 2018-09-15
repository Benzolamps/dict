<#-- @ftlvariable name="status" type="com.benzolamps.dict.service.base.VersionService.Status" -->
<#-- @ftlvariable name="newVersionName" type="java.lang.String" -->
<blockquote class="layui-elem-quote dict-blockquote" style="margin-top: 10px;">
  <div class="update-content" style="<#if status != 'HAS_NEW'>display: none;</#if>">
    检测到新版本：${newVersionName}&nbsp;&nbsp;&nbsp;&nbsp;
    <button id="update" class="layui-btn layui-btn-warm layui-btn-sm">
      <i class="fa fa-puzzle-piece" style="font-size: 20px;"></i> &nbsp; 开始更新
    </button>
  </div>
  <#switch status>
    <#case 'ALREADY_NEW'>
      当前已是最新版本，无需更新！
      <#break/>
    <#case 'DOWNLOADING'>
      正在下载新版本！
      <#break/>
    <#case 'COPYING'>
      正在复制文件！
      <#break/>
    <#case 'DELETING'>
      正在删除旧版本！
      <#break/>
    <#case 'FINISHED'>
      更新已完成！
      <#break/>
    <#case 'FAILED'>
      更新失败！
      <#break/>
  </#switch>
</blockquote>

<script type="text/javascript">
  $('#update').click(function () {
    parent.layer.confirm('是否开始更新？', {icon: 3, title: '提示'}, function (index) {
      parent.layer.close(index);
      dict.loadText({
        url: 'update.json',
        type: 'post',
        error: function (result, status, request) {
          parent.layer.alert(result.message, {
            icon: 2,
            title: result.status
          });
        }
      });
    });
  });
</script>