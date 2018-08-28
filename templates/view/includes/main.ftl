<#include 'list.ftl'/>

<#--
  -- 不做任何事情
  -->
<#macro nothing></#macro>

<#--
  -- 压缩代码成一行
  -->
<#macro compress>
  <#assign nested><#nested/></#assign>
  ${nested?replace("^\\s+|\\s+$|\\n|\\r", "", "rm")}
</#macro>