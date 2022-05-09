VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "FrmClass"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents btn As CommandButton
Attribute btn.VB_VarHelpID = -1
Public obj

Private Sub btn_Click()
    If btn.Caption = "재배열" Then
        비밀번호.랜덤
    ElseIf btn.Caption = "정정" Then
        If pw <> "" Then
            pw = Left(pw, Len(pw) - 1)
            obj.TextBox1.Text = pw
        End If
    ElseIf btn.Caption = "입력" Then
        회원가입.TextBox3 = pw
        Unload obj
    ElseIf btn.Caption = "X" Then
        Debug.Print "아무동작도 안하는 래훙"
    Else
        pw = pw + btn.Caption
        obj.TextBox1.Text = pw
    End If


    
End Sub
