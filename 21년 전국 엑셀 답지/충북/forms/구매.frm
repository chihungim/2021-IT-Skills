VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���� 
   Caption         =   "����"
   ClientHeight    =   5745
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9600
   OleObjectBlob   =   "����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public dic��ǰ As Object
Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox3_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton1_Click()
    search
End Sub

Private Sub CommandButton2_Click()

    If ComboBox2 = "" Then eMsg "ī�带 �����ؾ� �մϴ�.": Exit Sub
    
    If ComboBox3 = "" Then eMsg "�Һο��θ� �����ؾ� �մϴ�.": Exit Sub
    
    pw = ""
    ��й�ȣ.Show
    
    If Not ��й�ȣflag Then eMsg "2�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    ���� = Int(ListBox1.list(ListBox1.ListIndex, 1))
    
    mkdic dic, Sheet4.Range("A2")
    If Int(dic(ComboBox2.Value).Cells(1, 5)) > ���� Then
        If MsgBox("����Ʈ�� ���Ű� �����մϴ�." & vbCrLf & "����Ʈ�� �����Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
            With Sheet6
                lrow = .Range("A1000").End(3).Row + 1
                .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, Now, [��ȣ], "", "", ComboBox2, dic��ǰ(ListBox1.list(ListBox1.ListIndex)).Cells(1, -1), ����, 0, 0)
                dic(ComboBox2.Value).Cells(1, 5) = Int(dic(ComboBox2.Value).Cells(1, 5)) - ����
                iMsg "������ �Ϸ�Ǿ����ϴ�."
                Exit Sub
            End With
        End If
    End If
    
    
    If ListBox1.list(ListBox1.ListIndex, 2) = "��" Then
        ������ = Split(Sheet3.Cells(1 + dic(ComboBox2.Value).Cells(1, 3), 9 + Sheet2.Range("H" & dic(ComboBox2.Value).Cells(1, 2) + 1)), "/")(1)
        ���� = ���� + (���� * Split(������, "%")(0) / 100)
    End If
    
    If ComboBox3.ListIndex <> 0 Then
        �Һ� = Sheet2.Range("F" & dic(ComboBox2.Value).Cells(1, 2) + 1)
        ���� = ���� + Evaluate(���� * �Һ�)
    End If
    
    ���� = Split(Sheet2.Range("E" & dic(ComboBox2.Value).Cells(1, 2) + 1), ",")
    For Each i In ����
        If Sheet3.Range("F" & i) >= �������� Then
            If Sheet3.Range("B" & i) = "����" Then
                If InStr(Sheet3.Range("D" & i), ComboBox1) > 0 Then
                    ���� = ���� - Evaluate(���� * Sheet3.Range("E" & i))
                End If
            Else
                If InStr(Sheet3.Range("D" & i), ComboBox1) > 0 Then
                    ���� = Evaluate(���� * Sheet3.Range("E" & i))
                End If
            End If
        End If
    Next
    
    
    
    If MsgBox(Format(����, "#,##0��") & "�Դϴ�." & vbCrLf & "�����Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
        With Sheet6
            lrow = .Range("A1000").End(3).Row + 1
            .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, Now, [��ȣ], "", "", ComboBox2, dic��ǰ(ListBox1.list(ListBox1.ListIndex)).Cells(1, -1), ����, 0, Val(ComboBox3))
            dic(ComboBox2.Value).Cells(1, 5) = Int(dic(ComboBox2.Value).Cells(1, 5)) + ����
            iMsg "������ �Ϸ�Ǿ����ϴ�."
        End With
    Else
        Exit Sub
    End If
    
    Unload Me
End Sub

Function ��������()
        
    m = 0
    For J = 2 To Sheet6.Range("A1000").End(3).Row
        If Sheet6.Range("F" & J) = Sheet4.Range("A" & J) And Month(Sheet6.Range("B" & J)) = Month(Date) - 1 Then
            m = m + Int(Sheet6.Range("H" & J))
        End If
    Next
    �������� = m
End Function

Sub ����()
    
    With Sheet6
        lrow = .Range("A1000").End(3).Row + 1
        mkdic dic, Sheet5.Range("C2")
        .Range("A" & lrow & ":J" & lrow) = Array()
    End With
    
End Sub

Private Sub ListBox1_Click()

End Sub

Private Sub UserForm_Click()

End Sub

Sub search()
    
    With Sheet5
        .Range("I2") = ComboBox1
        .Range("J2") = TextBox1
        
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("I1:J2"), .Range("L1:O1")
        For i = 2 To .Range("O1000").End(3).Row
            .Range("O" & i) = IIf(.Range("O" & i) = 0, "�ƴϿ�", "��")
        Next
        
        ListBox1.RowSource = "��ǰ���!M2:O" & .Range("O1000").End(3).Row
        
    End With
    
End Sub

Private Sub UserForm_Initialize()
    
    Set dic��ǰ = mkdic(Sheet5.Range("C2"))
    ��ǰ = Array("�ܽ�", "����", "����", "����")
    For Each i In ��ǰ
        ComboBox1.AddItem i
    Next
    ComboBox1.ListIndex = 0
    
    For i = 2 To Sheet4.Range("A1000").End(3).Row
        If Sheet4.Range("D" & i) = [��ȣ] Then
            ComboBox2.AddItem Sheet4.Range("A" & i)
        End If
    Next
    
    
    ComboBox3.AddItem "�Ͻú�"
    For i = 1 To 12
        ComboBox3.AddItem i & "����"
    Next
    
    search
    
End Sub

