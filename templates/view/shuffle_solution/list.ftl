<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->

<div class="layui-row">
  <form class="layui-form shuffle-solutions" lay-filter="shuffle-solutions" onsubmit="return false;" method="post">
    <button class="layui-btn layui-btn-warm layui-btn-sm add" lay-event="add">
      <i class="layui-icon" style="font-size: 20px; color: #FFFFFF">&#xe654; 添加</i>
    </button>
    <button class="layui-btn layui-btn-danger layui-btn-sm layui-disabled" lay-event="del">
      <i class="layui-icon" style="font-size: 20px; color: #FFFFFF">&#xe640; 删除</i>
    </button>
    <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="refresh">
      <i class="layui-icon" style="font-size: 20px; color: #FFFFFF">&#xe666; 刷新</i>
    </button>
    <table class="layui-table" id="shuffle-solutions" lay-filter="shuffle-solutions"></table>
  </form>
</div>

<script type="text/html" id="shuffle-solutions-tools">
  <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">
    <i class="layui-icon" style="font-size: 20px;">&#xe642;</i> 修改
  </button>
  <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="del">
    <i class="layui-icon" style="font-size: 20px;">&#xe640;</i> 删除
  </button>
</script>

<script type="text/javascript">
  layui.use(['table', 'form'], function () {
    var table = layui.table;
    var form = layui.form;
    table.render({
      elem: $('.shuffle-solutions table'),
      page: false,
      cols: [[
        {type: 'numbers'},
        {type: 'checkbox'},
        {field: 'id', title: 'id', sort: 'true'},
        {field: 'name', title: '随机方案名称', sort: 'true'},
        {field: 'strategyClass', title: '随机策略名称', sort: 'true'},
        {field: 'remark', title: '备注', sort: 'true'},
        {fixed: 'right', align: 'left', toolbar: '#shuffle-solutions-tools'}
      ]],
      data: <@json_dumper obj = page.content/>,
      id: 'shuffle_solutions'
    });

    $('.shuffle-solutions [lay-event=add]').click(function () {
      layer.open({
        type: 2,
        content: '${base_url}/shuffle_solution/add.html',
        area: ['800px', '600px']
      });
    });

    table.on('tool(shuffle-solutions)', function (obj) {
      layer.open({
        type: 2,
        content: '${base_url}/shuffle_solution/edit.html?id=' + obj.data.id,
        area: ['800px', '600px']
      });
    });
  });
</script>
