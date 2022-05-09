VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �������� 
   Caption         =   "��������"
   ClientHeight    =   10170
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6630
   OleObjectBlob   =   "��������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public ������
Dim ���� As Boolean, �ֹι�ȣȮ�� As Boolean
Dim r, cnt
Dim cls(1000) As cls��������


Private Sub ComboBox1_Change()
    ComboBox2.Clear
    
    If ComboBox1.ListIndex = 0 Then
        ComboBox2.AddItem "����"
    ElseIf ComboBox1.ListIndex = 1 Then
        ComboBox2.AddItem "1��"
        ComboBox2.AddItem "2��"
    Else
        ComboBox2.AddItem "1��"
        ComboBox2.AddItem "2��"
        ComboBox2.AddItem "3��"
    End If
    
End Sub

Private Sub ComboBox2_Change()
    
    �ڰ��� = ComboBox1 & " " & ComboBox2
    For i = 2 To Sheet3.Range("A1000").End(3).Row
        If �ڰ��� = Sheet3.Range("J" & i) Then
            r = i - 1
            Exit For
        End If
    Next
    
    With Sheet4
        .Range("K2") = r
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("K1:K2"), .Range("M1:Q1")
    End With
    
    MultiPage1.Height = 330
    Me.Height = 442
    
    CommandButton2.Top = MultiPage1.Top + MultiPage1.Height + 20
    CommandButton3.Top = MultiPage1.Top + MultiPage1.Height + 20
    
    Call ������������
    
End Sub

Sub ������������()
    Set dic���� = CreateObject("Scripting.Dictionary")
    Set sc = Sheet4.Range("M2")
    Set lc = sc.Cells(10000, 1).End(3)
    
    On Error Resume Next
    For i = 0 To 1000
        Me.Controls.Remove cls(i).frm.Name
        Set cls(i) = Nothing
    Next
    Repaint
    
    For Each cell In Range(sc, lc)
        ��1 = Format(cell.Cells(1, 2), "yyyy-MM-dd")
        ��2 = Format(cell.Cells(1, 3), "h:mm")
        If dic����.exists(��1) Then
            dic����(��1) = dic����(��1) & " " & ��2 & "," & cell.Cells(1, 4)
        Else
            dic����.Add ��1, ��2 & "," & cell.Cells(1, 4)
        End If
    Next
    
    i = 0
    For Each k In dic����
        Debug.Print k
        Set cls(i) = New cls��������
        Call cls(i).init(0 + (i * 100), k, dic����(k))
        i = i + 1
    Next
    
    Frame1.ScrollTop = 0
    Frame1.ScrollHeight = (i + 1) * 120
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton2_Click()
    If MultiPage1.Value = 0 Then Exit Sub
    MultiPage1.Value = MultiPage1 - 1
End Sub

Private Sub CommandButton3_Click()
    
    If MultiPage1.Value = 0 Then
        If ������ = "" Then eMsg "�ð��븦 �������ּ���.": Exit Sub
    
        �������� = Val(Mid(Sheet3.Range("D" & r + 1), 5, 1))
        
        �ð� = Split(������)
        ������ = DateValue(�ð�(0)) - ��������
        
        If �ð�(2) = "����" Then eMsg "�����Ǿ����ϴ�.": Exit Sub
        
        If Date > ������ Then eMsg "�����Ǿ����ϴ�.": Exit Sub
        
        If r = 2 Then
            If [��Ȱ2��] = 0 Then eMsg "�����Ͻ� �� ���� ����Դϴ�.": Exit Sub
        ElseIf r = 4 Then
            If [��2��] = 0 Then eMsg "�����Ͻ� �� ���� ����Դϴ�.": Exit Sub
        ElseIf r = 5 Then
            If [��3��] = 0 Then eMsg "�����Ͻ� �� ���� ����Դϴ�.": Exit Sub
        End If
        
        ���� = True
        MultiPage1.Value = 1
        CommandButton2.Enabled = True
        Label5 = [�̸�]
        Label7 = [��ȭ��ȣ]
        
    ElseIf MultiPage1.Value = 1 Then
        If Not �ֹι�ȣȮ�� Then eMsg "�ֹε�Ϲ�ȣ ���ڸ� Ȯ���� ���ּ���.": Exit Sub
        
        MultiPage1.Value = 2
        
        �ð� = Split(������)
        
        Label10 = ComboBox1 & " " & ComboBox2 & " / " & �ð�(1)
        Label12 = [�̸�]
        Label14 = �ð�(0)
        
        ��ǥ���� = IIf(Sheet3.Range("E" & r + 1) = "����", 1, 0)
        Label16 = DateAdd("d", ��ǥ����, �ð�(0))
        
        Label18 = Format(Sheet3.Range("H" & r + 1), "#,##0")
        Label20 = Format(Round(Sheet3.Range("H" & r + 1) * 0.07 / 100) * 100, "#,##0")
        Label22 = iFormat(vFormat(Label18) + vFormat(Label20))
        
    Else
        iMsg "������ �Ϸ�Ǿ����ϴ�."
        
        �ð� = Split(������)
        With Sheet4
            ������ȣ = 0
            For i = 2 To .Range("M1000").End(3).Row
                Debug.Print .Range("N" & i); �ð�(0); Format(.Range("O" & i), "h:mm"); �ð�(1)
                If DateValue(.Range("N" & i)) = DateValue(�ð�(0)) And Format(.Range("O" & i), "h:mm") = �ð�(1) Then
                    iMsg .Range("M" & i)
                    ������ȣ = .Range("M" & i)
                    Exit For
                End If
            Next
        End With
        
        With Sheet5
            lrow = .Range("A1000").End(3).Row + 1
            .Range("A" & lrow & ":H" & lrow) = Array(lrow - 1, [��ȣ], ������ȣ, Date, �ð�(0), Label16, "", "")
        End With
        
        Unload Me
        ��������.Show
    End If
    
    
End Sub


Private Sub CommandButton4_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("Image", "*.jpg")
        
        If .Show <> 0 Then pt = .SelectedItems(1)
        
        If pt = "" Then
            Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹�������.jpg")
        Else
            Image1.Picture = LoadPicture(pt)
        End If
        Image1.Tag = pt
        
        Repaint
        
    End With
End Sub

Private Sub CommandButton5_Click()
    If TextBox1 = "3���̻� Ʋ�� �� �α׾ƿ��մϴ�." Then eMsg "�ٽ� �Է����ּ���.": Exit Sub
    
    If Format([�������], "yyMMdd") <> TextBox1 Then
         eMsg "�ٽ� �Է����ּ���."
         cnt = cnt + 1
         
         If cnt = 3 Then
            eMsg "�α׾ƿ��Ǿ����ϴ�. �ٽ� �α������ּ���."
            �α׾ƿ�
            Unload Me
            Sheet1.Select
            �α���.Show
            Exit Sub
         End If
         
         Exit Sub
    End If
    
    �ֹι�ȣȮ�� = True
    iMsg "Ȯ�εǾ����ϴ�."
    
End Sub

Private Sub MultiPage1_Change()
    If MultiPage1.Value = 2 Then
        CommandButton3.Caption = "�Ϸ�"
    Else
        CommandButton3.Caption = "����"
    End If
    
End Sub

Private Sub MultiPage1_Click(ByVal Index As Long)
    If Not ���� Then MultiPage1.Value = 0: Exit Sub
End Sub

'Private Sub TextBox1_BeforeUpdate(ByVal Cancel As MSForms.ReturnBoolean)
'    If TextBox1 = "" Then TextBox1 = "3���̻� Ʋ�� �� �α׾ƿ��մϴ�.": TextBox1.ForeColor = &H8000000A
'    Cancel = True
'End Sub

Private Sub TextBox1_Change()

End Sub


Private Sub TextBox1_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    Ű�е�.Show
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    ' 78 330
    ' 223 442
    
    MultiPage1.Height = 78
    Me.Height = 223
    CommandButton2.Top = MultiPage1.Top + MultiPage1.Height + 20
    CommandButton3.Top = MultiPage1.Top + MultiPage1.Height + 20
    MultiPage1.Value = 0
    
    ComboBox1.list = Split("�������μ��� ��ǻ��Ȱ��ɷ� ��")
    
    ComboBox1.Style = fmStyleDropDownList
    ComboBox2.Style = fmStyleDropDownList
    
    CommandButton2.Enabled = False
    
End Sub
