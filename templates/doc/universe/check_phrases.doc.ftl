<#-- 短语排查表 -->
<#-- @ftlvariable name="content" type="java.util.List<com.benzolamps.dict.bean.Phrase>" -->
<#-- @ftlvariable name="is_definition" type="boolean" -->
<w:tbl>
  <w:tblPr>
    <w:tblW w:w="0" w:type="auto"/>
    <w:jc w:val="center"/>
    <w:tblBorders>
      <w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${table_border_color}"/>
      <w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${table_border_color}"/>
      <w:bottom w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${table_border_color}"/>
      <w:right w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${table_border_color}"/>
      <w:insideH w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${table_border_color}"/>
      <w:insideV w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${table_border_color}"/>
    </w:tblBorders>
    <w:tblLook w:val="04A0"/>
  </w:tblPr>
  <w:tblGrid>
    <w:gridCol w:w="484"/>
    <w:gridCol w:w="1869"/>
  </w:tblGrid>
  <#list content as phrase>
    <w:tr wsp:rsidR="007813FA" wsp:rsidRPr="00C2588E" wsp:rsidTr="00C2588E">
      <w:trPr>
        <w:jc w:val="center"/>
        <w:cantSplit/>
      </w:trPr>
      <w:tc>
        <w:tcPr>
          <w:tcW w:w="484" w:type="dxa"/>
        </w:tcPr>
        <w:p wsp:rsidR="00220549" wsp:rsidRPr="00C2588E" wsp:rsidRDefault="00220549" wsp:rsidP="00220549">
          <w:pPr>
            <w:spacing w:line="${line_height}" w:line-rule="auto"/>
            <w:jc w:val="left"/>
            <w:rPr>
              <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
              <wx:font wx:val="${english_font_family}"/>
              <w:sz-cs w:val="${font_size}"/>
            </w:rPr>
          </w:pPr>
          <w:r>
            <w:rPr>
              <w:rFonts w:ascii="${chinese_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${chinese_font_family}" w:hint="fareast"/>
              <wx:font wx:val="${chinese_font_family}"/>
              <w:sz-cs w:val="${font_size}"/>
            </w:rPr>
            <w:t>□</w:t>
          </w:r>
        </w:p>
      </w:tc>
      <w:tc>
        <w:tcPr>
          <w:tcW w:w="1869" w:type="dxa"/>
          <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
        </w:tcPr>
        <w:p wsp:rsidR="007813FA" wsp:rsidRPr="00C2588E" wsp:rsidRDefault="007813FA" wsp:rsidP="00C2588E">
          <w:pPr>
            <w:spacing w:line="${line_height}" w:line-rule="auto"/>
            <w:jc w:val="left"/>
            <w:rPr>
              <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
              <wx:font wx:val="${english_font_family}"/>
              <w:sz w:val="${font_size}"/>
              <w:sz-cs w:val="${font_size}"/>
            </w:rPr>
          </w:pPr>
          <w:r wsp:rsidRPr="00C2588E">
            <w:rPr>
              <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
              <wx:font wx:val="${chinese_font_family}"/>
              <w:sz w:val="${font_size}"/>
              <w:sz-cs w:val="${font_size}"/>
            </w:rPr>
            <w:t>${abbreviate(is_definition?string(phrase.definition, phrase.prototype), 18, '...')}</w:t>
          </w:r>
        </w:p>
      </w:tc>
    </w:tr>
  </#list>
</w:tbl>
