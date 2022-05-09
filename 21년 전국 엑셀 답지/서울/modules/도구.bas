Attribute VB_Name = "도구"
Public dic, dic회원, dic정보, dic음식, dic금지, dic주문, dic리뷰
Public arr, arr회원, arr정보, arr음식, arr금지, arr주문, arr리뷰

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
    MsgBox msg, vbCritical, "오류"
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "확인"
End Sub
