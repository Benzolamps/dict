<#--
  -- layout界面
  -- author: Benzolamps
  -- version: 2.1.1
  -- datetime: 2018-7-11 19:25:14
  -->

<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="content_path" type="java.lang.String" -->
<@compress>
  <!doctype html>
  <html lang="zh-CN">
    <head>
      <meta charset="utf-8"/>
      <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=0.8"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/layui/css/layui.css"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/font-awesome/css/font-awesome.min.css"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/res/css/common.css"/>
      <script type="text/javascript" src="${base_url}/js/jquery-3.3.1.js"></script>
      <script type="text/javascript" src="${base_url}/js/jquery.form.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/jquery.validate.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/additional-methods.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/localization/messages_zh.js"></script>
      <script type="text/javascript" src="${base_url}/layui/layui.all.js"></script>
      <script type="text/javascript" src="${base_url}/res/js/common.js"></script>
    </head>
    <body>
      <#include '/${content_path}.ftl'/>
      <script type="text/javascript">
        $(function () {
          var columns = null;

          /* 请求栏目JSON */
          dict.loadText({
            type: "get",
            url: '${base_url}/res/json/columns.json',
            cache: true,
            dataType: 'json',
            success: function(data) {
              columns = data;
            }
          });

          /* 将当前页面地址与JSON中逐个比对 */
          var href = location.href, column = -1, child = -1, title = '${title!""}';
          for (var i = 0; i < columns.length; i++) {
            var children = columns[i].children;
            for (var j = 0; j < children.length; j++) {
              if (href.indexOf(children[j].href) > -1) {
                column = i;
                child = j;
                title || title.trim().length > 0 || (title = children[j].title);
                break;
              }
            }
          }

          document.title = title;
          parent.document.title = title + ' - ${system_title} - ${system_version}';
        });
      </script>
    </body>
  </html>
</@compress>