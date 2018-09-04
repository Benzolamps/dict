<#macro data_list
  id fields values
  add='add.html' edit='edit.html' delete='delete.json'
  add_enabled=true edit_enabled=true delete_enabled=true
  toolbar=[]
  head_toolbar=[]
  page_enabled=true
>
  <#-- @ftlvariable name="toolbar" type="java.util.Collection<com.benzolamps.dict.directive.Toolbar>" -->
  <#-- @ftlvariable name="head_toolbar" type="java.util.Collection<com.benzolamps.dict.directive.Toolbar>" -->
  <style>
    .layui-table-view .layui-table {
      width: 100%;
    }
  </style>
  <div class="layui-row">
    <form class="layui-form" id="${id}" lay-filter="${id}" onsubmit="return false;" method="post">
      <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="refresh">
        <i class="layui-icon" style="font-size: 20px;">&#xe666;</i> 刷新
      </button>
      <#list head_toolbar as tool>
        <button
          class="layui-btn layui-btn-primary <#if tool.needSelected?? && tool.needSelected>layui-btn-disabled </#if>layui-btn-sm"
          lay-event="head-toolbar-${tool_index}"
          <#if tool.needSelected?? && tool.needSelected>disabled</#if>
        >
          ${tool.html}
        </button>
      </#list>
      <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="add">
        <i class="layui-icon" style="font-size: 20px;">&#xe654;</i> 添加
      </button>
      <button class="layui-btn layui-btn-danger layui-btn-disabled layui-btn-sm" lay-event="delMany" disabled>
        <i class="layui-icon" style="font-size: 20px;">&#xe640;</i> 删除
      </button>
      <table class="layui-table" lay-filter="${id}"></table>
    </form>
  </div>

  <script type="text/html" id="${id}-tools">
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">
      <i class="layui-icon" style="font-size: 20px;">&#xe642;</i> 修改
    </button>
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="del">
      <i class="layui-icon" style="font-size: 20px;">&#xe640;</i> 删除
    </button>
    <#list toolbar as tool>
       <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="toolbar-${tool_index}">
         ${tool.html}
       </button>
    </#list>
  </script>

  <script type="text/javascript">
      var fields = <@json_dump obj=fields/>;
      fields.push({field: 'id', title: '操作', align: 'left', toolbar: '#${id}-tools'});
      table.render({
        elem: $('#${id} table'),
        page: false,
        cols: [fields],
        data: <@json_dump obj=values/>,
        id: '${id}'
      });

      var add = $('#${id} [lay-event=add]');
      var delMany = $('#${id} [lay-event=delMany]');
      var refresh = $('#${id} [lay-event=refresh]');

      add.click(function () {
        parent.layer.open({
          type: 2,
          content: '${add}',
          area: ['800px', '600px']
        });
      });

      delMany.click(function () {
        parent.layer.confirm('确定要删除选中的记录吗？', {icon: 3, title: '提示'}, function (index) {
          var checkStatus = table.checkStatus('${id}');
          var ids = checkStatus.data.map(function (value) {
            return value.id;
          });
          console.log(checkStatus.data);
          dict.loadText({
            url: '${delete}',
            data: {id: ids},
            async: true,
            success: function () {
              parent.layer.alert('删除成功', function (index) {
                location.reload();
                parent.layer.close(index);
              });
            }
          });
          parent.layer.close(index);
        });
      });

      refresh.click(function () {
        location.reload();
      });

      <#list head_toolbar as tool>
        $('#${id} [lay-event=head-toolbar-${tool_index}]').click(function () {
          var checkStatus = table.checkStatus('${id}');
          !function (data, element) {
            ${tool.handler}
          }(checkStatus.data, this);
        });
      </#list>

      table.on('tool(${id})', function (obj) {
        if (obj.event == 'edit') {
          parent.layer.open({
            type: 2,
            content: '${edit}?id=' + obj.data.id,
            area: ['800px', '600px']
          });
        } else if (obj.event == 'del') {
          parent.layer.confirm('确定要删除这条记录吗？', {icon: 3, title: '提示'}, function (index) {
            dict.loadText({
              url: '${delete}',
              data: {id: obj.data.id},
              async: true,
              success: function () {
                parent.layer.alert('删除成功', function (index) {
                  location.reload();
                  parent.layer.close(index);
                });
              }
            });
            parent.layer.close(index);
          });
        }
        <#if toolbar?size gt 0>
          else {
            switch (obj.event) {
              <#list toolbar as tool>
                case 'toolbar-${tool_index}':
                  !function (data) {
                    ${tool.handler}
                  }(obj.data);
                  break;
              </#list>
            }
          }
        </#if>
      });

      var needSelected = $('#${id}>button:disabled');

      table.on('checkbox(${id})', function (obj) {
        var checkStatus = table.checkStatus('${id}');
        if (needSelected.attr('disabled') && checkStatus.data.length > 0) {
          needSelected.removeClass('layui-disabled');
          needSelected.removeClass('layui-btn-disabled');
          needSelected.attr('disabled', false);
        } else {
          needSelected.addClass('layui-disabled');
          needSelected.addClass('layui-btn-disabled');
          needSelected.attr('disabled', true);
        }
      });
  </script>
</#macro>