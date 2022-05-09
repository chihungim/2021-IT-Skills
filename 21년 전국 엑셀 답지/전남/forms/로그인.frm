VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 로그인 
   Caption         =   "로그인"
   ClientHeight    =   1440
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4560
   OleObjectBlob   =   "로그인.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "로그인"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cnt
Private Sub CommandButton1_Click()
    uid = TextBox1: upw = TextBox2
    
    mkDic dic회원, Sheet2.Range("C2")
    
    If uid = "" And upw = "" Then eMsg "빈칸이 존재합니다.": Exit Sub
    If uid = "admin" Then
        
        If upw = "1234" Then
            iMsg "관리자로 로그인되었습니다.": Call 관리자로그인: Unload Me: Exit Sub
        Else
            If cnt = 2 Then TextBox2 = "1234": cnt = 0
            cnt = cnt + 1
            Exit Sub
        End If
    End If
    
    If Not dic회원.exists(uid) Then eMsg "일치하는 아이디가 없습니다.": TextBox1 = "": TextBox2 = "": TextBox1.SetFocus: Exit Sub
    If dic회원(uid).Cells(1, 2) <> upw Then
        If cnt = 2 Then tmpPW = 임시비번생성: eMsg "비밀번호가 변경되었습니다." & vbCrLf & "변경된 비밀번호 [" & tmpPW & "]": cnt = 0: Exit Sub
        cnt = cnt + 1
    End If
    
    iMsg dic회원(uid).Cells(1, 0) & "님 환영합니다."
    [번호] = dic회원(uid).Cells(1, -1)
    Call 유저로그인
    Unload Me

End Sub

Function 임시비번생성()
    Set 대문자 = New ArrayList
    Set 소문자 = New ArrayList
    숫자 = Split("0 1 2 3 4 5 6 7 8 9")
    특문자 = Split("! @ # $ ?")
    
    For i = Asc("A") To Asc("Z")
        대문자.Add Chr(i)
    Next
    
    For i = Asc("a") To Asc("z")
        소문자.Add Chr(i)
    Next
    
    Randomize
    임시비번 = 대문자(Int(Rnd * 10) + 15) & 소문자(Int(Rnd * 10) + 15) & 소문자(Int(Rnd * 10) + 15) & 소문자(Int(Rnd * 10) + 15) & 숫자(Int(Rnd * 10)) & 특문자(Int(Rnd * 5))
    임시비번생성 = 임시비번
End Function

Private Sub Label2_Click()

End Sub

Private Sub UserForm_Initialize()
    cnt = 0
End Sub
