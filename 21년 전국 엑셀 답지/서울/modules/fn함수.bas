Attribute VB_Name = "fnÇÔ¼ö"
Function fnÁÂÇ¥È®ÀÎ(ÁÂÇ¥)
    xy = Split(ÁÂÇ¥, ",")
    If UBound(xy) < 1 Then fnÁÂÇ¥È®ÀÎ = False: Exit Function
    If UBound(xy) >= 2 Then fnÁÂÇ¥È®ÀÎ = False: Exit Function
    
    If Not IsNumeric(xy(0)) Or Not IsNumeric(xy(1)) Then fnÁÂÇ¥È®ÀÎ = False: Exit Function
    
    x = Val(xy(0))
    y = Val(xy(1))
    

    If Not (x >= 0 And x <= 99) And Not (y >= 0 And y <= 99) Then fnÁÂÇ¥È®ÀÎ = False: Exit Function
    If WorksheetFunction.CountIf(Sheet3.Range("C2:C1000"), ÁÂÇ¥) > 1 Then fnÁÂÇ¥È®ÀÎ = False: Exit Function
    If WorksheetFunction.CountIf(Sheet6.Range("B2:B1000"), ÁÂÇ¥) > 1 Then fnÁÂÇ¥È®ÀÎ = False: Exit Function
    fnÁÂÇ¥È®ÀÎ = True
End Function

Function fnµî±Þ(¹øÈ£)
    Å¸ÀÔ = Left(¹øÈ£, 1)
    If Å¸ÀÔ = "A" Then
        ÇÕ = WorksheetFunction.SumIf(Sheet7.Range("Ç¥3[È¸¿ø¹øÈ£]"), ¹øÈ£, Sheet7.Range("Ç¥3[°¡°Ý (¿ø°¡)]"))
        If ÇÕ > 500000 Then
            fnµî±Þ = "VIP"
        ElseIf ÇÕ > 300000 Then
            fnµî±Þ = "Gold"
        ElseIf ÇÕ > 200000 Then
            fnµî±Þ = "Silver"
        Else
            fnµî±Þ = "Normal"
        End If
    Else
        Æò±Õ = WorksheetFunction.IfError(WorksheetFunction.AverageIf([Ç¥4[À½½ÄÁ¡ ¹øÈ£]], ¹øÈ£, [Ç¥4[º°Á¡]]), 0)
        If Æò±Õ >= 4.5 Then
            fnµî±Þ = "¸ÀÁý"
        Else
            fnµî±Þ = "-"
        End If
    End If
End Function

