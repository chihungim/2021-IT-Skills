Attribute VB_Name = "사전모듈"
Public 사전 As Object

Function mkdic(시작셀 As Range) As Object
    Set dic = CreateObject("Scripting.Dictionary")
    For Each c In Range(시작셀, 시작셀.Cells(10000, 1))
        If Not dic.exists(c.Value) Then dic.Add c.Value, c
    Next
    
    Set mkdic = dic
End Function
