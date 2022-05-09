VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 회원가입수정 
   Caption         =   "히히"
   ClientHeight    =   6585
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6000
   OleObjectBlob   =   "회원가입수정.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "회원가입수정"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim chk, chk2, chk3
Public mode
Dim fpath

Private Sub CommandButton1_Click()
    If TextBox2.Text = "" Then eMsg "아이디를 입력해주세요.": Exit Sub
    If dic회원.exists(TextBox2.Text) Then eMsg "아이디가 중복되었습니다.": Exit Sub
    iMsg "사용 가능 한 아이디 입니다.": chk2 = True
End Sub

Private Sub CommandButton2_Click()
    우편번호찾기.Show
End Sub

Private Sub CommandButton3_Click()
    If chk2 = False Then eMsg "중복확인을 해주세요.": Exit Sub
    
    
    For i = 1 To 7
        If i = 4 Then
            If Me.Controls("TextBox" & i) = "YYYY-MM-DD" Then
                eMsg "빈칸을 입력해주세요."
                Exit Sub
            End If
        ElseIf i = 7 Then
            If Me.Controls("TextBox" & i) = "상세주소2" Then
                eMsg "빈칸을 입력해주세요."
                Exit Sub
            End If
        Else
            If Me.Controls("TextBox" & i) = "" Then
                eMsg "빈칸을 입력해주세요."
                Exit Sub
            End If
        End If
    Next

    If CDate(TextBox4.Text) > Now() Then eMsg "생년월일 형식이 일치하지 않습니다.": Exit Sub
    If Not TextBox3 Like "*[A-Z][a-z][a-z][a-z][0-9][?!@#$]" Then eMsg "비밀번호 형식이 일치하지 않습니다.": Exit Sub
    
    If CommandButton3.Caption = "회원가입" Then
        irow = Sheet2.Range("A10000").End(xlUp).row
        Sheet2.Range("A" & irow & ":H" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, TextBox4, IIf(OptionButton1 = True, "남", "여"), TextBox5, TextBox7)
        If chk Then SavePicture Image1.Picture, ThisWorkbook.Path & "\지급자료\회원사진\" & irow - 1 & ".jpg"
        iMsg "회원가입이 완료되었습니다."
    Else
        mkDic dic회원, Sheet2.Range("A2")
        
        Set 유저row = dic회원([번호].Value)
        
        With 유저row
            .Cells(1, 3) = TextBox2
            .Cells(1, 4) = TextBox3
            .Cells(1, 7) = TextBox5
            .Cells(1, 8) = TextBox7
        End With
        
        If chk Then SavePicture Image1.Picture, ThisWorkbook.Path & "\지급자료\회원사진\" & [번호] & ".jpg"
        If chk3 Then Kill ThisWorkbook.Path & "\지급자료\회원사진\" & [번호] & ".jpg"
        iMsg "정보수정이 완료되었습니다."
    End If
    
    Unload Me
End Sub

Private Sub Image1_DblClick(ByVal Cancel As MSForms.ReturnBoolean)
    fpath = ""
    With Application.FileDialog(msoFileDialogFilePicker)
        .Show
        .Filters.Clear
        .Filters.Add "이미지", "*.jpg"
        If .SelectedItems.Count > 0 Then fpath = .SelectedItems(1)
    End With
    If fpath Like "*[0.jpg]*" Then chk3 = True
    If (IsEmpty(fpath)) Then Exit Sub
    If Dir(fpath) <> "" Then Image1.Picture = LoadPicture(fpath): chk = True: Repaint
End Sub

Private Sub TextBox7_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If TextBox7.Text = "" Then TextBox7.Text = "상세주소2"
End Sub

Private Sub TextBox7_MouseUp(ByVal Button As Integer, ByVal Shift As Integer, ByVal x As Single, ByVal y As Single)
    If TextBox7.Text = "상세주소2" Then TextBox7.Text = ""
End Sub

Private Sub TextBox4_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If Not TextBox4.Text Like "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]" Then eMsg "날짜 형식이 아닙니다.": TextBox4.SetFocus: Exit Sub
    If TextBox4.Text = "" Then TextBox4.Text = "YYYY-MM-DD": Exit Sub
End Sub

Private Sub TextBox4_MouseUp(ByVal Button As Integer, ByVal Shift As Integer, ByVal x As Single, ByVal y As Single)
    If TextBox4.Text = "YYYY-MM-DD" Then TextBox4.Text = ""
End Sub


Private Sub UserForm_Activate()
    mkDic dic우편, Sheet3.Range("B2")
    mkDic dic회원, Sheet2.Range("C2")
    
    TextBox6.Enabled = False
    If mode = "회원가입" Then
        Me.Caption = "회원가입"
    Else
        Me.Caption = "정보수정"
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\회원사진\" & [번호] & ".jpg")
        TextBox1.Enabled = False
        TextBox4.Enabled = False
        TextBox1.Text = [이름]
        TextBox2.Text = [아이디]
        TextBox3.Text = [비번]
        TextBox4.Text = Format([생년월일], "yyyy-MM-dd")
        TextBox5.Text = [우편번호].Value
        TextBox7.Text = [상세주소]
        If [성별] = "남" Then
            OptionButton1 = True
        Else
            OptionButton2 = True
        End If

        Dim 우편 As Range
        n = [우편번호].Value
        Set 우편 = dic우편(Val(n))
        TextBox6.Text = 우편.Cells(1, 2) & " " & 우편.Cells(1, 3) & " " & 우편.Cells(1, 4) & " " & 우편.Cells(1, 5)
    End If
    
    CommandButton3.Caption = Me.Caption
End Sub

