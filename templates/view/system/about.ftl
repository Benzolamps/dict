<#assign accounts = url_yaml_parse('${remote_base_url}/yml/accounts.yml')/>
<#assign about><#include '/res/yml/about.yml.ftl'/></#assign>
<#assign about = string_yaml_parse(about)/>
<div class="layui-row">
  <div class="layui-col-xs4">
    <div class="layui-card">
      <div class="layui-card-header"><h2>关于系统</h2></div>
      <div class="layui-card-body">
        ${about.system.description}
        <hr class="layui-bg-gray">
        <#list about.system.columns as column>
          <fieldset class="layui-elem-field">
            <legend>${column.title}</legend>
            <div class="layui-field-box">${column.content}</div>
          </fieldset>
        </#list>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card">
      <div class="layui-card-header"><h2>关于开发工具</h2></div>
      <div class="layui-card-body">
        ${about.devtools.description}
        <hr class="layui-bg-gray">
        <#list about.devtools.columns as column>
          <fieldset class="layui-elem-field">
            <legend>${column.title}</legend>
            <div class="layui-field-box">${column.content}</div>
          </fieldset>
        </#list>
      </div>
    </div>
  </div>
  <div class="layui-col-xs4">
    <div class="layui-card">
      <div class="layui-card-header"><h2>关于作者</h2></div>
      <div class="layui-card-body" style="text-align: center">
        <fieldset class="layui-elem-field">
          <legend>${accounts.name}</legend>
          <div class="layui-field-box">英文名：${accounts.english_name}</div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>~~~~</legend>
          <div class="layui-field-box">${accounts.description}</div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>电子邮箱</legend>
          <div class="layui-field-box">
            <a href="mailto:${accounts.e_mail}">${accounts.e_mail}</a>
          </div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>GitHub</legend>
          <div class="layui-field-box">
            <a href="${accounts.github}" target="_blank"></a>
          </div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>CSDN</legend>
          <div class="layui-field-box">
            <a href="${accounts.csdn}" target="_blank"></a>
          </div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>码云</legend>
          <div class="layui-field-box">
            <a href="${accounts.gitee}" target="_blank"></a>
          </div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>Steam</legend>
          <div class="layui-field-box">
            <a href="${accounts.steam}" target="_blank"></a>
          </div>
        </fieldset>

        <fieldset class="layui-elem-field">
          <legend>新浪微博</legend>
          <div class="layui-field-box">
            <a href="${accounts.weibo}" target="_blank"></a>
          </div>
        </fieldset>

        <br>
        <span class="layui-breadcrumb" lay-separator=" ">
          <a class="img-handler" href="javascript:;" div="wechat-div">
            <i class="layui-icon" style="font-size: xx-large; color: #01bb0f">&#xe677;</i>
          </a>
          <a class="img-handler" href="javascript:;" div="qq-div">
            <i class="layui-icon" style="font-size: xx-large; color: #0188fb">&#xe676;</i>
          </a>
        </span>

        <script type="text/html" id="qq-div">
          <a href="tencent://message/?uin=${accounts.qq}&Menu=yes">
            <img src="${base_url}/remote/img/qrcode-qq.jpg" style="width: 300px"/>
          </a>
          <br><br>
          <a href="tencent://message/?uin=${accounts.qq}&Menu=yes" style="font-size: medium; color: #AAAAAA">
            ${accounts.qq}
          </a>
        </script>

        <script type="text/html" id="wechat-div">
          <img src="${base_url}/remote/img/qrcode-wechat.jpg" style="width: 300px"/><br><br>
          <a href="#" style="font-size: medium; color: #AAAAAA">${accounts.wechat}</a>
        </script>

        <script>
          $('a').each(function () {
            if ($(this).html().trim().length <= 0) {
              $(this).text($(this).attr('href'));
            }
          });
          $('.img-handler').mouseenter(function () {
            var div = $(this).attr('div');
            var content = $('#' + div).html();
            parent.layer.msg(content, {
              offset: 't',
              time: 0,
              anim: 2,
              shadeClose: true,
              shade: 0.3
            })
          });
        </script>
      </div>
    </div>
  </div>
</div>