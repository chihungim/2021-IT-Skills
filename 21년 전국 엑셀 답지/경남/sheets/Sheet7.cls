VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet7"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_BeforeRightClick(ByVal Target As Range, Cancel As Boolean)
    
    If (Target.Column = 1) Then
        For Each i In Application.CommandBars("Cell").Controls
            i.Delete
        Next
        
        Set btn = Application.CommandBars("Cell").Controls.Add(msoControlButton)
        
        If Target.Value = "" Then
            With btn
                .Caption = "문제 출제하기"
                .FaceId = 176
                .OnAction = "출제"
            End With
        Else
            With btn
                .Caption = "삭제하기"
                .FaceId = 67
                .OnAction = "'삭제 """ & Target.Row & """'"
            End With
        End If
        
        
        
        
    Else
        Application.CommandBars("Cell").Reset
    End If
    
End Sub

Private Sub Worksheet_Deactivate()
    Application.CommandBars("Cell").Reset
End Sub

Private Sub Worksheet_SelectionChange(ByVal Target As Range)
    
End Sub
