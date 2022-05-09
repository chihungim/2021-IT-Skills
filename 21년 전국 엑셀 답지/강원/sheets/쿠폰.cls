VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet9"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_BeforeRightClick(ByVal Target As Range, Cancel As Boolean)
    
    r = Target.row: c = Target.Column
    
    For Each i In Application.CommandBars("Cell").Controls
        i.Delete
    Next
    
    If Cells(r, c) = "" Or c <> 1 Then Application.CommandBars("Cell").Reset: Exit Sub
    
    If [번호] = "admin" Then
         With Application.CommandBars("Cell").Controls.Add
                .FaceId = 1552
                .Caption = "쿠폰 제거"
                .OnAction = "'쿠폰제거 """ & r & """'"
        End With
        Set main = Application.CommandBars("Cell").Controls.Add(msoControlPopup)
        With main
            .Caption = "쿠폰 생성"
            frow = Target.row
            
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 71
            .Caption = "총금액 할인"
            .OnAction = "'쿠폰생성기 ""0'"
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 72
            .Caption = "성인 할인"
            .OnAction = "'쿠폰생성기 ""1'"
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 73
            .Caption = "청소년 할인"
            .OnAction = "'쿠폰생성기 ""2'"
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 74
            .Caption = "어린이 할인"
            .OnAction = "'쿠폰생성기 ""3'"
        End With
    Else
        With Application.CommandBars("Cell").Controls.Add
            .Caption = "쿠폰 발급"
            .FaceId = 1000
            .OnAction = "쿠폰발급"
        End With
    End If
    
End Sub

Private Sub Worksheet_Deactivate()
    Application.CommandBars("Cell").Reset
End Sub
