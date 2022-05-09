VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls키패드"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents cb As CommandButton
Attribute cb.VB_VarHelpID = -1

Private Sub cb_Click()
    키패드.TextBox1 = 키패드.TextBox1 & cb.Caption
End Sub
