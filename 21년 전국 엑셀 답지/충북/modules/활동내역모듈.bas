Attribute VB_Name = "Ȱ���������"
Dim dic��ǰ As Object, dicȸ�� As Object
Dim act(500) As Activity
Dim k As Integer ''�迭�� �ε���
Dim h As Integer ''�˻� ����


Sub Ȱ�������ʱ�ȭ()
    
    k = 0: h = 0
    
    Application.ScreenUpdating = False
    
    Call init
    Call Ȱ�����������
    Call Ȱ����������
    Call Ȱ����������
    Call Ȱ������������
    
    Application.ScreenUpdating = True
End Sub
Function sdate()
    sdate = CDate(IIf(Sheet11.TextBox1.Text = "", Sheet13.Range("U1").Value, Sheet11.TextBox1))
End Function
Function ldate()
    ldate = CDate(IIf(Sheet11.TextBox2.Text = "", Sheet13.Range("V1").Value, Sheet11.TextBox2))
End Function

Sub init()
    Erase act
    Set dicȸ�� = mkdic(Sheet1.Range("I2"))
    Set dic��ǰ = mkdic(Sheet5.Range("A2"))
    
    
    With Sheet6
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("F" & i).Value <> "" And .Range("C" & i) = [��ȣ] Then
                v = .Range("G" & i).Value
                Set act(k) = New Activity
                
                title = dic��ǰ(v).Cells(1, 4) & " - " & dic��ǰ(v).Cells(1, 3)
                rdate = Format(.Range("B" & i), "yyyy-MM-dd HH:mm:ss")
                deal = "ī�� ����"
                amount = "- " & Format(.Range("H" & i), "#,##0��")
                pay = "Chase Total Checking Account" & vbCrLf & "�ŷ�������" & """""Chase*" & dic��ǰ(v).Cells(1, 4) & " - " & dic��ǰ(v).Cells(1, 4) & "(��)��" & vbCrLf & "ǥ�õ˴ϴ�"
                detail = dic��ǰ(v).Cells(1, 4) & " - " & dic��ǰ(v).Cells(1, 3) & Chr(13) & "�ŷ� ���� : ī�� ����" & Chr(13) & "�ŷ� �Ͻ� : " & Format(.Range("B" & i), "yyyy-MM-dd AMPM HH:mm:ss")
                delivery = [�̸�] & Chr(13) & Replace([�ּ�], " ", "," & Chr(13))
                
                Call act(k).init(title, rdate, deal, amount, pay, detail, delivery)
            End If
                
            If .Range("F" & i).Value = "" And (.Range("D" & i) = [����] Or .Range("E" & i) = [����]) Then
                Set act(k) = New Activity
                
                title = IIf(.Range("C" & i) = [��ȣ], dicȸ��(.Range("E" & i).Value).Cells(1, -4), dicȸ��(.Range("D" & i).Value).Cells(1, -4)) & IIf(.Range("C" & i) = [��ȣ], "���� �Ա�", "���� �Ա�")
                rdate = Format(.Range("B" & i), "yyyy-MM-dd HH:mm:ss")
                deal = "���°ŷ�"
                amount = IIf(.Range("C" & i) = [��ȣ], "-", "+") & Format(.Range("H" & i), "###,0��")
                pay = "Chase Total Checking Account" & vbCrLf & "�ŷ�������" & """""Chase*" & IIf(.Range("C" & i) = [��ȣ], dicȸ��(.Range("E" & i).Value).Cells(1, -4), dicȸ��(.Range("D" & i).Value).Cells(1, -4)) & IIf(.Range("C" & i) = [��ȣ], "���� �Ա�", "���� �Ա�") & "(��)��" & vbCrLf & "ǥ�õ˴ϴ�."
                detail = IIf(.Range("C" & i) = [��ȣ], dicȸ��(.Range("E" & i).Value).Cells(1, -4), dicȸ��(.Range("D" & i).Value).Cells(1, -4)) & IIf(.Range("C" & i) = [��ȣ], "���� �Ա�", "���� �Ա�") & Chr(13) & "�ŷ� ���� : ���� ����" & Chr(13) & "�ŷ� �Ͻ� : " & Format(.Range("B" & i), "yyyy-MM-dd AMPM HH:mm:ss")
                delivery = [�̸�] & Chr(13) & Replace([�ּ�], " ", "," & Chr(13))
                Call act(k).init(title, rdate, deal, amount, pay, detail, delivery)
                k = k + 1
            End If
        Next
    End With
End Sub


Sub Ȱ�����������()
    On Error GoTo e
    Sheet11.Shapes("������").Delete
e:
    For Each shp In Sheet11.Shapes
        If shp.Name Like "*Ȱ��*" Then
            shp.Delete
        End If
    Next


End Sub

Sub Ȱ����������()
    For i = 0 To k - 1
        For J = i + 1 To k - 1
            If act(i).rdate < act(J).rdate Then
                Set tmp = act(J)
                Set act(J) = act(i)
                Set act(i) = tmp
            End If
        Next
    Next
End Sub

Sub Ȱ����������()
    h = 0
    For i = 0 To k - 1
         If CDbl(CDate(act(i).rdate)) >= CDbl(sdate) And CDbl(CDate(act(i).rdate)) <= CDbl(ldate) And act(i).title Like "*" & Sheet11.TextBox3.Value & "*" Then
            Sheet13.Shapes("Ȱ�� 1").Copy
            Sheet11.Range("B" & 10 + (8 * h)).Select
            Sheet11.Paste
            h = h + 1
           
        End If
    Next
    
    i = 1
    For Each shp In Sheet11.Shapes
        If shp.Name Like "*Ȱ��*" Then
            shp.Name = "Ȱ�� " & i
            shp.GroupItems("�󼼺���").Name = "���� " & i
            i = i + 1
        End If
    Next
End Sub

Sub Ȱ������������()
    
    idx = 1
    With Sheet11
        For i = 0 To k - 1
            If act(i).rdate >= CDate(sdate) And act(i).rdate <= CDate(ldate) And act(i).title Like "*" & .TextBox3.Value & "*" Then
                .Shapes("Ȱ�� " & idx).GroupItems("����").TextFrame.Characters.Text = act(i).title
                .Shapes("Ȱ�� " & idx).GroupItems("��¥").TextFrame.Characters.Text = Format(act(i).rdate, "mmm, d, HH:mm:ss")
                .Shapes("Ȱ�� " & idx).GroupItems("�ŷ�").TextFrame.Characters.Text = act(i).deal
                .Shapes("Ȱ�� " & idx).GroupItems("�ݾ�").TextFrame.Characters.Text = act(i).amount
                .Shapes("Ȱ�� " & idx).GroupItems("���� " & idx).OnAction = "'info """ & i & """'"
                idx = idx + 1
            End If
        Next

    End With

End Sub

Sub info(i)
    Application.ScreenUpdating = False
    
    Call Ȱ�����������
    Sheet13.Shapes("������").Copy
    Sheet11.Range("B10").Select
    Sheet11.Paste
    
    With Sheet11.Shapes("������")
        .GroupItems("����").TextFrame.Characters.Text = act(i).title
        .GroupItems("��¥").TextFrame.Characters.Text = Format(act(i).rdate, "mmm, d, HH:mm:ss")
        .GroupItems("�ŷ�").TextFrame.Characters.Text = act(i).deal
        .GroupItems("�ݾ�").TextFrame.Characters.Text = act(i).amount
        .GroupItems("��������").TextFrame.Characters.Text = act(i).pay
        .GroupItems("������").TextFrame.Characters.Text = act(i).detail
        .GroupItems("�������").TextFrame.Characters.Text = act(i).delivery
    End With
    
    Application.ScreenUpdating = True
End Sub

Sub ����������ϱ�()
    iMsg "�������� ����Ͽ����ϴ�!"
    Sheet11.Range("A10:P47").ExportAsFixedFormat xlTypePDF, ThisWorkbook.Path & "\������.pdf"
End Sub



