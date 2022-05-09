VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 도서대출 
   Caption         =   "UserForm1"
   ClientHeight    =   6075
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7545
   OleObjectBlob   =   "도서대출.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "도서대출"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(5) As cls대출아이템
Public 책카운트 As Integer

Private Sub CommandButton1_Click()
    If Val(Label4) = 0 Then eMsg "대출할 도서가 선택되지 않았습니다.": Exit Sub
    If Val(Label3) < Val(Label4) Then eMsg "대출 권 수를 초과하였습니다.": Exit Sub
    
    If MsgBox(Val(Label4) & "권 도서를 대출하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
        
    End If
End Sub

Private Sub Frame1_Click()

End Sub

Private Sub UserForm_Initialize()
    자료처리초기화 Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    mkdic dic멤버, Sheet1.[a2]
    
    TextBox1.Value = Format(Date + 14, "yyyy년 mm월 dd일") & " (" & WeekdayName(Weekday(Date + 14)) & ")"
    
    cnt = 5
    
    For i = 0 To cnt - 1
        Set cls(i) = New cls대출아이템
        cls(i).대출init Frame1, ThisWorkbook.Path & "\지급자료\이미지\도서\3 2 1.jpg", "로그아웃", 10, (i * 120) + 10
    Next
    
    Max = (125 * cnt) + 10
    
    If 3 < cnt Then
        Frame1.ScrollBars = fmScrollBarsHorizontal
        Frame1.ScrollWidth = Max
    End If
    
    Label10 = dic멤버(Sheet11.[회원아이디].Value).Cells(1, 3)
End Sub

