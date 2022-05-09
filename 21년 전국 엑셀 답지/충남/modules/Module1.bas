Attribute VB_Name = "Module1"
Public dic멤버 As Object, dic책 As Object, dic대출 As Object, dic좌석 As Object, dic자료처리 As Object

Sub mkdic(obj As Object, sc As Range)
    
    Set obj = CreateObject("Scripting.Dictionary")
    Set lc = sc.Cells(10000, 1).End(3)
    
    For Each rg In Range(sc, lc)
        If Not obj.exists(rg.Value) Then obj.Add rg.Value, rg
    Next
    
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "정보"
End Sub

Sub eMsg(msg)
    MsgBox msg, vbCritical, "경고"
End Sub

Function cMsg(msg)
    cMsg = MsgBox(msg, vbInformation + vbYesNo, "확인")
End Function

Sub swap(a, b)
    tmp = a
    a = b
    b = tmp
End Sub
