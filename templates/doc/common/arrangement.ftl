<#-- 纵向排列 -->
<#macro vertical_arrangement definations>
  <#list definations as def>
    <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidRDefault="00E602B2" wsp:rsidP="00E602B2">
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
      <#if !(def_index == 0 && !def_has_next)>
        <w:r wsp:rsidRPr="006C50AA">
          <w:rPr>
            <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
            <wx:font wx:val="${english_font_family}"/>
            <w:sz w:val="${font_size}"/>
            <w:sz-cs w:val="${font_size}"/>
          </w:rPr>
          <w:t>(${def_index + 1}) </w:t>
        </w:r>
      </#if>
      <w:r wsp:rsidRPr="006C50AA">
        <w:rPr>
          <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
          <wx:font wx:val="${chinese_font_family}"/>
          <w:sz w:val="${font_size}"/>
          <w:sz-cs w:val="${font_size}"/>
        </w:rPr>
        <w:t>${def}</w:t>
      </w:r>
    </w:p>
  </#list>
</#macro>

<#-- 横向排列 -->
<#macro horizental_arrangement definations>
  <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidRDefault="00E602B2" wsp:rsidP="00E602B2">
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
    <#list definations as def>
      <#if !(def_index == 0 && !def_has_next)>
        <w:r wsp:rsidRPr="006C50AA">
          <w:rPr>
            <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
            <wx:font wx:val="${english_font_family}"/>
            <w:sz w:val="${font_size}"/>
            <w:sz-cs w:val="${font_size}"/>
          </w:rPr>
          <w:t>(${def_index + 1}) </w:t>
        </w:r>
      </#if>
      <w:r wsp:rsidRPr="006C50AA">
        <w:rPr>
          <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
          <wx:font wx:val="$cn_font_family}"/>
          <w:sz w:val="${font_size}"/>
          <w:sz-cs w:val="${font_size}"/>
        </w:rPr>
        <w:t>${def}<#sep>；</#sep></w:t>
      </w:r>
    </#list>
  </w:p>
</#macro>