VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 회원가입 
   Caption         =   "회원가입"
   ClientHeight    =   7530
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10200
   OleObjectBlob   =   "회원가입.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "회원가입"
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
    
    If TextBox1 Like "*[a-zA-Z]*" = False Or TextBox1 Like "*[0-9]*" = False Then eMsg "아이디를 다시 입력해주세요.": Exit Sub
    
    If TextBox2 Like "*[a-zA-Z]*" = False Or TextBox2 Like "*[0-9]*" = False Or TextBox2 Like "*[\!\@\#\$\%\^\&\*]*" = False Or (Len(TextBox2) < 8 And Len(TextBox2) > 12) Then eMsg "비밀번호를 다시 입력해주세요.": Exit Sub
    
    If TextBox2 <> TextBox3 Then eMsg "비밀번호가 일치하지 않습니다.": Exit Sub
    
    If Len(TextBox5) <> 8 Then eMsg "생년월일 형식이 올바르지 않습니다.": Exit Sub
    
    If CDate(Format(TextBox5.Value, "####/##/##")) > Date Then eMsg "생년월일 형식이 올바르지 않습니다.": Exit Sub
    
    If Len(TextBox7) <> 11 Then eMsg "전화번호는 11자리여야 합니다.": Exit Sub

    If Image1.Tag = "" Then eMsg "선택한 사진이 없습니다.": Exit Sub
    
    iMsg "회원가입이 완료되었습니다."
    
    With Sheet2
        lrow = .Range("A10000").End(3).Row + 1
        gen = IIf(OptionButton1.Value, "남", "여")
        .Range("A" & lrow & ":H" & lrow) = Array(lrow - 1, TextBox4, TextBox1, TextBox2, TextBox6 & "@" & ComboBox1, Format(TextBox7.Value, "0##-####-####"), gen, Format(TextBox5, "####-##-##"))
        Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\지급자료\회원사진\" & (lrow - 1) & ".jpg")
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
