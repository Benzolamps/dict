<div class="layui-card">
  <div class="layui-card-header">添加乱序方案</div>
  <div class="layui-card-body">
    <form class="layui-form">

      <table class="layui-table">
        <tr>
          <th>乱序方案名称</th>
          <td><input type="text" name="name" required lay-verify="required" placeholder="乱序方案名称..." autocomplete="off" class="layui-input"></td>
        </tr>
        <tr>
          <th>乱序策略</th>
          <td>
            <div class="layui-input-block">
              <select name="strategyClass" lay-verify="required" title="">
                <option value="" selected>请选择乱序策略</option>
                <#list strategies as strategy>
                  <option value="${strategy}" selected>${strategy}</option>
                </#list>
              </select>
            </div>
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
  });
</script>