<#-- @ftlvariable name="clazz" type="com.benzolamps.dict.bean.Group" -->
<@data_edit
  id='word-groups'
  fields=get_dict_property('com.benzolamps.dict.bean.Group')
  values=clazz
  messages={
    'name': {
      'remote': '单词分组名称已存在'
    }
  }
/>
