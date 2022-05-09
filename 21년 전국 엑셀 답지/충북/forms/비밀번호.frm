VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 비밀번호 
   Caption         =   "비밀번호"
   ClientHeight    =   3315
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7815
   OleObjectBlob   =   "비밀번호.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "비밀번호"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim frm(1 To 15) As New FrmClass
Sub 랜덤()
    With Sheet13.Sort
        .SetRange Range("AR2:AS16")
        .Header = xlNo
        .MatchCase = False
        .Orientation = xlTopToBottom
        .SortMethod = xlPinYin
        .Apply
    End With
    For i = 1 To 15
        With Sheet13
            Me.Controls("CommandButton" & i).Caption = .Cells(i + 1, "AS").Value
        End With
    Next
End Sub

Private Sub TextBox1_Change()

End Sub

Private Sub TextBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub UserForm_Initialize()
    랜덤
    
    For i = 1 To 15
        Set frm(i).obj = Me
        Set frm(i).btn = Me.Controls("CommandButton" & i)
    Next
End Sub

Private Sub UserForm_Terminate()
    pw = ""
End Sub
