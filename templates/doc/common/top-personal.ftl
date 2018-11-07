<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="groupId" type="java.lang.String" -->
<w:tbl>
  <w:tblPr>
    <w:tblW w:w="0" w:type="auto"/>
    <w:tblCellMar>
      <w:left w:w="0" w:type="dxa"/>
      <w:right w:w="0" w:type="dxa"/>
    </w:tblCellMar>
    <w:tblLook w:val="04A0"/>
  </w:tblPr>
  <w:tblGrid>
    <w:gridCol w:w="9622"/>
    <w:gridCol w:w="1150"/>
  </w:tblGrid>
  <w:tr wsp:rsidR="002A2BA6" wsp:rsidRPr="0004152A" wsp:rsidTr="002A2BA6">
    <w:tc>
      <w:tcPr>
        <w:tcW w:w="9632" w:type="dxa"/>
        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
        <w:vAlign w:val="bottom"/>
      </w:tcPr>
      <w:p wsp:rsidR="002A2BA6" wsp:rsidRDefault="002A2BA6"/>
      <w:tbl>
        <w:tblPr>
          <w:tblW w:w="0" w:type="auto"/>
          <w:tblCellMar>
            <w:left w:w="0" w:type="dxa"/>
            <w:right w:w="0" w:type="dxa"/>
          </w:tblCellMar>
          <w:tblLook w:val="04A0"/>
        </w:tblPr>
        <w:tblGrid>
          <w:gridCol w:w="2024"/>
          <w:gridCol w:w="2027"/>
          <w:gridCol w:w="1993"/>
          <w:gridCol w:w="3568"/>
        </w:tblGrid>
        <w:tr wsp:rsidR="002A2BA6" wsp:rsidRPr="0004152A" wsp:rsidTr="002A2BA6">
          <w:tc>
            <w:tcPr>
              <w:tcW w:w="2115" w:type="dxa"/>
              <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
              <w:vAlign w:val="bottom"/>
            </w:tcPr>
            <w:p wsp:rsidR="00F417AE" wsp:rsidRPr="0004152A" wsp:rsidRDefault="00F417AE" wsp:rsidP="0004152A">
              <w:pPr>
                <w:pStyle w:val="a4" />
                <w:pBdr>
                  <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
                </w:pBdr>
                <w:jc w:val="both"/>
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:sz w:val="21"/>
                  <w:sz-cs w:val="21"/>
                </w:rPr>
              </w:pPr>
              <w:r wsp:rsidRPr="0004152A">
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:sz w:val="21"/>
                  <w:sz-cs w:val="21"/>
                </w:rPr>
                <w:t>学号：${student.number}</w:t>
              </w:r>
            </w:p>
          </w:tc>
          <w:tc>
            <w:tcPr>
              <w:tcW w:w="2117" w:type="dxa"/>
              <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
              <w:vAlign w:val="bottom"/>
            </w:tcPr>
            <w:p wsp:rsidR="00F417AE" wsp:rsidRPr="0004152A" wsp:rsidRDefault="00F417AE" wsp:rsidP="0004152A">
              <w:pPr>
                <w:pStyle w:val="a4"/>
                <w:pBdr>
                  <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
                </w:pBdr>
                <w:jc w:val="both"/>
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:sz w:val="21" />
                  <w:sz-cs w:val="21" />
                </w:rPr>
              </w:pPr>
              <w:r wsp:rsidRPr="0004152A">
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:sz w:val="21" />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:t>班级：${student.clazz.name}</w:t>
              </w:r>
            </w:p>
          </w:tc>
          <w:tc>
            <w:tcPr>
              <w:tcW w:w="2081" w:type="dxa"/>
              <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
              <w:vAlign w:val="bottom"/>
            </w:tcPr>
            <w:p wsp:rsidR="00F417AE" wsp:rsidRPr="0004152A" wsp:rsidRDefault="00F417AE" wsp:rsidP="0004152A">
              <w:pPr>
                <w:pStyle w:val="a4"/>
                <w:pBdr>
                  <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
                </w:pBdr>
                <w:jc w:val="both"/>
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:sz w:val="21" />
                  <w:sz-cs w:val="21" />
                </w:rPr>
              </w:pPr>
              <w:r wsp:rsidRPr="0004152A">
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:sz w:val="21" />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:t>姓名：${student.name}</w:t>
              </w:r>
            </w:p>
          </w:tc>
          <w:tc>
            <w:tcPr>
              <w:tcW w:w="3752" w:type="dxa"/>
              <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
              <w:vAlign w:val="bottom"/>
            </w:tcPr>
            <w:p wsp:rsidR="00F417AE" wsp:rsidRPr="0004152A" wsp:rsidRDefault="00F417AE" wsp:rsidP="0004152A">
              <w:pPr>
                <w:pStyle w:val="a4"/>
                <w:pBdr>
                  <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
                </w:pBdr>
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:b/>
                  <w:sz w:val="30"/>
                  <w:sz-cs w:val="30"/>
                </w:rPr>
              </w:pPr>
              <w:r wsp:rsidRPr="0004152A">
                <w:rPr>
                  <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                  <wx:font wx:val="楷体"/>
                  <w:b/>
                  <w:sz w:val="30"/>
                  <w:sz-cs w:val="30"/>
                </w:rPr>
                <w:t>${title}</w:t>
              </w:r>
            </w:p>
          </w:tc>
        </w:tr>
      </w:tbl>
      <#if need_top_info>
        <#include 'top-info.ftl'/>
      <#else>
        <w:p wsp:rsidR="0081488E" wsp:rsidRPr="002A2BA6" wsp:rsidRDefault="0081488E" wsp:rsidP="002A2BA6">
          <w:pPr>
            <w:pStyle w:val="a4"/>
            <w:pBdr>
              <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
            </w:pBdr>
            <w:spacing w:line="240" w:line-rule="at-least"/>
            <w:jc w:val="left"/>
            <w:rPr>
              <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体"/>
              <wx:font wx:val="楷体"/>
            </w:rPr>
          </w:pPr>
          <wx:allowEmptyCollapse/>
        </w:p>
      </#if>
    </w:tc>
    <w:tc>
      <w:tcPr>
        <w:tcW w:w="1257" w:type="dxa"/>
        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
        <w:vAlign w:val="bottom"/>
      </w:tcPr>
      <w:p wsp:rsidR="00F417AE" wsp:rsidRPr="0004152A" wsp:rsidRDefault="00B753B3" wsp:rsidP="0004152A">
        <w:pPr>
          <w:pStyle w:val="a4"/>
          <w:pBdr>
            <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
          </w:pBdr>
          <w:jc w:val="right"/>
          <w:rPr>
            <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
            <wx:font wx:val="楷体"/>
          </w:rPr>
        </w:pPr>
        <w:r wsp:rsidRPr="00B753B3">
          <w:rPr>
            <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
            <wx:font wx:val="楷体"/>
          </w:rPr>
          <w:pict>
            <v:shapetype id="_x0000_t75" coordsize="21600,21600" o:spt="75" o:preferrelative="t" path="m@4@5l@4@11@9@11@9@5xe" filled="f" stroked="f">
              <v:stroke joinstyle="miter"/>
              <v:formulas>
                <v:f eqn="if lineDrawn pixelLineWidth 0"/>
                <v:f eqn="sum @0 1 0"/>
                <v:f eqn="sum 0 0 @1"/>
                <v:f eqn="prod @2 1 2"/>
                <v:f eqn="prod @3 21600 pixelWidth"/>
                <v:f eqn="prod @3 21600 pixelHeight"/>
                <v:f eqn="sum @0 0 1"/>
                <v:f eqn="prod @6 1 2"/>
                <v:f eqn="prod @7 21600 pixelWidth"/>
                <v:f eqn="sum @8 21600 0"/>
                <v:f eqn="prod @7 21600 pixelHeight"/>
                <v:f eqn="sum @10 21600 0"/>
              </v:formulas>
              <v:path o:extrusionok="f" gradientshapeok="t" o:connecttype="rect"/>
              <o:lock v:ext="edit" aspectratio="t"/>
            </v:shapetype>
            <#assign content='${student.id},${groupId}'/>
            <w:binData w:name="wordml://03000002.png" xml:space="preserve"><@qr_code_base64 content=content/></w:binData>
            <v:shape id="_x0000_i1130" type="#_x0000_t75" style="width:57pt;height:57pt;mso-position-vertical:absolute">
              <v:imagedata src="wordml://03000002.png" o:title="1"/>
              <o:lock v:ext="edit" aspectratio="f"/>
            </v:shape>
          </w:pict>
        </w:r>
      </w:p>
    </w:tc>
  </w:tr>
</w:tbl>