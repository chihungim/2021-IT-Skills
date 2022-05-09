VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls달력"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents btn As CommandButton
Attribute btn.VB_VarHelpID = -1

Sub btn_Click()
    iMsg DateSerial(Val(달력.년), Val(달력.월), Val(btn.Caption))
End Sub
