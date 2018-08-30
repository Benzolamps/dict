<#include 'list.ftl'/>
<#include 'data-add.ftl'/>
<#include 'data-edit.ftl'/>

<#-- 不做任何事情 -->
<#macro nothing></#macro>

<#-- 压缩代码成一行 -->
<#macro compress>
  <#local nested><#nested/></#local>
  <#if is_release>
    ${nested?replace('^\\s+|\\s+$|\\n|\\r', '', 'rm')}
  <#else>
    <#compress>${nested}</#compress>
  </#if>
</#macro>

<#function regexp pattern flag=''>
  <#return '/${pattern}/${flag}'>
</#function>