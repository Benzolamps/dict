<#-- @ftlvariable name="status" type="com.benzolamps.dict.service.base.VersionService.Status" -->
<#-- @ftlvariable name="newVersionName" type="java.lang.String" -->
<#-- @ftlvariable name="deltaTime" type="long" -->
<#-- @ftlvariable name="total" type="long" -->
<#-- @ftlvariable name="totalSize" type="java.lang.String" -->
<blockquote class="layui-elem-quote dict-blockquote" style="margin-top: 10px;">
  <span class="update-content">
    <#switch status>
      <#case 'ALREADY_NEW'>当前已是最新版本，无需更新！<#break/>
      <#case 'HAS_NEW'>检测到新版本：${newVersionName}<#break/>
      <#case 'DOWNLOADING'>正在下载新版本！<#break/>
      <#case 'DOWNLOADED'>下载完成！<br>共下载 #{total} 个文件！<br>总共 ${totalSize} ！<br>用时 #{0.001 * deltaTime; m3M3} 秒！<#break/>
      <#case 'INSTALLED'>安装完成！<br>共安装 #{total} 个文件！<br>总共 ${totalSize} ！<br>用时 #{0.001 * deltaTime; m3M3} 秒！<#break/>
      <#case 'FAILED'>更新失败！<#break/>
    </#switch>
  </span>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <span class="update-button"<#if ['DOWNLOADING', 'DOWNLOADED', 'INSTALLED']?seq_contains(status)> style="display: none"</#if>>
    <button id="update" class="layui-btn layui-btn-warm layui-btn-sm">
      <i class="fa fa-puzzle-piece" style="font-size: 20px;"></i> &nbsp; <#if status='FAILED'>重试<#else>开始更新</#if>
    </button>
    <button id="update-data" class="layui-btn layui-btn-sm">
      <a href="${remote_base_url}/dict/dict.rar">
        <i class="fa fa-puzzle-piece" style="font-size: 20px;"></i> &nbsp; 下载完整版数据包
      </a>
    </button>
    <button id="update-data-jre" class="layui-btn layui-btn-normal layui-btn-sm">
      <a href="${remote_base_url}/dict/dict-jre.rar">
        <i class="fa fa-puzzle-piece" style="font-size: 20px;"></i> &nbsp; 下载完整版数据包 + JRE
      </a>
    </button>
  </span>
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