VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 회원가입수정 
   Caption         =   "회원가입"
   ClientHeight    =   5760
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5985
   OleObjectBlob   =   "회원가입수정.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "회원가입수정"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim max As Integer, chk As Boolean

Private Sub ComboBox1_Change()
    TextBox7.Visible = IIf(ComboBox1.ListIndex = ComboBox1.ListCount - 1, True, False)
    max = IIf(ComboBox1.ListIndex = ComboBox1.ListIndex - 1, 6, 5)
End Sub

Private Sub CommandButton1_Click()
    createRandomCharacters
End Sub

Sub createRandomCharacters()
    max = 0
    Randomize
    
    확인문자 = ""
    
    For i = 0 To 5
        ch = Int(Rnd * 2)
        max = max + ch
        
        If ch = 1 Then
            확인문자 = 확인문자 + Chr(Int(Rnd * 26) + 65)
        Else
            확인문자 = 확인문자 & Int(Rnd * 10)
        End If
    Next
    
    If max <> 4 Then createRandomCharacters
    
End Sub

Private Sub CommandButton2_Click()
    For i = 1 To max
        If Me.Controls("TextBox" & i) = "" Then
            eMsg "빈칸이 있습니다."
            Exit Sub
        End If
    Next
    
    Call 사전생성(Sheet2.Range("C2"))
    
    If 사전.exists(TextBox1.Value) Then eMsg "이미있는 아이디입니다.": Exit Sub
    
    If Not (TextBox3 Like "*[0-9]*" And TextBox3 Like "*[a-zA-Z]*" And TextBox3 Like "*[!0-9a-zA-Zㄱ-힣]*") Or Len(TextBox3) < 6 Then
        eMsg "비밀번호는 숫자, 영문자, 특수문자를 포함하여 6글자 이상으로 입력해주세요."
        Exit Sub
    End If
    
    If TextBox3 <> TextBox4 Then
        eMsg "비밀번호가 일치하지 않습니다."
        Exit Sub
    End If
    
    If OptionButton1 = False And OptionButton2 = False Then
        eMsg "분류를 선택해주세요": Exit Sub
    End If

    
    If Not (LCase(확인문자) = LCase(확인문자) Or UCase(확인문자) = UCase(확인문자)) Then
        eMsg "확인문자를 잘못입력하셨습니다."
        Exit Sub
    End If
    
    If CommandButton2.Caption = "수정 완료" Then
        iMsg "수정이 완료되었습니다."
        irow = Sheet1.Range("번호").Value + 1
        Sheet2.Range("A" & irow & ":E" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, IIf(max = 5, TextBox5 & "@" & ComboBox1, TextBox5 & "@" & TextBox6))
    Else
        iMsg "회원가입이 완료되었습니다."
        irow = Sheet2.Range("A1000").End(xlUp).Row + 1
        Sheet2.Range("A" & irow & ":E" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, IIf(max = 5, TextBox5 & "@" & ComboBox1, TextBox5 & "@" & TextBox6))
    End If
    
    Sheet2.Range("F" & irow) = IIf(OptionButton1 = True, "일반", "화가")
    Sheet2.Range("G" & irow).FillDown
    
    If chk Then
        번호 = Sheet1.Range("번호").Value
        Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\지급자료\회원사진\" & 번호 & ".jpg")
        With Sheet2.Range("B" & irow).AddComment
            .Shape.Fill.UserPicture ThisWorkbook.Path & "\지급자료\회원사진\" & 번호 & ".jpg"
        End With
    End If
End Sub

Private Sub CommandButton3_Click()
    Unload Me
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("image file", "*.jpg")
        cho = .Show
        If cho <> 0 Then
            chk = True
            pic = .SelectedItems(1)
        Else
            pic = ThisWorkbook.Path & "\지급자료\회원사진\기본.jpg"
        End If
        Image1.Picture = LoadPicture(pic)
        Repaint
    End With
End Sub

Private Sub OptionButton1_Click()

End Sub

Private Sub OptionButton1_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    OptionButton1.ControlTipText = "일반 회원은 작품 제작이 불가능합니다."
End Sub


Private Sub OptionButton2_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    OptionButton2.ControlTipText = "화가 회원은 작품 구매칙 평가가 불가능 합니다."
End Sub

Private Sub UserForm_Initialize()
    createRandomCharacters
    ComboBox1.List = Array("naver.com", "gmail.com", "hanmail.net", "icloude.com", "직접입력")
    ComboBox1.ListIndex = 0
    TextBox1.SetFocus

    If Sheet1.CommandButton2.Caption = "회원정보 수정" Then
        Me.Caption = "회원정보 수정"
        CommandButton2.Caption = "수정 완료"
        
        경로 = ThisWorkbook.Path & "\지급자료\회원사진\" & Sheet1.Range("번호").Value & ".jpg"

        If Not Dir(경로) = "" Then
            Image1.Picture = LoadPicture(경로)
        End If
        TextBox1 = Sheet1.Range("이름")
        사전생성 Sheet2.Range("B2")
        
        With 사전(TextBox1.Value)
            TextBox2 = .Cells(1, 2)
            이메일 = Split(.Cells(1, 4), "@")
            TextBox5 = 이메일(0)
            For i = 0 To ComboBox1.ListCount
                ComboBox1.ListIndex = i
                If 이메일(1) = ComboBox1 Then Exit For
            Next
            Debug.Print .Cells(1, 5)
            OptionButton1 = IIf(.Cells(1, 5) = "일반", True, False)
            OptionButton2 = IIf(.Cells(1, 5) = "화가", True, False)
        End With
    
        TextBox1.Enabled = False
        TextBox2.Enabled = False
    End If
End Sub

