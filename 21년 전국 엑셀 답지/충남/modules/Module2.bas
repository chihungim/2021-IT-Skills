Attribute VB_Name = "Module2"
Public �ѱۻ��� As Object
Sub mk�ѱۻ���(����, �迭)
    Set ���� = CreateObject("Scripting.Dictionary")
    For Each s In �迭
        t = Split(s)
        eidx = UBound(t)
        For i = 0 To eidx - 1
            ����.Add t(i), t(eidx)
        Next
    Next
End Sub


Function fnCallNM(������, ���ڸ�, �ΰ���ȣ)
    
    ����з���ȣ = Mid(�ΰ���ȣ, 21, 3)
    ������ȣ = Left(���ڸ�, 1) & " "
    
    ��ȣ = ""
    Call mk�ѱۻ���(�ѱۻ���, Split("�� �� �� 1,�� �� �� 2,�� �� �� 3,�� �� �� �� 4,�� �� �� 5,�� �� �� �� �� 6,�� �� �� �� �� �� �� 7,�� �� 8,�� �� 9,�� 21,�� �� 22,�� 23,�� 81,�� 82,�� 83,�� 84", ","))
    For Each i In fn�ѱ�(Mid(���ڸ�, 2, 1))
        ��ȣ = ��ȣ & �ѱۻ���(i)
    Next
'    For Each i In fn�ѱ�(Mid(���ڸ�, 2, 1))
'        Select Case i
'            Case "��", "��", "��": ��ȣ = ��ȣ & "1"
'            Case "��", "��", "��": ��ȣ = ��ȣ & "2"
'            Case "��", "��", "��": ��ȣ = ��ȣ & "3"
'            Case "��", "��", "��", "��": ��ȣ = ��ȣ & "4"
'            Case "��", "��", "��": ��ȣ = ��ȣ & "5"
'            Case "��", "��", "��", "��", "��": ��ȣ = ��ȣ & "6"
'            Case "��", "��", "��", "��", "��", "��", "��": ��ȣ = ��ȣ & "7"
'            Case "��", "��": ��ȣ = ��ȣ & "8"
'            Case "��", "��": ��ȣ = ��ȣ & "9"
'            Case "��": ��ȣ = ��ȣ & "21"
'            Case "��", "��": ��ȣ = ��ȣ & "22"
'            Case "��": ��ȣ = ��ȣ & "23"
'            Case "��": ��ȣ = ��ȣ & "81"
'            Case "��": ��ȣ = ��ȣ & "82"
'            Case "��": ��ȣ = ��ȣ & "83"
'            Case "��": ��ȣ = ��ȣ & "84"
'        End Select
'    Next
    
    '���� = IIf(Left(������, 1) Like "*[a-zA-Z]*", Left(������, 1), fn�ѱ�(IIf(IsNumeric(Left(������, 1)), ������(1, 7), Left(������, 1)))(0))
    '���� = IIf(Left(������, 1) Like "[a-zA-Z]", Left(������, 1), fn�ѱ�(IIf(IsNumeric(Left(������, 1)), ������(1, 7), Left(������, 1)))(0))
    If IsNumeric(Left(������, 1)) Then
        ���� = fn�ѱ�(������(1, 7))(0)
    ElseIf Left(������, 1) Like "[a-zA-Z]" Then
        ���� = Left(������, 1)
    Else
        ���� = fn�ѱ�(Left(������, 1))(0)
    End If
    
    �ΰ���ȣ = ""
    �Ľ� = Split(������)
    ��idx = UBound(�Ľ�): ��len = Len(�Ľ�(��idx))
    ������ = �Ľ�(��idx): �ճ��� = Left(������, Len(������) - ��len)
    
    If IsNumeric(������) Then
        �Ǽ� = WorksheetFunction.CountIf(Range("B:B"), �ճ��� & "*")
        If �Ǽ� > 1 Then �ΰ���ȣ = "-" & �Ľ�(��idx)
    End If
    
    �Ǽ� = WorksheetFunction.CountIf(Range("B:B"), ������)
    If �Ǽ� > 1 Then �ΰ���ȣ = �ΰ���ȣ & "=" & WorksheetFunction.CountIf(Range("B2:B" & ������.row), ������)
    
    fnCallNM = ����з���ȣ & " " & ������ȣ & ��ȣ & " " & ���� & �ΰ���ȣ
    
End Function

Sub fncallnm�׽�Ʈ()
    iMsg "L" Like "*[a-zA-Z]*"
    'f = fnCallNM(Range("B10"), Range("C10"), Range("F10"))
End Sub

Function fn�ѱ�(kor)
    
    �ʼ� = Split("�� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� ��")
    �߼� = Split("�� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� ��")
    
    If kor Like "*[��-�R]*" Then
        n = AscW(kor) + 21504
        k1 = Int(n / (21 * 28))
        k2 = Int((n Mod 21 * 28) / 28)
        
        fn�ѱ� = Split(�ʼ�(k1) & " " & �߼�(k2))
        
    Else
        Exit Function
    End If
    
End Function

Sub strtophon�׽�Ʈ()
    
    
    
End Sub

Sub case�׽�Ʈ()
    
    i = 3
    
    Select Case i
        Case Split("1 2 3 4 5"): iMsg "5����"
        Case Split("5 6 7 8 9"): iMsg "5�̻�"
    End Select
    
End Sub
 
