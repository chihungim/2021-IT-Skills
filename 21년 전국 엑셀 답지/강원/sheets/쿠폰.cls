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
    
    If [��ȣ] = "admin" Then
         With Application.CommandBars("Cell").Controls.Add
                .FaceId = 1552
                .Caption = "���� ����"
                .OnAction = "'�������� """ & r & """'"
        End With
        Set main = Application.CommandBars("Cell").Controls.Add(msoControlPopup)
        With main
            .Caption = "���� ����"
            frow = Target.row
            
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 71
            .Caption = "�ѱݾ� ����"
            .OnAction = "'���������� ""0'"
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 72
            .Caption = "���� ����"
            .OnAction = "'���������� ""1'"
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 73
            .Caption = "û�ҳ� ����"
            .OnAction = "'���������� ""2'"
        End With
        With main.Controls.Add(msoControlButton)
            .FaceId = 74
            .Caption = "��� ����"
            .OnAction = "'���������� ""3'"
        End With
    Else
        With Application.CommandBars("Cell").Controls.Add
            .Caption = "���� �߱�"
            .FaceId = 1000
            .OnAction = "�����߱�"
        End With
    End If
    
End Sub

Private Sub Worksheet_Deactivate()
    Application.CommandBars("Cell").Reset
End Sub
