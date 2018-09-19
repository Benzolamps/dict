<#-- @ftlvariable name="clazz" type="com.benzolamps.dict.bean.Clazz" -->
<@data_edit
  id='clazzes'
  fields=get_dict_property('com.benzolamps.dict.bean.Clazz')
  values=clazz
  messages={
      'name': {
      'pattern': '名称必须是汉字、字母、数字的组合'
    }
  }
/>
