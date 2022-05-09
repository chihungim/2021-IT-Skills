Attribute VB_Name = "MyBookM"
Sub 대출현황()
    버튼색바꾸기 1
    
    Application.ScreenUpdating = False
    Sheet11.Range("P1").CurrentRegion.ClearContents
    자료처리초기화 Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    Sheet11.Range("E1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet10.[a1:c2], Sheet11.[p1]
    
    With Sheet10
        .Range("F16") = "대출일"
        .Range("G16") = "반납예정일"
    End With
    
    With Sheet11
        cnt = 0
        For Each rg In .Range("P2:P" & .[p1000].End(3).row)
            cnt = cnt + 1
            With Sheet10
                .Cells(16 + cnt, 2) = cnt
                .Cells(16 + cnt, 3) = rg.Value
                .Cells(16 + cnt, 4) = dic책(rg.Value).Cells(1, 2)
                .Cells(16 + cnt, 5) = dic책(rg.Value).Cells(1, 3)
                .Cells(16 + cnt, 6) = rg.Cells(1, 3)
                .Cells(16 + cnt, 7) = rg.Cells(1, 4)
                .Cells(16 + cnt, 8) = IIf(rg.Cells(1, 4) < Date, "연체", IIf(rg.Cells(1, 6) = "Y", "반납연기", "대출"))
                .Cells(16 + cnt, 9) = IIf(rg.Cells(1, 7) = "", "`", "")
                아이콘색바꾸기 16 + cnt, 9, vbGreen
            End With
        Next
    End With
    
    Application.ScreenUpdating = True
End Sub

Sub 예약현황()
    버튼색바꾸기 2
    
    Application.ScreenUpdating = False
    Sheet11.Range("P1").CurrentRegion.ClearContents
    자료처리초기화 Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    Sheet11.Range("E1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet10.[e1:e2], Sheet11.[p1]
    
    With Sheet10
        .Range("F16") = "예약일자"
        .Range("G16") = "예약순번"
    End With
    
    With Sheet11
        cnt = 0
        For Each rg In .Range("P2:P" & .[p1000].End(3).row)
            cnt = cnt + 1
            With Sheet10
                .Cells(16 + cnt, 2) = cnt
                .Cells(16 + cnt, 3) = rg.Value
                .Cells(16 + cnt, 4) = dic책(rg.Value).Cells(1, 2)
                .Cells(16 + cnt, 5) = dic책(rg.Value).Cells(1, 3)
                .Cells(16 + cnt, 6) = rg.Cells(1, 8)
                .Cells(16 + cnt, 7) = "1번째" & vbCrLf & "(1명 예약)"
                .Cells(16 + cnt, 8) = "예약중" & vbCrLf & rg.Cells(1, 4)
                .Cells(16 + cnt, 9) = "r"
                아이콘색바꾸기 16 + cnt, 9, vbRed
            End With
        Next
    End With
    
    Application.ScreenUpdating = True
End Sub

Sub 대출이력()
    버튼색바꾸기 3
    
    Application.ScreenUpdating = False
    Sheet11.Range("P1").CurrentRegion.ClearContents
    자료처리초기화 Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    Sheet11.Range("E1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet10.[g1:h2], Sheet11.[p1]
    
    With Sheet10
        .Range("F16") = "대출일"
        .Range("G16") = "반납일"
    End With
    
    With Sheet11
        cnt = 0
        For Each rg In .Range("P2:P" & .[p1000].End(3).row)
            cnt = cnt + 1
            With Sheet10
                .Cells(16 + cnt, 2) = cnt
                .Cells(16 + cnt, 3) = rg.Value
                .Cells(16 + cnt, 4) = dic책(rg.Value).Cells(1, 2)
                .Cells(16 + cnt, 5) = dic책(rg.Value).Cells(1, 3)
                .Cells(16 + cnt, 6) = rg.Cells(1, 8)
                .Cells(16 + cnt, 7) = "1번째" & vbCrLf & "(1명 예약)"
                .Cells(16 + cnt, 8) = "예약중" & vbCrLf & rg.Cells(1, 4)
                .Cells(16 + cnt, 9) = "r"
                아이콘색바꾸기 16 + cnt, 9, vbRed
            End With
        Next
    End With
    
    Application.ScreenUpdating = True
End Sub
 
Sub 아이콘색바꾸기(row, column, color)
    With Sheet10
        .Select
        .Cells(row, column).Select
        With Selection.Font
            .name = "Webdings"
            .color = color
            .Bold = True
            .Size = 20
        End With
    End With
End Sub

Sub 버튼색바꾸기(idx As Integer)
    For i = 1 To 3
        With ActiveSheet.Shapes.Range(Array("btn_" & i))
            .Fill.ForeColor.ObjectThemeColor = IIf(i = idx, msoThemeColorAccent1, msoThemeColorBackground1)
        End With
    Next
    
     Range("B17:I1000").ClearContents
End Sub
