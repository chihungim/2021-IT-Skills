VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 로그인 
   Caption         =   "로그인"
   ClientHeight    =   1305
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4275
   OleObjectBlob   =   "로그인.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "로그인"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim loginCnt

Private Sub CommandButton1_Click()

    Set 회원사전 = mkdic(Sheet2.Range("C2"))
        
    If TextBox1 = "" Or TextBox2 = "" Then
        eMsg "빈칸이 있습니다"
        Exit Sub
    End If
    
    If Not 회원사전.exists(TextBox1.Text) Then
        eMsg "아이디가 일치하지 않습니다."
        Exit Sub
    End If
    
    If Not 회원사전(TextBox1.Text).Cells(1, 2) = TextBox2.Text Then
        If loginCnt = 3 Then
            loginCnt = 0
            Dim yn: yn = MsgBox("비밀번호를 3회 이상 잘못입력하셨습니다." & vbLf & "로그인을 위한 임시 비밀번호를 발급받으시겠습니까?", vbCritical + vbOKCancel)
  
            If yn = vbOK Then
                비번 = randPW
                이름 = 회원사전(TextBox1.Text).Cells(1, 0)
                회원사전(TextBox1.Text).Cells(1, 2) = 비번
                iMsg 이름 & "님의 임시 비밀번호는 " & 비번 & "입니다."
            End If
            
            Exit Sub
        End If
        eMsg "비밀번호가 일치하지 않습니다."
        loginCnt = loginCnt + 1
        Exit Sub
    End If
    
    With Sheet1
        .CommandButton1.Caption = "로그아웃"
        .CommandButton2.Caption = "회원정보 수정"
        .CommandButton3.Enabled = True
        .CommandButton4.Enabled = True
        .CommandButton5.Enabled = True
        
        Dim 번호
        Dim 구분
        
        With 회원사전(TextBox1.Text)
            번호 = .Cells(1, -1): 이름 = .Cells(1, 0): 구분 = .Cells(1, 4)
        End With
    
        With Sheet1
            .Cells(1, "A") = 이름
            .Cells(1, "B") = 번호
            .Cells(1, "C") = 구분
        End With
    
    
        If .Cells(1, 3) = "화가" Then
            Sheet1.CommandButton3.Caption = "작품 제작"
        Else
            Sheet1.CommandButton3.Caption = "구매내역 보기"
        End If
        
    End With
    
    iMsg 이름 & "님으로 로그인되었습니다."
    
    stateChange
    
    Unload Me
End Sub

Function randPW() As String

   passw = ""
    
    For i = 0 To 3
        passw = passw + Chr(Int(Rnd * 26) + 97)
    Next
    
    For i = 0 To 2
        passw = passw & Int(Rnd * 10)
    Next
    
    a = Int(Rnd * 8)
    
    specialCh = Array("!", "@", "#", "$", "%", "^", "&", "*")
    
    randPW = passw & specialCh(a)
End Function


Private Sub Label2_Click()

End Sub

Private Sub UserForm_Click()

End Sub
