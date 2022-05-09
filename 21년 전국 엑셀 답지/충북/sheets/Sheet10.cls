VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet10"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_Activate()
    Sheet6.Range("U1").Calculate
    Sheet6.Range("V1").Calculate
    Sheet6.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet13.Range("AB1:AC2"), Sheet13.Range("AF1:AO1"), False
End Sub

