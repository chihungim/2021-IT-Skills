VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 결제 
   Caption         =   "결제"
   ClientHeight    =   3825
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10575
   OleObjectBlob   =   "결제.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "결제"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim hap

Private Sub CheckBox1_Click()
    If CheckBox1 Then
        TextBox1 = [포인트]
    Else
        TextBox1 = "0"
    End If
    
    Call update
End Sub

Sub update()
    lbl포인트 = Val(TextBox1)
    총금액 = hap - Val(TextBox1)
    lbl총결제금 = Format(총금액, "#,##0")
End Sub

Private Sub CommandButton1_Click()

    mkDic dic도서, Sheet4.Range("B2")
    mkDic dic행사, Sheet7.Range("B2")

    Set arr행사 = New ArrayList
    
    With ListView1
        For i = 1 To ListView1.ListItems.Count
            irow = Sheet5.Range("A1000").End(xlUp).row + 1
            MsgBox irow
            도서코드 = .ListItems(i).Text
            갯수 = .ListItems(i).SubItems(3)
            
            If dic행사.exists(도서코드) Then
                Dim 행사row As Range
                Set 행사row = dic행사(도서코드)
                If Not 행사row.Rows.Hidden Then
                    arr행사.Add 행사row
                    MsgBox 행사row.Cells(1, 1)

                End If
            End If
            
            dic도서(도서코드).Cells(1, 8) = dic도서(도서코드).Cells(1, 8) - .ListItems(i).SubItems(3)
            
            If 구매모드 Then
                
                For J = 2 To Sheet5.Range("A1000").End(xlUp).row
                    If Not Sheet5.Cells(J, "A").Rows.Hidden Then
                        If Sheet5.Cells(J, "C") = 도서코드 Then
                            Sheet5.Cells(J, "C").EntireRow.Delete xlShiftUp
                        End If
                    End If
                Next
            End If
            
            Sheet5.Range("A" & irow & ":I" & irow) = Array(irow - 1, [아이디], 도서코드, Date, Val(갯수), "", "", lbl총결제금, IIf(Val(TextBox1) > 0, "0", dic도서(도서코드).Cells(1, 9) * 갯수))
        Next
    End With
    
    If arr행사.Count > 0 Then
        If MsgBox("구매목록중 행사상품이 있습니다.", vbInformation + vbYesNo, "행사참여") Then
            행사.Show
            Exit Sub
        End If
    End If
    
    
    
    If MsgBox("영수증 출력", vbInformation + vbYesNo, "영수증 출력") = vbYes Then
     
    Else
        Sheet1.Select
    End If
End Sub

Private Sub TextBox1_Change()
    If TextBox1 Like "*[!0-9]*" Then eMsg "문자는 입력이 불가능합니다.": TextBox1 = "0": Exit Sub
    If Val(TextBox1) > [포인트] Then eMsg "포인트가 부족합니다.": TextBox1 = "0": Exit Sub
    If Val(TextBox1) > hap Then TextBox1 = hap & ""
    Call update
End Sub

Private Sub UserForm_Initialize()
    With ListView1
        .View = lvwReport
        .Gridlines = True
        With .ColumnHeaders
            .Add , , "도서코드", 50
            .Add , , "도서명", 80
            .Add , , "판매가", 50
            .Add , , "수량", 50
            .Add , , "가격", 50
        End With
    End With
    
    
    For Each v In arr구매
        arr = v
        With ListView1.ListItems.Add(, , arr(0))
            For i = 1 To 4
                .SubItems(i) = arr(i)
            Next
        End With
    Next
    
    
    For i = 1 To ListView1.ListItems.Count
        hap = hap + Val(ListView1.ListItems(i).SubItems(4))
    Next
    
    TextBox1 = "0"
    
    lbl주문금 = Format(hap, "#,##0")
    lbl포인트 = "0"
    lbl총결제금 = lbl주문금
    
End Sub
