<#-- 单词生词表 -->
<#-- @ftlvariable name="content" type="java.util.List<com.benzolamps.dict.bean.Word>" -->
<w:tbl>
  <w:tblPr>
    <w:tblW w:w="4500" w:type="pct"/>
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
    <w:gridCol w:w="5236"/>
  </w:tblGrid>
  <#list content as word>
    <w:tr wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidTr="00E602B2">
      <w:trPr>
        <w:cantSplit/>
      </w:trPr>
      <w:tc>
        <w:tcPr>
          <w:tcW w:w="5236" w:type="dxa"/>
          <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
        </w:tcPr>
        <w:tbl>
          <w:tblPr>
            <w:tblW w:w="0" w:type="auto"/>
            <w:jc w:val="center"/>
            <w:tblLook w:val="04A0"/>
          </w:tblPr>
          <w:tblGrid>
            <w:gridCol w:w="1985"/>
            <w:gridCol w:w="525"/>
            <w:gridCol w:w="2510"/>
          </w:tblGrid>
          <w:tr wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidTr="00E602B2">
            <w:trPr>
              <w:cantSplit/>
              <w:jc w:val="center"/>
            </w:trPr>
            <w:tc>
              <w:tcPr>
                <w:tcW w:w="1985" w:type="dxa"/>
                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                <w:vAlign w:val="center"/>
              </w:tcPr>
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
                <w:r wsp:rsidRPr="006C50AA">
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                  <w:t>${word_index + 1}．</w:t>
                </w:r>
                <w:r wsp:rsidRPr="006C50AA">
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:b/>
                    <w:i/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                  <w:t>${word.prototype}</w:t>
                </w:r>
              </w:p>
            </w:tc>
            <w:tc>
              <w:tcPr>
                <w:tcW w:w="3035" w:type="dxa"/>
                <w:gridSpan w:val="2"/>
                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                <w:vAlign w:val="center"/>
              </w:tcPr>
              <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidRDefault="00D5413F" wsp:rsidP="00E602B2">
                <w:pPr>
                  <w:jc w:val="right"/>
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                </w:pPr>
                <w:r wsp:rsidRPr="006C50AA">
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}" w:hint="fareast"/>
                    <wx:font wx:val="${chinese_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                  <w:t>【<#list word.clazzes as clazz>${clazz.name}<#sep>，</#sep></#list>】</w:t>
                </w:r>
              </w:p>
            </w:tc>
          </w:tr>
          <w:tr wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidTr="00E602B2">
            <w:trPr>
              <w:cantSplit/>
              <w:jc w:val="center"/>
            </w:trPr>
            <w:tc>
              <w:tcPr>
                <w:tcW w:w="2510" w:type="dxa"/>
                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                <w:vAlign w:val="center"/>
              </w:tcPr>
              <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidRDefault="00E602B2" wsp:rsidP="00E602B2">
                <w:pPr>
                  <w:spacing w:line="${line_height}" w:line-rule="auto"/>
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${english_font_family}" w:h-ansi="${english_font_family}"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                </w:pPr>
                <w:proofErr w:type="gramStart"/>
                <w:r wsp:rsidRPr="006C50AA">
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${english_font_family}" w:h-ansi="${english_font_family}" w:cs="${english_font_family}" w:hint="fareast"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                  <w:t>英音：${word.britishPronunciation}</w:t>
                </w:r>
              </w:p>
            </w:tc>
            <w:tc>
              <w:tcPr>
                <w:tcW w:w="2510" w:type="dxa"/>
                <w:gridSpan w:val="2"/>
                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                <w:vAlign w:val="center"/>
              </w:tcPr>
              <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidRDefault="00E602B2" wsp:rsidP="00E602B2">
                <w:pPr>
                  <w:jc w:val="right"/>
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${english_font_family}" w:h-ansi="${english_font_family}"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                </w:pPr>
                <w:proofErr w:type="spellStart"/>
                <w:r wsp:rsidRPr="006C50AA">
                  <w:rPr>
                    <w:rFonts w:ascii="${english_font_family}" w:fareast="${english_font_family}" w:h-ansi="${english_font_family}" w:cs="${english_font_family}" w:hint="fareast"/>
                    <wx:font wx:val="${english_font_family}"/>
                    <w:sz w:val="${font_size}"/>
                    <w:sz-cs w:val="${font_size}"/>
                  </w:rPr>
                  <w:t>美音：${word.americanPronunciation}</w:t>
                </w:r>
              </w:p>
            </w:tc>
          </w:tr>
          <w:tr wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidTr="00E602B2">
            <w:trPr>
              <w:cantSplit/>
              <w:jc w:val="center"/>
            </w:trPr>
            <w:tc>
              <w:tcPr>
                <w:tcW w:w="5020" w:type="dxa"/>
                <w:gridSpan w:val="3"/>
                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                <w:vAlign w:val="center"/>
              </w:tcPr>
              <#if definition_arranges_vertically>
                <@vertical_arrangement definations=word.definition/>
              <#else>
                <@horizental_arrangement definations=word.definition/>
              </#if>
            </w:tc>
          </w:tr>
          <#if insert_blank_line_between_words>
            <w:tr wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidTr="00E602B2">
              <w:trPr>
                <w:cantSplit/>
                <w:jc w:val="center"/>
              </w:trPr>
              <w:tc>
                <w:tcPr>
                  <w:tcW w:w="5020" w:type="dxa"/>
                  <w:gridSpan w:val="3"/>
                  <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                  <w:vAlign w:val="center"/>
                </w:tcPr>
                <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="006C50AA" wsp:rsidRDefault="00E602B2" wsp:rsidP="00E602B2">
                  <w:pPr>
                    <w:spacing w:line="${line_height}" w:line-rule="auto"/>
                    <w:jc w:val="left"/>
                    <w:rPr>
                      <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}" w:hint="fareast"/>
                      <wx:font wx:val="${english_font_family}"/>
                      <w:sz w:val="${font_size}"/>
                      <w:sz-cs w:val="${font_size}"/>
                    </w:rPr>
                  </w:pPr>
                </w:p>
              </w:tc>
            </w:tr>
          </#if>
        </w:tbl>
        <w:p wsp:rsidR="00E602B2" wsp:rsidRPr="00E602B2" wsp:rsidRDefault="00E602B2" wsp:rsidP="00E602B2">
          <w:pPr>
            <w:spacing w:line="${line_height}" w:line-rule="auto"/>
            <w:jc w:val="left"/>
            <w:rPr>
              <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}" w:hint="fareast"/>
              <wx:font wx:val="${english_font_family}"/>
              <w:sz w:val="${font_size}"/>
              <w:sz-cs w:val="${font_size}"/>
            </w:rPr>
          </w:pPr>
          <wx:allowEmptyCollapse/>
        </w:p>
      </w:tc>
    </w:tr>
  </#list>
</w:tbl>
