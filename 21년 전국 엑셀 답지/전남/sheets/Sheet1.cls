VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Dim 버튼(1 To 8) As cls버튼
 

Public Sub Worksheet_Activate()
    For i = 1 To 8
        Set 버튼(i) = New cls버튼
        Set 버튼(i).btn = OLEObjects("CommandButton" & i).object
    Next
End Sub

