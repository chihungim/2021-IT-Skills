VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ȸ�����Լ��� 
   Caption         =   "ȸ������"
   ClientHeight    =   5760
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5985
   OleObjectBlob   =   "ȸ�����Լ���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ȸ�����Լ���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim max As Integer, chk As Boolean

Private Sub ComboBox1_Change()
    TextBox7.Visible = IIf(ComboBox1.ListIndex = ComboBox1.ListCount - 1, True, False)
    max = IIf(ComboBox1.ListIndex = ComboBox1.ListIndex - 1, 6, 5)
End Sub

Private Sub CommandButton1_Click()
    createRandomCharacters
End Sub

Sub createRandomCharacters()
    max = 0
    Randomize
    
    Ȯ�ι��� = ""
    
    For i = 0 To 5
        ch = Int(Rnd * 2)
        max = max + ch
        
        If ch = 1 Then
            Ȯ�ι��� = Ȯ�ι��� + Chr(Int(Rnd * 26) + 65)
        Else
            Ȯ�ι��� = Ȯ�ι��� & Int(Rnd * 10)
        End If
    Next
    
    If max <> 4 Then createRandomCharacters
    
End Sub

Private Sub CommandButton2_Click()
    For i = 1 To max
        If Me.Controls("TextBox" & i) = "" Then
            eMsg "��ĭ�� �ֽ��ϴ�."
            Exit Sub
        End If
    Next
    
    Call ��������(Sheet2.Range("C2"))
    
    If ����.exists(TextBox1.Value) Then eMsg "�̹��ִ� ���̵��Դϴ�.": Exit Sub
    
    If Not (TextBox3 Like "*[0-9]*" And TextBox3 Like "*[a-zA-Z]*" And TextBox3 Like "*[!0-9a-zA-Z��-�R]*") Or Len(TextBox3) < 6 Then
        eMsg "��й�ȣ�� ����, ������, Ư�����ڸ� �����Ͽ� 6���� �̻����� �Է����ּ���."
        Exit Sub
    End If
    
    If TextBox3 <> TextBox4 Then
        eMsg "��й�ȣ�� ��ġ���� �ʽ��ϴ�."
        Exit Sub
    End If
    
    If OptionButton1 = False And OptionButton2 = False Then
        eMsg "�з��� �������ּ���": Exit Sub
    End If

    
    If Not (LCase(Ȯ�ι���) = LCase(Ȯ�ι���) Or UCase(Ȯ�ι���) = UCase(Ȯ�ι���)) Then
        eMsg "Ȯ�ι��ڸ� �߸��Է��ϼ̽��ϴ�."
        Exit Sub
    End If
    
    If CommandButton2.Caption = "���� �Ϸ�" Then
        iMsg "������ �Ϸ�Ǿ����ϴ�."
        irow = Sheet1.Range("��ȣ").Value + 1
        Sheet2.Range("A" & irow & ":E" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, IIf(max = 5, TextBox5 & "@" & ComboBox1, TextBox5 & "@" & TextBox6))
    Else
        iMsg "ȸ�������� �Ϸ�Ǿ����ϴ�."
        irow = Sheet2.Range("A1000").End(xlUp).Row + 1
        Sheet2.Range("A" & irow & ":E" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, IIf(max = 5, TextBox5 & "@" & ComboBox1, TextBox5 & "@" & TextBox6))
    End If
    
    Sheet2.Range("F" & irow) = IIf(OptionButton1 = True, "�Ϲ�", "ȭ��")
    Sheet2.Range("G" & irow).FillDown
    
    If chk Then
        ��ȣ = Sheet1.Range("��ȣ").Value
        Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & ��ȣ & ".jpg")
        With Sheet2.Range("B" & irow).AddComment
            .Shape.Fill.UserPicture ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & ��ȣ & ".jpg"
        End With
    End If
End Sub

Private Sub CommandButton3_Click()
    Unload Me
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("image file", "*.jpg")
        cho = .Show
        If cho <> 0 Then
            chk = True
            pic = .SelectedItems(1)
        Else
            pic = ThisWorkbook.Path & "\�����ڷ�\ȸ������\�⺻.jpg"
        End If
        Image1.Picture = LoadPicture(pic)
        Repaint
    End With
End Sub

Private Sub OptionButton1_Click()

End Sub

Private Sub OptionButton1_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    OptionButton1.ControlTipText = "�Ϲ� ȸ���� ��ǰ ������ �Ұ����մϴ�."
End Sub


Private Sub OptionButton2_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    OptionButton2.ControlTipText = "ȭ�� ȸ���� ��ǰ ����Ģ �򰡰� �Ұ��� �մϴ�."
End Sub

Private Sub UserForm_Initialize()
    createRandomCharacters
    ComboBox1.List = Array("naver.com", "gmail.com", "hanmail.net", "icloude.com", "�����Է�")
    ComboBox1.ListIndex = 0
    TextBox1.SetFocus

    If Sheet1.CommandButton2.Caption = "ȸ������ ����" Then
        Me.Caption = "ȸ������ ����"
        CommandButton2.Caption = "���� �Ϸ�"
        
        ��� = ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & Sheet1.Range("��ȣ").Value & ".jpg"

        If Not Dir(���) = "" Then
            Image1.Picture = LoadPicture(���)
        End If
        TextBox1 = Sheet1.Range("�̸�")
        �������� Sheet2.Range("B2")
        
        With ����(TextBox1.Value)
            TextBox2 = .Cells(1, 2)
            �̸��� = Split(.Cells(1, 4), "@")
            TextBox5 = �̸���(0)
            For i = 0 To ComboBox1.ListCount
                ComboBox1.ListIndex = i
                If �̸���(1) = ComboBox1 Then Exit For
            Next
            Debug.Print .Cells(1, 5)
            OptionButton1 = IIf(.Cells(1, 5) = "�Ϲ�", True, False)
            OptionButton2 = IIf(.Cells(1, 5) = "ȭ��", True, False)
        End With
    
        TextBox1.Enabled = False
        TextBox2.Enabled = False
    End If
End Sub

