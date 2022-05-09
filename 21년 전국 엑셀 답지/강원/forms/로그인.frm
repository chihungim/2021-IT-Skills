VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 로그인 
   Caption         =   "로그인"
   ClientHeight    =   3825
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4920
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
    
    If uid = "" Or upw = "" Then eMsg "빈칸이 존재합니다.": Exit Sub
    
    If uid = "admin" And upw = "1234" Then
        [번호] = "admin"
        iMsg "관리자로 로그인 하였습니다."
        관리자로그인
        Unload Me
        Exit Sub
    End If
    
    mkdic dic회원, Sheet4.Range("B2")
    
    If Not dic회원.exists(uid) Then
        eMsg "아이디 혹은 비밀번호가 일치하지 않습니다.": TextBox1 = "": TextBox2 = "": TextBox1.SetFocus: Exit Sub
    Else
        If dic회원(uid).Cells(1, 2) <> upw Then eMsg "아이디 혹은 비밀번호가 일치하지 않습니다.": TextBox1 = "": TextBox2 = "": TextBox1.SetFocus: Exit Sub
    End If
    
    iMsg dic회원(uid).Cells(1, 3) & "님으로 로그인 하였습니다."
    유저로그인
    Unload Me
        
    
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub Label3_Click()
    Unload Me
    회원가입.Show
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
   chgImg
    
End Sub


Private Sub UserForm_Terminate()
    stopchgImg
End Sub
