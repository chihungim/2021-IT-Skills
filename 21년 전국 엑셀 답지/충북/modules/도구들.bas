Attribute VB_Name = "������"
Public pw
Public ��ûflag As Boolean
Public ����ī��(5) As Variant
Sub eMsg(msg)
    MsgBox msg, vbCritical
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation
End Sub

Sub ��Ʈ�����()
    For Each sht In Worksheets
        If sht.Name <> "����" Then
            sht.Visible = False
        End If
    Next
End Sub

Sub ��Ʈ�ʱ�ȭ()
    ��Ʈ�����
    Sheet1.Visible = xlSheetVisible
    Sheet2.Visible = xlSheetVisible
    Sheet3.Visible = xlSheetVisible
    Sheet4.Visible = xlSheetVisible
    Sheet5.Visible = xlSheetVisible
    Sheet6.Visible = xlSheetVisible
    Sheet7.Visible = xlSheetVisible
End Sub


Sub �ڷΰ���()
    Sheet12.Shapes("�׷� 8").Visible = msoFalse
End Sub

Sub �α׾ƿ�()
    [��ȣ] = ""
    Sheet7.Shapes("�׷� 5").Visible = msoTrue
    Sheet7.Shapes("�׸� 46").Visible = msoFalse
    ��Ʈ�ʱ�ȭ
    Sheet7.Select
End Sub
