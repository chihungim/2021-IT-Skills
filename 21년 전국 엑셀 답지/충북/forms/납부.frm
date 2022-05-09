VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 납부 
   Caption         =   "납부"
   ClientHeight    =   6450
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4560
   OleObjectBlob   =   "납부.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "납부"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim list As ArrayList

Private Sub ComboBox1_Change()
    If ComboBox1.ListIndex <> -1 Then
    
        With list(ComboBox1.ListIndex)
            TextBox1 = Format(.Cells(1, 8), "#,##0원")
            Debug.Print .Cells(1, 8)
            TextBox2 = IIf(.Cells(1, 10) = 0, "일시불", .Cells(1, 10) & "개월")
            
            If TextBox2 = "일시불" Then
                If .Cells(1, 9) = 0 Then TextBox3 = "납부 안됨"
            Else
                TextBox3 = .Cells(1, 9) & "회차 납부 / " & .Cells(1, 10) - .Cells(1, 9) & "회차 남음"
            End If
            
            TextBox4 = .Cells(1, 6)
        End With
    
    End If
End Sub

Private Sub CommandButton1_Click()
    If ComboBox1.ListIndex = -1 Then eMsg "납부할 결제건을 선택해야 합니다.": Exit Sub
    
    pw = ""
    비밀번호.Show
        If Not 비밀번호flag Then eMsg "2차 비밀번호가 일치하지 않습니다.": Exit Sub
        
        If Val(TextBox1) > [잔액] Then eMsg "잔액이 부족합니다.": Exit Sub
        With list(ComboBox1.ListIndex)
        
        If TextBox2 = "일시불" Then
            iMsg "납부를 완료했습니다."
        Else
            iMsg .Cells(1, 10) & "회차 중 " & .Cells(1, 9) + 1 & "회차를 납부했습니다."
        End If
    End With
    Sheet1.Range("J" & [번호] + 1) = Sheet1.Range("J" & [번호] + 1) - Val(TextBox1)
End Sub


Private Sub UserForm_Initialize()

    Set list = New ArrayList

    With Sheet6
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("C" & i) = [번호] And .Range("F" & i) <> "" And .Range("J" & i) - .Range("I" & i) >= 0 Then
                ComboBox1.AddItem Format(.Range("B" & i), "yy-MM-dd") & " " & Sheet5.Range("C" & .Range("G" & i) + 1)
                list.Add Range("A" & i)
            End If
        Next
    End With
End Sub
