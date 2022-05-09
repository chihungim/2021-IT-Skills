VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet2"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub CommandButton1_Click()
    Sheet1.Select
End Sub

Private Sub Worksheet_Activate()
    Range("P4") = 3
    Call box_evnt
End Sub

Sub box_evnt()
    Application.ScreenUpdating = False
    
    For i = 2 To Sheet2.Range("A1000").End(xlUp).row
        Range("A" & i & ":L" & i).Interior.Color = vbWhite
        If fn³ªÀÌ´ë(Cells(i, "K")) = Range("Q4").Value Then Range("A" & i & ":L" & i).Interior.Color = vbGreen
    Next
    
    Application.ScreenUpdating = True
End Sub


