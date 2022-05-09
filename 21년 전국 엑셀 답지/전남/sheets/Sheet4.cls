VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet4"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub ComboBox1_Change()
    Sheet4.Range("A1").CurrentRegion.AutoFilter 3, ComboBox1
End Sub

Private Sub CommandButton1_Click()
    Sheet1.Select
End Sub

Private Sub Worksheet_Activate()
    If [타입] <> "관리자" Then
        ComboBox1.Visible = False
        Sheet4.Range("A1").CurrentRegion.AutoFilter Field:=3
    Else
        ComboBox1.Visible = True
        ComboBox1.List = Split("문학,철학,과학,역사,예술,종교,여행,정치,경제", ",")
        ComboBox1.ListIndex = 2
        Sheet4.Range("A1").CurrentRegion.AutoFilter 3, ComboBox1
        
    End If
End Sub

Private Sub Worksheet_BeforeRightClick(ByVal Target As Range, Cancel As Boolean)
    
    On Error Resume Next

    
    For i = 1 To Application.CommandBars("Cell").Controls.Count
        Application.CommandBars("Cell").Controls(1).Delete
    Next
    
    For i = 1 To Application.CommandBars("List Range PopUp").Controls.Count
        Application.CommandBars("List Range PopUp").Controls(1).Delete
    Next
    
    If [타입] <> "관리자" Then
        Application.CommandBars("Cell").Reset
        Application.CommandBars("List Range PopUp").Reset
    Else
        If Target.Column = 1 Then
            If Target.row = Range("A10000").End(xlUp).row + 1 Then
                With Application.CommandBars("Cell").Controls.Add
                    .FaceId = 2171
                    .Caption = "도서등록"
                    .OnAction = ""
                End With
            Else
                Application.CommandBars("Cell").Reset
            End If
            
            If Target <> "" Then
                With Application.CommandBars("List Range PopUp").Controls.Add
                    .FaceId = 6512
                    .Caption = "도서수정"
                    .OnAction = ""
                End With
            End If
        Else
            Application.CommandBars("Cell").Reset
            Application.CommandBars("List Range PopUp").Reset
        End If
    End If
    
End Sub
