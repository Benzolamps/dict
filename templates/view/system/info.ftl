<#--
  -- 系统信息界面
  -- author: Benzolamps
  -- version: 2.1.1
  -- datetime: 2018-7-11 19:19:16
  -->

<div class="layui-row">
  <div class="layui-col-xs6">
    <div class="layui-card">
      <div class="layui-card-body">
        <table class="layui-table">
          <colgroup>
            <col width="30">
            <col width="70">
          </colgroup>
          <thead></thead>
          <tbody>
            <tr>
              <th title="系统名称">系统名称</th>
              <td>${system_title}</td>
            </tr>
            <tr>
              <th title="系统版本">系统版本</th>
              <td>${system_version}</td>
            </tr>
            <tr>
              <th title="当前时间">当前时间</th>
              <td class="date-time-text"><#include '/res/txt/current_time.txt.ftl'/></td>
            </tr>
            <tr>
              <th title="操作系统名称">操作系统名称</th>
              <td>${os_name}</td>
            </tr>
            <tr>
              <th title="操作系统版本">操作系统版本</th>
              <td>${os_version}</td>
            </tr>
            <tr>
              <th title="操作系统架构">操作系统架构</th>
              <td>${os_arth}</td>
            </tr>
            <tr>
              <th title="用户名">用户名</th>
              <td>${user_name}</td>
            </tr>
            <tr>
              <th title="主机名">主机名</th>
              <td>${host_name}</td>
            </tr>
            <tr>
              <th title="IP地址">IP地址</th>
              <td>${host_address}</td>
            </tr>
            <tr>
              <th title="浏览器信息">浏览器信息</th>
              <td><script>document.write(navigator.userAgent);</script></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="layui-col-xs6">
    <div class="layui-card">
      <div class="layui-card-body">
        <table class="layui-table">
          <thead></thead>
          <tbody>
            <tr>
              <th>Java版本</th>
              <td>${java_version}</td>
            </tr>
            <tr>
              <th>SpringBoot版本</th>
              <td>${springboot_version}</td>
            </tr>
            <tr>
              <th>Tomcat版本</th>
              <td>${tomcat_version}</td>
            </tr>
            <tr>
              <th>Servlet版本</th>
              <td>${servlet_version}</td>
            </tr>
            <tr>
              <th>Freemarker版本</th>
              <td>${.version}</td>
            </tr>
            <tr>
              <th>Hibernate版本</th>
              <td>${hibernate_version}</td>
            </tr>
            <tr>
              <th>POI版本</th>
              <td>${poi_version}</td>
            </tr>
            <tr>
              <th>JQuery版本</th>
              <td id="jquery-version"><script>document.write($.fn.jquery);</script></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
  dict.rtDateTimeText('.date-time-text');
</script>