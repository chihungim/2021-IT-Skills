Attribute VB_Name = "Module4"
Sub ��ũ��8()
Attribute ��ũ��8.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��8 ��ũ��
'

'
    ActiveSheet.Shapes.Range(Array("���࿩��")).Select
End Sub
Sub ��ũ��9()
Attribute ��ũ��9.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��9 ��ũ��
'

'
    Selection.ShapeRange(1).TextFrame2.TextRange.Characters.Text = "? ��������Ұ�"
    With Selection.ShapeRange(1).TextFrame2.TextRange.Characters(1, 8). _
        ParagraphFormat
        .FirstLineIndent = 0
        .Alignment = msoAlignRight
    End With
    With Selection.ShapeRange(1).TextFrame2.TextRange.Characters(1, 2).Font
        .Bold = msoTrue
        .NameComplexScript = "+mn-cs"
        .NameFarEast = "+mn-ea"
        .Fill.Visible = msoTrue
        .Fill.ForeColor.RGB = RGB(15, 0, 0)
        .Fill.Transparency = 0
        .Fill.Solid
        .Size = 12
        .name = "Bahnschrift"
        .NameOther = "Webdings"
    End With
    With Selection.ShapeRange(1).TextFrame2.TextRange.Characters(3, 6).Font
        .Bold = msoTrue
        .NameComplexScript = "+mn-cs"
        .NameFarEast = "+mn-ea"
        .Fill.Visible = msoTrue
        .Fill.ForeColor.RGB = RGB(15, 0, 0)
        .Fill.Transparency = 0
        .Fill.Solid
        .Size = 12
        .name = "Bahnschrift"
        .NameOther = "Webdings"
    End With
End Sub
