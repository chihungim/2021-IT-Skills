Attribute VB_Name = "Module1"
Public dic회원 As Object, dic축제 As Object, dic패키지 As Object, dic예약 As Object, dic리뷰 As Object, dic쿠폰 As Object, dic관광지 As Object

Sub mkdic(obj As Object, sc As Range)
    
    Set obj = CreateObject("Scripting.Dictionary")
    Set lc = sc.Cells(10000, 1).End(3)
    
    For Each cell In Range(sc, lc)
        If Not obj.exists(cell.Value) Then obj.Add cell.Value, cell
    Next
    
End Sub
Sub mkdic2(rg, obj, key, off)

    Set obj = CreateObject("Scripting.Dictionary")
    
    For Each c In Range(rg, rg.Cells(10000, 1).End(3))
        If key = "" Or key = c Then
            obj.Add c.Cells(1, off).Value, c.Cells(1, off)
        End If
    Next

End Sub
Sub mkdic3(rg, obj)
    Set obj = CreateObject("Scripting.Dictionary")
    
    For Each c In rg
        If c = "" Then Exit For
        obj.Add c.Value, c
    Next
End Sub

Sub mkValuelist(obj As Object, sc As Range)
    Set obj = New ArrayList
    Set lc = sc.Cells(10000, 1).End(3)
    
    For Each cell In Range(sc, lc)
        If Not obj.Contains(cell.Value) Then obj.Add cell.Value
    Next
    
End Sub

Sub mklist(obj As Object, sc As Range)
    Set obj = New ArrayList
    Set lc = sc.Cells(10000, 1).End(3)
    
    For Each cell In Range(sc, lc)
        obj.Add cell
    Next
    
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "확인"
End Sub
Sub eMsg(msg)
    MsgBox msg, vbCritical, "오류"
End Sub

Sub 매크로1()
Attribute 매크로1.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로1 매크로
'

'
    Sheet1.Shapes("shp_1").Select
    With Selection.ShapeRange.Fill
        .Visible = msoTrue
        .ForeColor.ObjectThemeColor = msoThemeColorText2
        .ForeColor.TintAndShade = 0
        .ForeColor.Brightness = -0.25
        .Transparency = 0
        .Solid
    End With
End Sub
Sub 매크로2()
Attribute 매크로2.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로2 매크로
'

'
    With Selection.ShapeRange.Fill
        .Visible = msoTrue
        .ForeColor.ObjectThemeColor = msoThemeColorAccent4
        .ForeColor.TintAndShade = 0
        .ForeColor.Brightness = 0
        .Transparency = 0
        .Solid
    End With
End Sub
 
Function fn유효성(rg)
    a = Trim(Mid(rg, 9))
    For i = 1 To Len(a)
        If Mid(a, i, 1) = "" Or IsNumeric(Mid(a, i, 1)) Then
            MsgBox Trim(Mid(a, 1, i - 1))
            Exit For
        End If
    Next
    MsgBox a
End Function

Sub 금액구하기()
    
    With Sheet6
        
        For i = 2 To .Range("A1000").End(3).row
            
            축제 = Split(Replace(.Range("C" & i), " ", ""), ",") 'E 5
            관광지 = Split(Replace(.Range("D" & i), " ", ""), ",") 'D 3
            
            m = 0
            For Each k In 축제
               m = m + Sheet5.Range("E" & k + 1)
            Next
            For Each k In 관광지
                m = m + Sheet3.Range("D" & k + 1)
            Next
            
            cnt = UBound(관광지) + 1
            
            dc = 1
            If cnt >= 5 Then
                dc = 0.75
            ElseIf cnt >= 3 Then
                dc = 0.9
            ElseIf cnt >= 2 Then
                dc = 0.95
            End If
            
            .Range("E" & i) = m * dc
            
        Next
    End With
    
End Sub
