Attribute VB_Name = "Module1"
Public dic��� As Object, dicå As Object, dic���� As Object, dic�¼� As Object, dic�ڷ�ó�� As Object

Sub mkdic(obj As Object, sc As Range)
    
    Set obj = CreateObject("Scripting.Dictionary")
    Set lc = sc.Cells(10000, 1).End(3)
    
    For Each rg In Range(sc, lc)
        If Not obj.exists(rg.Value) Then obj.Add rg.Value, rg
    Next
    
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "����"
End Sub

Sub eMsg(msg)
    MsgBox msg, vbCritical, "���"
End Sub

Function cMsg(msg)
    cMsg = MsgBox(msg, vbInformation + vbYesNo, "Ȯ��")
End Function

Sub swap(a, b)
    tmp = a
    a = b
    b = tmp
End Sub
