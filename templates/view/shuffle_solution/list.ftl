<#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->

<div class="layui-row">
  <form class="layui-form shuffle-solutions" lay-filter="shuffle-solutions" onsubmit="return false;" method="post">
    <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="refresh">
      <i class="layui-icon" style="font-size: 20px;">&#xe666; 刷新</i>
    </button>
    <button class="layui-btn layui-btn-warm layui-btn-sm add" lay-event="add">
      <i class="layui-icon" style="font-size: 20px;">&#xe654; 添加</i>
    </button>
    <button class="layui-btn layui-btn-disabled layui-btn-sm" lay-event="delMany" disabled>
      <i class="layui-icon" style="font-size: 20px;">&#xe640; 删除</i>
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
  layui.use('table', function () {
    var table = layui.table;
    table.render({
      elem: $('.shuffle-solutions table'),
      page: false,
      cols: [[
        {type: 'numbers'},
        {type: 'checkbox'},
        {field: 'name', title: '随机方案名称', sort: 'true'},
        {field: 'strategyClass', title: '随机策略名称', sort: 'true'},
        {field: 'remark', title: '备注', sort: 'true'},
        {field: 'id', title: '操作', align: 'left', toolbar: '#shuffle-solutions-tools'}
      ]],
      data: <@json_dumper obj = page.content/>,
      id: 'shuffle-solutions'
    });

    var add = $('.shuffle-solutions [lay-event=add]');
    var delMany = $('.shuffle-solutions [lay-event=delMany]');
    var refresh = $('.shuffle-solutions [lay-event=refresh]');

    add.click(function () {
      layer.open({
        type: 2,
        content: '${base_url}/shuffle_solution/add.html',
        area: ['800px', '600px']
      });
    });

    delMany.click(function () {
      layer.confirm('确定要删除选中的记录吗？', {icon: 3, title: '提示'}, function (index) {
        var checkStatus = table.checkStatus('shuffle-solutions');
        var ids = checkStatus.data.map(function (value) {
            return value.id;
        });
        dict.loadText({
            url: '${base_url}/shuffle_solution/delete.json',
            data: {id: ids},
            async: true,
            success: function () {
              layer.alert('删除成功', function () {
                location.reload();
              });
            }
        });
        layer.close(index);
      });
    });

    refresh.click(function () {
      location.reload();
    });

    table.on('tool(shuffle-solutions)', function (obj) {
      if (obj.event == 'edit') {
        layer.open({
          type: 2,
          content: '${base_url}/shuffle_solution/edit.html?id=' + obj.data.id,
          area: ['800px', '600px']
        });
      } else if (obj.event == 'del') {
        layer.confirm('确定要删除这条记录吗？', {icon: 3, title: '提示'}, function (index) {
          dict.loadText({
            url: '${base_url}/shuffle_solution/delete.json',
            data: {id: obj.data.id},
            async: true,
            success: function () {
              layer.alert('删除成功', function () {
                location.reload();
              });
            }
          });
          layer.close(index);
        });
      }
    });

    table.on('checkbox(shuffle-solutions)', function (obj) {
      var checkStatus = table.checkStatus('shuffle-solutions');
      if (delMany.attr('disabled') && obj.checked) {
        delMany.removeClass('layui-disabled');
        delMany.removeClass('layui-btn-disabled');
        delMany.addClass('layui-btn-danger');
        delMany.attr('disabled', false);
      } else if (!delMany.attr('disabled') && checkStatus.data.length < 1) {
        delMany.removeClass('layui-btn-danger');
        delMany.addClass('layui-disabled');
        delMany.addClass('layui-btn-disabled');
        delMany.attr('disabled', true);
      }
    });
  });
</script>
