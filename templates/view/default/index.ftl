<#-- @ftlvariable name="nav_column_index" type="java.lang.Integer" -->
<#-- @ftlvariable name="nav_child_index" type="java.lang.Integer" -->
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
      <script type="text/javascript" src="${base_url}/js/sockjs-0.3.js"></script>
      <script type="text/javascript" src="${base_url}/js/stomp.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/jquery.validate.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/additional-methods.js"></script>
      <script type="text/javascript" src="${base_url}/jquery-validation-1.17.0/localization/messages_zh.js"></script>
      <script type="text/javascript" src="${base_url}/layui/layui.all.js"></script>
      <script type="text/javascript" src="${base_url}/res/js/common.js"></script>
      <script type="text/javascript" src="${base_url}/res/js/update-socket.js"></script>
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

        .layui-nav-itemed>.dict-column {
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
            <a href="javascript:dict.reload(true);" title="${system_title} ~ ${system_version}" >
              ${abbreviate('${system_title} ~ ${system_version}', 20, '...')?html}
            </a>
          </div>
          <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item layui-nav-itemed" lay-unselect>
              <a class="dict-nav" id="toggle-nav">
                <i class="fa fa-angle-double-left" aria-hidden="true"></i>
              </a>
            </li>
            <#if current_library()??>
              <li class="layui-nav-item" lay-unselect>
                <a style="color: #333333">当前词库：${current_library().name}</a>
                <dl class="layui-nav-child">
                  <#list all_libraries() as library>
                    <#-- @ftlvariable name="library" type="com.benzolamps.dict.bean.Library" -->
                    <dd<#if current_library() == library> class="layui-this"</#if>>
                      <a onclick="changeCurrentLibrary(${library.id});">
                        ${library.name}
                      </a>
                    </dd>
                  </#list>
                </dl>
              </li>
            </#if>
            <li class="layui-nav-item" lay-unselect>
              <a style="color: #333333" class="date-time-text">
                <script type="text/javascript">
                  document.write(dict.dateToStr(new Date()));
                </script>
              </a>
            </li>
            <li class="layui-nav-item" lay-unselect>
              <a style="color: #333333" class="lunar-date-text">
                ${current_lunar_date()}
              </a>
            </li>
          </ul>
          <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item" lay-unselect>
              <a id="lock_screen" style="color: #333333">${(current_user().nickname)!''} </a>
              <dl class="layui-nav-child">
                <dd><a id="password-nav">修改密码</a></dd>
                <dd><a id="profile-nav">个人资料</a></dd>
              </dl>
            </li>
            <li class="layui-nav-item" lay-unselect>
              <a id="lock-screen-nav" style="color: #333333">锁屏</a>
            </li>
            <li class="layui-nav-item" lay-unselect>
              <a id="logout" style="color: #333333">注销</a>
            </li>
          </ul>
        </div>
        <div class="layui-side dict-bg">
          <div class="layui-side-scroll">
            <ul class="dict-nav-tree layui-nav layui-nav-tree" lay-shrink="all">
              <#assign column_data><#include '/res/json/columns.json.ftl'/></#assign>
                <#list column_data?eval as column>
                  <li class="layui-nav-item">
                    <a class="dict-column">${column.title}</a>
                    <dl class="layui-nav-child">
                      <#list column.children as child>
                        <dd>
                          <a class="child-item dict-child" src="${child.href}">
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
          <div class="layui-tab tab" lay-filter="docDemoTabBrief" style="height: 95%">
            <div class="layui-tab-content" style="width: 100%; height: 100%">
              <div class="layui-tab-item layui-show" style="width: 100%; height: 100%">
                <iframe src="javascript:;" id="content-frame" frameborder="0" style="width: 98%; height: 100%"></iframe>
              </div>
            </div>
          </div>
        </div>
        <div class="layui-footer dict-footer" style="background-color: #FBFBFB; text-align: center">
          &copy; <a target="_blank">benzolamps.com</a>
        </div>
      </div>
      <script type="text/javascript">
        element.render();

        setInterval(function () {
          var now = new Date();
          $('.date-time-text').text(dict.dateToStr(now));
          if (now.getHours() == 0 && now.getMinutes() == 0 &&now.getSeconds() == 0) {
            dict.loadText({
              type: "get",
              url: '${base_url}/res/txt/lunar_date.txt',
              dataType: 'text',
              success: function(data) {
                $('.lunar-date-text').text(data);
              },
              error: function (result) {
                layer.alert(result.message, {
                  icon: 2,
                  title: result.status
                });
              }
            });
          }
        }, 500);

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
          $('#content-frame').attr('src', columns[column].children[child].href);
          localStorage.setItem('column', column);
          localStorage.setItem('child', child);
        }

        var tipsId;

        $('.child-item').click(function () {
          $('#content-frame').attr('src', $(this).attr('src'));
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
            area: ['400px', '400px'],
            title: '修改密码'
          });
        });

        $('#lock-screen-nav').click(function () {
          layer.open({
            type: 2,
            content: '${base_url}/user/lock_screen.html',
            area: ['500px', '500px'],
            anim: 5,
            title: false,
            closeBtn: 0,
            shade: [0.8, '#393D49']
          });
        });

        $('#logout').click(function () {
          layer.confirm('确定要注销吗？', {icon: 3, title: '提示'}, function (index) {
            dict.loadText({
              url: '${base_url}/user/logout.json',
              type: 'post',
              success: function (result, status, request) {
                layer.alert('注销成功！', {
                  icon: 1,
                  end: function () {
                    dict.reload(true);
                  }
                });
              },
              error: function (result, status, request) {
                layer.alert(result.message, {
                  icon: 2,
                  title: result.status
                });
              }
            });
          });
        });

        $('#profile-nav').click(function () {
          layer.open({
            type: 2,
            content: '${base_url}/user/profile.html',
            area: ['400px', '400px'],
            title: '个人资料'
          });
        });

        if (null != localStorage.getItem('password')) {
          $('*').css('pointer-events', 'none');
          setTimeout(function () {
            $('#lock-screen-nav').trigger('click');
          }, 500);
        }

        var changeCurrentLibrary = function (id) {
          setTimeout(function () {
            dict.loadText({
              url: '${base_url}/library/change_current.json',
              type: 'get',
              data: {id: id},
              success: function () {
                dict.reload(true);
              },
              error: function (result) {
                layer.alert(result.message, {
                  icon: 2,
                  title: result.status
                });
              }
            });
          }, 500);
        };
        dict.updateSocket.connect();

        lastTimeStr = localStorage.getItem("lastTime");
        var needShow = lastTimeStr == null;
        if (!needShow) {
          var now = new Date();
          var lastTime = new Date(parseInt(lastTimeStr));
          console.log(now.getDate());
          if (!(now.getFullYear() == lastTime.getFullYear() && now.getMonth() == lastTime.getMonth() && now.getDate() == lastTime.getDate())) {
            needShow = true;
          }
        }

        if (needShow) {
          dict.loadText({
            url: 'https://benzolamps.oss-cn-beijing.aliyuncs.com/dict/tips.txt',
            type: 'get',
            dataType: 'text',
            success: function (result) {
              if ($.trim(result) != '') {
                layer.alert(result, {
                  icon: 6
                });
                localStorage.setItem("lastTime", new Date().getTime());
              }
            },
            error: function (result) {
              layer.alert(JSON.stringify(result), {
                icon: 2
              });
            }
          });
        }
      </script>
    </body>
  </html>
</@compress>