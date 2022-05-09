Attribute VB_Name = "카드모듈"
Public 비교카드 As New ArrayList

Function fn카테고리(rg, key)
    flag = False
    arr = Split(rg, ",")
    For Each i In arr
        If InStr(Sheet3.Cells(Val(i) + 1, 4), key) > 0 Then ' 4 = 적용카테고리
            flag = True
            Exit For
        End If
    Next
    
    fn카테고리 = flag
    
End Function

Function fn카드이름(rg, key)
    flag = False
    arr = Split(rg, ",")
    For Each i In arr
        If InStr(Sheet3.Cells(Val(i) + 1, 9), key) > 0 Then ' 9 = 카드이름
            flag = True
            Exit For
        End If
    Next
    
    fn카드이름 = flag
End Function

Function li(nums, t)
    
    Set 목록 = New ArrayList
    num = Split(nums, ",")
    
    If t = "혜택" Then
        
        For i = 0 To UBound(num)
            txt = Sheets("카드정보").Range("D" & num(i) + 1)
            If txt = "외식,여행,쇼핑,교통" Then
                목록.Add txt
                Exit For
            Else
                arrTxt = Split(txt, ",")
                For Each rg In arrTxt
                    If Not 목록.Contains(rg) Then
                        목록.Add rg
                    End If
                Next
            End If
        Next
        
    Else
        
        For i = 0 To UBound(num)
            txt = Sheets("카드정보").Range("I" & num(i) + 1)
            If Not 목록.Contains(txt) Then
                목록.Add txt
            End If
        Next
        
    End If
    
    li = Join(목록.ToArray, ",")
    
End Function

Sub 카드비교하기(card) ''카드의 번호임 ㅇㅇ
    If 비교카드.Count = 3 Then
        If MsgBox("비교 목록을 초기화 하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
            비교카드.Clear
        End If
        Exit Sub
    End If
    
    If Not 비교카드.Contains(card) Then
        비교카드.Add card
        If MsgBox(Sheet2.Range("B" & card + 1) & vbCrLf & "카드를 비교 목록에 추가했습니다." & vbCrLf & "지금 이동하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
            Sheet9.Select
        End If
    Else
        비교카드.Remove card
        MsgBox "선택한 카드를 비교 목록에서 제거했습니다.", vbInformation
    End If
End Sub
Sub 카드신청하기(card)
    iMsg card
End Sub

Sub 카드초기화()
    Sheet2.Range("AF2:AW2").ClearContents
    For i = 1 To 15
        With Sheet8.Shapes("check" & i)
            .OnAction = "'action""" & Sheet8.Shapes("check" & i).Name & """'"
            With .TextFrame2.TextRange
                .Characters = ""
                .Font.Fill.ForeColor.ObjectThemeColor = msoThemeColorText1
                .Font.Name = "Wingdings 2"
                .Font.Size = 16
            End With
        End With
    Next
    
    Application.ScreenUpdating = False
    
    Call 카드지우기
    Call 카드복사
    Call 카드데이터
    Call shtAction
    
    Application.ScreenUpdating = True
    
End Sub

Sub 카드지우기()
    On Error Resume Next
    For Each shp In Sheet8.Shapes
        If shp.Name Like "*카드*" Then
            shp.Delete
        End If
    Next
End Sub

Sub shtAction()
    k = 1
    With Sheet8
        For i = 2 To Sheet2.Range("AD1000").End(xlUp).Row
            If Sheet2.Range("AD" & i) > 0 Then
                .Shapes("카드 " & k).GroupItems("비교 " & Sheet2.Range("A" & i)).OnAction = "'카드비교하기""" & Sheet2.Range("A" & i) & """'"
                k = k + 1
            End If
        Next
        
        k = 1
        
        For i = 2 To Sheet2.Range("AD1000").End(3).Row
            If Sheet2.Range("AD" & i) > 0 Then
                .Shapes("카드 " & k).GroupItems("신청 " & Sheet2.Range("A" & i)).OnAction = "'카드신청하기 """ & Sheet2.Range("A" & i) & """'"
                k = k + 1
            End If
        Next
    End With
End Sub

Sub 카드복사()
    cnt = 0
    On Error Resume Next
    With Sheet2
        For i = 2 To .Range("AD1000").End(xlUp).Row
            If .Range("AD" & i) > 0 Then
                Sheet13.Shapes("카드 1").Copy
                Sheet8.Range("D" & 21 + (15 * cnt)).Select
                Sheet8.Paste
                cnt = cnt + 1
            End If
        Next
    End With
    
    Range("D17") = "총" & cnt & "개의 결과"
    
    i = 1
    
    For Each shp In Sheet8.Shapes
        If shp.Name Like "*카드*" Then
            shp.Name = "카드 " & i
            i = i + 1
        End If
    Next
End Sub

Sub action(조건명 As String)
    With Sheet8.Shapes(조건명).TextFrame2.TextRange.Characters
        If .Text = ChrW(80) Then
            .Text = ""
            값 = ""
        Else
            .Text = ChrW(80)
            값 = True
        End If
    End With
    
    Select Case 조건명
        Case "check1": Sheet2.Range("AF2") = 값
        Case "check2": Sheet2.Range("AG2") = 값
        Case "check3": Sheet2.Range("AH2") = 값
        
        Case "check4": Sheet2.Range("AL2") = 값
        Case "check5": Sheet2.Range("AK2") = 값
        Case "check6": Sheet2.Range("AM2") = 값
        Case "check7": Sheet2.Range("AJ2") = 값
        
        Case "check8": Sheet2.Range("AO2") = 값
        Case "check9": Sheet2.Range("AP2") = 값
        
        Case "check10": Sheet2.Range("AW2") = 값
        Case "check11": Sheet2.Range("AR2") = 값
        Case "check12": Sheet2.Range("AV2") = 값
        Case "check13": Sheet2.Range("AT2") = 값
        Case "check14": Sheet2.Range("AS2") = 값
        Case "check15": Sheet2.Range("AU2") = 값
    End Select
    
    Application.ScreenUpdating = False
    
    Call 카드지우기
    Call 카드복사
    Call 카드데이터
    Call shtAction
    
    Application.ScreenUpdating = True

End Sub

Sub 카드데이터()
    k = 1
    With Sheet2
        For i = 2 To .Range("AD1000").End(xlUp).Row
            If .Range("AD" & i) > 0 Then
                Sheet8.Shapes("카드 " & k).GroupItems("카드이미지").Fill.UserPicture ThisWorkbook.Path & "\지급자료\이미지\" & .Range("A" & i) & ".jpg"
                Sheet8.Shapes("카드 " & k).GroupItems("제목").TextFrame.Characters.Text = .Range("B" & i)
                Sheet8.Shapes("카드 " & k).GroupItems("설명").TextFrame.Characters.Text = .Range("C" & i)
                Sheet8.Shapes("카드 " & k).GroupItems("이자").TextFrame.Characters.Text = Format(.Range("F" & i), "#.#0%")
                Sheet8.Shapes("카드 " & k).GroupItems("혜택").TextFrame.Characters.Text = .Range("D" & i) & vbCrLf & li(.Range("E" & i), "혜택")
                Sheet8.Shapes("카드 " & k).GroupItems("연회비").TextFrame.Characters.Text = Format(.Range("I" & i), "#,##0") & "원"
                Sheet8.Shapes("카드 " & k).GroupItems("해외").TextFrame.Characters.Text = li(.Range("G" & i), "해외")
                
                Sheet8.Shapes("카드 " & k).GroupItems("비교").Name = "비교 " & .Range("A" & i)
                Sheet8.Shapes("카드 " & k).GroupItems("신청").Name = "신청 " & .Range("A" & i)
                k = k + 1
            End If
        Next
    End With
End Sub
