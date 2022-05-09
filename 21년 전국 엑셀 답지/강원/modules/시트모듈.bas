Attribute VB_Name = "시트모듈"


Sub 쿠폰발급()
    mkdic dic쿠폰, Sheet9.Range("A2")
    Sheet10.Select
    Sheet10.Range("A1:BB7").Clear
    cnt = 0
    For Each k In dic쿠폰
        If dic쿠폰(k).Cells(1, 2) = [번호] And dic쿠폰(k).Cells(1, 7) = "X" Then
            If dic쿠폰(k).Cells(1, 6) Like "*총금액*" Then
                cName = "총금액 "
            ElseIf dic쿠폰(k).Cells(1, 6) Like "*성인*" Then
                cName = "성인 "
            ElseIf dic쿠폰(k).Cells(1, 6) Like "*청소년*" Then
                cName = "청소년 "
            Else
                cName = "어린이 "
            End If
            cName = cName & 100 * Split(dic쿠폰(k).Cells(1, 6), " = ")(1) & "% 할인쿠폰"
            With Sheet10
                .Range("쿠폰양식").Copy
                .Cells(2, 2 + (6 * cnt)).PasteSpecial
                .Cells(3, 4 + (6 * cnt)) = [아이디]
                .Cells(4, 4 + (6 * cnt)) = dic쿠폰(k).Cells(1, 3)
                .Cells(5, 4 + (6 * cnt)) = cName
                .Cells(6, 4 + (6 * cnt)) = Format(dic쿠폰(k).Cells(1, 5), "yyyy년 MM월 dd일 까지")
                cnt = cnt + 1
            End With
        End If
    Next
    Sheet10.Range("A1").Select
End Sub

Sub 쿠폰생성기(cate)
    With 쿠폰생성
        .ComboBox2.ListIndex = cate
        .Show
    End With
    
End Sub

Sub 쿠폰제거(r)
    If MsgBox("쿠폰을 정말 삭제하시겠습니까?", vbInformation + vbYesNo, "정보") = vbYes Then
        Sheet9.Rows(r & ":" & r).Delete
    End If
    
End Sub

Sub 관광지Height()
    For i = 2 To Range("A10000").End(3).row
        Rows(i & ":" & i).EntireRow.AutoFit
        Rows(i & ":" & i).RowHeight = Rows(i & ":" & i).RowHeight + 10
    Next
End Sub

Function fn등급(id As Range)
    예약횟수 = id.Cells(1, 9)
    예약금액 = id.Cells(1, 10)

    With Sheet2
        For k = 5 To 2 Step -1
            Set c = .Cells(1, k)
            
            결과 = 1
            For i = 5 To 9
                If c.Cells(i, 1) = "" Then Exit For
                If Mid(c.Cells(i, 1), 6, 2) = "횟수" Then
                    결과 = 결과 * Evaluate(예약횟수 & Split(c.Cells(i, 1), "]")(1))
                Else
                    결과 = 결과 * Evaluate(예약금액 & Split(c.Cells(i, 1), "]")(1))
                End If
            Next
            
            
            If 결과 Then
                Debug.Print 결과
                fn등급 = c.Value
                Exit Function
            End If
        Next
    End With
    
    fn등급 = "일반"
End Function
Function email(rg)
    If InStr(rg, "@") = 0 Then email = False: Exit Function
    
    id = Split(rg, "@")(0)
    domain = Split(rg, "@")(1)
    
    l = Left(id, 1)
    
    chk = True
    
    If Not ((l >= "a" And l <= "Z") Or (l >= "a" And l <= "z")) Then
        chk = False
    End If
    
    For i = 1 To Len(id)
        If Not ((Mid(id, i, 1) >= "A" And Mid(id, i, 1) <= "Z") Or (Mid(id, i, 1) >= "a" And Mid(id, i, 1) <= "z") Or (Mid(id, i, 1) >= "0" And Mid(id, i, 1) <= "9")) Then
            chk = False
            Exit For
        End If
    Next
    
    If Len(id) < 3 Then email = False: Exit Function
    
    If InStr(domain, ".") = 0 Then email = False: Exit Function
    
    For i = 1 To Len(domain)
        If Not ((Mid(domain, i, 1) >= "A" And Mid(domain, i, 1) <= "Z") Or (Mid(domain, i, 1) >= "a" And Mid(domain, i, 1) <= "z") Or (Mid(domain, i, 1) >= "0" And Mid(domain, i, 1) <= "9") Or (Mid(domain, i, 1) = ".")) Then
            Debug.Print Mid(id, i, 1)
            chk = False
            Exit For
        End If
    Next
    
    If Len(Split(domain, ".")(1)) < 2 Then email = False: Exit Function
    
    email = chk

End Function


Sub tesett()
    
    iMsg email("rlsmd2@skills.com")
    
End Sub
