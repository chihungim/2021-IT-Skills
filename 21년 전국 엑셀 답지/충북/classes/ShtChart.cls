VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "shtChart"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents cht As Chart
Attribute cht.VB_VarHelpID = -1
Private Sub cht_Select(ByVal ElementID As Long, ByVal Arg1 As Long, ByVal Arg2 As Long)
    If Arg1 = 1 And Arg2 <> -1 Then
        With Sheet12
            v = cht.SeriesCollection(1).XValues
            
            .Range("T1") = v(Arg2)
            .Shapes("차트 1").Chart.ChartTitle.Characters.Text = v(Arg2) & Chr(13) & "카드 사용자 연령 분포"
            .Shapes("그룹 8").Visible = msoTrue
        End With
    End If
    Sheet12.Range("H9").Select
End Sub
