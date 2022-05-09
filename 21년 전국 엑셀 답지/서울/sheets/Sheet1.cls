VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True

Dim loading As ChartObject

Private Sub Worksheet_Activate()
    
    For Each sht In ThisWorkbook.Worksheets
        If sht.name <> "로딩" Then sht.Visible = xlSheetHidden
    Next
    
    Sheet1.Range("M1").Select
    Sheet1.Shapes("그림 4").Left = 22
     [p1] = 0
    Set loading = ChartObjects("차트 2")
    loading.Chart.Refresh
    Do While [p1] < 220
        [p1] = [p1] + 1
        Sheet1.Shapes("직사각형 3").Fill.ForeColor.RGB = RGB(0, [p1], [p1])
        Sheet1.Shapes("그림 4").Left = 520 * ([p1] / 220)
        loading.Chart.Refresh ''학교컴보다 연산이 너무 빠르다 ㅋㅋㅋ
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
        DoEvents
    Loop
    
    For Each sht In ThisWorkbook.Worksheets
        If sht.name <> "로딩" Or sht.name <> "데이타" Then sht.Visible = True
    Next
    
    Sheet1.Visible = xlSheetHidden
    
    Sheet2.Select
    
    v = 0
    
    Do While v < 255
        Sheet2.Range("A1").EntireRow.EntireColumn.Interior.Color = RGB(v, 255, 255)
        v = v + 10
        DoEvents
    Loop

    
End Sub


