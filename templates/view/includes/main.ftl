<#include 'data-list.ftl'/>
<#include 'data-add.ftl'/>
<#include 'data-edit.ftl'/>

<#-- 不做任何事情 -->
<#macro nothing></#macro>

<#-- 压缩代码成一行 -->
<#macro compress>
  <#local nested><#nested/></#local>
  <#if is_release>
    ${nested?replace(constant('HTML_COMPRESS_PATTERN'), 'rm')}
  <#else>
    <#compress>${nested}</#compress>
  </#if>
</#macro>

<#function regexp pattern flag=''>
  <#return '/${pattern}/${flag}'>
</#function>

<#macro update_socket>
  parent.dict.updateSocket.onHasNew = function (data) {
      parent.layer.confirm('有新版本, 是否更新？', {
          icon: 1,
          title: '提示',
          id: 'update'
      }, function (index) {
          var path = '${base_url}/system/settings.html';
          var iframeWindow = parent.$('#content-frame')[0].contentWindow;
          if (iframeWindow.location.href.indexOf(path) > -1) {
              iframeWindow.$('.update').trigger('click');
          } else {
              iframeWindow.location.href = path + '#update';
          }
          parent.layer.close(index);
      });
  };

  parent.dict.updateSocket.onFinished = function (data) {
      data = JSON.parse(data).data;
      var total = data.total;
      var totalSize = data.totalSize;
      var deltaTime = (data.deltaTime * 0.001).toFixed(3);
      parent.layer.alert(dict.format('更新完成！<br>共更新 {0} 个文件！<br>总共 {1} ！<br>用时 {2} 秒', total, totalSize, deltaTime));
  }

  parent.dict.updateSocket.onFailed = function (data) {
      parent.layer.alert('更新失败');
  }
</#macro>