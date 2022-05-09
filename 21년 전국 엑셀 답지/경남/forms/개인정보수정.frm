VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ������������ 
   Caption         =   "������������"
   ClientHeight    =   7515
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10200
   OleObjectBlob   =   "������������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "������������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
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

Private Sub CommandButton2_Click()
    
    For i = 1 To 7
        If Me.Controls("Textbox" & i) = "" Then eMsg "�� �׸��� �ֽ��ϴ�.": Exit Sub
    Next
    If ComboBox1 = "" Then eMsg "�� �׸��� �ֽ��ϴ�.": Exit Sub
    
    If TextBox2 Like "*[a-zA-Z]*" = False Or TextBox2 Like "*[0-9]*" = False Or TextBox2 Like "*[\!\@\#\$\%\^\&\*]*" = False Or (Len(TextBox2) < 8 And Len(TextBox2) > 12) Then eMsg "��й�ȣ�� �ٽ� �Է����ּ���.": Exit Sub
    
    If TextBox2 <> TextBox3 Then eMsg "��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    If Len(TextBox5) <> 8 Then eMsg "������� ������ �ùٸ��� �ʽ��ϴ�.": Exit Sub
    
    If CDate(Format(TextBox5.Value, "####/##/##")) > Date Then eMsg "������� ������ �ùٸ��� �ʽ��ϴ�.": Exit Sub
    
    If Len(TextBox7) <> 11 Then eMsg "��ȭ��ȣ�� 11�ڸ����� �մϴ�.": Exit Sub
    
    iMsg "ȸ�������� �Ϸ�Ǿ����ϴ�."
    
    With Sheet2
        .Range("B" & ��������r & ":H" & ��������r) = Array(TextBox4, TextBox1, TextBox2, TextBox6 & "@" & ComboBox1, Format(TextBox7.Value, "0##-####-####"), gen, Format(TextBox5, "####-##-##"))
        If Image1.Tag <> "" Then Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & .Range("A" & ��������r) & ".jpg")
    End With
    
    Unload Me
    �α���.Show
    
End Sub

Private Sub CommandButton3_Click()
    Unload Me
    Sheet1.Select
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()

    ComboBox1.list = Split("empal.com gmail.com hanmil.net kebi.com korea.com nate.com naver.com yahoo.com")
    k = 1
    For Each i In Split("3 4 4 2 8 5 6")
        Me.Controls("Textbox" & k) = Sheet2.Cells(��������r, Val(i))
        k = k + 1
    Next
    
    If (Sheet2.Range("G" & ��������r) = "��") Then
        OptionButton1 = True
    Else
        OptionButton2 = True
    End If
    
    TextBox5 = Replace(TextBox5, "-", "")
    TextBox7 = Replace(TextBox7, "-", "")
    
    mail = Split(TextBox6, "@")
    ComboBox1 = mail(1)
    TextBox6 = mail(0)
    
    Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & ��������r - 1 & ".jpg")
    
    ' 3 4 4 2 8
    
    
    
End Sub
