<#macro data_list
  id fields page values=page.content
  name=id
  add='add.html' edit='edit.html' delete='delete.json'
  add_enabled=true edit_enabled=true delete_enabled=true
  delete_confirm=''
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

  <#-- TODO: 样式 -->
  <style>
    #${id} .field-th-hover {
      background-color: #DDDDDD;
    }

    #${id} .field-th-active {
      background-color: #FFB800;
    }
  </style>

  <div id="${id}-container" class="layui-container">
    <form class="layui-form" id="${id}" lay-filter="${id}" onsubmit="return false;" method="post">
      <#-- 头部工具栏 -->
      <div class="layui-row layui-col-space10" style="margin: auto 0">
        <div class="head-toolbar">
          <button style="margin-bottom: 5px" class="layui-btn layui-btn-normal layui-btn-sm" lay-event="refresh" type="button">
            <i class="fa fa-refresh" style="font-size: 20px;"></i> &nbsp; 刷新
          </button>
          <#list head_toolbar as tool>
            <button style="margin-bottom: 5px"
              class="layui-btn layui-btn-primary <#if tool.needSelected?? && tool.needSelected>layui-btn-disabled </#if>layui-btn-sm"
              lay-event="head-toolbar-${tool_index}"
              <#if tool.needSelected?? && tool.needSelected>disabled</#if>
              type="button"
            >
              ${tool.html}
            </button>
          </#list>
          <button style="margin-bottom: 5px" class="layui-btn layui-btn-warm layui-btn-sm" lay-event="add" type="button">
            <i class="fa fa-plus" style="font-size: 20px;"></i> &nbsp; 添加
          </button>
          <button style="margin-bottom: 5px" class="layui-btn layui-btn-danger layui-btn-disabled layui-btn-sm" lay-event="delMany" type="button" disabled>
            <i class="fa fa-trash-o" style="font-size: 20px;"></i> &nbsp; 删除
          </button>
        </div>
      </div>

      <#-- 筛选 -->
      <#if search?size gt 0>
        <div id="${id}-search" class="layui-row layui-col-space10" style="margin-top: 10px; margin-left: 0; margin-right: 0"></div>
      </#if>

      <#-- 表格主体 -->
      <div class="layui-row layui-col-space10" style="margin: auto 0">
        <div class="layui-col-lg12">
          <table class="layui-table" lay-filter="${id}"></table>
        </div>
      </div>

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

  <#-- 工具栏 -->
  <script type="text/html" id="${id}-tools">
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" type="button">
      <i class="fa fa-pencil-square-o" style="font-size: 20px;"></i> &nbsp; 修改
    </button>
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="del" type="button">
      <i class="fa fa-trash-o" style="font-size: 20px;"></i> &nbsp; 删除
    </button>
    <#list toolbar as tool>
      <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="toolbar-${tool_index}" type="button">
        ${tool.html}
      </button>
    </#list>
  </script>

  <script type="text/javascript">
    $('#${id}-container').css('width', (parent.$('body').width() - 300) + 'px');

    <#-- 表格字段 -->
    var fields = <@json_dump obj=fields/>;
    $.each(fields, function (index, item) {
      delete fields[index].sort;
      fields[index].unresize = true;
      if ('format' in fields[index]) {
        fields[index].templet = '<div><span title="' + fields[index].format + '">' + fields[index].format + '</span></div>';
        delete fields[index].format;
      } else {
        fields[index].templet =
          '<div>' +
          '{{# var value = \'' + fields[index].field + '\' in d ? d.' + fields[index].field + ' : \'\'; }}' +
          '<span title="{{value}}">{{value}}</span>' +
          '</div>';
      }
    });

    <#assign width = 110/>
    <#if delete_enabled && edit_enabled><#assign width += 110/></#if>
    <#assign width += 110 * toolbar?size/>

    fields.push({field: 'id', title: '操作', align: 'left', fixed: 'right', toolbar: '#${id}-tools', width: ${width}});
    fields.unshift(/*{type: 'numbers'},*/{type: 'checkbox'});

    var emptyText = [
      '空空如也~',
      '这儿什么都没有~',
      'Empty List!',
      '快加点数据吧！'
    ];

    <#-- TODO: 表格渲染 -->
    table.render({
      elem: $('#${id} table'),
      page: false,
      limit: ${page.pageSize},
      cols: [fields],
      data: <@json_dump obj=values/>,
      id: '${id}',
      height: 'full-250',
      text: {
        none: emptyText[(Math.random() * emptyText.length) | 0]
      }
    });

    <#-- TODO: 分页渲染 -->
    <#if page_enabled>
      laypage.render({
        elem: '${id}-page',
        count: ${page.total},
        curr: ${page.pageNumber},
        limit: ${page.pageSize},
        limits: [10, 20, 30, 50, 100, 200, 500, 1000],
        layout: ['count', 'prev', 'page', 'next', 'skip', 'limit'],
        jump: function (obj, first) {
          if (!first) {
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

    <#-- TODO: 添加操作 -->
    add.click(function () {
      parent.layer.open({
        type: 2,
        title: '添加${name}',
        content: '${add}',
        area: ['800px', '600px']
      });
    });

    <#-- 批量删除 -->
    delMany.click(function () {
      var process = function (index) {
        var checkStatus = table.checkStatus('${id}');
        var ids = checkStatus.data.map(function (value) {
          return value.id;
        });
        dict.loadText({
          url: '${delete}',
          data: {id: ids},
          success: function () {
            parent.layer.alert('删除成功！', {
              icon: 1,
              end: function () {
                dict.reload();
              }
            });
          },
          error: function (result, status, request) {
            parent.layer.alert(result.message, {
              icon: 2,
              title: result.status
            });
          }
        });
        parent.layer.close(index);
      };
      parent.layer.confirm('确定要删除选中的记录吗？', {icon: 3, title: '删除${name}'}, function (index) {
        <#if delete_confirm != ''>
          parent.layer.confirm('${delete_confirm}', {icon: 3, title: '删除${name}'}, process);
        <#else>
          process(index);
        </#if>
      });
    });

    <#-- 刷新 -->
    refresh.click(function () {
      execute();
    });

    <#-- 头部工具栏操作 -->
    <#list head_toolbar as tool>
      $('#${id} [lay-event=head-toolbar-${tool_index}]').click(function () {
        var checkStatus = table.checkStatus('${id}');
        !function (data, element) {
          ${tool.handler}
        }(checkStatus.data, this);
      });
    </#list>

    <#-- TODO: 工具栏操作 -->
    table.on('tool(${id})', function (obj) {

      <#-- 修改 -->
      if (obj.event == 'edit') {
        parent.layer.open({
          type: 2,
          title: '修改${name}',
          content: '${edit}?id=' + obj.data.id,
          area: ['800px', '600px']
        });
      }
      <#-- 删除 -->
      else if (obj.event == 'del') {
        var process = function (index) {
          dict.loadText({
            url: '${delete}',
            data: {id: obj.data.id},
            success: function () {
              parent.layer.alert('删除成功！', {
                icon: 1,
                end: function () {
                  dict.reload();
                }
              });
            },
            error: function (result, status, request) {
              parent.layer.alert(result.message, {
                icon: 2,
                title: result.status
              });
            }
          });
          parent.layer.close(index);
        };

        parent.layer.confirm('确定要删除这条记录吗？', {icon: 3, title: '删除${name}'}, function (index) {
          <#if delete_confirm != ''>
            parent.layer.close(index);
            parent.layer.confirm('${delete_confirm}', {icon: 3, title: '删除${name}'}, process);
          <#else>
            process(index);
          </#if>
        });
      }

      <#-- 自定义工具栏操作 -->
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

    <#-- TODO: 需要有选择项时的操作的禁用与恢复 -->
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

    <#-- TODO: 排序操作 -->
    var currSort = '${page.orders[0].field}';
    var currDirection = '${page.orders[0].direction}';

    var $currSort = $('th[data-field=' + currSort + ']');
    if ($currSort.length == 1) {
      $currSort.addClass('field-th-active');
      $currSort.find('span')[0].innerText += currDirection == 'DESC' ? ' ↓' : ' ↑';
    }
    $('th[data-field=id]').removeClass('field-th-active');

    $.each([<#list fields as field><#if field.sort?? && field.sort>'${field.field}'</#if><#sep>, </#list>], function (index, value) {
      $('#${id} th[data-field=' + value + ']').css('cursor', 'pointer').click(function () {
        var $old = $('#${id} .field-th-active');
        if ($old.length == 1) {
          $old.removeClass('field-th-active');
          $old.find('span')[0].innerText = $old.find('span')[0].innerText.slice(0, -2);
        }
        $(this).addClass('field-th-active');
        $('#${id}-order-info [name=field]').val(value);
        $('#${id}-order-info [name=direction]').val((value == currSort && currDirection == 'ASC') ? 'DESC' : 'ASC');
        $(this).find('span')[0].innerText += (value == currSort && currDirection == 'ASC') ? ' ↓' : ' ↑';
        execute();
      }).mouseenter(function () {
        $(this).addClass('field-th-hover');
      }).mouseleave(function () {
        $(this).removeClass('field-th-hover');
      });
    });

    <#-- TODO: 筛选表格 -->
    var $form = $('#${id}');
    var $pageInfo = $form.find('#${id}-page-info');
    var $orderInfo = $form.find('#${id}-order-info');

    var searchValues = <@json_dump obj=page.searches/>;

    var searchValueMap = {};

    $.each(searchValues, function (index, value) {
      searchValueMap[value.field] = value.value;
    });

    dict.dynamicSearch(
      $form,
      $('#${id}-search'),
      <@json_dump obj=search/>,
      searchValueMap,
      <@json_dump obj=rules/>,
      <@json_dump obj=messages/>,
      function () {
        $pageInfo.find('[name=pageNumber]').val(1);
        execute();
      }
    );
    $('#${id}-search').append($(
      '<div class="layui-col-md2" style="height: 100%" >' +
      '<button class="layui-btn layui-btn-radius layui-btn-normal" lay-event="search" type="submit">' +
      '<i class="layui-icon" style="font-size: 20px;">&#xe615;</i> 搜索' +
      '</button>' +
      '</div>'
    ));

    <#if page_enabled>
      $pageInfo.find('[name=pageSize]').val(#{page.pageSize});
      $pageInfo.find('[name=pageNumber]').val(#{page.pageNumber});
    </#if>

    $orderInfo.find('[name=field]').val(currSort);
    $orderInfo.find('[name=direction]').val(currDirection);

    <#-- TODO: 加载表格表单筛选数据 -->
    dict.loadFormData = function () {
      var data = {};
      <#if page_enabled>
        data.pageSize = $pageInfo.find('[name=pageSize]').val();
        data.pageNumber = $pageInfo.find('[name=pageNumber]').val();
      </#if>
      data.orders = [
        {
          field: $orderInfo.find('[name=field]').val(),
          direction: $orderInfo.find('[name=direction]').val()
        }
      ];
      if (data.orders[0].field == null || data.orders[0].field == undefined || data.orders[0].field == '') data.orders = [];
      dict.preSubmit($form);
      var searchesMap = dict.generateObject(dict.serializeObject($form)).search;
      data.searches = [];
      $.each(searchesMap, function (key, value) {
        data.searches.push({field: key, value: value});
      });
      return data;
    };

    <#-- 执行 -->
    var execute = function (forcedReload) {
      dict.loadText({
        url: location.pathname,
        requestBody: true,
        type: 'POST',
        data: forcedReload ? null : dict.loadFormData(),
        success: function (result, status, request) {
          document.close();
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

    <#-- 页面刷新 -->
    if (!dict.reload.callArray) {
      dict.reload = dict.extendsFunction(null, execute);
    } else if (dict.reload.callArray.indexOf(execute) < 0) {
      dict.reload = dict.extendsFunction(dict.reload, execute);
    }
  </script>
</#macro>