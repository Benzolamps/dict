<#-- @ftlvariable name="strategies" type="java.util.Collection<java.lang.String>" -->
<div class="layui-card">
  <div class="layui-card-header">添加乱序方案</div>
  <div class="layui-card-body">
    <form class="layui-form shuffle-solution-form">
      <table class="layui-table">
        <tr>
          <th>乱序方案名称</th>
          <td><input type="text" name="name" required lay-verify="required" placeholder="乱序方案名称..." autocomplete="off" class="layui-input"></td>
        </tr>
        <tr>
          <th>乱序策略</th>
          <td>
            <select name="strategyClass" lay-filter="strategy-class" lay-verify="required" title="">
              <option value="" selected>请选择乱序策略</option>
              <#list strategies as strategy>
                <option value="${strategy}">${strategy}</option>
              </#list>
            </select>
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>

<script type="text/javascript">
  layui.use('form', function(){
    var form = layui.form;
    //监听提交
    form.on('submit(formDemo)', function(data){
      layer.msg(JSON.stringify(data.field));
      return false;
    });

    form.on('select(strategy-class)', function (selectData) {
      dict.loadText({
        url: 'property_info.json',
        data: {
          className: selectData.value
        }
      });
    });
  });
</script>