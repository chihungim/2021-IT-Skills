Attribute VB_Name = "Module1"
Public 사전 As Object
Public irow
Public arr As Object
Public calData(42)
Public pNo

Function mkdic(시작셀 As Range) As Object
    Set dic = CreateObject("Scripting.Dictionary")

    Set 끝셀 = 시작셀.Cells(10000, 1).End(xlUp)
    
    For Each 셀 In Range(시작셀, 끝셀)
        If Not dic.exists(셀.Value) Then dic.Add 셀.Value, 셀
    Next
    
    Set mkdic = dic
    
End Function


Sub 사전생성(시작셀 As Range)
    Set 사전 = CreateObject("Scripting.Dictionary")

    Set 끝셀 = 시작셀.Cells(10000, 1).End(xlUp)
    
    For Each 셀 In Range(시작셀, 끝셀)
        If Not 사전.exists(셀.Value) Then 사전.Add 셀.Value, 셀
    Next
End Sub
Sub 차트()
    With Sheet7
        .ChartObjects("차트 1").Visible = IIf(.Range("C1") = 1, True, False)
        .ChartObjects("차트 2").Visible = IIf(.Range("C1") = 2, True, False)
    End With
End Sub

Function fn최고가작품(회원번호 As Integer) As String
    
    Set 회원사전 = mkdic(Sheet2.Range("A2"))
    Set 작품사전 = mkdic(Sheet3.Range("A2"))
    
    Dim max: max = 0
    Dim 작품명
    
    If 회원사전(회원번호).Cells(1, 6) = "일반" Then
        For i = 2 To Sheet6.Range("A1000").End(xlUp).Row
            If Sheet6.Range("C" & i).Value = 회원번호 Then
                If max < Sheet6.Range("D" & i).Value Then
                    max = Sheet6.Range("D" & i).Value
                    작품명 = Sheet6.Range("E" & i).Value
                End If
            End If
        Next
    
        fn최고가작품 = IIf(max = 0, "", 작품명)
    Else
        For i = 2 To Sheet6.Range("A1000").End(xlUp).Row
            If Sheet6.Range("F" & i) = 회원번호 Then
                If max < Sheet6.Range("D" & i).Value Then
                    max = Sheet6.Range("D" & i).Value
                    작품명 = Sheet6.Range("E" & i).Value
                End If
            End If
        Next
        
        fn최고가작품 = IIf(max = 0, "", 작품명)
    End If
End Function


Sub eMsg(msg As String)
    MsgBox msg, vbCritical + vbOKOnly, "경고"
End Sub


Sub iMsg(msg As String)
    MsgBox msg, vbInformation + vbOKOnly, "정보"
End Sub

Sub stateChange()
    
    Call mkarr(Sheet3.Range("D2"), arr, [이름], 0)
    
    If [분류] = "일반" Then
        Sheet1.Range("N4") = "이름 : " & [이름]
    Else
        Sheet1.Range("N4") = "이름 : " & [이름] & vbLf & "작품 수 : " & arr.Count & "개" & vbLf & "평점 : " & Format(fn평균([이름]), "0.0") & "점"
    End If
    
End Sub


Sub mkarr(rg, obj, key, off)

    Set obj = New ArrayList
    
    For Each C In Range(rg, rg.Cells(10000, 1).End(3))
        If key = "" Or C = key Then
            obj.Add C.Cells(1, off).Value
        End If
    Next

End Sub

Function fn평균(name As String)

    With Sheet4
        For i = 2 To .Range("F10000").End(3).Row
            If .Range("F" & i) = name Then
                tot = tot + .Range("D" & i)
                sel = sel + 1
            End If
        Next
    End With
    
    If sel = 0 Then
        avg = 0
    Else
        avg = tot / sel
    End If
    fn평균 = avg

End Function

Sub edit()
    수정.Show
End Sub

Sub 삭제()
    ''에러500배
    MsgBox "ㅇㅇ"
    
End Sub
