Attribute VB_Name = "활동내역모듈"
Dim dic상품 As Object, dic회원 As Object
Dim act(500) As Activity
Dim k As Integer ''배열의 인덱슈
Dim h As Integer ''검색 개수


Sub 활동내역초기화()
    
    k = 0: h = 0
    
    Application.ScreenUpdating = False
    
    Call init
    Call 활동내역지우기
    Call 활동내역복사
    Call 활동내역정렬
    Call 활동내역데이터
    
    Application.ScreenUpdating = True
End Sub
Function sdate()
    sdate = CDate(IIf(Sheet11.TextBox1.Text = "", Sheet13.Range("U1").Value, Sheet11.TextBox1))
End Function
Function ldate()
    ldate = CDate(IIf(Sheet11.TextBox2.Text = "", Sheet13.Range("V1").Value, Sheet11.TextBox2))
End Function

Sub init()
    Erase act
    Set dic회원 = mkdic(Sheet1.Range("I2"))
    Set dic상품 = mkdic(Sheet5.Range("A2"))
    
    
    With Sheet6
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("F" & i).Value <> "" And .Range("C" & i) = [번호] Then
                v = .Range("G" & i).Value
                Set act(k) = New Activity
                
                title = dic상품(v).Cells(1, 4) & " - " & dic상품(v).Cells(1, 3)
                rdate = Format(.Range("B" & i), "yyyy-MM-dd HH:mm:ss")
                deal = "카드 결제"
                amount = "- " & Format(.Range("H" & i), "#,##0원")
                pay = "Chase Total Checking Account" & vbCrLf & "거래명세서에" & """""Chase*" & dic상품(v).Cells(1, 4) & " - " & dic상품(v).Cells(1, 4) & "(으)로" & vbCrLf & "표시됩니다"
                detail = dic상품(v).Cells(1, 4) & " - " & dic상품(v).Cells(1, 3) & Chr(13) & "거래 유형 : 카드 결제" & Chr(13) & "거래 일시 : " & Format(.Range("B" & i), "yyyy-MM-dd AMPM HH:mm:ss")
                delivery = [이름] & Chr(13) & Replace([주소], " ", "," & Chr(13))
                
                Call act(k).init(title, rdate, deal, amount, pay, detail, delivery)
            End If
                
            If .Range("F" & i).Value = "" And (.Range("D" & i) = [계좌] Or .Range("E" & i) = [계좌]) Then
                Set act(k) = New Activity
                
                title = IIf(.Range("C" & i) = [번호], dic회원(.Range("E" & i).Value).Cells(1, -4), dic회원(.Range("D" & i).Value).Cells(1, -4)) & IIf(.Range("C" & i) = [번호], "에게 입금", "에서 입금")
                rdate = Format(.Range("B" & i), "yyyy-MM-dd HH:mm:ss")
                deal = "계좌거래"
                amount = IIf(.Range("C" & i) = [번호], "-", "+") & Format(.Range("H" & i), "###,0원")
                pay = "Chase Total Checking Account" & vbCrLf & "거래명세서에" & """""Chase*" & IIf(.Range("C" & i) = [번호], dic회원(.Range("E" & i).Value).Cells(1, -4), dic회원(.Range("D" & i).Value).Cells(1, -4)) & IIf(.Range("C" & i) = [번호], "에게 입금", "에서 입금") & "(으)로" & vbCrLf & "표시됩니다."
                detail = IIf(.Range("C" & i) = [번호], dic회원(.Range("E" & i).Value).Cells(1, -4), dic회원(.Range("D" & i).Value).Cells(1, -4)) & IIf(.Range("C" & i) = [번호], "에게 입금", "에서 입금") & Chr(13) & "거래 유형 : 계좌 결제" & Chr(13) & "거래 일시 : " & Format(.Range("B" & i), "yyyy-MM-dd AMPM HH:mm:ss")
                delivery = [이름] & Chr(13) & Replace([주소], " ", "," & Chr(13))
                Call act(k).init(title, rdate, deal, amount, pay, detail, delivery)
                k = k + 1
            End If
        Next
    End With
End Sub


Sub 활동내역지우기()
    On Error GoTo e
    Sheet11.Shapes("영수증").Delete
e:
    For Each shp In Sheet11.Shapes
        If shp.Name Like "*활동*" Then
            shp.Delete
        End If
    Next


End Sub

Sub 활동내역정렬()
    For i = 0 To k - 1
        For J = i + 1 To k - 1
            If act(i).rdate < act(J).rdate Then
                Set tmp = act(J)
                Set act(J) = act(i)
                Set act(i) = tmp
            End If
        Next
    Next
End Sub

Sub 활동내역복사()
    h = 0
    For i = 0 To k - 1
         If CDbl(CDate(act(i).rdate)) >= CDbl(sdate) And CDbl(CDate(act(i).rdate)) <= CDbl(ldate) And act(i).title Like "*" & Sheet11.TextBox3.Value & "*" Then
            Sheet13.Shapes("활동 1").Copy
            Sheet11.Range("B" & 10 + (8 * h)).Select
            Sheet11.Paste
            h = h + 1
           
        End If
    Next
    
    i = 1
    For Each shp In Sheet11.Shapes
        If shp.Name Like "*활동*" Then
            shp.Name = "활동 " & i
            shp.GroupItems("상세보기").Name = "보기 " & i
            i = i + 1
        End If
    Next
End Sub

Sub 활동내역데이터()
    
    idx = 1
    With Sheet11
        For i = 0 To k - 1
            If act(i).rdate >= CDate(sdate) And act(i).rdate <= CDate(ldate) And act(i).title Like "*" & .TextBox3.Value & "*" Then
                .Shapes("활동 " & idx).GroupItems("제목").TextFrame.Characters.Text = act(i).title
                .Shapes("활동 " & idx).GroupItems("날짜").TextFrame.Characters.Text = Format(act(i).rdate, "mmm, d, HH:mm:ss")
                .Shapes("활동 " & idx).GroupItems("거래").TextFrame.Characters.Text = act(i).deal
                .Shapes("활동 " & idx).GroupItems("금액").TextFrame.Characters.Text = act(i).amount
                .Shapes("활동 " & idx).GroupItems("보기 " & idx).OnAction = "'info """ & i & """'"
                idx = idx + 1
            End If
        Next

    End With

End Sub

Sub info(i)
    Application.ScreenUpdating = False
    
    Call 활동내역지우기
    Sheet13.Shapes("영수증").Copy
    Sheet11.Range("B10").Select
    Sheet11.Paste
    
    With Sheet11.Shapes("영수증")
        .GroupItems("제목").TextFrame.Characters.Text = act(i).title
        .GroupItems("날짜").TextFrame.Characters.Text = Format(act(i).rdate, "mmm, d, HH:mm:ss")
        .GroupItems("거래").TextFrame.Characters.Text = act(i).deal
        .GroupItems("금액").TextFrame.Characters.Text = act(i).amount
        .GroupItems("결제정보").TextFrame.Characters.Text = act(i).pay
        .GroupItems("상세정보").TextFrame.Characters.Text = act(i).detail
        .GroupItems("배송정보").TextFrame.Characters.Text = act(i).delivery
    End With
    
    Application.ScreenUpdating = True
End Sub

Sub 영수증출력하기()
    iMsg "영수증을 출력하였습니다!"
    Sheet11.Range("A10:P47").ExportAsFixedFormat xlTypePDF, ThisWorkbook.Path & "\영수증.pdf"
End Sub



