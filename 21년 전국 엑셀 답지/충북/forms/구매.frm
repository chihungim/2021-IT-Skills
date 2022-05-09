VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 구매 
   Caption         =   "구매"
   ClientHeight    =   5745
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9600
   OleObjectBlob   =   "구매.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "구매"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public dic상품 As Object
Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox3_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton1_Click()
    search
End Sub

Private Sub CommandButton2_Click()

    If ComboBox2 = "" Then eMsg "카드를 선택해야 합니다.": Exit Sub
    
    If ComboBox3 = "" Then eMsg "할부여부를 선택해야 합니다.": Exit Sub
    
    pw = ""
    비밀번호.Show
    
    If Not 비밀번호flag Then eMsg "2차 비밀번호가 일치하지 않습니다.": Exit Sub
    
    가격 = Int(ListBox1.list(ListBox1.ListIndex, 1))
    
    mkdic dic, Sheet4.Range("A2")
    If Int(dic(ComboBox2.Value).Cells(1, 5)) > 가격 Then
        If MsgBox("포인트로 구매가 가능합니다." & vbCrLf & "포인트로 구매하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
            With Sheet6
                lrow = .Range("A1000").End(3).Row + 1
                .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, Now, [번호], "", "", ComboBox2, dic상품(ListBox1.list(ListBox1.ListIndex)).Cells(1, -1), 가격, 0, 0)
                dic(ComboBox2.Value).Cells(1, 5) = Int(dic(ComboBox2.Value).Cells(1, 5)) - 가격
                iMsg "결제가 완료되었습니다."
                Exit Sub
            End With
        End If
    End If
    
    
    If ListBox1.list(ListBox1.ListIndex, 2) = "예" Then
        수수료 = Split(Sheet3.Cells(1 + dic(ComboBox2.Value).Cells(1, 3), 9 + Sheet2.Range("H" & dic(ComboBox2.Value).Cells(1, 2) + 1)), "/")(1)
        가격 = 가격 + (가격 * Split(수수료, "%")(0) / 100)
    End If
    
    If ComboBox3.ListIndex <> 0 Then
        할부 = Sheet2.Range("F" & dic(ComboBox2.Value).Cells(1, 2) + 1)
        가격 = 가격 + Evaluate(가격 * 할부)
    End If
    
    혜택 = Split(Sheet2.Range("E" & dic(ComboBox2.Value).Cells(1, 2) + 1), ",")
    For Each i In 혜택
        If Sheet3.Range("F" & i) >= 전월실적 Then
            If Sheet3.Range("B" & i) = "할인" Then
                If InStr(Sheet3.Range("D" & i), ComboBox1) > 0 Then
                    가격 = 가격 - Evaluate(가격 * Sheet3.Range("E" & i))
                End If
            Else
                If InStr(Sheet3.Range("D" & i), ComboBox1) > 0 Then
                    적립 = Evaluate(가격 * Sheet3.Range("E" & i))
                End If
            End If
        End If
    Next
    
    
    
    If MsgBox(Format(가격, "#,##0원") & "입니다." & vbCrLf & "구매하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
        With Sheet6
            lrow = .Range("A1000").End(3).Row + 1
            .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, Now, [번호], "", "", ComboBox2, dic상품(ListBox1.list(ListBox1.ListIndex)).Cells(1, -1), 가격, 0, Val(ComboBox3))
            dic(ComboBox2.Value).Cells(1, 5) = Int(dic(ComboBox2.Value).Cells(1, 5)) + 적립
            iMsg "결제가 완료되었습니다."
        End With
    Else
        Exit Sub
    End If
    
    Unload Me
End Sub

Function 전월실적()
        
    m = 0
    For J = 2 To Sheet6.Range("A1000").End(3).Row
        If Sheet6.Range("F" & J) = Sheet4.Range("A" & J) And Month(Sheet6.Range("B" & J)) = Month(Date) - 1 Then
            m = m + Int(Sheet6.Range("H" & J))
        End If
    Next
    전월실적 = m
End Function

Sub 결제()
    
    With Sheet6
        lrow = .Range("A1000").End(3).Row + 1
        mkdic dic, Sheet5.Range("C2")
        .Range("A" & lrow & ":J" & lrow) = Array()
    End With
    
End Sub

Private Sub ListBox1_Click()

End Sub

Private Sub UserForm_Click()

End Sub

Sub search()
    
    With Sheet5
        .Range("I2") = ComboBox1
        .Range("J2") = TextBox1
        
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("I1:J2"), .Range("L1:O1")
        For i = 2 To .Range("O1000").End(3).Row
            .Range("O" & i) = IIf(.Range("O" & i) = 0, "아니오", "예")
        Next
        
        ListBox1.RowSource = "상품목록!M2:O" & .Range("O1000").End(3).Row
        
    End With
    
End Sub

Private Sub UserForm_Initialize()
    
    Set dic상품 = mkdic(Sheet5.Range("C2"))
    상품 = Array("외식", "여행", "교통", "쇼핑")
    For Each i In 상품
        ComboBox1.AddItem i
    Next
    ComboBox1.ListIndex = 0
    
    For i = 2 To Sheet4.Range("A1000").End(3).Row
        If Sheet4.Range("D" & i) = [번호] Then
            ComboBox2.AddItem Sheet4.Range("A" & i)
        End If
    Next
    
    
    ComboBox3.AddItem "일시불"
    For i = 1 To 12
        ComboBox3.AddItem i & "개월"
    Next
    
    search
    
End Sub

