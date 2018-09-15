<@data_add
  id='phrases'
  title='添加词库'
  fields=get_dict_property('com.benzolamps.dict.bean.Library')
  messages={
    'name': {
      'remote': '词库已存在',
      'pattern': '乱序方案名称必须是汉字、字母、数字的组合'
    }
  }
submit_handler="parent.dict.reload(true);"
/>