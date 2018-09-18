<#-- @ftlvariable name="phrase" type="com.benzolamps.dict.bean.Phrase" -->
<@data_edit
  id='phrases'
  fields=get_dict_property('com.benzolamps.dict.controller.vo.PhraseVo')
  values=phrase
  messages={
      'prototype': {
      'remote': '短语已存在'
    }
  }
/>
