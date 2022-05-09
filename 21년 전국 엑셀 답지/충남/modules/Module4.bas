Attribute VB_Name = "Module4"
Sub 매크로8()
Attribute 매크로8.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로8 매크로
'

'
    ActiveSheet.Shapes.Range(Array("예약여부")).Select
End Sub
Sub 매크로9()
Attribute 매크로9.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로9 매크로
'

'
    Selection.ShapeRange(1).TextFrame2.TextRange.Characters.Text = "? 도서예약불가"
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
