VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 검색예약 
   Caption         =   "검색/예약"
   ClientHeight    =   9660
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7785
   OleObjectBlob   =   "검색예약.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "검색예약"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim dic시군구 As Object
Dim n, v, spinMax
Dim 기본할인율(4)
Dim 패키지번호
Function fn관광지(패키지, 관광지, 시군구, 키워드)
    체크1 = 패키지 Like "*" & 키워드 & "*"
    
    번호들 = Split(Replace(Trim(관광지), " ", ""), ",")
    답 = ""
    For Each n In 번호들
        Set c = dic관광지(Val(n))
        체크2 = c.Cells(1, 3) Like "*" & 키워드 & "*"
        체크3 = c.Cells(1, 5) Like "*" & 키워드 & "*"
        체크4 = c.Cells(1, 6) Like "*" & 키워드 & "*"
        
        If (c.Cells(1, 2) = 시군구 Or 시군구 = "전체") And (체크1 Or 체크2 Or 체크3 Or 체크4) Then
            답 = 답 & " " & n
        End If
    Next
    
    fn관광지 = Join(Split(Trim(답), " "), ", ")
End Function

Function fn축제(패키지, 축제, 시군구, 시작, 끝, 키워드)
    체크1 = 패키지 Like "*" & 키워드 & "*"
    
    번호들 = Split(Replace(Trim(축제), " ", ""), ",")
    답 = ""
    For Each n In 번호들
        Set c = dic축제(Val(n))
        체크2 = c.Cells(1, 3) Like "*" & 키워드 & "*"
        체크3 = c.Cells(1, 4) Like "*" & 키워드 & "*"
        체크4 = c.Cells(1, 8) Like "*" & 키워드 & "*"
        
        체크5 = DateValue(c.Cells(1, 6)) >= DateValue(IIf(시작 = "", "2021-01-01", 시작)) And DateValue(c.Cells(1, 7)) <= DateValue(IIf(끝 = "", "2022-12-31", 끝))
        
        If (c.Cells(1, 2) = 시군구 Or 시군구 = "전체") And (체크1 Or 체크2 Or 체크3 Or 체크4) And 체크5 Then
            답 = 답 & " " & n
        End If
    Next
    
    fn축제 = Join(Split(Trim(답), " "), ", ")
End Function

Private Sub CommandButton1_Click()
    TreeView1.Nodes.Clear ' 초기화
    setNode
End Sub

Private Sub CommandButton3_Click()
    MultiPage1.Value = 0
    CommandButton3.Enabled = False
    CommandButton4.Enabled = True
End Sub

Private Sub CommandButton4_Click()
    If TreeView1.SelectedItem Is Nothing Then eMsg "예약을 위해 패키지를 선택해주세요.": Exit Sub
    패키지번호 = TreeView1.SelectedItem.Index
    Label5 = IIf(TreeView1.SelectedItem.Parent Is Nothing, TreeView1.SelectedItem, TreeView1.SelectedItem.Parent)
    Label12 = Format(Sheet6.Range("E" & 패키지번호 + 1), "#,##0원")
    
    Set 이미지 = New ArrayList
    For Each i In TreeView1.Nodes
        If i.key Like "*" & Label5 & "*" And (Not i.Parent Is Nothing) Then
            패키지명 = Replace(i.key, Label5, "")
            이미지.Add Split(패키지명, ",")(0) & "\" & Split(패키지명, ",")(2)
        End If
    Next
    
    For i = 1 To IIf(이미지.Count < 4, 이미지.Count, 4)
        Me.Controls("Image" & i).Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\image\" & 이미지(i - 1) & ".jpg")
    Next
    
    MultiPage1.Value = 1
    CommandButton3.Enabled = True
    CommandButton4.Enabled = False
    
End Sub

Sub set가격()
    
    Label20 = Int(unformat(Label12) * spinMax)
    Label22 = Int(((unformat(Label12) - (unformat(Label12) * percent(TextBox6))) * SpinButton1) + ((unformat(Label12) - (unformat(Label12) * percent(TextBox7))) * SpinButton2) + ((unformat(Label12) - (unformat(Label12) * percent(TextBox8))) * SpinButton3)) * percent(TextBox5)
    Label21 = Int((Val(Label20) - Val(Label22)))
    
End Sub


Private Sub CommandButton5_Click()
    With CommandButton5
        If Not TextBox12.Visible Then
            .Left = 330
            .Width = 42
            .Caption = "적용"
            TextBox12.Visible = True
            CommandButton7.Visible = True
            Exit Sub
        End If
    End With
    
    If TextBox12 = "" Then eMsg "등록할 쿠폰을 입력해주세요.": Exit Sub
    If Not dic쿠폰.exists(TextBox12.Value) Then eMsg "존재하지 않는 쿠폰 입니다.": Exit Sub
    If dic쿠폰(TextBox12.Value).Cells(1, 0) <> [번호] Then eMsg "쿠폰의 소유자가 아닙니다!": Exit Sub
    If dic쿠폰(TextBox12.Value).Cells(1, 5) = "O" Then eMsg "이미 사용된 쿠폰 입니다!": Exit Sub
    
    If dic쿠폰(TextBox12.Value).Cells(1, 4) Like "*총금액*" Then
        TextBox5 = Format(기본할인율(0) + Val(Split(dic쿠폰(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    ElseIf dic쿠폰(TextBox12.Value).Cells(1, 4) Like "*성인*" Then
        TextBox6 = Format(기본할인율(1) + Val(Split(dic쿠폰(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    ElseIf dic쿠폰(TextBox12.Value).Cells(1, 4) Like "*청소년*" Then
        TextBox7 = Format(기본할인율(2) + Val(Split(dic쿠폰(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    Else
        TextBox8 = Format(기본할인율(3) + Val(Split(dic쿠폰(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    End If
    set가격
End Sub

Private Sub CommandButton6_Click()
    If spinMax < 1 Then eMsg "최소 1명은 예약하여야 합니다!": Exit Sub
    
    With Sheet7
        lrow = .Range("A1000").End(3).row + 1
        .Range("A" & lrow & ":G" & lrow) = Array(lrow - 1, [번호], Date, 패키지번호, SpinButton1, SpinButton2, SpinButton3)
        .Range("I" & lrow & ":L" & lrow) = Array(TextBox6, TextBox7, TextBox8, TextBox5)
        If TextBox12.Visible Then
            dic쿠폰(TextBox12.Value).Cells(1, 5) = "O"
        End If
        iMsg "예약되었습니다."
        Unload Me
    End With
    
End Sub

Private Sub CommandButton7_Click()
    With CommandButton5
        If .Caption = "적용" Then
            .Left = 300
            .Width = 72
            .Caption = "쿠폰 적용하기"
            TextBox12.Visible = False
            CommandButton7.Visible = False
            For i = 5 To 8
                Me.Controls("Textbox" & i) = Format(기본할인율(i - 5), "0%")
            Next
            set가격
        End If
    End With
    
    
End Sub

Private Sub MultiPage1_Change()
    
End Sub

Private Sub SpinButton1_Change()
     
    If spinMax >= 100 Then eMsg "총 인원을 100명을 초과할 수 없습니다.": Exit Sub
    
    TextBox9 = SpinButton1
    spinMax = SpinButton1 + SpinButton2 + SpinButton3
    set가격
    
End Sub
Private Sub SpinButton2_Change()
     
    If spinMax >= 100 Then eMsg "총 인원을 100명을 초과할 수 없습니다.": Exit Sub
    
    TextBox10 = SpinButton2
    spinMax = SpinButton1 + SpinButton2 + SpinButton3
    set가격
    
End Sub
Private Sub SpinButton3_Change()
     
    If spinMax >= 100 Then eMsg "총 인원을 100명을 초과할 수 없습니다.": Exit Sub
    
    TextBox11 = SpinButton3
    spinMax = SpinButton1 + SpinButton2 + SpinButton3
    set가격
    
End Sub


Private Sub TreeView1_DblClick()
    값 = TreeView1.SelectedItem.Text
    
    If dic패키지.exists(값) Then Exit Sub
    
    With 사진
        .Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\image\" & Split(TreeView1.SelectedItem.key, ",")(0) & "\" & TreeView1.SelectedItem.Text & ".jpg")
        .Caption = 값
        .Show
    End With
        
End Sub

Private Sub UserForm_Initialize()
    
    MultiPage1.Style = fmTabStyleNone
    
    ComboBox1.AddItem "전체"
    
    With Sheet13
        For i = 2 To .Range("A1000").End(3).row
            ComboBox1.AddItem .Range("A" & i)
        Next
    End With
    ComboBox1.ListIndex = 0
    
    With TreeView1
        .LineStyle = tvwRootLines
        
    End With
    
    
    
    TextBox4 = Date
    With Sheet2
        For i = get등급 To 2 Step -1
            Set c = .Cells(1, i)
            For k = 2 To 4
                If c.Cells(k, 1) = "" Then Exit For
                If c.Cells(k, 1) Like "*총금액*" Then
                    TextBox5 = Val(TextBox5) + Val(Split(c.Cells(k, 1), " = ")(1))
                ElseIf c.Cells(k, 1) Like "*성인*" Then
                    TextBox6 = Val(TextBox6) + Val(Split(c.Cells(k, 1), " = ")(1))
                ElseIf c.Cells(k, 1) Like "*청소년*" Then
                    TextBox7 = Val(TextBox7) + Val(Split(c.Cells(k, 1), " = ")(1))
                Else
                    TextBox8 = Val(TextBox8) + Val(Split(c.Cells(k, 1), " = ")(1))
                End If
            Next
        Next
    End With
    For i = 5 To 8
        기본할인율(i - 5) = Me.Controls("Textbox" & i)
        Me.Controls("Textbox" & i) = Format(Me.Controls("Textbox" & i), "0%")
    Next
    
    mkdic dic패키지, Sheet6.Range("B2")
    mkdic dic관광지, Sheet3.Range("A2")
    mkdic dic축제, Sheet5.Range("A2")
    mkdic dic쿠폰, Sheet9.Range("C2")
    Set dic시군구 = CreateObject("Scripting.Dictionary")
    setNode
    
End Sub

Function get등급()
    Select Case [등급]
        Case "다이아": get등급 = 5
        Case "골드": get등급 = 4
        Case "실버": get등급 = 3
        Case "브론즈": get등급 = 2
    End Select
End Function

Sub setNode()
    
    With TreeView1.Nodes
        For i = 2 To Sheet6.Range("A1000").End(3).row
            .Add key:=Sheet6.Range("B" & i), Text:=Sheet6.Range("B" & i)
        Next
        For i = 2 To Sheet6.Range("A1000").End(3).row
            Call child(i, Sheet6.Range("B" & i).Value)
        Next
    End With
    
End Sub

Sub child(row, cont)
    
    With Sheet6
        fes = Split(fn축제(.Range("B" & row).Value, .Range("C" & row).Value, ComboBox1, TextBox1, TextBox2, TextBox3), ", ")
        tou = Split(fn관광지(.Range("B" & row).Value, .Range("D" & row).Value, ComboBox1, TextBox3), ", ")
        
        cnt = 1

        For i = 0 To UBound(fes)
            TreeView1.Nodes.Add .Range("B" & row).Value, tvwChild, "축제," & cont & CStr(cnt) & "," & dic축제(Int(fes(i))).Cells(1, 3), dic축제(Int(fes(i))).Cells(1, 3)
            cnt = cnt + 1
        Next
        
        For i = 0 To UBound(tou)
            TreeView1.Nodes.Add .Range("B" & row).Value, tvwChild, "관광지," & cont & CStr(cnt) & "," & dic관광지(Int(tou(i))).Cells(1, 3), dic관광지(Int(tou(i))).Cells(1, 3)
            cnt = cnt + 1
        Next
    End With
    
End Sub
