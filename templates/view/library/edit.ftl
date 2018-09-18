<#-- @ftlvariable name="library" type="com.benzolamps.dict.bean.Library" -->
<@data_edit
  id='phrases'
  fields=get_dict_property('com.benzolamps.dict.bean.Library')
  values=library
  messages={
      'name': {
      'remote': '词库已存在',
      'pattern': '名称必须是汉字、字母、数字的组合'
    }
  }
  submit_handler="parent.dict.reload(true);"
/>
