Attribute VB_Name = "����"
Public dic, dicȸ��, dic����, dic����, dic����, dic�ֹ�, dic����
Public arr, arrȸ��, arr����, arr����, arr����, arr�ֹ�, arr����

Sub mkDic(ByRef dic, s As Range)
    Set dic = CreateObject("Scripting.Dictionary")
    Set e = s.Cells(10000, 1).End(xlUp)
    
    For Each C In Range(s, e)
        If Not dic.exists(C.Value) Then dic.add C.Value, C
    Next
End Sub

Sub mkList(ByRef arr, s As Range)
    Set arr = New ArrayList
    Set e = s.Cells(10000, 1).End(xlUp)
    
    For Each C In Range(s, e)
        If Not arr.Contains(C) Then arr.add C
    Next
End Sub

Sub eMsg(msg)
    MsgBox msg, vbCritical, "����"
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "Ȯ��"
End Sub
