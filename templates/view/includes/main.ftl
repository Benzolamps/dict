<#assign _>
  <#include 'data-list.ftl'/>
  <#include 'data-add.ftl'/>
  <#include 'data-edit.ftl'/>
  <#include 'data-tree.ftl'/>
  <#include 'swicth-options.ftl'/>

  <#-- 不做任何事情 -->
  <#macro nothing></#macro>

  <#function regexp pattern flag=''>
    <#return '/${pattern}/${flag}'>
  </#function>
</#assign>