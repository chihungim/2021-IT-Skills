VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 카드신청 
   Caption         =   "카드 신청"
   ClientHeight    =   9240
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   11610
   OleObjectBlob   =   "카드신청.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "카드신청"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim dic카드이름 As Object
Dim sum

Private Sub ComboBox1_Change()
    Set dic카드이름 = mkdic(Sheet2.Range("B2"))
    
    카드설명 = dic카드이름(카드명.Caption).Cells(1, 2)
    타입 = dic카드이름(카드명.Caption).Cells(1, 3)
    이자 = Format(dic카드이름(카드명.Caption).Cells(1, 5), "#.#0%")
    등급 = dic카드이름(카드명.Caption).Cells(1, 7) & "등급"
    
    기본연회비 = Format(dic카드이름(카드명.Caption).Cells(1, 8), "#,##0원")
    
    Set dic = mkdic(Sheet3.Range("I2"))
    추가연회비 = Format(Val(Sheet3.Cells(dic(ComboBox1.Value).Row, 9 + Val(등급.Caption))), "#,##0원")
    sum = dic카드이름(카드명.Caption).Cells(1, 8) + Val(Sheet3.Cells(dic(ComboBox1.Value).Row, 9 + Val(등급.Caption)))
    
    총연회비 = sum
    
    혜택 = Split(dic카드이름(카드명.Caption).Cells(1, 4), ",")
    
    For i = 0 To UBound(혜택)
        st = "전월실적 " & Format(Sheet3.Range("F" & 혜택(i) + 1), "#,##0원") & " 이상 만족시, "
        If Sheet3.Range("B" & 혜택(i) + 1) = "할인" Then
            할인 = st & Sheet3.Range("C" & 혜택(i) + 1) & vbCrLf & "(" & Sheet3.Range("D" & 혜택(i) + 1) & " 카테고리 한정, 최대 " & Format(Sheet3.Range("E" & 혜택(i) + 1), "#%") & " 할인)"
        Else
            적립 = st & Sheet3.Range("C" & 혜택(i) + 1) & vbCrLf & "(" & Sheet3.Range("D" & 혜택(i) + 1) & " 카테고리 한정, 최대 " & Format(Sheet3.Range("E" & 혜택(i) + 1), "#%") & " 적립)"
        End If
    Next
End Sub


Private Sub UserForm_Terminate()
    If Not 신청flag Then eMsg "카드가 정상적으로 발급되지 않았습니다.."
End Sub
