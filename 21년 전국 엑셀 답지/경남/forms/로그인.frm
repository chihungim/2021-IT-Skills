VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 로그인 
   Caption         =   "로그인"
   ClientHeight    =   2760
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6780
   OleObjectBlob   =   "로그인.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "로그인"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    
    uid = TextBox1: upw = TextBox2
    
    If uid = "" Then eMsg "아이디를 입력해주세요.": Exit Sub
    
    If upw = "" Then eMsg "비밀번호를 입력해주세요.": Exit Sub
    
    If uid = "admin" And upw = "1234" Then
        iMsg "관리자님 환영합니다."
        Exit Sub
    End If
    
    mkdic dic회원, Sheet2.Range("C2")
    
    If Not dic회원.exists(uid) Then
        eMsg "입력하신 아이디 또는 비밀번호를 다시 확인해주세요.": Exit Sub
    Else
        If dic회원(uid).Cells(1, 2) <> upw Then eMsg "입력하신 아이디 또는 비밀번호를 다시 확인해주세요.": Exit Sub
    End If
    
    iMsg "회원 " & dic회원(uid).Cells(1, 0) & "님 환영합니다."
    
End Sub

Private Sub CommandButton2_Click()
    회원가입.Show
End Sub

Private Sub CommandButton3_Click()
    idpw찾기.Show
End Sub

Private Sub UserForm_Click()

End Sub
