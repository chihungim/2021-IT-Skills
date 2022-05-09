VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 회원가입 
   Caption         =   "회원가입"
   ClientHeight    =   6495
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9315
   OleObjectBlob   =   "회원가입.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "회원가입"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim rg


Private Sub ComboBox1_Change()
    ComboBox2.ListIndex = -1
    ComboBox3.ListIndex = -1
End Sub

Private Sub ComboBox2_Change()
    If ComboBox2.ListIndex <> -1 Then
        ComboBox3.Clear
        끝일 = Day(WorksheetFunction.EoMonth(DateValue(ComboBox1.Text & "-" & ComboBox2 & "-" & 1), 0))
        For i = 1 To 끝일
            ComboBox3.AddItem i
        Next
    End If
End Sub

Function setAccount(start)
    r = WorksheetFunction.RandBetween(100000000000#, 1000000000000# - 1)
    setAccount = Format(r, start & "00-0000-0000-00")
End Function

Private Sub CommandButton1_Click()
    For i = 1 To 6
        If Me.Controls("TextBox" & i) = "" Then eMsg "빈칸이 존재합니다.": Exit Sub
    Next
    
    If ComboBox1 = "" Or ComboBox2 = "" Or ComboBox3 = "" Then eMsg "빈칸이 존재합니다.": Exit Sub
    
    If TextBox2 Like "*[0-9]*" = False Or TextBox2 Like "*[a-zA-Z]*" = False Or TextBox2 Like "*[\!\@\#\$]*" = False Or Not (Len(TextBox2) >= 8 And Len(TextBox2) <= 16) Then eMsg "비밀번호는 특수문자, 영문자, 숫자를 포함한 8~16자 이내로 구성해야 합니다.": Exit Sub
    
    If Len(TextBox3) < 4 Then eMsg "2차 비밀번호는 최소 4자리 이상으로 구성해야 합니다.": Exit Sub
    
    If Not (TextBox6 Like "[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]" Or TextBox6 Like "[0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]") Then eMsg "전화번호 형식을 올바르게 구성해야합니다.": Exit Sub
    
    Set 사전 = mkdic(Sheet1.Range("B2"))
    If 사전.exists(TextBox1.Value) Then eMsg "이미 등록된 아이디 입니다.": Exit Sub
    
    Set 사전 = mkdic(Sheet1.Range("H2"))
    If 사전.exists(TextBox6.Value) Then eMsg "이미 등록된 전화번호 입니다.": Exit Sub
    Frame1.Visible = True
End Sub

Private Sub CommandButton2_Click()
    If Not (계좌1.Value Or 계좌2.Value Or 계좌3.Value) Then eMsg "계좌 유형 항목을 선택해야 합니다.": Exit Sub
    If Not (카드0.Value Or 카드1.Value Or 카드2.Value Or 카드3.Value) Then eMsg "카드 신청 항목을 선택해야합니다.": Exit Sub
    
    If Not (CheckBox1 And CheckBox2) Then eMsg "타인으로부터 받은 금융 요청이 없어야 합니다.": Exit Sub
    
    If 카드0.Value Then
        If MsgBox("지금 카드를 신청하면 연회비가 면제됩니다." & vbCrLf & "그래도 신청하지 않으시겠습니까?", vbInformation + vbYesNo) = vbYes Then
            Call 회원가입
        Else
            Exit Sub
        End If
    Else
        With 카드신청
            .Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & rg.Cells(1, 0) & ".jpg")
            .카드명 = rg
            연회비 = " (면제)"
            
            해외 = Split(rg.Cells(1, 6), ",")
            For i = 0 To UBound(해외)
                .ComboBox1.AddItem Sheet3.Range("I" & 해외(i) + 1)
            Next
            .ComboBox1.ListIndex = 0
            
            Erase 선택카드
            신청flag = False
            .Show
        End With
        If 신청flag Then
            Call 회원가입
            With Sheet4
                lrow = .Range("A10000").End(xlUp).Row + 1
                .Range("A" & lrow & ":E" & lrow) = Array(선택카드(0), 선택카드(1), 선택카드(2), Sheet1.Range("A1000").End(xlUp).Row - 1, 0)
            End With
        Else
            Exit Sub
        End If
    End If
End Sub

Sub 회원가입()
    Set dic = mkdic(Sheet1.Range("L2"))
    Do While True
        account = setAccount(IIf(계좌1, "3", IIf(계좌2, "7", "9")))
        If Not dic.exists(account) Then Exit Do
    Loop
    
    With Sheet1
        lrow = .Range("A1000").End(xlUp).Row + 1
        잔액 = IIf(계좌1, 200000, 25000)
        
        .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, TextBox1, TextBox2, TextBox4, ComboBox1 & "-" & ComboBox2 & "-" & ComboBox3, TextBox3, TextBox5, TextBox6, account, 잔액)
        Unload Me
    End With
End Sub

Private Sub TextBox3_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub TextBox3_MouseUp(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    비밀번호.Show
End Sub

Private Sub UserForm_Initialize()
    For i = 1 To 3
        Me.Controls("ComboBox" & i).Style = fmStyleDropDownList
    Next

    For i = 1960 To Year(Now())
        ComboBox1.AddItem i
    Next
    
    For i = 1 To 12
        ComboBox2.AddItem i
    Next
    
    Sheet12.Range("B10:C" & Sheet12.Range("B1000").End(xlUp).Row - 1).Copy Sheet13.Range("AX2")
    Sheet13.Range("AX1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet13.Range("BA1:BB2"), Sheet13.Range("BD1:BE1"), False


    For i = 1 To 3
        Me.Controls("카드" & i).Caption = Sheet13.Range("BD" & i + 1)
    Next

    Frame1.Visible = False
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox3_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub 카드1_Click()
    Set rg = Sheet2.Range("B:B").Find(What:=카드1.Caption, LookAt:=xlWhole)
End Sub

Private Sub 카드2_Click()
    Set rg = Sheet2.Range("B:B").Find(What:=카드2.Caption, LookAt:=xlWhole)
End Sub

Private Sub 카드3_Click()
    Set rg = Sheet2.Range("B:B").Find(What:=카드3.Caption, LookAt:=xlWhole)
End Sub
