VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 로그인 
   Caption         =   "UserForm1"
   ClientHeight    =   3300
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6810
   OleObjectBlob   =   "로그인.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "로그인"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    If TextBox1 = "" Or TextBox2 = "" Then eMsg "빈 칸 없이 입력하세요.": Exit Sub
    If TextBox1 = "admin" And TextBox2 = "1234" Then
        iMsg "관리자님 로그인 되었습니다"
        Sheet11.[회원아이디] = TextBox1
        Exit Sub
    End If
    If Not dic멤버.exists(TextBox1.Value) Then eMsg "잘못된 정보입니다.": Exit Sub
    If Not dic멤버(TextBox1.Value).Cells(1, 2) = TextBox2 Then eMsg "잘못된 정보입니다.": Exit Sub
    
    iMsg dic멤버(TextBox1.Value).Cells(1, 3) & "님 로그인 되었습니다."
    Sheet11.[회원아이디] = TextBox1
    Unload Me
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub UserForm_Initialize()
    mkdic dic멤버, Sheet1.[a2]
    
    TextBox1.SetFocus
End Sub
