Attribute VB_Name = "�������"
Public ���� As Object

Function mkdic(���ۼ� As Range) As Object
    Set dic = CreateObject("Scripting.Dictionary")
    For Each c In Range(���ۼ�, ���ۼ�.Cells(10000, 1))
        If Not dic.exists(c.Value) Then dic.Add c.Value, c
    Next
    
    Set mkdic = dic
End Function
