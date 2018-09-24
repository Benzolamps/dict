<#-- @ftlvariable name="phraseGroup" type="com.benzolamps.dict.bean.Group" -->
<@data_edit
  id='phrase-groups'
  fields=get_dict_property('com.benzolamps.dict.bean.Group')
  values=phraseGroup
  messages={
      'name': {
      'remote': '名称已存在'
    }
  }
/>
