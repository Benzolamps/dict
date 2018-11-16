<#macro switch10 gt0=true gt1=true gt10=true>
  [
    <#list 0..10 as i>
      {'id': ${i}, 'value': '${i}'}<#sep>,
    </#list>
    <#local option = 10/>
    <#if gt0><#local option++/>, {'id': ${option}, 'value': '大于 0'}</#if>
    <#if gt1><#local option++/>, {'id': ${option}, 'value': '大于 1'}</#if>
    <#if gt10><#local option++/>, {'id': ${option}, 'value': '大于 10'}</#if>
  ]
</#macro>

<#macro switch100 gt0=true gt1=true gt100=true eq1=true>
  [
    {'id': 0, 'value': '0'},
    <#list 1..10 as i>
      {'id': ${i}, 'value': '${10 * (i - 1) + 1}～${10 * i}'}<#sep>,
    </#list>
    <#local option = 10/>
    <#if gt0><#local option++/>, {'id': ${option}, 'value': '大于 0'}</#if>
    <#if gt1><#local option++/>, {'id': ${option}, 'value': '大于 1'}</#if>
    <#if gt100><#local option++/>, {'id': ${option}, 'value': '大于 100'}</#if>
    <#if eq1><#local option++/>, {'id': ${option}, 'value': '1'}</#if>
  ]
</#macro>

<#macro switch1000 gt0=true gt1=true gt1000=true eq1=true>
  [
    {'id': 0, 'value': '0'},
    <#list 1..10 as i>
      {'id': ${i}, 'value': '${10 * (i - 1) + 1}～${10 * i}'}<#sep>,
    </#list>
    {'id': 11, 'value': '101～200'},
    {'id': 12, 'value': '201～500'},
    {'id': 13, 'value': '501～1000'}
    <#local option = 13/>
    <#if gt0><#local option++/>, {'id': ${option}, 'value': '大于 0'}</#if>
    <#if gt1><#local option++/>, {'id': ${option}, 'value': '大于 1'}</#if>
    <#if gt1000><#local option++/>, {'id': ${option}, 'value': '大于 1000'}</#if>
    <#if eq1><#local option++/>, {'id': ${option}, 'value': '1'}</#if>
  ]
</#macro>