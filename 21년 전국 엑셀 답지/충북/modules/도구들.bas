Attribute VB_Name = "도구들"
Public pw
Public 신청flag As Boolean
Public 선택카드(5) As Variant
Sub eMsg(msg)
    MsgBox msg, vbCritical
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation
End Sub

Sub 시트숨기기()
    For Each sht In Worksheets
        If sht.Name <> "메인" Then
            sht.Visible = False
        End If
    Next
End Sub

Sub 시트초기화()
    시트숨기기
    Sheet1.Visible = xlSheetVisible
    Sheet2.Visible = xlSheetVisible
    Sheet3.Visible = xlSheetVisible
    Sheet4.Visible = xlSheetVisible
    Sheet5.Visible = xlSheetVisible
    Sheet6.Visible = xlSheetVisible
    Sheet7.Visible = xlSheetVisible
End Sub


Sub 뒤로가기()
    Sheet12.Shapes("그룹 8").Visible = msoFalse
End Sub

Sub 로그아웃()
    [번호] = ""
    Sheet7.Shapes("그룹 5").Visible = msoTrue
    Sheet7.Shapes("그림 46").Visible = msoFalse
    시트초기화
    Sheet7.Select
End Sub
