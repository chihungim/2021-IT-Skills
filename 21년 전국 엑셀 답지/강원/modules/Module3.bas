Attribute VB_Name = "Module3"
Sub 매크로3()
Attribute 매크로3.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로3 매크로
'

'
    With Selection.ShapeRange.Fill
        .Visible = msoTrue
        .ForeColor.ObjectThemeColor = msoThemeColorAccent5
        .ForeColor.TintAndShade = 0
        .ForeColor.Brightness = 0
        .Transparency = 0
        .Solid
    End With
End Sub

Function unformat(txt)
    unformat = Format(Left(txt, Len(txt) - 1), "#")
End Function

Function rei(txt)
    rei = Format(txt, "#")
End Function

Function decF(txt)
    decF = Format(txt, "#,##0")
End Function

Function percent(txt)
    If Val(txt) = 0 Then
        percent = 1
    Else
        percent = Val(txt) / 100
    End If
    
End Function

Sub testtetssttest()
    [번호].Cells(1, 2).Select
End Sub
