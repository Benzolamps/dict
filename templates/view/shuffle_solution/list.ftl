<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->

<div class="layui-row">
  <button class="layui-btn layui-btn-warm layui-btn-sm">
    <i class="layui-icon" style="font-size: 20px; color: #FFFFFF">&#xe654; 添加</i>
  </button>
  <button class="layui-btn layui-btn-danger layui-btn-sm layui-disabled">
    <i class="layui-icon" style="font-size: 20px;  color: #FFFFFF">&#xe640; 删除</i>
  </button>
  <button class="layui-btn layui-btn-normal layui-btn-sm" onclick=";">
    <i class="layui-icon" style="font-size: 20px;  color: #FFFFFF">&#xe666; 刷新</i>
  </button>
  <table class="layui-table" id="shuffle-solutions"></table>
</div>

<script type="text/html" id="shuffle-solutions-tools">
  <button class="layui-btn layui-btn-primary layui-btn-xs">
    <i class="layui-icon" style="font-size: 20px;">&#xe642;</i> 修改
  </button>
  <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="del">
    <i class="layui-icon" style="font-size: 20px;">&#xe640;</i> 删除
  </button>
</script>

<script type="text/javascript">
  layui.use('table', function () {
    var table = layui.table;
    table.render({
      // language=JQuery-CSS
      elem: '#shuffle-solutions',
      page: false,
      cols: [[
        {type: 'numbers'},
        {type: 'checkbox'},
        {field: 'name', title: '随机方案名称', sort: 'true'},
        {field: 'strategyClass', title: '随机策略名称', sort: 'true'},
        {field: 'remark', title: '备注', sort: 'true'},
        {fixed: 'right', align: 'left', toolbar: '#shuffle-solutions-tools'}
      ]],
      data: <@json_dumper obj = page.content/>,
      id: 'shuffle_solutions'
    });
  });
</script>
