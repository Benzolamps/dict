<#-- 短语抄写表 -->

<#import 'common.ftl' as common/>
<@common.contatiner>
  <@common.title common.copy_phrases_title/>
    <w:tbl>
      <w:tblPr>
        <w:tblW w:w="0" w:type="auto"/>
        <w:jc w:val="center"/>
        <w:tblBorders>
          <w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${common.table_border_color}"/>
          <w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${common.table_border_color}"/>
          <w:bottom w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${common.table_border_color}"/>
          <w:right w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${common.table_border_color}"/>
          <w:insideH w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${common.table_border_color}"/>
          <w:insideV w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="${common.table_border_color}"/>
        </w:tblBorders>
        <w:tblLook w:val="04A0"/>
      </w:tblPr>
      <w:tblGrid>
        <w:gridCol w:w="3539"/>
        <w:gridCol w:w="1471"/>
      </w:tblGrid>
      <#list pairs as pair>
        <w:tr wsp:rsidR="007813FA" wsp:rsidRPr="00C2588E" wsp:rsidTr="00C2588E">
          <w:trPr>
            <w:jc w:val="center"/>
          </w:trPr>
          <w:tc>
            <w:tcPr>
              <w:tcW w:w="3539" w:type="dxa"/>
              <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
            </w:tcPr>
            <w:p wsp:rsidR="007813FA" wsp:rsidRPr="00C2588E" wsp:rsidRDefault="007813FA" wsp:rsidP="00C2588E">
              <w:pPr>
                <w:spacing w:line="${common.line_height}" w:line-rule="auto"/>
                <w:jc w:val="right"/>
                <w:rPr>
                  <w:rFonts w:ascii="${common.en_font_family}" w:fareast="${common.cn_font_family}" w:h-ansi="${common.en_font_family}"/>
                  <wx:font wx:val="${common.en_font_family}"/>
                  <w:sz w:val="${common.font_size}"/>
                  <w:sz-cs w:val="${common.font_size}"/>
                </w:rPr>
              </w:pPr>
              <w:r wsp:rsidRPr="00C2588E">
                <w:rPr>
                  <w:rFonts w:ascii="${common.en_font_family}" w:fareast="${common.cn_font_family}" w:h-ansi="${common.en_font_family}"/>
                  <wx:font wx:val="${common.cn_font_family}"/>
                  <w:sz w:val="${common.font_size}"/>
                  <w:sz-cs w:val="${common.font_size}"/>
                </w:rPr>
                <w:t>${pair.value.definition} <#if pair.key gte 0>(${pair.key + 1})</#if></w:t>
              </w:r>
            </w:p>
          </w:tc>
          <w:tc>
            <w:tcPr>
              <w:tcW w:w="1471" w:type="dxa"/>
              <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
              <w:vAlign w:val="bottom"/>
            </w:tcPr>
            <w:p wsp:rsidR="007813FA" wsp:rsidRPr="00C2588E" wsp:rsidRDefault="000F4AB4" wsp:rsidP="000F4AB4">
              <w:pPr>
                <w:spacing w:line="${common.line_height}" w:line-rule="auto"/>
                <w:rPr>
                  <w:rFonts w:ascii="${common.cn_font_family}" w:fareast="${common.cn_font_family}" w:h-ansi="${common.cn_font_family}"/>
                  <wx:font wx:val="${common.cn_font_family}"/>
                  <w:sz w:val="${common.font_size}"/>
                  <w:sz-cs w:val="${common.font_size}"/>
                  <w:u w:val="single"/>
                </w:rPr>
              </w:pPr>
              <w:r>
                <w:rPr>
                  <w:rFonts w:ascii="${common.cn_font_family}" w:fareast="${common.cn_font_family}" w:h-ansi="${common.cn_font_family}" w:hint="fareast"/>
                  <wx:font wx:val="${common.cn_font_family}"/>
                  <w:sz w:val="${common.font_size}"/>
                  <w:sz-cs w:val="${common.font_size}"/>
                  <w:u w:val="single"/>
                </w:rPr>
                <w:t> </w:t>
              </w:r>
            </w:p>
          </w:tc>
        </w:tr>
      </#list>
    </w:tbl>
  <@common.line/>
</@common.contatiner>
