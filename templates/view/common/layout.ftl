<#--
  -- layout界面
  -- author: Benzolamps
  -- version: 2.1.1
  -- datetime: 2018-7-11 19:25:14
  -->

<#-- @ftlvariable name="nav_column_index" type="java.lang.Integer" -->
<#-- @ftlvariable name="nav_child_index" type="java.lang.Integer" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="content_path" type="java.lang.String" -->
<!doctype html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" type="text/css" href="${base_url}/layui/css/layui.css"/>
    <link rel="stylesheet" type="text/css" href="${base_url}/layui/css/modules/layer/default/layer.css"/>
    <link rel="stylesheet" type="text/css" href="${base_url}/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base_url}/res/css/common.css"/>
    <script type="text/javascript" src="${base_url}/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="${base_url}/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/jquery.validate.js"></script>
    <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/additional-methods.js"></script>
    <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/localization/messages_zh.js"></script>
    <script type="text/javascript" src="${base_url}/layui/layui.js"></script>
    <script type="text/javascript" src="${base_url}/layui/lay/modules/layer.js"></script>
    <script type="text/javascript" src="${base_url}/res/js/common.js"></script>
  </head>
  <body>
    <div class="layui-layout layui-layout-admin">
      <div class="layui-header layui-bg-gray">
        <div class="layui-logo" style="width: auto; text-align: left; padding-left: 20px">
          <a href="${base_url}">
            ${system_title}~&nbsp;&nbsp;${system_version}
          </a>
        </div>
        <ul class="layui-nav layui-layout-left">
          <li class="layui-nav-item layui-nav-itemd" lay-unselect>
            <a style="color: #666"></a>
          </li>
        </ul>
      </div>
      <div class="layui-side layui-bg-cyan">
        <div class="layui-side-scroll">
          <#-- 左侧导航区域 (可配合layui已有的垂直导航) -->
          <ul class="dict-nav-tree layui-nav layui-nav-tree layui-bg-cyan" lay-shrink="all">
            <#assign column_data><#include '/res/json/columns.json.ftl'/></#assign>
            <#list column_data?eval as column>
              <li class="layui-nav-item">
                <a href="javascript:;">${column.title}</a>
                <dl class="layui-nav-child">
                  <#list column.children as child>
                    <dd>
                      <a class="child-item" href="${child.href}">
                        &nbsp;&nbsp; <span class="child-title">${child.title}</span>
                      </a>
                    </dd>
                  </#list>
                </dl>
              </li>
            </#list>
          </ul>
        </div>
      </div>
      <div id="content-div" class="layui-body">
        <#-- 内容主体区域 -->
        <div style="padding: 15px;">
          <div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
            <ul class="layui-tab-title">
              <li class="layui-this content-title"></li>
            </ul>
            <div class="layui-tab-content">
              <#include '/${content_path}.ftl'/>
            </div>
          </div>
        </div>
      </div>
      <div class="layui-footer">
        &copy;
        <a target="_blank" href="https://benzolamps.com">benzolamps.com</a>
      </div>
    </div>
    <script src="${base_url}/res/js/navigation.js<#if title??>?title=${title}</#if>"></script>
  </body>
</html>