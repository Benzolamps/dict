<#macro data_list
  id fields page values=page.content
  add='add.html' edit='edit.html' delete='delete.json'
  add_enabled=true edit_enabled=true delete_enabled=true
  toolbar=[]
  head_toolbar=[]
  page_enabled=false
>
  <#-- @ftlvariable name="toolbar" type="java.util.Collection<com.benzolamps.dict.directive.Toolbar>" -->
  <#-- @ftlvariable name="head_toolbar" type="java.util.Collection<com.benzolamps.dict.directive.Toolbar>" -->
  <#-- @ftlvariable name="page" type="com.benzolamps.dict.dao.core.Page" -->
  <style>
    .layui-table-view .layui-table {
      width: 100%;
    }
  </style>
  <div class="layui-row">
    <form class="layui-form" id="${id}" lay-filter="${id}" onsubmit="this.action = location.href;" method="post">
      <div class="head-toolbar">
        <button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="refresh" type="button">
          <i class="layui-icon" style="font-size: 20px;">&#xe666;</i> 刷新
        </button>
        <#list head_toolbar as tool>
          <button
            class="layui-btn layui-btn-primary <#if tool.needSelected?? && tool.needSelected>layui-btn-disabled </#if>layui-btn-sm"
            lay-event="head-toolbar-${tool_index}"
            <#if tool.needSelected?? && tool.needSelected>disabled</#if>
            type="button"
          >
            ${tool.html}
          </button>
        </#list>
        <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="add" type="button">
          <i class="layui-icon" style="font-size: 20px;">&#xe654;</i> 添加
        </button>
        <button class="layui-btn layui-btn-danger layui-btn-disabled layui-btn-sm" lay-event="delMany" type="button" disabled>
          <i class="layui-icon" style="font-size: 20px;">&#xe640;</i> 删除
        </button>
      </div>
      <table class="layui-table" lay-filter="${id}"></table>
      <div id="${id}-page"></div>
      <div id="${id}-page-info">
        <input name="pageSize" type="hidden" value="${page.pageSize}">
        <input name="pageNumber" type="hidden" value="${page.pageNumber}">
      </div>
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
      limit: ${page.pageSize},
      cols: [fields],
      data: <@json_dump obj=values/>,
      id: '${id}',
      height: $(document).height() - 120
    });

    laypage.render({
      elem: '${id}-page',
      count: ${page.total},
      curr: ${page.pageNumber},
      limit: ${page.pageSize},
      limits: [10, 20, 30],
      jump: function (obj, first) {
        if (!first) {
          console.log(obj);
          $('#${id}-page-info [name=pageSize]').val(obj.limit);
          $('#${id}-page-info [name=pageNumber]').val(obj.curr);
          $('#${id}').submit();
        }
      }
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

    var needSelected = $('#${id} .head-toolbar>button.layui-btn-disabled');

    table.on('checkbox(${id})', function (obj) {
      var checkStatus = table.checkStatus('${id}');
      if (checkStatus.data.length > 0) {
        needSelected.removeClass('layui-disabled');
        needSelected.removeClass('layui-btn-disabled');
        needSelected.attr('disabled', false);
      } else if (checkStatus.data.length <= 0)  {
        needSelected.addClass('layui-disabled');
        needSelected.addClass('layui-btn-disabled');
        needSelected.attr('disabled', true);
      }
    });
  </script>
</#macro>