VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 카드지갑 
   Caption         =   "카드지갑"
   ClientHeight    =   6525
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8505
   OleObjectBlob   =   "카드지갑.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "카드지갑"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim wal As Wallet


Private Sub UserForm_Initialize()
    cnt = 0
    With Sheet4
        For i = 2 To .Range("A1000").End(3).Row
            m = 0
            If Val(.Range("D" & i)) = Val([번호]) Then
                For J = 2 To Sheet6.Range("A1000").End(3).Row
                    If Sheet6.Range("F" & J) = .Range("A" & J) And Month(Sheet6.Range("B" & J)) = Month(Date) - 1 Then
                        m = m + Int(Sheet6.Range("H" & J))
                    End If
                Next
                
                MultiPage1.Pages.Add
                MultiPage1.Pages(cnt).Caption = "카드 " & cnt + 1
                
                Set wal = New Wallet
                Call wal.init(Me, cnt, .Range("B" & i), .Range("F" & i), .Range("A" & i), "전월 실적 : " & Format(m, "#,##0원"), "카드 포인트 : " & Format(.Range("E" & i), "#,##0"), "해외 결제 브랜드 : " & Sheet3.Range("I" & .Range("C" & i) + 1))
                
                cnt = cnt + 1
            End If
        Next
    End With
    
End Sub

