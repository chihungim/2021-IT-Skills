Attribute VB_Name = "��Ʈ���"


Sub �����߱�()
    mkdic dic����, Sheet9.Range("A2")
    Sheet10.Select
    Sheet10.Range("A1:BB7").Clear
    cnt = 0
    For Each k In dic����
        If dic����(k).Cells(1, 2) = [��ȣ] And dic����(k).Cells(1, 7) = "X" Then
            If dic����(k).Cells(1, 6) Like "*�ѱݾ�*" Then
                cName = "�ѱݾ� "
            ElseIf dic����(k).Cells(1, 6) Like "*����*" Then
                cName = "���� "
            ElseIf dic����(k).Cells(1, 6) Like "*û�ҳ�*" Then
                cName = "û�ҳ� "
            Else
                cName = "��� "
            End If
            cName = cName & 100 * Split(dic����(k).Cells(1, 6), " = ")(1) & "% ��������"
            With Sheet10
                .Range("�������").Copy
                .Cells(2, 2 + (6 * cnt)).PasteSpecial
                .Cells(3, 4 + (6 * cnt)) = [���̵�]
                .Cells(4, 4 + (6 * cnt)) = dic����(k).Cells(1, 3)
                .Cells(5, 4 + (6 * cnt)) = cName
                .Cells(6, 4 + (6 * cnt)) = Format(dic����(k).Cells(1, 5), "yyyy�� MM�� dd�� ����")
                cnt = cnt + 1
            End With
        End If
    Next
    Sheet10.Range("A1").Select
End Sub

Sub ����������(cate)
    With ��������
        .ComboBox2.ListIndex = cate
        .Show
    End With
    
End Sub

Sub ��������(r)
    If MsgBox("������ ���� �����Ͻðڽ��ϱ�?", vbInformation + vbYesNo, "����") = vbYes Then
        Sheet9.Rows(r & ":" & r).Delete
    End If
    
End Sub

Sub ������Height()
    For i = 2 To Range("A10000").End(3).row
        Rows(i & ":" & i).EntireRow.AutoFit
        Rows(i & ":" & i).RowHeight = Rows(i & ":" & i).RowHeight + 10
    Next
End Sub

Function fn���(id As Range)
    ����Ƚ�� = id.Cells(1, 9)
    ����ݾ� = id.Cells(1, 10)

    With Sheet2
        For k = 5 To 2 Step -1
            Set c = .Cells(1, k)
            
            ��� = 1
            For i = 5 To 9
                If c.Cells(i, 1) = "" Then Exit For
                If Mid(c.Cells(i, 1), 6, 2) = "Ƚ��" Then
                    ��� = ��� * Evaluate(����Ƚ�� & Split(c.Cells(i, 1), "]")(1))
                Else
                    ��� = ��� * Evaluate(����ݾ� & Split(c.Cells(i, 1), "]")(1))
                End If
            Next
            
            
            If ��� Then
                Debug.Print ���
                fn��� = c.Value
                Exit Function
            End If
        Next
    End With
    
    fn��� = "�Ϲ�"
End Function
Function email(rg)
    If InStr(rg, "@") = 0 Then email = False: Exit Function
    
    id = Split(rg, "@")(0)
    domain = Split(rg, "@")(1)
    
    l = Left(id, 1)
    
    chk = True
    
    If Not ((l >= "a" And l <= "Z") Or (l >= "a" And l <= "z")) Then
        chk = False
    End If
    
    For i = 1 To Len(id)
        If Not ((Mid(id, i, 1) >= "A" And Mid(id, i, 1) <= "Z") Or (Mid(id, i, 1) >= "a" And Mid(id, i, 1) <= "z") Or (Mid(id, i, 1) >= "0" And Mid(id, i, 1) <= "9")) Then
            chk = False
            Exit For
        End If
    Next
    
    If Len(id) < 3 Then email = False: Exit Function
    
    If InStr(domain, ".") = 0 Then email = False: Exit Function
    
    For i = 1 To Len(domain)
        If Not ((Mid(domain, i, 1) >= "A" And Mid(domain, i, 1) <= "Z") Or (Mid(domain, i, 1) >= "a" And Mid(domain, i, 1) <= "z") Or (Mid(domain, i, 1) >= "0" And Mid(domain, i, 1) <= "9") Or (Mid(domain, i, 1) = ".")) Then
            Debug.Print Mid(id, i, 1)
            chk = False
            Exit For
        End If
    Next
    
    If Len(Split(domain, ".")(1)) < 2 Then email = False: Exit Function
    
    email = chk

End Function


Sub tesett()
    
    iMsg email("rlsmd2@skills.com")
    
End Sub
