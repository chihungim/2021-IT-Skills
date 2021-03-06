Attribute VB_Name = "fn함수"
Function fn좌표확인(좌표)
    xy = Split(좌표, ",")
    If UBound(xy) < 1 Then fn좌표확인 = False: Exit Function
    If UBound(xy) >= 2 Then fn좌표확인 = False: Exit Function
    
    If Not IsNumeric(xy(0)) Or Not IsNumeric(xy(1)) Then fn좌표확인 = False: Exit Function
    
    x = Val(xy(0))
    y = Val(xy(1))
    

    If Not (x >= 0 And x <= 99) And Not (y >= 0 And y <= 99) Then fn좌표확인 = False: Exit Function
    If WorksheetFunction.CountIf(Sheet3.Range("C2:C1000"), 좌표) > 1 Then fn좌표확인 = False: Exit Function
    If WorksheetFunction.CountIf(Sheet6.Range("B2:B1000"), 좌표) > 1 Then fn좌표확인 = False: Exit Function
    fn좌표확인 = True
End Function

Function fn등급(번호)
    타입 = Left(번호, 1)
    If 타입 = "A" Then
        합 = WorksheetFunction.SumIf(Sheet7.Range("표3[회원번호]"), 번호, Sheet7.Range("표3[가격 (원가)]"))
        If 합 > 500000 Then
            fn등급 = "VIP"
        ElseIf 합 > 300000 Then
            fn등급 = "Gold"
        ElseIf 합 > 200000 Then
            fn등급 = "Silver"
        Else
            fn등급 = "Normal"
        End If
    Else
        평균 = WorksheetFunction.IfError(WorksheetFunction.AverageIf([표4[음식점 번호]], 번호, [표4[별점]]), 0)
        If 평균 >= 4.5 Then
            fn등급 = "맛집"
        Else
            fn등급 = "-"
        End If
    End If
End Function

