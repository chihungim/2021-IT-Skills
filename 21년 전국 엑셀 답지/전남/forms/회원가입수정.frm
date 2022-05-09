VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ȸ�����Լ��� 
   Caption         =   "����"
   ClientHeight    =   6585
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6000
   OleObjectBlob   =   "ȸ�����Լ���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ȸ�����Լ���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim chk, chk2, chk3
Public mode
Dim fpath

Private Sub CommandButton1_Click()
    If TextBox2.Text = "" Then eMsg "���̵� �Է����ּ���.": Exit Sub
    If dicȸ��.exists(TextBox2.Text) Then eMsg "���̵� �ߺ��Ǿ����ϴ�.": Exit Sub
    iMsg "��� ���� �� ���̵� �Դϴ�.": chk2 = True
End Sub

Private Sub CommandButton2_Click()
    �����ȣã��.Show
End Sub

Private Sub CommandButton3_Click()
    If chk2 = False Then eMsg "�ߺ�Ȯ���� ���ּ���.": Exit Sub
    
    
    For i = 1 To 7
        If i = 4 Then
            If Me.Controls("TextBox" & i) = "YYYY-MM-DD" Then
                eMsg "��ĭ�� �Է����ּ���."
                Exit Sub
            End If
        ElseIf i = 7 Then
            If Me.Controls("TextBox" & i) = "���ּ�2" Then
                eMsg "��ĭ�� �Է����ּ���."
                Exit Sub
            End If
        Else
            If Me.Controls("TextBox" & i) = "" Then
                eMsg "��ĭ�� �Է����ּ���."
                Exit Sub
            End If
        End If
    Next

    If CDate(TextBox4.Text) > Now() Then eMsg "������� ������ ��ġ���� �ʽ��ϴ�.": Exit Sub
    If Not TextBox3 Like "*[A-Z][a-z][a-z][a-z][0-9][?!@#$]" Then eMsg "��й�ȣ ������ ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    If CommandButton3.Caption = "ȸ������" Then
        irow = Sheet2.Range("A10000").End(xlUp).row
        Sheet2.Range("A" & irow & ":H" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, TextBox4, IIf(OptionButton1 = True, "��", "��"), TextBox5, TextBox7)
        If chk Then SavePicture Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & irow - 1 & ".jpg"
        iMsg "ȸ�������� �Ϸ�Ǿ����ϴ�."
    Else
        mkDic dicȸ��, Sheet2.Range("A2")
        
        Set ����row = dicȸ��([��ȣ].Value)
        
        With ����row
            .Cells(1, 3) = TextBox2
            .Cells(1, 4) = TextBox3
            .Cells(1, 7) = TextBox5
            .Cells(1, 8) = TextBox7
        End With
        
        If chk Then SavePicture Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & [��ȣ] & ".jpg"
        If chk3 Then Kill ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & [��ȣ] & ".jpg"
        iMsg "���������� �Ϸ�Ǿ����ϴ�."
    End If
    
    Unload Me
End Sub

Private Sub Image1_DblClick(ByVal Cancel As MSForms.ReturnBoolean)
    fpath = ""
    With Application.FileDialog(msoFileDialogFilePicker)
        .Show
        .Filters.Clear
        .Filters.Add "�̹���", "*.jpg"
        If .SelectedItems.Count > 0 Then fpath = .SelectedItems(1)
    End With
    If fpath Like "*[0.jpg]*" Then chk3 = True
    If (IsEmpty(fpath)) Then Exit Sub
    If Dir(fpath) <> "" Then Image1.Picture = LoadPicture(fpath): chk = True: Repaint
End Sub

Private Sub TextBox7_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If TextBox7.Text = "" Then TextBox7.Text = "���ּ�2"
End Sub

Private Sub TextBox7_MouseUp(ByVal Button As Integer, ByVal Shift As Integer, ByVal x As Single, ByVal y As Single)
    If TextBox7.Text = "���ּ�2" Then TextBox7.Text = ""
End Sub

Private Sub TextBox4_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If Not TextBox4.Text Like "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]" Then eMsg "��¥ ������ �ƴմϴ�.": TextBox4.SetFocus: Exit Sub
    If TextBox4.Text = "" Then TextBox4.Text = "YYYY-MM-DD": Exit Sub
End Sub

Private Sub TextBox4_MouseUp(ByVal Button As Integer, ByVal Shift As Integer, ByVal x As Single, ByVal y As Single)
    If TextBox4.Text = "YYYY-MM-DD" Then TextBox4.Text = ""
End Sub


Private Sub UserForm_Activate()
    mkDic dic����, Sheet3.Range("B2")
    mkDic dicȸ��, Sheet2.Range("C2")
    
    TextBox6.Enabled = False
    If mode = "ȸ������" Then
        Me.Caption = "ȸ������"
    Else
        Me.Caption = "��������"
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & [��ȣ] & ".jpg")
        TextBox1.Enabled = False
        TextBox4.Enabled = False
        TextBox1.Text = [�̸�]
        TextBox2.Text = [���̵�]
        TextBox3.Text = [���]
        TextBox4.Text = Format([�������], "yyyy-MM-dd")
        TextBox5.Text = [�����ȣ].Value
        TextBox7.Text = [���ּ�]
        If [����] = "��" Then
            OptionButton1 = True
        Else
            OptionButton2 = True
        End If

        Dim ���� As Range
        n = [�����ȣ].Value
        Set ���� = dic����(Val(n))
        TextBox6.Text = ����.Cells(1, 2) & " " & ����.Cells(1, 3) & " " & ����.Cells(1, 4) & " " & ����.Cells(1, 5)
    End If
    
    CommandButton3.Caption = Me.Caption
End Sub

