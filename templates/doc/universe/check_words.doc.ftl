<#-- 单词排查表 -->
<#-- @ftlvariable name="content" type="java.util.List<com.benzolamps.dict.bean.Word>" -->
<#-- @ftlvariable name="is_definition" type="boolean" -->
<#list content as word>
  <w:p wsp:rsidR="002A2BA6" wsp:rsidRDefault="002A2BA6" wsp:rsidP="002A2BA6">
    <w:pPr>
      <w:spacing w:line="280" w:line-rule="exact"/>
      <w:ind w:left="357" w:hanging="357"/>
      <w:jc w:val="left"/>
      <w:rPr>
        <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
        <wx:font wx:val="${english_font_family}"/>
        <w:sz w:val="${font_size}"/>
        <w:sz-cs w:val="${font_size}"/>
      </w:rPr>
    </w:pPr>
    <w:r wsp:rsidRPr="006C50AA">
      <w:rPr>
        <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
        <wx:font wx:val="${english_font_family}"/>
        <w:sz w:val="${font_size}"/>
        <w:sz-cs w:val="${font_size}"/>
      </w:rPr>
      <w:t>${word_index + 1}．</w:t>
    </w:r>
    <w:r>
      <w:rPr>
        <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
        <wx:font wx:val="${english_font_family}"/>
        <w:sz w:val="${font_size}"/>
        <w:sz-cs w:val="${font_size}"/>
      </w:rPr>
      <w:t>${abbreviate(is_definition?string(word.definition, word.prototype), 18, '...')}</w:t>
    </w:r>
  </w:p>
</#list>
