<#--
  -- 错误界面
  -- author: Benzolamps
  -- version: 2.1.1
  -- datetime: 2018-7-11 19:20:43
  -->

<#-- @ftlvariable name="timestamp" type="java.util.Date" -->
<#-- @ftlvariable name="status" type="java.lang.Integer" -->
<#-- @ftlvariable name="path" type="java.lang.String" -->
<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="exception" type="java.lang.String" -->
<#-- @ftlvariable name="trace" type="java.lang.String" -->
<#assign title>程序出问题了</#assign>
<div class="layui-card">
  <div class="layui-card-body">
    <blockquote class="layui-elem-quote">
      请求出错啦！&nbsp;&nbsp;&nbsp;&nbsp;
      <button class="layui-btn layui-btn-normal layui-btn-sm" onclick="history.back();">
        <i class="fa fa-arrow-circle-left" style="font-size: 20px;"></i> &nbsp; 返回
      </button>
      <button class="layui-btn layui-btn-warm layui-btn-sm" onclick="location.reload(true);">
        <i class="fa fa-refresh" style="font-size: 20px;"></i> &nbsp; 刷新
      </button>
    </blockquote>
    <div class="layui-row">
      <table class="layui-table">
        <colgroup>
          <col width="30">
          <col width="70">
        </colgroup>
        <tbody>
          <tr>
            <td title="时间戳">时间戳</td>
            <td>${timestamp?string('yyyy-MM-dd HH:mm:ss.SSS')}</td>
          </tr>
          <tr>
            <td title="状态码">状态码</td>
            <td>${status}</td>
          </tr>
          <tr>
            <td title="错误信息">错误信息</td>
            <td>${error}</td>
          </tr>
          <tr>
            <td title="请求路径">请求路径</td>
            <td>${path}</td>
          </tr>
          <tr>
            <td title="提示信息">提示信息</td>
            <td>${message}</td>
          </tr>
          <#if exception?? && trace??>
            <tr>
              <td title="异常">异常</td>
              <td>${exception!''}</td>
            </tr>
            <tr>
              <td colspan="2">
                <pre class="layui-code">${trace}</pre>
              </td>
            </tr>
          </#if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
  layui.use('code', function() {
    parent.dict.updateSocket.initCallback();
    layui.code({
      about: false,
      title: 'Stack Trace'
    });
  });
</script>