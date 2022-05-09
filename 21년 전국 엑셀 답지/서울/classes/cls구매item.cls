VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls±¸¸Åitem"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public fno, fname, fexp, fprice

Sub init(fno, fname, fexp, fprice)
    Me.fno = fno: Me.fname = fname: Me.fexp = fexp: Me.fprice = fprice
End Sub
