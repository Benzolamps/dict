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
              <td>${host_address?join('／')}</td>
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
              <th title="Java版本">Java版本</th>
              <td>${java_version}</td>
            </tr>
            <tr>
              <th title="SpringBoot版本">SpringBoot版本</th>
              <td>${springboot_version}</td>
            </tr>
            <tr>
              <th title="Tomcat版本">Tomcat版本</th>
              <td>${tomcat_version}</td>
            </tr>
            <tr>
              <th title="Servlet版本">Servlet版本</th>
              <td>${servlet_version}</td>
            </tr>
            <tr>
              <th title="Freemarker版本">Freemarker版本</th>
              <td>${.version}</td>
            </tr>
            <tr>
              <th title="Hibernate版本">MySQL版本</th>
              <td>${mysql_version}</td>
            </tr>
            <tr>
              <th title="Hibernate版本">Hibernate版本</th>
              <td>${hibernate_version}</td>
            </tr>
            <tr>
              <th title="POI版本">POI版本</th>
              <td>${poi_version}</td>
            </tr>
            <tr>
              <th title="JQuery版本">JQuery版本</th>
              <td id="jquery-version"><script>document.write($.fn.jquery);</script></td>
            </tr>
            <tr>
              <th title="LayUI版本">LayUI版本</th>
              <td id="jquery-version"><script>document.write(layui.v);</script></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>