Attribute VB_Name = "Module1"
Public ���� As Object
Public irow
Public arr As Object
Public calData(42)
Public pNo

Function mkdic(���ۼ� As Range) As Object
    Set dic = CreateObject("Scripting.Dictionary")

    Set ���� = ���ۼ�.Cells(10000, 1).End(xlUp)
    
    For Each �� In Range(���ۼ�, ����)
        If Not dic.exists(��.Value) Then dic.Add ��.Value, ��
    Next
    
    Set mkdic = dic
    
End Function


Sub ��������(���ۼ� As Range)
    Set ���� = CreateObject("Scripting.Dictionary")

    Set ���� = ���ۼ�.Cells(10000, 1).End(xlUp)
    
    For Each �� In Range(���ۼ�, ����)
        If Not ����.exists(��.Value) Then ����.Add ��.Value, ��
    Next
End Sub
Sub ��Ʈ()
    With Sheet7
        .ChartObjects("��Ʈ 1").Visible = IIf(.Range("C1") = 1, True, False)
        .ChartObjects("��Ʈ 2").Visible = IIf(.Range("C1") = 2, True, False)
    End With
End Sub

Function fn�ְ���ǰ(ȸ����ȣ As Integer) As String
    
    Set ȸ������ = mkdic(Sheet2.Range("A2"))
    Set ��ǰ���� = mkdic(Sheet3.Range("A2"))
    
    Dim max: max = 0
    Dim ��ǰ��
    
    If ȸ������(ȸ����ȣ).Cells(1, 6) = "�Ϲ�" Then
        For i = 2 To Sheet6.Range("A1000").End(xlUp).Row
            If Sheet6.Range("C" & i).Value = ȸ����ȣ Then
                If max < Sheet6.Range("D" & i).Value Then
                    max = Sheet6.Range("D" & i).Value
                    ��ǰ�� = Sheet6.Range("E" & i).Value
                End If
            End If
        Next
    
        fn�ְ���ǰ = IIf(max = 0, "", ��ǰ��)
    Else
        For i = 2 To Sheet6.Range("A1000").End(xlUp).Row
            If Sheet6.Range("F" & i) = ȸ����ȣ Then
                If max < Sheet6.Range("D" & i).Value Then
                    max = Sheet6.Range("D" & i).Value
                    ��ǰ�� = Sheet6.Range("E" & i).Value
                End If
            End If
        Next
        
        fn�ְ���ǰ = IIf(max = 0, "", ��ǰ��)
    End If
End Function


Sub eMsg(msg As String)
    MsgBox msg, vbCritical + vbOKOnly, "���"
End Sub


Sub iMsg(msg As String)
    MsgBox msg, vbInformation + vbOKOnly, "����"
End Sub

Sub stateChange()
    
    Call mkarr(Sheet3.Range("D2"), arr, [�̸�], 0)
    
    If [�з�] = "�Ϲ�" Then
        Sheet1.Range("N4") = "�̸� : " & [�̸�]
    Else
        Sheet1.Range("N4") = "�̸� : " & [�̸�] & vbLf & "��ǰ �� : " & arr.Count & "��" & vbLf & "���� : " & Format(fn���([�̸�]), "0.0") & "��"
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

Function fn���(name As String)

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
    fn��� = avg

End Function

Sub edit()
    ����.Show
End Sub

Sub ����()
    ''����500��
    MsgBox "����"
    
End Sub
