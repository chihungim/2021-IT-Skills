VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 문제출제 
   Caption         =   "문제출제"
   ClientHeight    =   8235
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6945
   OleObjectBlob   =   "문제출제.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "문제출제"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    If ComboBox1.ListIndex = 0 Then
        ComboBox2.AddItem "단일"
    ElseIf ComboBox1.ListIndex = 1 Then
        ComboBox2.AddItem "1급"
        ComboBox2.AddItem "2급"
    Else
        ComboBox2.AddItem "1급"
        ComboBox2.AddItem "2급"
        ComboBox2.AddItem "3급"
    End If
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton2_Click()
    If ComboBox1.ListIndex = -1 Or ComboBox2.ListIndex = -1 Then eMsg "종목을 선택해주세요.": Exit Sub
    
    For i = 1 To 5
        If Me.Controls("Textbox" & i) = "" Then eMsg "빈 항목이 있습니다.": Exit Sub
    Next
    
    If OptionButton1 = False And OptionButton2 = False And OptionButton3 = False And OptionButton4 = False Then eMsg "정답을 선택해 주세요.": Exit Sub
    
    정답 = ""
    For i = 1 To 4
        If Me.Controls("OptionButton" & i) Then
            정답 = i
            Exit For
        End If
    Next
    
    With Sheet7
        lrow = .Range("A10000").End(3).Row + 1
        .Range("A" & lrow & ":D" & lrow) = Array(lrow - 1, get종목번호, TextBox1, 정답)
    End With
    
    With Sheet8
        lrow = .Range("A10000").End(3).Row + 1
        .Range("B" & lrow & ":E" & lrow) = Array(TextBox2, TextBox3, TextBox4, TextBox5)
    End With
    
    iMsg "문제 출제가 완료되었습니다.": Exit Sub
    
End Sub

Function get종목번호()
    For i = 2 To Sheet3.Range("A1000").End(3).Row
        If Sheet3.Range("B" & i) & Sheet3.Range("C" & i) = ComboBox1 & ComboBox2 Then
            get종목번호 = Sheet3.Range("A" & i)
            Exit Function
        End If
    Next
End Function

Private Sub UserForm_Click()
    
End Sub

Private Sub UserForm_Initialize()
    
    For i = 2 To Sheet3.Range("A100").End(3).Row
        ComboBox1.AddItem Sheet3.Range("B" & i)
    Next
    
    ComboBox1.Style = fmStyleDropDownList
    ComboBox2.Style = fmStyleDropDownList
    
End Sub
