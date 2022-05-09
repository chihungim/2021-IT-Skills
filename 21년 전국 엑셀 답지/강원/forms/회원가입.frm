VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 회원가입 
   Caption         =   "회원가입"
   ClientHeight    =   5445
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7320
   OleObjectBlob   =   "회원가입.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "회원가입"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim id여부

Private Sub CommandButton1_Click()
        
    If TextBox2 = "" Then eMsg "아이디를 입력해주세요.": Exit Sub
    
    mkdic dic회원, Sheet4.Range("B2")
    If dic.exists(TextBox2.Value) Then eMsg "이미 존재하는 아이디입니다.": TextBox2 = "": TextBox2.SetFocus: Exit Sub
    
    iMsg "사용 가능한 아이디입니다."
    id여부 = True
    
End Sub

Private Sub CommandButton2_Click()
    
    If Not id여부 Then eMsg "중복 확인을 해주세요.": Exit Sub
    
    If TextBox3 <> TextBox4 Then eMsg "비밀번호를 확인해주세요.": Exit Sub
    
    If Not email(TextBox10.Value & "@" & TextBox11.Value) Then eMsg "이메일 형식이 일치하지 않습니다.": Exit Sub
    
    If Not IsDate(TextBox5) Then eMsg "생년월일 형식이 일치하지 않습니다.": Exit Sub
    
    If IsNumeric(TextBox7) = False Or IsNumeric(TextBox8) = False Or IsNumeric(TextBox9) = False Then eMsg "전화번호는 숫자만 입력되어야 합니다.": Exit Sub
    
    iMsg "회원가입이 완료되었습니다."
    With Sheet4
        lrow = .Range("A1000").End(3).row + 1
        .Range("A" & lrow & ":G" & lrow) = Array(lrow - 1, TextBox2, TextBox3, TextBox1, TextBox7 & "-" & TextBox8 & "-" & TextBox9, TextBox5 & "-" & TextBox6 & Label9, TextBox10 & "@" & TextBox11)
        
        Unload Me
        로그인.Show
    End With
    
End Sub

Private Sub CommandButton3_Click()
    Unload Me
End Sub

Private Sub TextBox2_Change()
    id여부 = False
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    
    
End Sub
