Attribute VB_Name = "FindbookM"
Public bookChk(5) As clsFindBook, pages(7) As clsFindBook
Public totSearchCnt
Public curPage, startBookidx, endBookidx
Public 전체도서 As Object, 빌린도서 As Object

Sub aasdfasdf()
    ' box 163, chkbox= 82
    'Sheet7.체크1 = ChrW(163)
    With Sheet7.Shapes("직사각형 3")
        .TextFrame.Characters.Text = ChrW(168) & " 도서예약불가"
        '.TextFrame.Characters(1, 2).Font.Name = "Webdings"
        
    End With
   
    
End Sub

Sub 글씨색바꿨다()
    Call 글씨색바꾸기(5, 4, vbBlue)
End Sub

Sub changeRgColor(rg, s, l, col)
    rg.Characters(s, l).Font.color = col
End Sub

Sub changeShpColor(shp, s, l, col)
    shp.TextFrame2.TextRange.Characters(s, l).Font.Fill.ForeColor.RGB = col
End Sub

Sub FindBook검색()
    Application.ScreenUpdating = False
    
    With Sheet7
        .Range("Z2:AB2").ClearContents
        Select Case .ComboBox1
            Case "서명": .[Z2] = "*" & .TextBox1 & "*"
            Case "저자": .[AA2] = "*" & .TextBox1 & "*"
            Case "출판사": .[AB2] = "*" & .TextBox1 & "*"
        End Select
        
        Sheet3.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("Z1:AB2"), .Range("AD1"), False
        Sheet3.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("Z1:AB2"), .Range("AR1:AX1"), False
        Sheet4.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("AF1:AF2"), .Range("AH1")
        
        .ListObjects("검색테이블").Sort.SortFields.Clear
        .ListObjects("검색테이블").Sort.SortFields.Add Key:=.Range("AV1"), Order:=xlDescending
        '.ListObjects("검색테이블").Sort.SortFields.Add Key:=[검색테이블[도서명]], Order:=xlAscending
        .ListObjects("검색테이블").Sort.Apply
        
        curPage = 1
        startBookidx = 1
        totSearchCnt = .Range("AD1000").End(3).row - 1
        If totSearchCnt = 0 Then eMsg "검색된 자료가 없습니다.": Exit Sub
        
        .ListObjects("검색테이블").Resize .Range("AQ1:BB" & .Range("AD1000").End(3).row)
        Call bookUI
        
        
    End With
    Application.ScreenUpdating = True
End Sub

Public Sub bookUI()
    With Sheet7
    
        totSearchCnt = .Range("AD10000").End(3).row - 1
        .Range("B11") = "※ 총 " & totSearchCnt & "개의 책이 존재합니다."
        Call changeRgColor(.Range("B11"), 5, Len(CStr(totSearchCnt)) + 1, vbBlue)
        
        endBookidx = IIf(startBookidx + 4 <= totSearchCnt, startBookidx + 4, totSearchCnt)
        
            
        For i = startBookidx To endBookidx
            Call showBookInfo(i)
            Sheet7.Shapes("검색" & (i - 1) Mod 5 + 1).Visible = True
        Next
        For i = endBookidx + 1 To 5
            Sheet7.Shapes("검색" & (i - 1) Mod 5 + 1).Visible = False
        Next
        
        첫페이지 = Val(bookChk(1).페이지.Caption)
        남은페이지 = Round((totSearchCnt - (첫페이지 - 1) * 5) / 5 + 0.5)
        
        If 남은페이지 >= 4 Then
            끝페이지 = 첫페이지 + 4
        Else
            끝페이지 = 첫페이지 + 남은페이지 - 1
        End If
        
        For i = 1 To 5
            bookChk(i).페이지.Visible = False
        Next
        For i = 첫페이지 To 끝페이지
            bookChk((i - 1) Mod 5 + 1).페이지.Caption = i
            bookChk((i - 1) Mod 5 + 1).페이지.Visible = True
        Next
        
    End With
    
End Sub

Sub showBookInfo(i)
    
    idx = (i - 1) Mod 5 + 1
    
    With Sheet7
        Dim shp As Shape
        Set shp = .Shapes("검색" & idx)
        shp.GroupItems(9).TextFrame.Characters.Text = i
        
        Set obj = .OLEObjects("체크" & idx).Object
        If .Range("AQ2").Cells(idx, 1) Then
            obj.Caption = ChrW(82)
            obj.ForeColor = vbBlue
        Else
            obj.Caption = ChrW(163)
            obj.ForeColor = vbBlack
        End If
        
        'Debug.Print ThisWorkbook.Path & "\지급자료\이미지\도서\" & .Range("AQ2").Cells(i, 3) & ".jpg"
        shp.GroupItems("서명").TextFrame.Characters.Text = "도서명 " & .Range("AQ2").Cells(i, 3)
        shp.GroupItems("내용").TextFrame.Characters.Text = "저자 : " & .Range("AQ2").Cells(i, 4) & " / 출판사 : " & .Range("AQ2").Cells(i, 5) & " / 출간년도 : " & year(.Range("AQ2").Cells(i, 6))
        shp.GroupItems("isbn").TextFrame.Characters.Text = "ISBN : " & Mid(.Range("AQ2").Cells(i, 7), 1, 17)
        shp.GroupItems("청구기호").TextFrame.Characters.Text = "청구기호 : " & .Range("AQ2").Cells(i, 8)
        
        If .Range("AQ2").Cells(i, 10) Then
            shp.GroupItems("대출여부").TextFrame.Characters.Text = "대출가능[비치중]"
            shp.GroupItems("예약여부").TextFrame.Characters.Text = .Range("O2")
            Call changeShpColor(shp.GroupItems("대출여부"), 1, 4, vbGreen)
            Call changeShpColor(shp.GroupItems("대출여부"), 5, 5, vbBlack)
            Call changeShpColor(shp.GroupItems("예약여부"), 1, 1, vbButtonFace)
        Else
            shp.GroupItems("대출여부").TextFrame.Characters.Text = "대출불가[대출중] (예약 : " & .Range("AQ2").Cells(i, 11) & "명) (반납예정일:" & .Range("AQ2").Cells(i, 9) & ")"
            shp.GroupItems("예약여부").TextFrame.Characters.Text = .Range("O3")
            Call changeShpColor(shp.GroupItems("대출여부"), 1, 4, vbRed)
            Call changeShpColor(shp.GroupItems("대출여부"), 5, 34, vbBlack)
            Call changeShpColor(shp.GroupItems("예약여부"), 1, 1, vbRed)
        End If
        
        
    End With
    
End Sub
