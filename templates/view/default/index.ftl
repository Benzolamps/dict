<#--
  -- index界面
  -- author: Benzolamps
  -- version: 2.1.1
  -- datetime: 2018-7-11 19:25:14
  -->
<#-- @ftlvariable name="nav_column_index" type="java.lang.Integer" -->
<#-- @ftlvariable name="nav_child_index" type="java.lang.Integer" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="content_path" type="java.lang.String" -->
<@compress>
  <!doctype html>
  <html lang="zh-CN">
    <head>
      <meta charset="utf-8"/>
      <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/layui/css/layui.css"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/font-awesome/css/font-awesome.min.css"/>
      <link rel="stylesheet" type="text/css" href="${base_url}/res/css/common.css"/>
      <script type="text/javascript" src="${base_url}/js/jquery-3.3.1.js"></script>
      <script type="text/javascript" src="${base_url}/js/jquery.cookie.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/jquery.validate.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/additional-methods.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/localization/messages_zh.js"></script>
      <script type="text/javascript" src="${base_url}/layui/layui.js"></script>
      <script type="text/javascript" src="${base_url}/res/js/common.js"></script>
      <style>
        .dict-bg {
          background-color: #EEEEEE !important;
        }

        .dict-nav {
          color: #333333 !important;
          font-size: x-large;
        }

        .dict-column {
          background-color: #EEEEEE !important;
          color: #333333 !important;
          border-bottom: solid 1px #CCCCCC;
        }
        .dict-column:hover {
          background-color: #DDDDDD !important;
          color: #222222 !important;
        }

        .dict-column:active {
          background-color: #DDDDDD !important;
          color: #222222 !important;
        }

        .dict-child {
          background-color: #CCCCCC !important;
          color: #333333 !important;
        }

        .dict-child:hover {
          background-color: #BBBBBB !important;
          color: #222222 !important;
        }

        .dict-child:active {
          background-color: #BBBBBB !important;
          color: #222222 !important;
        }

        .layui-nav-itemd>.dict-column {
          background-color: #DDDDDD !important;
          color: #222222 !important !important;
        }

        .layui-this>.dict-child {
          background-color: #FFB800 !important;
          color: #222222 !important;
        }

        .layui-nav-bar {
          background-color: #222222 !important;
        }

        .layui-nav-more {
          border-color: #222222 transparent transparent transparent !important;
        }

        .layui-nav-mored,.layui-nav-itemed>.dict-column .layui-nav-more {
          border-color: transparent transparent #222222 transparent !important;
        }
      </style>
    </head>
    <body>
      <div class="layui-layout layui-layout-admin">
        <div class="layui-header" style="background-color: #FBFBFB">
          <div class="layui-logo" style="background-color: #FFB800">
            <a href="${base_url}" title="${system_title} ~ ${system_version}">
              ${abbreviate('${system_title} ~ ${system_version}', 20, '...')?html}
            </a>
          </div>
          <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item layui-nav-itemd" lay-unselect>
              <a href="javascript:;" class="dict-nav" id="toggle-nav">
                <i class="fa fa-angle-double-left" aria-hidden="true"></i>
              </a>
            </li>
          </ul>
          <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item" lay-unselect>
              <a href="javascript:;" id="lock_screen" style="color: #333333">${(current_user().nickname)!''} </a>
              <dl class="layui-nav-child"> <!-- 二级菜单 -->
                <dd><a href="javascript:;" id="password-nav">修改密码</a></dd>
                <dd><a href="javascript:;" id="profile-nav">个人资料</a></dd>
              </dl>
            </li>
            <li class="layui-nav-item" lay-unselect>
              <a href="javascript:;" id="lock_screen" style="color: #333333">锁屏</a>
            </li>
            <li class="layui-nav-item" lay-unselect>
              <a href="javascript:;" id="logout" style="color: #333333">注销</a>
            </li>
          </ul>
        </div>
        <div class="layui-side dict-bg">
          <div class="layui-side-scroll">
            <#-- 左侧导航区域 (可配合layui已有的垂直导航) -->
            <ul class="dict-nav-tree layui-nav layui-nav-tree" lay-shrink="all">
              <#assign column_data><#include '/res/json/columns.json.ftl'/></#assign>
                <#list column_data?eval as column>
                  <li class="layui-nav-item">
                    <a class="dict-column" href="javascript:;">${column.title}</a>
                    <dl class="layui-nav-child">
                      <#list column.children as child>
                        <dd>
                          <a class="child-item dict-child" href="javascript:;" src="${child.href}">
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
        <div id="content-div" class="layui-body dict-body" style="margin: 5px; padding: 5px;">
          <#-- 内容主体区域 -->
          <div class="layui-tab tab" lay-filter="docDemoTabBrief" style="height: 95%">
            <div class="layui-tab-content" style="width: 100%; height: 100%">
              <div class="layui-tab-item layui-show" style="width: 100%; height: 100%">
                <iframe src="javascript:;" frameborder="0" style="width: 98%; height: 100%"></iframe>
              </div>
            </div>
          </div>
        </div>
        <div class="layui-footer dict-footer" style="background-color: #FBFBFB; text-align: center">
          &copy; <a target="_blank" href="https://benzolamps.com">benzolamps.com</a>
        </div>
      </div>
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

          var column = 0, child = 0;

          if (localStorage.getItem('column')) {
            column = localStorage.getItem('column');
          }

          if (localStorage.getItem('child')) {
            child = localStorage.getItem('child');
          }

          /* 将找到的栏目设为选中 */
          if (column >= 0 && child >= 0) {
            var $li = $('ul.dict-nav-tree>li').eq(column);
            $li.addClass('layui-nav-itemed');
            /* language=JQuery-CSS */
            var $dd = $li.find('dl.layui-nav-child>dd').eq(child);
            $dd.addClass('layui-this');
            $('iframe').attr('src', columns[column].children[child].href);
            localStorage.setItem('column', column);
            localStorage.setItem('child', child);
          }

          var tipsId;

          $('.child-item').click(function () {
            $('iframe').attr('src', $(this).attr('src'));
            child = $(this).parentsUntil('dl').last().prevAll().length;
            column = $(this).parentsUntil('ul').last().prevAll().length;
            localStorage.setItem('column', column);
            localStorage.setItem('child', child);
          }).mouseenter(function () {
            tipsId = layer.tips($(this).children('span').text(), this);
          }).mouseleave(function () {
            if (tipsId != null) {
              layer.close(tipsId);
              tipsId = null;
            }
          });

          var $dictBg = $('.dict-bg');

          $('#toggle-nav').click(function () {
            if ($dictBg.css('display') != 'none') {
              $('.dict-bg').slideUp(function () {
                $('.dict-body,.dict-footer').animate({left: 0});
              });
            } else {
              $('.dict-body,.dict-footer').animate({left: '200px'}, 'fast', function () {
                $dictBg.slideDown();
                }
              );
            }
          });

          $('#password-nav').click(function () {
            layer.open({
              type: 2,
              content: '${base_url}/user/edit_password.html',
              area: ['500px', '500px']
            });
          });

          $('#lock_screen').click(function () {

          });

          $('#logout').click(function () {
            layer.confirm('确定要注销吗？', {icon: 3, title: '提示'}, function (index) {
              dict.loadText({
                url: '${base_url}/user/logout.json',
                type: 'post',
                success: function (result, status, request) {
                  if (result) {
                    layer.alert('注销成功', {
                      icon: 1,
                      title: '注销成功',
                      yes: function (index) {
                        location.reload(true);
                      }
                    });
                  } else {
                    layer.alert('注销失败', {
                      icon: 2,
                      title: '注销失败',
                      yes: function (index) {
                        layer.close(index);
                      }
                    });
                  }
                },
                error: function (result, status, request) {
                  layer.alert(result.message, {
                    icon: 2,
                    title: result.status,
                    yes: function (index) {
                      layer.close(index);
                    }
                  });
                }
              });
            });
          });

/*          var lockScreenDelta = 10;
          var lockScreenDelay = 0;
          var interval = setInterval(function () {
            lockScreenDelay++;
            if (lockScreenDelay >= lockScreenDelta) {
              alert('hhh');
              clearInterval(interval);
              $('body').unbind('mousedown keydown mousemove wheel');
            }
          }, 1000);

          var rechargeLockScreen = function () {
            lockScreenDelay = 0;
            console.log('recharge');
          };

          $('body').bind('mousedown keydown mousemove wheel', rechargeLockScreen);*/
        });
      </script>
    </body>
  </html>
</@compress>