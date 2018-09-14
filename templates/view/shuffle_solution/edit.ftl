<#-- @ftlvariable name="strategies" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="solution" type="com.benzolamps.dict.bean.ShuffleSolution" -->
<div class="layui-card">
  <div class="layui-card-header">修改乱序方案</div>
  <div class="layui-card-body">
    <form id="shuffle-solution" class="layui-form" method="post" action="update.json">
      <table class="layui-table">
      </table>
      <input type="submit" value="确定" class="layui-btn layui-btn-normal" style="margin-top: 10px;">
    </form>
  </div>
  <script>
    var $form = $('#shuffle-solution');
    var $table = $('#shuffle-solution table');
    var shuffleSolution = [
      <#list get_dict_property('com.benzolamps.dict.bean.ShuffleSolution') as field>
        <#-- @ftlvariable name="field" type="com.benzolamps.dict.controller.vo.DictPropertyInfoVo" -->
        <#if field.name == 'strategyClass'>
          {
            'name': '${field.name}',
            'type': '${field.type}',
            'options': [
              <#list strategies as strategy>
                {'id': '${strategy}', 'value': '${strategy}'}<#sep>,
              </#list>
            ],
            'display': '${field.display}',
            'description': '${field.description}',
            'notEmpty': ${field.notEmpty???c}
          }
        <#else>
          <@json_dump obj=field/>
        </#if>
        <#sep>,
      </#list>
    ];
    var shuffleSolutionValues = <@json_dump obj=solution/>;
    var messages = {
      name: {
        pattern: '名称必须是汉字、字母、数字的组合'
      }
    };
    var loadProperties = function (start, className) {
      dict.loadText({
        url: 'property_info.json',
        data: {className: start ? shuffleSolutionValues.strategyClass : className},
        dataType: 'json',
        success: function (data) {
          var initValues = {
            id: shuffleSolutionValues.id,
            name: start ? shuffleSolutionValues.name : $form.find('input[name=name]').val(),
            remark: start ? shuffleSolutionValues.remark : $form.find('textarea[name=remark]').val(),
            strategyClass: start ? shuffleSolutionValues.strategyClass : $form.find('input[name=strategyClass]').val()
          };
          if (start) $.each(shuffleSolutionValues.properties, function (key, value) {
            initValues['properties.' + key] = value;
          });
          $form.validate().destroy();
          var shuffleSolutionProperties = data.data.map(function (item) {
            var property = {};
            $.each(item, function (key, value) {
              if (key == 'name') {
                property[key] = 'properties.' + value;
              } else {
                property[key] = value;
              }
            });
            return property;
          });
          dict.dynamicForm($table, shuffleSolution.concat(shuffleSolutionProperties), '', initValues, {}, messages, function () {
            dict.loadText({
              url: $form.attr('action'),
              type: $form.attr('method'),
              data: dict.generateObject(dict.serializeObject($form)),
              requestBody: true,
              success: function (result, status, request) {
                var parent1 = parent;
                var index = parent1.layer.getFrameIndex(window.name);
                parent1.layer.close(index);
                parent1.layer.alert('添加成功', function (index) {
                  parent1.layer.close(index);
                  parent1.$('iframe')[0].contentWindow.dict.reload(true);
                });
              },
              error: function (result, status, request) {
                parent.layer.alert(result.message, {
                  icon: 2,
                  title: result.status
                });
              }
            });
            return false;
          });
        }
      });
    };
    loadProperties(true);
    form.on('select(strategyClass)', function (data) {
      console.log('hhh');
      if (data.value != null && data.value != '') {
        loadProperties(false, data.value);
      } else {
        var initValues = {
          name: $form.find('input[name=name]').val(),
          remark: $form.find('textarea[name=remark]').val(),
          strategyClass: $form.find('input[name=strategyClass]').val()
        };
        $form.validate().destroy();
        dict.dynamicForm($table, shuffleSolution, '', initValues, {}, messages, dict.nothing);
      }
    });
  </script>
</div>