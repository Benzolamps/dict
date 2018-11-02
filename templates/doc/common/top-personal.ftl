<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="group" type="com.benzolamps.dict.bean.Group" -->
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
    <w:gridCol w:w="2112"/>
    <w:gridCol w:w="2115"/>
    <w:gridCol w:w="2079"/>
    <w:gridCol w:w="2903"/>
    <w:gridCol w:w="1257"/>
  </w:tblGrid>
  <w:tr wsp:rsidR="0004152A" wsp:rsidRPr="0004152A" wsp:rsidTr="0097708E">
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
          <w:t>学号：${student.id}</w:t>
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
        <w:tcW w:w="2906" w:type="dxa"/>
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
              <v:stroke joinstyle="miter" />
              <v:formulas>
                <v:f eqn="if lineDrawn pixelLineWidth 0" />
                <v:f eqn="sum @0 1 0" />
                <v:f eqn="sum 0 0 @1" />
                <v:f eqn="prod @2 1 2" />
                <v:f eqn="prod @3 21600 pixelWidth" />
                <v:f eqn="prod @3 21600 pixelHeight" />
                <v:f eqn="sum @0 0 1" />
                <v:f eqn="prod @6 1 2" />
                <v:f eqn="prod @7 21600 pixelWidth" />
                <v:f eqn="sum @8 21600 0" />
                <v:f eqn="prod @7 21600 pixelHeight" />
                <v:f eqn="sum @10 21600 0" />
              </v:formulas>
              <v:path o:extrusionok="f" gradientshapeok="t" o:connecttype="rect" />
              <o:lock v:ext="edit" aspectratio="t" />
            </v:shapetype>
            <w:binData w:name="wordml://03000001.png" xml:space="preserve">
              iVBORw0KGgoAAAANSUhEUgAAAGQAAABkAQAAAABYmaj5AAABJklEQVR42rXUu42EMBAG4B85cMY2YMltOHNL0MACDUBLztwGEg1AtoHlucG7QncB4wvuEAGfJYPnBejbteNvlAADv0IDuqKBbHbmES3FijptOo+GTFOXnaJd4q80OjX6ugbiL2zLdbJ7cXx95PuK9k5nrvKZhiuDd0o9qeVld78dURZlv/YvmzWnTVZ6ag5uOwIvVNT6bXZ8nHd8stQUVkB99t2KlkBTTE9Pc0VpCKZ1pnMqe1lEYW0iOtjyFkk70PKjw1lbUbPfMmwGl1cW19YeVBa8LKKYmkCjVnNFCRyZW1ug07K4TbhhUx/Tg2SV+XPc1O8ukMTzN3oMvK8mnpUZdgTg6zrnj+tWlxlKzkoXSOKz7I4WSm1NJT4uLydD1n/8FX/oC5YN3PF1zDYiAAAAAElFTkSuQmCC
            </w:binData>
            <v:shape id="_x0000_i1130" type="#_x0000_t75" style="width:57pt;height:57pt;mso-position-vertical:absolute">
              <v:imagedata src="wordml://03000001.png" o:title="1"/>
              <o:lock v:ext="edit" aspectratio="f"/>
            </v:shape>
          </w:pict>
        </w:r>
      </w:p>
    </w:tc>
  </w:tr>
</w:tbl>