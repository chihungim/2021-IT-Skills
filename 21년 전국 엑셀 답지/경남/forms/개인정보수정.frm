VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 개인정보수정 
   Caption         =   "개인정보수정"
   ClientHeight    =   7515
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10200
   OleObjectBlob   =   "개인정보수정.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "개인정보수정"
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
            Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지없음.jpg")
        Else
            Image1.Picture = LoadPicture(pt)
        End If
        Image1.Tag = pt
        
        Repaint
        
    End With
End Sub

Private Sub CommandButton2_Click()
    
    For i = 1 To 7
        If Me.Controls("Textbox" & i) = "" Then eMsg "빈 항목이 있습니다.": Exit Sub
    Next
    If ComboBox1 = "" Then eMsg "빈 항목이 있습니다.": Exit Sub
    
    If TextBox2 Like "*[a-zA-Z]*" = False Or TextBox2 Like "*[0-9]*" = False Or TextBox2 Like "*[\!\@\#\$\%\^\&\*]*" = False Or (Len(TextBox2) < 8 And Len(TextBox2) > 12) Then eMsg "비밀번호를 다시 입력해주세요.": Exit Sub
    
    If TextBox2 <> TextBox3 Then eMsg "비밀번호가 일치하지 않습니다.": Exit Sub
    
    If Len(TextBox5) <> 8 Then eMsg "생년월일 형식이 올바르지 않습니다.": Exit Sub
    
    If CDate(Format(TextBox5.Value, "####/##/##")) > Date Then eMsg "생년월일 형식이 올바르지 않습니다.": Exit Sub
    
    If Len(TextBox7) <> 11 Then eMsg "전화번호는 11자리여야 합니다.": Exit Sub
    
    iMsg "회원수정이 완료되었습니다."
    
    With Sheet2
        .Range("B" & 개인정보r & ":H" & 개인정보r) = Array(TextBox4, TextBox1, TextBox2, TextBox6 & "@" & ComboBox1, Format(TextBox7.Value, "0##-####-####"), gen, Format(TextBox5, "####-##-##"))
        If Image1.Tag <> "" Then Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\지급자료\회원사진\" & .Range("A" & 개인정보r) & ".jpg")
    End With
    
    Unload Me
    로그인.Show
    
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
        Me.Controls("Textbox" & k) = Sheet2.Cells(개인정보r, Val(i))
        k = k + 1
    Next
    
    If (Sheet2.Range("G" & 개인정보r) = "남") Then
        OptionButton1 = True
    Else
        OptionButton2 = True
    End If
    
    TextBox5 = Replace(TextBox5, "-", "")
    TextBox7 = Replace(TextBox7, "-", "")
    
    mail = Split(TextBox6, "@")
    ComboBox1 = mail(1)
    TextBox6 = mail(0)
    
    Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\회원사진\" & 개인정보r - 1 & ".jpg")
    
    ' 3 4 4 2 8
    
    
    
End Sub
