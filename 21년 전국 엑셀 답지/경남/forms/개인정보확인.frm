VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 개인정보확인 
   Caption         =   "개인정보확인"
   ClientHeight    =   1440
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6810
   OleObjectBlob   =   "개인정보확인.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "개인정보확인"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    If TextBox1 = "" Then eMsg "빈칸입니다.": Exit Sub
    
    mkdic dic회원, Sheet2.Range("D2")
    
    If dic회원.exists(TextBox1.Value) Then
        iMsg "인증되었습니다."
        개인정보r = dic회원(TextBox1.Value).Row
        Unload Me
        개인정보수정.Show
    End If
    
End Sub
