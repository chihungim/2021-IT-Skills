Attribute VB_Name = "����"


Public dic���� As Object, dicȸ�� As Object, dic���� As Object, dic��ٱ��� As Object, dic��� As Object, dic���� As Object
Public arr����, arrȸ��, arr����, arr��ٱ���, arr���
Public ������, ���Ÿ��


Sub eMsg(msg)
    MsgBox msg, vbCritical, "���"
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "����"
End Sub
Sub mkDic(ByRef obj, s As Range)
    Set obj = CreateObject("Scripting.Dictionary")
    Set e = s.End(xlDown)
    
    For Each c In Range(s, e)
        If Not obj.exists(c.Value) Then obj.Add c.Value, c
    Next
    
End Sub

Sub mkList(obj, s)
    Set obj = New ArrayList
    Set e = s.End(xlDown)
    For Each c In Range(s, e)
        If Not obj.Contains(c) Then obj.Add c
    Next
End Sub

Function fn���̴�(Age As Range) As String
    ���� = Age.Value
    Select Case ����
        Case Is < 9: fn���̴� = "���"
        Case 10 To 19: fn���̴� = "10��": Exit Function
        Case 20 To 29: fn���̴� = "20��": Exit Function
        Case 30 To 39: fn���̴� = "30��": Exit Function
        Case 40 To 49: fn���̴� = "40��": Exit Function
        Case 50 To 59: fn���̴� = "50��": Exit Function
        Case 60 To 69: fn���̴� = "60��": Exit Function
        Case Is >= 70: fn���̴� = "70�� �̻�"
    End Select
End Function

Function fn����Ʈ(�����ڵ�)
    �ڵ� = Split(�����ڵ�, "-")(1)
    
    Select Case (�ڵ�)
        Case "L": fn����Ʈ = 100
        Case "P": fn����Ʈ = 200
        Case "S": fn����Ʈ = 300
        Case "H": fn����Ʈ = 400
        Case "A": fn����Ʈ = 500
        Case "R": fn����Ʈ = 600
        Case "T": fn����Ʈ = 700
        Case "O": fn����Ʈ = 800
        Case "E": fn����Ʈ = 900
    End Select
End Function

Function fn�з�(�����ڵ�)
    �ڵ� = Split(�����ڵ�, "-")(1)
    
    Select Case (�ڵ�)
        Case "L": fn�з� = "����"
        Case "P": fn�з� = "ö��"
        Case "S": fn�з� = "����"
        Case "H": fn�з� = "����"
        Case "A": fn�з� = "����"
        Case "R": fn�з� = "����"
        Case "T": fn�з� = "����"
        Case "O": fn�з� = "��ġ"
        Case "E": fn�з� = "����"
    End Select
End Function
