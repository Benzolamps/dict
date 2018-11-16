<#-- @ftlvariable name="wordGroup" type="com.benzolamps.dict.bean.Group" -->
<@data_edit
  id='word-groups'
  fields=get_dict_property('com.benzolamps.dict.bean.Group')
  values=wordGroup
  messages={
      'name': {
      'remote': '名称已存在'
    }
  }
/>