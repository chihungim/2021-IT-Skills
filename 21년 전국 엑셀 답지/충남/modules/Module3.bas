Attribute VB_Name = "Module3"
Sub 매크로4()
Attribute 매크로4.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로4 매크로
'

'
    ActiveSheet.ListObjects("검색테이블").Resize Range("$AQ$1:$BB$11")
    Range("AE35").Select
End Sub
Sub 매크로5()
Attribute 매크로5.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로5 매크로
'

'
'    ActiveWorkbook.Worksheets("FindBook (4)").ListObjects("검색테이블").Sort.SortFields. _
'        Clear
'    ActiveWorkbook.Worksheets("FindBook (4)").ListObjects("검색테이블").Sort.SortFields. _
'        Add Key:=Range("검색테이블[[#All],[출간일]]"), SortOn:=xlSortOnValues, Order:= _
'        xlDescending, DataOption:=xlSortNormal
'    With ActiveWorkbook.Worksheets("FindBook (4)").ListObjects("검색테이블").Sort
'        .Header = xlYes
'        .MatchCase = False
'        .Orientation = xlTopToBottom
'        .SortMethod = xlPinYin
'        .Apply
'    End With
'    Range("AB64").Select
    '[검색테이블[출간일]]
    
    
End Sub
Sub 매크로6()
Attribute 매크로6.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로6 매크로
'

'
    ActiveSheet.Shapes.Range(Array("대출여부")).Select
    With Selection.ShapeRange(1).TextFrame2.TextRange.Characters(5, 5).Font.Fill
        .Visible = msoTrue
        .ForeColor.ObjectThemeColor = msoThemeColorText1
        .ForeColor.TintAndShade = 0
        .ForeColor.Brightness = 0
        .Transparency = 0
        .Solid
    End With
End Sub
