Attribute VB_Name = "Module3"
Sub ��ũ��4()
Attribute ��ũ��4.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��4 ��ũ��
'

'
    ActiveSheet.ListObjects("�˻����̺�").Resize Range("$AQ$1:$BB$11")
    Range("AE35").Select
End Sub
Sub ��ũ��5()
Attribute ��ũ��5.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��5 ��ũ��
'

'
'    ActiveWorkbook.Worksheets("FindBook (4)").ListObjects("�˻����̺�").Sort.SortFields. _
'        Clear
'    ActiveWorkbook.Worksheets("FindBook (4)").ListObjects("�˻����̺�").Sort.SortFields. _
'        Add Key:=Range("�˻����̺�[[#All],[�Ⱓ��]]"), SortOn:=xlSortOnValues, Order:= _
'        xlDescending, DataOption:=xlSortNormal
'    With ActiveWorkbook.Worksheets("FindBook (4)").ListObjects("�˻����̺�").Sort
'        .Header = xlYes
'        .MatchCase = False
'        .Orientation = xlTopToBottom
'        .SortMethod = xlPinYin
'        .Apply
'    End With
'    Range("AB64").Select
    '[�˻����̺�[�Ⱓ��]]
    
    
End Sub
Sub ��ũ��6()
Attribute ��ũ��6.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��6 ��ũ��
'

'
    ActiveSheet.Shapes.Range(Array("���⿩��")).Select
    With Selection.ShapeRange(1).TextFrame2.TextRange.Characters(5, 5).Font.Fill
        .Visible = msoTrue
        .ForeColor.ObjectThemeColor = msoThemeColorText1
        .ForeColor.TintAndShade = 0
        .ForeColor.Brightness = 0
        .Transparency = 0
        .Solid
    End With
End Sub
