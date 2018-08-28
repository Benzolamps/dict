<#include 'list.ftl'/>

<#--
  -- 不做任何事情
  -->
<#macro nothing></#macro>

<#--
  -- 压缩代码成一行
  -->
<#macro compress>
  <#local nested><#nested/></#local>
  ${nested?replace('^\\s+|\\s+$|\\n|\\r', '', 'rm')}
</#macro>

<#function regexp pattern flag=''>
  <#return '/${pattern}/${flag}'>
</#function>