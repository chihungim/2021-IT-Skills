VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} idpw찾기 
   Caption         =   "idpw찾기"
   ClientHeight    =   3360
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5460
   OleObjectBlob   =   "idpw찾기.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "idpw찾기"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    
    Dim clip As MSForms.DataObject
    Set clip = New MSForms.DataObject
    
    
    txt1 = TextBox1: txt2 = TextBox2
    If txt1 = "" Or txt2 = "" Then eMsg "빈칸이 있습니다.": Exit Sub
    
    If CommandButton1.Caption = "아이디 찾기" Then
        mkdic dic회원, Sheet2.Range("B2")
        If Not dic회원.exists(txt1) Then
            eMsg "일치하는 정보가 없습니다": Exit Sub
        Else
            If dic회원(txt1).Cells(1, 4) <> txt2 Then eMsg "일치하는 정보가 없습니다": Exit Sub
        End If
        
        uid = dic회원(txt1).Cells(1, 2)
        If MsgBox("회원님께서 가입하신 아이디는 " & uid & "입니다." & vbCrLf & "복사하시겠습니까?", vbInformation + vbYesNo, "정보") = vbYes Then
            iMsg uid
            
            
            
        End If
        Unload Me
        
    Else
        mkdic dic회원, Sheet2.Range("C2")
        If Not dic회원.exists(txt1) Then
            eMsg "일치하는 정보가 없습니다": Exit Sub
        Else
            If dic회원(txt1).Cells(1, 3) <> txt2 Then eMsg "일치하는 정보가 없습니다": Exit Sub
        End If
        Randomize
        임시비번 = Format(Int(Rnd * 1000000), "0#####")
        dic회원(txt1).Cells(1, 2) = 임시비번
        
        If MsgBox("임시 비밀번호는 " & 임시비번 & "입니다." & vbCrLf & "복사하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
            
        End If
        
        Unload Me
    End If
    
End Sub

Private Sub OptionButton1_Click()
    Label1 = "이름"
    CommandButton1.Caption = "아이디 찾기"
End Sub

Private Sub OptionButton2_Click()
    Label1 = "아이디"
    CommandButton1.Caption = "비밀번호 찾기"
End Sub

Private Sub UserForm_Click()

End Sub
