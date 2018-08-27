<#include 'list.ftl'/>

<#--
  -- 不做任何事情
  -->
<#macro nothing></#macro>

<#--
  -- 压缩代码成一行
  -->
<#function compress str>
  <#return str?replace("^\\s+|\\s+$|\\n|\\r", "", "rm")/>
</#function>