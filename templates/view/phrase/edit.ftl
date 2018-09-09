<#-- @ftlvariable name="phrase" type="com.benzolamps.dict.bean.Phrase" -->
<@data_edit
  id='phrases'
  title='修改短语'
  fields=get_dict_property('com.benzolamps.dict.controller.vo.PhraseVo')
  values=phrase
  messages={
      'prototype': {
      'remote': '短语已存在'
    }
  }
/>
