VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet12"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Dim shtc As New shtChart

Private Sub Worksheet_Activate()
    Shapes("그룹 8").Visible = msoFalse
    Set shtc.cht = ChartObjects("차트 0").Chart
End Sub


