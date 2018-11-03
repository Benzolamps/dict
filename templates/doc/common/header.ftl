<#-- @ftlvariable name="content_path" type="java.lang.String" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="student" type="com.benzolamps.dict.bean.Student" -->
<#-- @ftlvariable name="groupId" type="java.lang.String" -->
<#include 'arrangement.ftl'/>
<#escape x as x?html>
  <?xml version="1.0" encoding="utf-8" standalone="yes"?>
  <?mso-application progid="Word.Document"?>
  <w:wordDocument
    xmlns:aml="http://schemas.microsoft.com/aml/2001/core"
    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
    xmlns:cx="http://schemas.microsoft.com/office/drawing/2014/chartex"
    xmlns:cx1="http://schemas.microsoft.com/office/drawing/2015/9/8/chartex"
    xmlns:cx2="http://schemas.microsoft.com/office/drawing/2015/10/21/chartex"
    xmlns:cx3="http://schemas.microsoft.com/office/drawing/2016/5/9/chartex"
    xmlns:cx4="http://schemas.microsoft.com/office/drawing/2016/5/10/chartex"
    xmlns:cx5="http://schemas.microsoft.com/office/drawing/2016/5/11/chartex"
    xmlns:cx6="http://schemas.microsoft.com/office/drawing/2016/5/12/chartex"
    xmlns:cx7="http://schemas.microsoft.com/office/drawing/2016/5/13/chartex"
    xmlns:cx8="http://schemas.microsoft.com/office/drawing/2016/5/14/chartex"
    xmlns:dt="uuid:C2F41010-65B3-11d1-A29F-00AA00C14882"
    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:aink="http://schemas.microsoft.com/office/drawing/2016/ink"
    xmlns:am3d="http://schemas.microsoft.com/office/drawing/2017/model3d"
    xmlns:o="urn:schemas-microsoft-com:office:office"
    xmlns:v="urn:schemas-microsoft-com:vml"
    xmlns:w10="urn:schemas-microsoft-com:office:word"
    xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml"
    xmlns:wx="http://schemas.microsoft.com/office/word/2003/auxHint"
    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
    xmlns:wsp="http://schemas.microsoft.com/office/word/2003/wordml/sp2"
    xmlns:sl="http://schemas.microsoft.com/schemaLibrary/2003/core"
    w:macrosPresent="no" w:embeddedObjPresent="no" w:ocxPresent="no" xml:space="preserve"
  >
    <w:ignoreSubtree w:val="http://schemas.microsoft.com/office/word/2003/wordml/sp2" />
    <o:DocumentProperties>
      <o:Author>${user_name}</o:Author>
      <o:LastAuthor>${user_name}</o:LastAuthor>
      <o:Revision>2</o:Revision>
      <o:TotalTime>1</o:TotalTime>
      <o:Created>${.now?iso_utc_ms}</o:Created>
      <o:LastSaved>${.now?iso_utc_ms}</o:LastSaved>
      <o:Pages>1</o:Pages>
      <o:Words>0</o:Words>
      <o:Characters>0</o:Characters>
      <o:Lines>0</o:Lines>
      <o:Paragraphs>0</o:Paragraphs>
      <o:CharactersWithSpaces>0</o:CharactersWithSpaces>
      <o:Version>16</o:Version>
    </o:DocumentProperties>
    <w:fonts>
      <w:defaultFonts w:ascii="${chinese_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${chinese_font_family}" w:cs="${english_font_family}" />
      <w:font w:name="${english_font_family}">
        <w:panose-1 w:val="02020603050405020304" />
        <w:charset w:val="00" />
        <w:family w:val="Roman" />
        <w:pitch w:val="variable" />
        <w:sig w:usb-0="E0002EFF" w:usb-1="C000785B" w:usb-2="00000009" w:usb-3="00000000" w:csb-0="000001FF" w:csb-1="00000000" />
      </w:font>
      <w:font w:name="${chinese_font_family}">
        <w:altName w:val="SimSun" />
        <w:panose-1 w:val="02010600030101010101" />
        <w:charset w:val="86" />
        <w:family w:val="auto" />
        <w:pitch w:val="variable" />
        <w:sig w:usb-0="00000003" w:usb-1="288F0000" w:usb-2="00000016" w:usb-3="00000000" w:csb-0="00040001" w:csb-1="00000000" />
      </w:font>
      <w:font w:name="@${chinese_font_family}">
        <w:charset w:val="86" />
        <w:family w:val="Modern" />
        <w:pitch w:val="fixed" />
        <w:sig w:usb-0="800002BF" w:usb-1="38CF7CFA" w:usb-2="00000016" w:usb-3="00000000" w:csb-0="00040001" w:csb-1="00000000" />
      </w:font>
      <w:font w:name="@${chinese_font_family}">
        <w:panose-1 w:val="02010600030101010101" />
        <w:charset w:val="86" />
        <w:family w:val="Modern" />
        <w:pitch w:val="fixed" />
        <w:sig w:usb-0="800002BF" w:usb-1="38CF7CFA" w:usb-2="00000016" w:usb-3="00000000" w:csb-0="00040001" w:csb-1="00000000" />
      </w:font>
    </w:fonts>
    <w:lists>
      <w:listDef w:listDefId="0">
        <w:lsid w:val="65930E62"/>
        <w:plt w:val="HybridMultilevel"/>
        <w:tmpl w:val="BD841D68"/>
        <w:lvl w:ilvl="0" w:tplc="11AE7DE8">
          <w:start w:val="1"/>
          <w:lvlText w:val="%1."/>
          <w:lvlJc w:val="left"/>
          <w:pPr>
            <w:ind w:left="360" w:hanging="360"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="1" w:tplc="04090019">
          <w:start w:val="1"/>
          <w:nfc w:val="4"/>
          <w:lvlText w:val="%2)"/>
          <w:lvlJc w:val="left"/>
          <w:pPr>
            <w:ind w:left="840" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="2" w:tplc="0409001B">
          <w:start w:val="1"/>
          <w:nfc w:val="2"/>
          <w:lvlText w:val="%3."/>
          <w:lvlJc w:val="right"/>
          <w:pPr>
            <w:ind w:left="1260" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="3" w:tplc="0409000F">
          <w:start w:val="1"/>
          <w:lvlText w:val="%4."/>
          <w:lvlJc w:val="left"/>
          <w:pPr>
            <w:ind w:left="1680" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="4" w:tplc="04090019">
          <w:start w:val="1"/>
          <w:nfc w:val="4"/>
          <w:lvlText w:val="%5)"/>
          <w:lvlJc w:val="left"/>
          <w:pPr>
            <w:ind w:left="2100" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="5" w:tplc="0409001B">
          <w:start w:val="1"/>
          <w:nfc w:val="2"/>
          <w:lvlText w:val="%6."/>
          <w:lvlJc w:val="right"/>
          <w:pPr>
            <w:ind w:left="2520" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="6" w:tplc="0409000F">
          <w:start w:val="1"/>
          <w:lvlText w:val="%7."/>
          <w:lvlJc w:val="left"/>
          <w:pPr>
            <w:ind w:left="2940" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="7" w:tplc="04090019">
          <w:start w:val="1"/>
          <w:nfc w:val="4"/>
          <w:lvlText w:val="%8)"/>
          <w:lvlJc w:val="left"/>
          <w:pPr>
            <w:ind w:left="3360" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
        <w:lvl w:ilvl="8" w:tplc="0409001B">
          <w:start w:val="1"/>
          <w:nfc w:val="2"/>
          <w:lvlText w:val="%9."/>
          <w:lvlJc w:val="right"/>
          <w:pPr>
            <w:ind w:left="3780" w:hanging="420"/>
          </w:pPr>
        </w:lvl>
      </w:listDef>
      <w:list w:ilfo="1">
        <w:ilst w:val="0"/>
        <w:lvlOverride w:ilvl="0">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="1">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="2">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="3">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="4">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="5">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="6">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="7">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
        <w:lvlOverride w:ilvl="8">
          <w:startOverride w:val="1"/>
        </w:lvlOverride>
      </w:list>
    </w:lists>
    <w:styles>
      <w:versionOfBuiltInStylenames w:val="7" />
      <w:latentStyles w:defLockedState="off" w:latentStyleCount="375">
        <w:lsdException w:name="Normal" />
        <w:lsdException w:name="heading 1" />
        <w:lsdException w:name="heading 2" />
        <w:lsdException w:name="heading 3" />
        <w:lsdException w:name="heading 4" />
        <w:lsdException w:name="heading 5" />
        <w:lsdException w:name="heading 6" />
        <w:lsdException w:name="heading 7" />
        <w:lsdException w:name="heading 8" />
        <w:lsdException w:name="heading 9" />
        <w:lsdException w:name="caption" />
        <w:lsdException w:name="Title" />
        <w:lsdException w:name="Subtitle" />
        <w:lsdException w:name="Strong" />
        <w:lsdException w:name="Emphasis" />
        <w:lsdException w:name="Normal Table" />
        <w:lsdException w:name="Table Simple 1" />
        <w:lsdException w:name="Table Simple 2" />
        <w:lsdException w:name="Table Simple 3" />
        <w:lsdException w:name="Table Classic 1" />
        <w:lsdException w:name="Table Classic 2" />
        <w:lsdException w:name="Table Classic 3" />
        <w:lsdException w:name="Table Classic 4" />
        <w:lsdException w:name="Table Colorful 1" />
        <w:lsdException w:name="Table Colorful 2" />
        <w:lsdException w:name="Table Colorful 3" />
        <w:lsdException w:name="Table Columns 1" />
        <w:lsdException w:name="Table Columns 2" />
        <w:lsdException w:name="Table Columns 3" />
        <w:lsdException w:name="Table Columns 4" />
        <w:lsdException w:name="Table Columns 5" />
        <w:lsdException w:name="Table Grid 1" />
        <w:lsdException w:name="Table Grid 2" />
        <w:lsdException w:name="Table Grid 3" />
        <w:lsdException w:name="Table Grid 4" />
        <w:lsdException w:name="Table Grid 5" />
        <w:lsdException w:name="Table Grid 6" />
        <w:lsdException w:name="Table Grid 7" />
        <w:lsdException w:name="Table Grid 8" />
        <w:lsdException w:name="Table List 1" />
        <w:lsdException w:name="Table List 2" />
        <w:lsdException w:name="Table List 3" />
        <w:lsdException w:name="Table List 4" />
        <w:lsdException w:name="Table List 5" />
        <w:lsdException w:name="Table List 6" />
        <w:lsdException w:name="Table List 7" />
        <w:lsdException w:name="Table List 8" />
        <w:lsdException w:name="Table 3D effects 1" />
        <w:lsdException w:name="Table 3D effects 2" />
        <w:lsdException w:name="Table 3D effects 3" />
        <w:lsdException w:name="Table Contemporary" />
        <w:lsdException w:name="Table Elegant" />
        <w:lsdException w:name="Table Professional" />
        <w:lsdException w:name="Table Subtle 1" />
        <w:lsdException w:name="Table Subtle 2" />
        <w:lsdException w:name="Table Web 1" />
        <w:lsdException w:name="Table Web 2" />
        <w:lsdException w:name="Table Web 3" />
        <w:lsdException w:name="Table Theme" />
        <w:lsdException w:name="No Spacing" />
        <w:lsdException w:name="Light Shading" />
        <w:lsdException w:name="Light List" />
        <w:lsdException w:name="Light Grid" />
        <w:lsdException w:name="Medium Shading 1" />
        <w:lsdException w:name="Medium Shading 2" />
        <w:lsdException w:name="Medium List 1" />
        <w:lsdException w:name="Medium List 2" />
        <w:lsdException w:name="Medium Grid 1" />
        <w:lsdException w:name="Medium Grid 2" />
        <w:lsdException w:name="Medium Grid 3" />
        <w:lsdException w:name="Dark List" />
        <w:lsdException w:name="Colorful Shading" />
        <w:lsdException w:name="Colorful List" />
        <w:lsdException w:name="Colorful Grid" />
        <w:lsdException w:name="Light Shading Accent 1" />
        <w:lsdException w:name="Light List Accent 1" />
        <w:lsdException w:name="Light Grid Accent 1" />
        <w:lsdException w:name="Medium Shading 1 Accent 1" />
        <w:lsdException w:name="Medium Shading 2 Accent 1" />
        <w:lsdException w:name="Medium List 1 Accent 1" />
        <w:lsdException w:name="List Paragraph" />
        <w:lsdException w:name="Quote" />
        <w:lsdException w:name="Intense Quote" />
        <w:lsdException w:name="Medium List 2 Accent 1" />
        <w:lsdException w:name="Medium Grid 1 Accent 1" />
        <w:lsdException w:name="Medium Grid 2 Accent 1" />
        <w:lsdException w:name="Medium Grid 3 Accent 1" />
        <w:lsdException w:name="Dark List Accent 1" />
        <w:lsdException w:name="Colorful Shading Accent 1" />
        <w:lsdException w:name="Colorful List Accent 1" />
        <w:lsdException w:name="Colorful Grid Accent 1" />
        <w:lsdException w:name="Light Shading Accent 2" />
        <w:lsdException w:name="Light List Accent 2" />
        <w:lsdException w:name="Light Grid Accent 2" />
        <w:lsdException w:name="Medium Shading 1 Accent 2" />
        <w:lsdException w:name="Medium Shading 2 Accent 2" />
        <w:lsdException w:name="Medium List 1 Accent 2" />
        <w:lsdException w:name="Medium List 2 Accent 2" />
        <w:lsdException w:name="Medium Grid 1 Accent 2" />
        <w:lsdException w:name="Medium Grid 2 Accent 2" />
        <w:lsdException w:name="Medium Grid 3 Accent 2" />
        <w:lsdException w:name="Dark List Accent 2" />
        <w:lsdException w:name="Colorful Shading Accent 2" />
        <w:lsdException w:name="Colorful List Accent 2" />
        <w:lsdException w:name="Colorful Grid Accent 2" />
        <w:lsdException w:name="Light Shading Accent 3" />
        <w:lsdException w:name="Light List Accent 3" />
        <w:lsdException w:name="Light Grid Accent 3" />
        <w:lsdException w:name="Medium Shading 1 Accent 3" />
        <w:lsdException w:name="Medium Shading 2 Accent 3" />
        <w:lsdException w:name="Medium List 1 Accent 3" />
        <w:lsdException w:name="Medium List 2 Accent 3" />
        <w:lsdException w:name="Medium Grid 1 Accent 3" />
        <w:lsdException w:name="Medium Grid 2 Accent 3" />
        <w:lsdException w:name="Medium Grid 3 Accent 3" />
        <w:lsdException w:name="Dark List Accent 3" />
        <w:lsdException w:name="Colorful Shading Accent 3" />
        <w:lsdException w:name="Colorful List Accent 3" />
        <w:lsdException w:name="Colorful Grid Accent 3" />
        <w:lsdException w:name="Light Shading Accent 4" />
        <w:lsdException w:name="Light List Accent 4" />
        <w:lsdException w:name="Light Grid Accent 4" />
        <w:lsdException w:name="Medium Shading 1 Accent 4" />
        <w:lsdException w:name="Medium Shading 2 Accent 4" />
        <w:lsdException w:name="Medium List 1 Accent 4" />
        <w:lsdException w:name="Medium List 2 Accent 4" />
        <w:lsdException w:name="Medium Grid 1 Accent 4" />
        <w:lsdException w:name="Medium Grid 2 Accent 4" />
        <w:lsdException w:name="Medium Grid 3 Accent 4" />
        <w:lsdException w:name="Dark List Accent 4" />
        <w:lsdException w:name="Colorful Shading Accent 4" />
        <w:lsdException w:name="Colorful List Accent 4" />
        <w:lsdException w:name="Colorful Grid Accent 4" />
        <w:lsdException w:name="Light Shading Accent 5" />
        <w:lsdException w:name="Light List Accent 5" />
        <w:lsdException w:name="Light Grid Accent 5" />
        <w:lsdException w:name="Medium Shading 1 Accent 5" />
        <w:lsdException w:name="Medium Shading 2 Accent 5" />
        <w:lsdException w:name="Medium List 1 Accent 5" />
        <w:lsdException w:name="Medium List 2 Accent 5" />
        <w:lsdException w:name="Medium Grid 1 Accent 5" />
        <w:lsdException w:name="Medium Grid 2 Accent 5" />
        <w:lsdException w:name="Medium Grid 3 Accent 5" />
        <w:lsdException w:name="Dark List Accent 5" />
        <w:lsdException w:name="Colorful Shading Accent 5" />
        <w:lsdException w:name="Colorful List Accent 5" />
        <w:lsdException w:name="Colorful Grid Accent 5" />
        <w:lsdException w:name="Light Shading Accent 6" />
        <w:lsdException w:name="Light List Accent 6" />
        <w:lsdException w:name="Light Grid Accent 6" />
        <w:lsdException w:name="Medium Shading 1 Accent 6" />
        <w:lsdException w:name="Medium Shading 2 Accent 6" />
        <w:lsdException w:name="Medium List 1 Accent 6" />
        <w:lsdException w:name="Medium List 2 Accent 6" />
        <w:lsdException w:name="Medium Grid 1 Accent 6" />
        <w:lsdException w:name="Medium Grid 2 Accent 6" />
        <w:lsdException w:name="Medium Grid 3 Accent 6" />
        <w:lsdException w:name="Dark List Accent 6" />
        <w:lsdException w:name="Colorful Shading Accent 6" />
        <w:lsdException w:name="Colorful List Accent 6" />
        <w:lsdException w:name="Colorful Grid Accent 6" />
        <w:lsdException w:name="Subtle Emphasis" />
        <w:lsdException w:name="Intense Emphasis" />
        <w:lsdException w:name="Subtle Reference" />
        <w:lsdException w:name="Intense Reference" />
        <w:lsdException w:name="Book Title" />
        <w:lsdException w:name="TOC Heading" />
      </w:latentStyles>
      <w:style w:type="paragraph" w:default="on" w:styleId="a">
        <w:name w:val="Normal" />
        <wx:uiName wx:val="正文" />
        <w:rsid w:val="00DA22B8" />
        <w:pPr>
          <w:widowControl w:val="off" />
          <w:jc w:val="both" />
        </w:pPr>
        <w:rPr>
          <wx:font wx:val="${chinese_font_family}" />
          <w:kern w:val="2" />
          <w:sz w:val="21" />
          <w:sz-cs w:val="22" />
          <w:lang w:val="EN-US" w:fareast="ZH-CN" w:bidi="AR-SA" />
        </w:rPr>
      </w:style>
      <w:style w:type="character" w:default="on" w:styleId="a0">
        <w:name w:val="Default Paragraph Font" />
        <wx:uiName wx:val="默认段落字体" />
      </w:style>
      <w:style w:type="table" w:default="on" w:styleId="a1">
        <w:name w:val="Normal Table" />
        <wx:uiName wx:val="普通表格" />
        <w:rPr>
          <wx:font wx:val="${chinese_font_family}" />
          <w:lang w:val="EN-US" w:fareast="ZH-CN" w:bidi="AR-SA" />
        </w:rPr>
        <w:tblPr>
          <w:tblInd w:w="0" w:type="dxa" />
          <w:tblCellMar>
            <w:top w:w="0" w:type="dxa" />
            <w:left w:w="108" w:type="dxa" />
            <w:bottom w:w="0" w:type="dxa" />
            <w:right w:w="108" w:type="dxa" />
          </w:tblCellMar>
        </w:tblPr>
      </w:style>
      <w:style w:type="list" w:default="on" w:styleId="a2">
        <w:name w:val="No List" />
        <wx:uiName wx:val="无列表" />
      </w:style>
      <w:style w:type="paragraph" w:styleId="a3">
        <w:name w:val="List Paragraph" />
        <wx:uiName wx:val="列表段落" />
        <w:basedOn w:val="a" />
        <w:rsid w:val="0028394F" />
        <w:pPr>
          <w:ind w:first-line-chars="200" w:first-line="420" />
        </w:pPr>
        <w:rPr>
          <wx:font wx:val="${chinese_font_family}" />
        </w:rPr>
      </w:style>
      <w:style w:type="paragraph" w:styleId="a4">
        <w:name w:val="header" />
        <wx:uiName wx:val="页眉" />
        <w:basedOn w:val="a" />
        <w:link w:val="a5" />
        <w:rsid w:val="00C226EB" />
        <w:pPr>
          <w:pBdr>
            <w:bottom w:val="single" w:sz="6" wx:bdrwidth="15" w:space="1" w:color="auto" />
          </w:pBdr>
          <w:tabs>
            <w:tab w:val="center" w:pos="4153" />
            <w:tab w:val="right" w:pos="8306" />
          </w:tabs>
          <w:snapToGrid w:val="off" />
          <w:jc w:val="center" />
        </w:pPr>
        <w:rPr>
          <wx:font wx:val="${chinese_font_family}" />
          <w:sz w:val="18" />
          <w:sz-cs w:val="18" />
        </w:rPr>
      </w:style>
      <w:style w:type="character" w:styleId="a5">
        <w:name w:val="页眉 字符" />
        <w:link w:val="a4" />
        <w:rsid w:val="00C226EB" />
        <w:rPr>
          <w:kern w:val="2" />
          <w:sz w:val="18" />
          <w:sz-cs w:val="18" />
        </w:rPr>
      </w:style>
      <w:style w:type="paragraph" w:styleId="a6">
        <w:name w:val="footer" />
        <wx:uiName wx:val="页脚" />
        <w:basedOn w:val="a" />
        <w:link w:val="a7" />
        <w:rsid w:val="00C226EB" />
        <w:pPr>
          <w:tabs>
            <w:tab w:val="center" w:pos="4153" />
            <w:tab w:val="right" w:pos="8306" />
          </w:tabs>
          <w:snapToGrid w:val="off" />
          <w:jc w:val="left" />
        </w:pPr>
        <w:rPr>
          <wx:font wx:val="${chinese_font_family}" />
          <w:sz w:val="18" />
          <w:sz-cs w:val="18" />
        </w:rPr>
      </w:style>
      <w:style w:type="character" w:styleId="a7">
        <w:name w:val="页脚 字符" />
        <w:link w:val="a6" />
        <w:rsid w:val="00C226EB" />
        <w:rPr>
          <w:kern w:val="2" />
          <w:sz w:val="18" />
          <w:sz-cs w:val="18" />
        </w:rPr>
      </w:style>
      <w:style w:type="table" w:styleId="a8">
        <w:name w:val="Table Grid"/>
        <wx:uiName wx:val="网格型"/>
        <w:basedOn w:val="a1"/>
        <w:rsid w:val="00F417AE"/>
        <w:rPr>
          <wx:font wx:val="${chinese_font_family}"/>
        </w:rPr>
        <w:tblPr>
          <w:tblBorders>
            <w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto"/>
            <w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto"/>
            <w:bottom w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto"/>
            <w:right w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto"/>
            <w:insideH w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto"/>
            <w:insideV w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto"/>
          </w:tblBorders>
        </w:tblPr>
      </w:style>
    </w:styles>
    <w:divs>
      <w:div w:id="129130794">
        <w:bodyDiv w:val="on"/>
        <w:marLeft w:val="0"/>
        <w:marRight w:val="0"/>
        <w:marTop w:val="0"/>
        <w:marBottom w:val="0"/>
        <w:divBdr>
          <w:top w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
          <w:left w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
          <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
          <w:right w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto"/>
        </w:divBdr>
      </w:div>
      <w:div w:id="275060745">
        <w:bodyDiv w:val="on" />
        <w:marLeft w:val="0" />
        <w:marRight w:val="0" />
        <w:marTop w:val="0" />
        <w:marBottom w:val="0" />
        <w:divBdr>
          <w:top w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
          <w:left w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
          <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
          <w:right w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
        </w:divBdr>
      </w:div>
      <w:div w:id="433211266">
        <w:bodyDiv w:val="on" />
        <w:marLeft w:val="0" />
        <w:marRight w:val="0" />
        <w:marTop w:val="0" />
        <w:marBottom w:val="0" />
        <w:divBdr>
          <w:top w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
          <w:left w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
          <w:bottom w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
          <w:right w:val="none" w:sz="0" wx:bdrwidth="0" w:space="0" w:color="auto" />
        </w:divBdr>
      </w:div>
    </w:divs>
    <w:shapeDefaults>
      <o:shapedefaults v:ext="edit" spidmax="2049" />
      <o:shapelayout v:ext="edit">
        <o:idmap v:ext="edit" data="1" />
      </o:shapelayout>
    </w:shapeDefaults>
    <w:docPr>
      <w:view w:val="print" />
      <w:zoom w:percent="100" />
      <w:doNotEmbedSystemFonts />
      <w:bordersDontSurroundHeader />
      <w:bordersDontSurroundFooter />
      <w:proofState w:spelling="clean" w:grammar="clean" />
      <w:defaultTabStop w:val="420" />
      <w:drawingGridHorizontalSpacing w:val="105" />
      <w:drawingGridVerticalSpacing w:val="156" />
      <w:displayHorizontalDrawingGridEvery w:val="0" />
      <w:displayVerticalDrawingGridEvery w:val="2" />
      <w:punctuationKerning />
      <w:characterSpacingControl w:val="CompressPunctuation" />
      <w:optimizeForBrowser />
      <w:allowPNG />
      <w:validateAgainstSchema />
      <w:saveInvalidXML w:val="off" />
      <w:ignoreMixedContent w:val="off" />
      <w:alwaysShowPlaceholderText w:val="off" />
      <w:hdrShapeDefaults>
        <o:shapedefaults v:ext="edit" spidmax="2049" />
      </w:hdrShapeDefaults>
      <w:footnotePr>
        <w:footnote w:type="separator">
          <w:p wsp:rsidR="00D92772" wsp:rsidRDefault="00D92772" wsp:rsidP="00DA22B8">
            <w:r>
              <w:separator />
            </w:r>
          </w:p>
        </w:footnote>
        <w:footnote w:type="continuation-separator">
          <w:p wsp:rsidR="00D92772" wsp:rsidRDefault="00D92772" wsp:rsidP="00DA22B8">
            <w:r>
              <w:continuationSeparator />
            </w:r>
          </w:p>
        </w:footnote>
      </w:footnotePr>
      <w:endnotePr>
        <w:endnote w:type="separator">
          <w:p wsp:rsidR="00D92772" wsp:rsidRDefault="00D92772" wsp:rsidP="00DA22B8">
            <w:r>
              <w:separator />
            </w:r>
          </w:p>
        </w:endnote>
        <w:endnote w:type="continuation-separator">
          <w:p wsp:rsidR="00D92772" wsp:rsidRDefault="00D92772" wsp:rsidP="00DA22B8">
            <w:r>
              <w:continuationSeparator />
            </w:r>
          </w:p>
        </w:endnote>
      </w:endnotePr>
      <w:compat>
        <w:spaceForUL />
        <w:balanceSingleByteDoubleByteWidth />
        <w:doNotLeaveBackslashAlone />
        <w:ulTrailSpace />
        <w:doNotExpandShiftReturn />
        <w:adjustLineHeightInTable />
        <w:breakWrappedTables />
        <w:snapToGridInCell />
        <w:wrapTextWithPunct />
        <w:useAsianBreakRules />
        <w:dontGrowAutofit />
        <w:useFELayout />
      </w:compat>
      <wsp:rsids>
        <wsp:rsidRoot wsp:val="004D154B"/>
        <wsp:rsid wsp:val="0004152A"/>
        <wsp:rsid wsp:val="000F428B"/>
        <wsp:rsid wsp:val="00167B55"/>
        <wsp:rsid wsp:val="0024565D"/>
        <wsp:rsid wsp:val="0028394F"/>
      <wsp:rsid wsp:val="002A2BA6"/>
        <wsp:rsid wsp:val="00301555"/>
        <wsp:rsid wsp:val="0036705E"/>
        <wsp:rsid wsp:val="003A3ADA"/>
        <wsp:rsid wsp:val="003F07E6"/>
        <wsp:rsid wsp:val="003F46FE"/>
        <wsp:rsid wsp:val="00433643"/>
        <wsp:rsid wsp:val="00482F13"/>
        <wsp:rsid wsp:val="004A79FD"/>
        <wsp:rsid wsp:val="004D154B"/>
        <wsp:rsid wsp:val="00631EE5"/>
        <wsp:rsid wsp:val="00672711"/>
      <wsp:rsid wsp:val="0081488E"/>
        <wsp:rsid wsp:val="008E644D"/>
        <wsp:rsid wsp:val="009419D7"/>
      <wsp:rsid wsp:val="0094515C"/>
        <wsp:rsid wsp:val="0097708E"/>
        <wsp:rsid wsp:val="009938D6"/>
        <wsp:rsid wsp:val="00A86321"/>
      <wsp:rsid wsp:val="00AD4447"/>
        <wsp:rsid wsp:val="00B753B3"/>
        <wsp:rsid wsp:val="00B90DF8"/>
        <wsp:rsid wsp:val="00C226EB"/>
        <wsp:rsid wsp:val="00D24F0D"/>
        <wsp:rsid wsp:val="00D434B7"/>
        <wsp:rsid wsp:val="00D47DEC"/>
        <wsp:rsid wsp:val="00D605A9"/>
      <wsp:rsid wsp:val="00D92772"/>
        <wsp:rsid wsp:val="00DA22B8"/>
        <wsp:rsid wsp:val="00E526BC"/>
        <wsp:rsid wsp:val="00E81B2F"/>
        <wsp:rsid wsp:val="00F2450F"/>
        <wsp:rsid wsp:val="00F417AE"/>
      </wsp:rsids>
    </w:docPr>
    <w:body>
      <wx:sect>
        <#include '/${content_path}.ftl'/>
        <w:p wsp:rsidR="00672711" wsp:rsidRPr="0028394F" wsp:rsidRDefault="00672711" wsp:rsidP="003A3ADA">
          <w:pPr>
            <w:jc w:val="left" />
            <w:rPr>
              <w:rFonts w:ascii="${english_font_family}" w:fareast="${chinese_font_family}" w:h-ansi="${english_font_family}" w:hint="fareast" />
              <wx:font wx:val="${english_font_family}" />
              <w:sz w:val="18" />
              <w:sz-cs w:val="18" />
            </w:rPr>
          </w:pPr>
        </w:p>
        <w:sectPr wsp:rsidR="002A2BA6" wsp:rsidRPr="0094515C" wsp:rsidSect="002A2BA6">
          <w:hdr w:type="odd">
            <#include student???string('top-personal.ftl', 'top-default.ftl')/>
            <wx:pBdrGroup>
              <wx:borders>
                <wx:bottom wx:val="solid" wx:bdrwidth="15" wx:space="1" wx:color="auto"/>
              </wx:borders>
              <w:p wsp:rsidR="00672711" wsp:rsidRPr="00F417AE" wsp:rsidRDefault="00F417AE" wsp:rsidP="00F417AE">
                <w:pPr>
                  <w:pStyle w:val="a4"/>
                  <w:spacing w:line="14" w:line-rule="exact"/>
                  <w:jc w:val="both"/>
                  <w:rPr>
                    <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体" w:hint="fareast"/>
                    <wx:font wx:val="楷体"/>
                  </w:rPr>
                </w:pPr>
                <w:r>
                  <w:rPr>
                    <w:rFonts w:ascii="楷体" w:fareast="楷体" w:h-ansi="楷体"/>
                    <wx:font wx:val="楷体"/>
                  </w:rPr>
                  <w:t> </w:t>
                </w:r>
              </w:p>
            </wx:pBdrGroup>
          </w:hdr>
          <w:ftr w:type="odd">
            <w:p wsp:rsidR="003F07E6" wsp:rsidRPr="003F07E6" wsp:rsidRDefault="00AD4447" wsp:rsidP="003F07E6">
              <w:pPr>
                <w:jc w:val="center" />
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" w:hint="fareast" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:sz-cs w:val="21" />
                </w:rPr>
              </w:pPr>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:fldChar w:fldCharType="begin" />
              </w:r>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:instrText>PAGE</w:instrText>
              </w:r>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:fldChar w:fldCharType="separate" />
              </w:r>
              <w:r wsp:rsidR="003F07E6" wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                  <w:lang w:val="ZH-CN" />
                </w:rPr>
                <w:t>2</w:t>
              </w:r>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:fldChar w:fldCharType="end" />
              </w:r>
              <w:r wsp:rsidR="003F07E6" wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:sz-cs w:val="21" />
                  <w:lang w:val="ZH-CN" />
                </w:rPr>
                <w:t> / </w:t>
              </w:r>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:fldChar w:fldCharType="begin" />
              </w:r>
              <w:r wsp:rsidR="003F07E6" wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:instrText>NUMPAGES</w:instrText>
              </w:r>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:fldChar w:fldCharType="separate" />
              </w:r>
              <w:r wsp:rsidR="003F07E6" wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                  <w:lang w:val="ZH-CN" />
                </w:rPr>
                <w:t>2</w:t>
              </w:r>
              <w:r wsp:rsidRPr="00E74F26">
                <w:rPr>
                  <w:rFonts w:ascii="${english_font_family}" w:h-ansi="${english_font_family}" />
                  <wx:font wx:val="${english_font_family}" />
                  <w:b-cs />
                  <w:sz-cs w:val="21" />
                </w:rPr>
                <w:fldChar w:fldCharType="end" />
              </w:r>
            </w:p>
          </w:ftr>
          <w:type w:val="continuous" />
          <w:pgSz w:w="11906" w:h="16838" w:code="9"/>
          <w:pgMar w:top="567" w:right="567" w:bottom="567" w:left="567" w:header="567" w:footer="567" w:gutter="0"/>
          <w:cols w:num="${separate_columns}" w:space="424"/>
          <w:docGrid w:type="lines" w:line-pitch="312" />
        </w:sectPr>
      </wx:sect>
    </w:body>
  </w:wordDocument>
</#escape>