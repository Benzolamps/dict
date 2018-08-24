<#-- @ftlvariable name="strategies" type="java.util.Collection<java.lang.String>" -->
<@data_add
  id='shuffle_solution'
  title='添加乱序方案'
  fields=get_dict_property('com.benzolamps.dict.bean.ShuffleSolution')
  messages={
    'name': {
      'required': '乱序方案名称不能为空',
      'minlength': '乱序方案名称不得少于2个字',
      'maxlength': '乱序方案名称不得多于20个字',
      'pattern': '乱序方案名称必须是汉字、字母、数字的组合'
    },
    'strategyClass': {
      'required': '乱序策略不能为空'
    },
    'remark': {
      'maxlength': '备注不得多于255个字'
    }
  }
  submit_handle="
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
  "
/>