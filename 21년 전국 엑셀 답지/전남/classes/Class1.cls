VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Class1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents cb As CommandButton
Attribute cb.VB_VarHelpID = -1

Private Sub cb_Click()
    If 달력.시작일 = "" Then
        행사이벤트.TextBox3 = DateSerial(달력.ComboBox1, 달력.Label2, cb.Caption)
    Else
        행사이벤트.TextBox4 = DateSerial(달력.ComboBox1, 달력.Label2, cb.Caption)
    End If
    Unload 달력
End Sub

