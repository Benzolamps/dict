<#macro data_list
  id fields page values=page.content
  add='add.html' edit='edit.html' delete='delete.json'
  add_enabled=true edit_enabled=true delete_enabled=true
  toolbar=[]
  head_toolbar=[]
  page_enabled=false
  search=[]
  rules=[]
  messages=[]
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
    <form class="layui-form" id="${id}" lay-filter="${id}" onsubmit="return false;" method="post">
      <#-- 头部工具栏 -->
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

      <#-- 筛选 -->
      <#if search?size gt 0>
        <div class="layui-container" style="margin: 10px auto">
          <div id="${id}-search" class="layui-row layui-col-space10">
          </div>
        </div>
      </#if>

      <#-- 表格主体 -->
      <table class="layui-table" lay-filter="${id}"></table>

      <#-- 分页 -->
      <#if page_enabled>
        <div id="${id}-page"></div>
        <div id="${id}-page-info">
          <span name="pageSize"></span>
          <span name="pageNumber"></span>
        </div>
      </#if>

      <#-- 排序 -->
      <div id="${id}-order-info">
        <span name="field"></span>
        <span name="direction"></span>
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
      height: 'full-200',
      <#if page.orders?size gt 0>
        initSort: {
          field: '${page.orders[0].field}',
          type: '${page.orders[0].direction}'
        }
      </#if>
    });

    <#if page_enabled>
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
            execute();
          }
        }
      });
    </#if>

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
      execute();
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

    table.on('sort(${id})', function (obj) {
      $('#${id}-order-info [name=field]').val(obj.field);
      $('#${id}-order-info [name=direction]').val(obj.type);
      execute();
    });

    var $form = $('#${id}');
    var $pageInfo = $form.find('#${id}-page-info');
    var $orderInfo = $form.find('#${id}-order-info');

    var searchValues = <@json_dump obj=page.searches/>;

    var searchValueMap = {};

    $.each(searchValues, function (index, value) {
      searchValueMap[value.field] = value.value;
    });

    dict.dynamicSearch($form, $('#${id}-search'), <@json_dump obj=search/>, searchValueMap, <@json_dump obj=rules/>, <@json_dump obj=messages/>, dict.nothing);

    $('#${id}-search').append($(
      '<div class="layui-col-md2" style="height: 100%" >\n' +
      '<button class="layui-btn layui-btn-normal layui-btn-sm" lay-event="search" type="button">\n' +
      '<i class="layui-icon" style="font-size: 20px;">&#xe666;</i> 搜索\n' +
      '</button>' +
      '</div>'
    ).click(function () {
      $pageInfo.find('[name=pageNumber]').val(1);
      execute();
    }));

    <#if page_enabled>
      $pageInfo.find('[name=pageSize]').val(#{page.pageSize});
      $pageInfo.find('[name=pageNumber]').val(#{page.pageNumber});
    </#if>

    $pageInfo.find('[name=field]').val('${page.orders[0].field}');
    $pageInfo.find('[name=direction]').val('${page.orders[0].direction}');

    var execute = function () {

      var data = {};

      <#if page_enabled>
        data.pageSize = $pageInfo.find('[name=pageSize]').val();
        data.pageNumber = $pageInfo.find('[name=pageNumber]').val();
      </#if>

      data.orders = [
        {
          field: $orderInfo.find('[name=field]').val(),
          direction: $orderInfo.find('[name=direction]').val().toUpperCase()
        }
      ];
      if (data.orders[0].field == null || data.orders[0].field == undefined || data.orders[0].field == '') data.orders = [];


      var searchesMap = dict.generateObject(dict.serializeObject($form));
      data.searches = [];

      $.each(searchesMap, function (key, value) {
        data.searches.push({field: key, value: value});
      });

      dict.loadText({
        url: 'list.html',
        requestBody: true,
        type: 'POST',
        data: data,
        success: function (result, status, request) {
          document.open();
          document.write(result);
        },
        error: function (result, status, request) {
          parent.layer.alert(result.message, {
            icon: 2,
            title: result.status
          });
        }
      });
    };
  </script>
</#macro>