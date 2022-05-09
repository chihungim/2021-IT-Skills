VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 수량선택 
   Caption         =   "수량선택"
   ClientHeight    =   3015
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4095
   OleObjectBlob   =   "수량선택.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "수량선택"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim 가격, 갯수, 도서코드

Private Sub CommandButton1_Click()
    
    Set arr구매 = New ArrayList
    
    mkDic dic도서, Sheet4.Range("B2")
    
    If 구매모드 Then
        arr구매.Add Array(도서코드, 도구.도서명, 가격, Val(TextBox1), Val(TextBox1) * 가격)
        Unload Me
        결제.Show
    Else
        With Sheet6
            If Val(.Range("G1")) = 20 Then
                eMsg "장바구니가 꽉 찼습니다."
                Exit Sub
            End If
            Dim 장바구니row As Range
            
            For i = 2 To Sheet6.Range("A10000").End(xlUp).row
                If Not Sheet6.Cells(i, "A").Rows.Hidden Then
                    If Sheet6.Cells(i, "C") = 도서코드 Then
                        Set 장바구니row = Sheet6.Range("C" & i)
                    End If
                End If
            Next
                
            If Not 장바구니row Is Nothing Then
                장바구니row.Cells(1, 2) = 장바구니row.Cells(1, 2) + Val(TextBox1)
            Else
                irow = .Range("A1000").End(xlUp).row + 1
                .Range("A" & irow & ":D" & irow) = Array(irow - 1, [아이디], 도서코드, Val(TextBox1))
            End If
            
            dic도서(도서코드).Cells(1, 8) = dic도서(도서코드).Cells(1, 8) - Val(TextBox1)
        
            If MsgBox("계속 검색하시겠습니까?", vbInformation + vbYesNo, "정보") = vbYes Then
                Unload Me
                도서검색.Show
            Else
                Unload Me
                장바구니.Show
            End If
        End With
    End If
End Sub

Private Sub CommandButton2_Click()
    Unload Me
    도서검색.Show
End Sub

Private Sub TextBox1_Change()
    

    If TextBox1 = "" Then Exit Sub
    
    
    If Val(TextBox1) < 1 Or TextBox1 Like "*[!0-9]*" Then eMsg "수량은 1 이상의 숫자만 입력가능합니다.": TextBox1 = "": Exit Sub
    If Val(TextBox1) > 갯수 Then eMsg "재고보다 많습니다": Exit Sub
    후가격 = Format(가격 * Val(TextBox1), "#,##0")
End Sub

Private Sub TextBox1_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If TextBox1 = "" Then TextBox1 = "1"
End Sub

Private Sub UserForm_Initialize()
    Me.도서명 = 도구.도서명
    
    가격 = dic도서(도구.도서명).Cells(1, 4)
    도서코드 = dic도서(도구.도서명).Cells(1, -2)
    갯수 = dic도서(도구.도서명).Cells(1, 5)
    Me.원가격 = Format(가격, "#,##0")
    TextBox1 = "1"
    후가격 = Format(가격 * Val(TextBox1), "#,##0")
End Sub
