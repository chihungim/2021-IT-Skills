VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 시험접수 
   Caption         =   "시험접수"
   ClientHeight    =   10170
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6630
   OleObjectBlob   =   "시험접수.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "시험접수"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public 종목선택
Dim 여부 As Boolean, 주민번호확인 As Boolean
Dim r, cnt
Dim cls(1000) As cls시험접수


Private Sub ComboBox1_Change()
    ComboBox2.Clear
    
    If ComboBox1.ListIndex = 0 Then
        ComboBox2.AddItem "단일"
    ElseIf ComboBox1.ListIndex = 1 Then
        ComboBox2.AddItem "1급"
        ComboBox2.AddItem "2급"
    Else
        ComboBox2.AddItem "1급"
        ComboBox2.AddItem "2급"
        ComboBox2.AddItem "3급"
    End If
    
End Sub

Private Sub ComboBox2_Change()
    
    자격증 = ComboBox1 & " " & ComboBox2
    For i = 2 To Sheet3.Range("A1000").End(3).Row
        If 자격증 = Sheet3.Range("J" & i) Then
            r = i - 1
            Exit For
        End If
    Next
    
    With Sheet4
        .Range("K2") = r
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("K1:K2"), .Range("M1:Q1")
    End With
    
    MultiPage1.Height = 330
    Me.Height = 442
    
    CommandButton2.Top = MultiPage1.Top + MultiPage1.Height + 20
    CommandButton3.Top = MultiPage1.Top + MultiPage1.Height + 20
    
    Call 시험접수내역
    
End Sub

Sub 시험접수내역()
    Set dic종목 = CreateObject("Scripting.Dictionary")
    Set sc = Sheet4.Range("M2")
    Set lc = sc.Cells(10000, 1).End(3)
    
    On Error Resume Next
    For i = 0 To 1000
        Me.Controls.Remove cls(i).frm.Name
        Set cls(i) = Nothing
    Next
    Repaint
    
    For Each cell In Range(sc, lc)
        값1 = Format(cell.Cells(1, 2), "yyyy-MM-dd")
        값2 = Format(cell.Cells(1, 3), "h:mm")
        If dic종목.exists(값1) Then
            dic종목(값1) = dic종목(값1) & " " & 값2 & "," & cell.Cells(1, 4)
        Else
            dic종목.Add 값1, 값2 & "," & cell.Cells(1, 4)
        End If
    Next
    
    i = 0
    For Each k In dic종목
        Debug.Print k
        Set cls(i) = New cls시험접수
        Call cls(i).init(0 + (i * 100), k, dic종목(k))
        i = i + 1
    Next
    
    Frame1.ScrollTop = 0
    Frame1.ScrollHeight = (i + 1) * 120
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton2_Click()
    If MultiPage1.Value = 0 Then Exit Sub
    MultiPage1.Value = MultiPage1 - 1
End Sub

Private Sub CommandButton3_Click()
    
    If MultiPage1.Value = 0 Then
        If 종목선택 = "" Then eMsg "시간대를 선택해주세요.": Exit Sub
    
        접수마감 = Val(Mid(Sheet3.Range("D" & r + 1), 5, 1))
        
        시간 = Split(종목선택)
        마감일 = DateValue(시간(0)) - 접수마감
        
        If 시간(2) = "마감" Then eMsg "마감되었습니다.": Exit Sub
        
        If Date > 마감일 Then eMsg "마감되었습니다.": Exit Sub
        
        If r = 2 Then
            If [컴활2급] = 0 Then eMsg "접수하실 수 없는 등급입니다.": Exit Sub
        ElseIf r = 4 Then
            If [비서2급] = 0 Then eMsg "접수하실 수 없는 등급입니다.": Exit Sub
        ElseIf r = 5 Then
            If [비서3급] = 0 Then eMsg "접수하실 수 없는 등급입니다.": Exit Sub
        End If
        
        여부 = True
        MultiPage1.Value = 1
        CommandButton2.Enabled = True
        Label5 = [이름]
        Label7 = [전화번호]
        
    ElseIf MultiPage1.Value = 1 Then
        If Not 주민번호확인 Then eMsg "주민등록번호 앞자리 확인을 해주세요.": Exit Sub
        
        MultiPage1.Value = 2
        
        시간 = Split(종목선택)
        
        Label10 = ComboBox1 & " " & ComboBox2 & " / " & 시간(1)
        Label12 = [이름]
        Label14 = 시간(0)
        
        발표일자 = IIf(Sheet3.Range("E" & r + 1) = "익일", 1, 0)
        Label16 = DateAdd("d", 발표일자, 시간(0))
        
        Label18 = Format(Sheet3.Range("H" & r + 1), "#,##0")
        Label20 = Format(Round(Sheet3.Range("H" & r + 1) * 0.07 / 100) * 100, "#,##0")
        Label22 = iFormat(vFormat(Label18) + vFormat(Label20))
        
    Else
        iMsg "접수가 완료되었습니다."
        
        시간 = Split(종목선택)
        With Sheet4
            일정번호 = 0
            For i = 2 To .Range("M1000").End(3).Row
                Debug.Print .Range("N" & i); 시간(0); Format(.Range("O" & i), "h:mm"); 시간(1)
                If DateValue(.Range("N" & i)) = DateValue(시간(0)) And Format(.Range("O" & i), "h:mm") = 시간(1) Then
                    iMsg .Range("M" & i)
                    일정번호 = .Range("M" & i)
                    Exit For
                End If
            Next
        End With
        
        With Sheet5
            lrow = .Range("A1000").End(3).Row + 1
            .Range("A" & lrow & ":H" & lrow) = Array(lrow - 1, [번호], 일정번호, Date, 시간(0), Label16, "", "")
        End With
        
        Unload Me
        접수내역.Show
    End If
    
    
End Sub


Private Sub CommandButton4_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("Image", "*.jpg")
        
        If .Show <> 0 Then pt = .SelectedItems(1)
        
        If pt = "" Then
            Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지없음.jpg")
        Else
            Image1.Picture = LoadPicture(pt)
        End If
        Image1.Tag = pt
        
        Repaint
        
    End With
End Sub

Private Sub CommandButton5_Click()
    If TextBox1 = "3번이상 틀릴 시 로그아웃합니다." Then eMsg "다시 입력해주세요.": Exit Sub
    
    If Format([생년월일], "yyMMdd") <> TextBox1 Then
         eMsg "다시 입력해주세요."
         cnt = cnt + 1
         
         If cnt = 3 Then
            eMsg "로그아웃되었습니다. 다시 로그인해주세요."
            로그아웃
            Unload Me
            Sheet1.Select
            로그인.Show
            Exit Sub
         End If
         
         Exit Sub
    End If
    
    주민번호확인 = True
    iMsg "확인되었습니다."
    
End Sub

Private Sub MultiPage1_Change()
    If MultiPage1.Value = 2 Then
        CommandButton3.Caption = "완료"
    Else
        CommandButton3.Caption = "다음"
    End If
    
End Sub

Private Sub MultiPage1_Click(ByVal Index As Long)
    If Not 여부 Then MultiPage1.Value = 0: Exit Sub
End Sub

'Private Sub TextBox1_BeforeUpdate(ByVal Cancel As MSForms.ReturnBoolean)
'    If TextBox1 = "" Then TextBox1 = "3번이상 틀릴 시 로그아웃합니다.": TextBox1.ForeColor = &H8000000A
'    Cancel = True
'End Sub

Private Sub TextBox1_Change()

End Sub


Private Sub TextBox1_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    키패드.Show
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    ' 78 330
    ' 223 442
    
    MultiPage1.Height = 78
    Me.Height = 223
    CommandButton2.Top = MultiPage1.Top + MultiPage1.Height + 20
    CommandButton3.Top = MultiPage1.Top + MultiPage1.Height + 20
    MultiPage1.Value = 0
    
    ComboBox1.list = Split("워드프로세서 컴퓨터활용능력 비서")
    
    ComboBox1.Style = fmStyleDropDownList
    ComboBox2.Style = fmStyleDropDownList
    
    CommandButton2.Enabled = False
    
End Sub
