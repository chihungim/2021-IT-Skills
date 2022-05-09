VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 상세정보 
   Caption         =   "상세정보"
   ClientHeight    =   8025
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4560
   OleObjectBlob   =   "상세정보.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "상세정보"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public imgNo
Dim purchaseList As New ArrayList
Private Sub CommandButton1_Click()
    For i = 2 To Sheet6.Range("A1000").End(xlUp).Row
        If Sheet6.Range("C" & i) = Sheet1.Range("번호") Then purchaseList.Add Sheet6.Range("B" & i).Value
    Next
    
    If purchaseList.Contains(imgNo) Then
        eMsg "이미 구해한 작품입니다."
        Exit Sub
    End If
    
    irow = Sheet6.Range("A1000").End(xlUp).Row + 1
    
    Sheet6.Range("A" & irow & ":C" & irow) = Array(irow - 1, imgNo, Sheet1.Range("번호"))
    Sheet6.Range("D" & irow).FillDown
    Sheet6.Range("E" & irow).FillDown
    Sheet6.Range("F" & irow).FillDown
    
    iMsg "구매가 완료되었습니다."
End Sub

Private Sub CommandButton2_Click()
    With 작품평
        .TextBox1 = Me.TextBox2
        .TextBox2 = Me.TextBox1
        For i = 2 To Sheet4.Range("A1000").End(xlUp).Row
            With Sheet4
                If .Range("B" & i) = imgNo And .Range("C" & i) = Sheet1.Range("번호") Then
                    irow = i
                    작품평.SpinButton1.Value = .Range("D" & irow) * 2
                    Debug.Print .Range("D" & irow) * 2
                    작품평.평점 = 작품평.SpinButton1.Value / 2
                    작품평.TextBox3 = .Range("E" & irow)
                    작품평.CommandButton1.Caption = "수정 완료"
                End If
            End With
        Next
        .imgNo = Me.imgNo
        .Show
    End With
End Sub

Private Sub CommandButton3_Click()
    Hide
End Sub

Private Sub UserForm_Initialize()
    
    If Sheet1.Range("분류").Value <> "일반" Then
        CommandButton1.Enabled = False
        CommandButton2.Enabled = False
    End If
    
    For i = 1 To 5
        Me.Controls("TextBox" & i).Enabled = False
    Next
    
End Sub

