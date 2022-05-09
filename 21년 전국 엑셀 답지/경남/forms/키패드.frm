VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 키패드 
   Caption         =   "키패드"
   ClientHeight    =   5640
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5115
   OleObjectBlob   =   "키패드.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "키패드"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(10) As New cls키패드

Private Sub CommandButton11_Click()
    If TextBox1 = "" Then eMsg "더 이상 지울 숫자가 없습니다.": Exit Sub
    TextBox1 = Left(TextBox1, Len(TextBox1) - 1)
End Sub

Private Sub CommandButton12_Click()
    TextBox1 = ""
    shuffle
End Sub

Private Sub CommandButton13_Click()
    shuffle
End Sub

Private Sub CommandButton14_Click()
    If TextBox1 = "" Then eMsg "빈칸입니다.": Exit Sub
    
    If Len(TextBox1) <> 6 Then eMsg "6자리로 입력해주세요.": TextBox1 = "": Exit Sub
    
    Unload Me
    시험접수.TextBox1.ForeColor = vbBlack
    시험접수.TextBox1 = Me.TextBox1
    
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    k = 11
    For Each i In Split("← 초기화 재배열 확인")
        Me.Controls("CommandButton" & k).Caption = i
        k = k + 1
    Next
    
    For i = 1 To 10
        Set cls(i).cb = Me.Controls("CommandButton" & i)
    Next
    TextBox1.Locked = True
    
    shuffle
    
End Sub

Sub shuffle()
    
    Randomize
    arr = Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    For i = 0 To UBound(arr)
        J = ((UBound(arr) - i) * Rnd) + i
        tmp = arr(i)
        arr(i) = arr(J)
        arr(J) = tmp
    Next
    
    For i = 1 To 10
        Me.Controls("CommandButton" & i).Caption = arr(i - 1)
    Next
    
End Sub
