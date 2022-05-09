VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ȸ������ 
   Caption         =   "ȸ������"
   ClientHeight    =   7530
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10200
   OleObjectBlob   =   "ȸ������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ȸ������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

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
    
    If TextBox1 Like "*[a-zA-Z]*" = False Or TextBox1 Like "*[0-9]*" = False Then eMsg "���̵� �ٽ� �Է����ּ���.": Exit Sub
    
    If TextBox2 Like "*[a-zA-Z]*" = False Or TextBox2 Like "*[0-9]*" = False Or TextBox2 Like "*[\!\@\#\$\%\^\&\*]*" = False Or (Len(TextBox2) < 8 And Len(TextBox2) > 12) Then eMsg "��й�ȣ�� �ٽ� �Է����ּ���.": Exit Sub
    
    If TextBox2 <> TextBox3 Then eMsg "��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    If Len(TextBox5) <> 8 Then eMsg "������� ������ �ùٸ��� �ʽ��ϴ�.": Exit Sub
    
    If CDate(Format(TextBox5.Value, "####/##/##")) > Date Then eMsg "������� ������ �ùٸ��� �ʽ��ϴ�.": Exit Sub
    
    If Len(TextBox7) <> 11 Then eMsg "��ȭ��ȣ�� 11�ڸ����� �մϴ�.": Exit Sub

    If Image1.Tag = "" Then eMsg "������ ������ �����ϴ�.": Exit Sub
    
    iMsg "ȸ�������� �Ϸ�Ǿ����ϴ�."
    
    With Sheet2
        lrow = .Range("A10000").End(3).Row + 1
        gen = IIf(OptionButton1.Value, "��", "��")
        .Range("A" & lrow & ":H" & lrow) = Array(lrow - 1, TextBox4, TextBox1, TextBox2, TextBox6 & "@" & ComboBox1, Format(TextBox7.Value, "0##-####-####"), gen, Format(TextBox5, "####-##-##"))
        Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\ȸ������\" & (lrow - 1) & ".jpg")
    End With
    
    Unload Me
    
End Sub

Private Sub CommandButton3_Click()
    Unload Me
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    ComboBox1.list = Split("empal.com gmail.com hanmil.net kebi.com korea.com nate.com naver.com yahoo.com")
End Sub
