Attribute VB_Name = "fn�Լ�"
Function fn��ǥȮ��(��ǥ)
    xy = Split(��ǥ, ",")
    If UBound(xy) < 1 Then fn��ǥȮ�� = False: Exit Function
    If UBound(xy) >= 2 Then fn��ǥȮ�� = False: Exit Function
    
    If Not IsNumeric(xy(0)) Or Not IsNumeric(xy(1)) Then fn��ǥȮ�� = False: Exit Function
    
    x = Val(xy(0))
    y = Val(xy(1))
    

    If Not (x >= 0 And x <= 99) And Not (y >= 0 And y <= 99) Then fn��ǥȮ�� = False: Exit Function
    If WorksheetFunction.CountIf(Sheet3.Range("C2:C1000"), ��ǥ) > 1 Then fn��ǥȮ�� = False: Exit Function
    If WorksheetFunction.CountIf(Sheet6.Range("B2:B1000"), ��ǥ) > 1 Then fn��ǥȮ�� = False: Exit Function
    fn��ǥȮ�� = True
End Function

Function fn���(��ȣ)
    Ÿ�� = Left(��ȣ, 1)
    If Ÿ�� = "A" Then
        �� = WorksheetFunction.SumIf(Sheet7.Range("ǥ3[ȸ����ȣ]"), ��ȣ, Sheet7.Range("ǥ3[���� (����)]"))
        If �� > 500000 Then
            fn��� = "VIP"
        ElseIf �� > 300000 Then
            fn��� = "Gold"
        ElseIf �� > 200000 Then
            fn��� = "Silver"
        Else
            fn��� = "Normal"
        End If
    Else
        ��� = WorksheetFunction.IfError(WorksheetFunction.AverageIf([ǥ4[������ ��ȣ]], ��ȣ, [ǥ4[����]]), 0)
        If ��� >= 4.5 Then
            fn��� = "����"
        Else
            fn��� = "-"
        End If
    End If
End Function

