VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ī������ 
   Caption         =   "ī������"
   ClientHeight    =   6525
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8505
   OleObjectBlob   =   "ī������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ī������"
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
            If Val(.Range("D" & i)) = Val([��ȣ]) Then
                For J = 2 To Sheet6.Range("A1000").End(3).Row
                    If Sheet6.Range("F" & J) = .Range("A" & J) And Month(Sheet6.Range("B" & J)) = Month(Date) - 1 Then
                        m = m + Int(Sheet6.Range("H" & J))
                    End If
                Next
                
                MultiPage1.Pages.Add
                MultiPage1.Pages(cnt).Caption = "ī�� " & cnt + 1
                
                Set wal = New Wallet
                Call wal.init(Me, cnt, .Range("B" & i), .Range("F" & i), .Range("A" & i), "���� ���� : " & Format(m, "#,##0��"), "ī�� ����Ʈ : " & Format(.Range("E" & i), "#,##0"), "�ؿ� ���� �귣�� : " & Sheet3.Range("I" & .Range("C" & i) + 1))
                
                cnt = cnt + 1
            End If
        Next
    End With
    
End Sub

