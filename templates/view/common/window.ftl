<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="content_path" type="java.lang.String" -->
<@compress>
  <!doctype html>
  <html lang="zh-CN">
    <head>
      <meta charset="utf-8"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/layui/css/layui.css"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/font-awesome/css/font-awesome.min.css"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/res/css/common.css"/>
      <link rel="icon" type="image/x-icon" href="${base_url}/favicon.ico"/>
      <script type="text/javascript" src="${base_url}/js/jquery-3.3.1.js"></script>
      <script type="text/javascript" src="${base_url}/js/jquery.form.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/jquery.validate.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/additional-methods.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/localization/messages_zh.js"></script>
      <script type="text/javascript" src="${base_url}/layui/layui.all.js"></script>
      <script type="text/javascript" src="${base_url}/res/js/common.js"></script>
      <script type="text/javascript">
        parent.dict.updateSocket.initCallback();
      </script>
    </head>
    <body>
      <#include '/${content_path}.ftl'/>
    </body>
  </html>
</@compress>