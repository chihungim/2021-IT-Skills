VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "clsFindBook"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents 체크 As MSForms.Label
Attribute 체크.VB_VarHelpID = -1
Public WithEvents 위치출력 As MSForms.Label
Attribute 위치출력.VB_VarHelpID = -1
Public WithEvents 페이지 As MSForms.Label
Attribute 페이지.VB_VarHelpID = -1


Private Sub 위치출력_Click()
    n = "번호" & Right(위치출력.name, 1)
    
    Set rg = Sheet7.Range("AQ" & Sheet7.Shapes(n).TextFrame.Characters.Text + 1)
    Sheet9.Range("I2") = rg.Cells(1, 3)
    Sheet9.Range("I3") = rg.Cells(1, 8)
    Sheet9.Range("I4") = rg.Cells(1, 2)
    Sheet9.Range("I5") = rg.Cells(1, 4)
    
    For Each shp In Sheet9.ChartObjects(1).Chart.Shapes
        shp.Delete
    Next
    
    Sheet9.Shapes("그림 1").Copy
    Sheet9.ChartObjects(1).Chart.Paste
    
    Sheet9.Shapes("청구기호출력").Copy
    Sheet7.Paste
    
End Sub

Private Sub 체크_Click()
    
    If 체크.Caption = ChrW(163) Then
        Sheet7.Range("AQ" & Sheet7.Shapes("번호" & Right(체크.name, 1)).TextFrame.Characters.Text + 1) = "O"
        체크.Caption = ChrW(82)
        체크.ForeColor = vbBlue
    Else
        Sheet7.Range("AQ" & Sheet7.Shapes("번호" & Right(체크.name, 1)).TextFrame.Characters.Text + 1) = ""
        체크.Caption = ChrW(163)
        체크.ForeColor = vbBlack
    End If
    
End Sub

Private Sub 페이지_Click()
    curPage = Val(페이지.Caption)
    startBookidx = (curPage - 1) * 5 + 1
    
    Call bookUI
End Sub
