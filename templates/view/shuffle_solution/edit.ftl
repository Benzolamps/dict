<#-- @ftlvariable name="strategies" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="solution" type="com.benzolamps.dict.bean.ShuffleSolution" -->
<div class="layui-card">
  <div class="layui-card-header">添加乱序方案</div>
  <div class="layui-card-body">
    <form class="layui-form shuffle-solution-form" method="post" action="update.json">
      <input type="hidden" name="id" value="${solution.id}">
      <table class="layui-table">
        <tbody>
        <tr>
          <th title="乱序方案名称">乱序方案名称</th>
          <td>
            <input type="text" name="name" placeholder="请输入乱序方案名称" required autocomplete="off" value="${solution.name}" class="layui-input">
          </td>
        </tr>
        <tr>
          <th title="备注">备注</th>
          <td>
            <textarea placeholder="请输入备注" class="layui-textarea" name="remark">${solution.remark}</textarea>
          </td>
        </tr>
        <tr>
          <th title="乱序策略">乱序策略</th>
          <td>
            <select name="strategyClass" lay-filter="strategy-class" required title="">
              <option value="">请选择乱序策略</option>
                <#list strategies as strategy>
                  <option value="${strategy}"<#if solution.strategyClass == strategy> selected</#if>>${strategy}</option>
                </#list>
            </select>
          </td>
        </tr>
        </tbody>
        <tbody class="shuffle-strategy-table"></tbody>
      </table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
</div>
<script type="text/javascript" src="${base_url}/res/js/dynamic-form.js"></script>
<script type="text/javascript">
  layui.use('form', function() {
    var form = layui.form;
    var $form = $('.shuffle-solution-form');
    var $table = $('.shuffle-strategy-table');

    var showEdit = function (className) {
      dict.dynamicForm($table, 'property_info.json', {className: className}, 'properties.', <@json_dumper obj = solution.properties/>);
    };

    form.on('select(strategy-class)', function (data) {
      if (data.value == null || data.value == '') {
        $table.empty();
      } else {
        showEdit(data.value);
      }
    });

    dict.validateForm($form, {
      name: {
        required: true,
        pattern: /^[0-9a-z\u4e00-\u9fa5]+$/i,
        minlength: 2,
        maxlength: 20
      },
      strategyClass: {
        required: true
      },
      remark: {
        maxlength: 255
      }
    }, {
      name: {
        required: '乱序方案名称不能为空',
        minlength: '乱序方案名称不得少于2个字',
        maxlength: '乱序方案名称不得多于20个字',
        pattern: '乱序方案名称必须是汉字、字母、数字的组合'
      },
      strategyClass: {
        required: '乱序策略不能为空'
      },
      remark: {
        maxlength: '备注不得多于255个字'
      }
    }, function () {
      $form.find('input[type=checkbox]').each(function () {
        $(this).val($(this).next().find('em').text().toLowerCase() == $(this).attr('lay-text').split('|')[0].toLowerCase());
        $(this).attr('type', 'hidden');
      });
      dict.loadText({
        url: $form.attr('action'),
        type: $form.attr('method'),
        data: dict.generateObject(dict.serializeObject($form)),
        requestBody: true,
        success: function () {
          var index = parent.layer.getFrameIndex(window.name);
          layer.close(index);
          parent.location.reload(true);
        }
      });
      return false;
    });

    <#if solution.strategyClass??>showEdit('${solution.strategyClass}');</#if>
  });
</script>