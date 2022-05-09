Attribute VB_Name = "Module2"
Public 한글사전 As Object
Sub mk한글사전(사전, 배열)
    Set 사전 = CreateObject("Scripting.Dictionary")
    For Each s In 배열
        t = Split(s)
        eidx = UBound(t)
        For i = 0 To eidx - 1
            사전.Add t(i), t(eidx)
        Next
    Next
End Sub


Function fnCallNM(도서명, 저자명, 부가기호)
    
    내용분류기호 = Mid(부가기호, 21, 3)
    도서기호 = Left(저자명, 1) & " "
    
    번호 = ""
    Call mk한글사전(한글사전, Split("ㄱ ㄲ ㅏ 1,ㅐ ㅑ ㅒ 2,ㅁ ㅓ ㅔ 3,ㅂ ㅃ ㅕ ㅖ 4,ㅅ ㅆ ㅗ 5,ㅇ ㅘ ㅙ ㅚ ㅛ 6,ㅈ ㅉ ㅜ ㅝ ㅞ ㅟ ㅠ 7,ㅡ ㅢ 8,ㅎ ㅣ 9,ㄴ 21,ㄷ ㄸ 22,ㄹ 23,ㅊ 81,ㅋ 82,ㅌ 83,ㅍ 84", ","))
    For Each i In fn한글(Mid(저자명, 2, 1))
        번호 = 번호 & 한글사전(i)
    Next
'    For Each i In fn한글(Mid(저자명, 2, 1))
'        Select Case i
'            Case "ㄱ", "ㄲ", "ㅏ": 번호 = 번호 & "1"
'            Case "ㅐ", "ㅑ", "ㅒ": 번호 = 번호 & "2"
'            Case "ㅁ", "ㅓ", "ㅔ": 번호 = 번호 & "3"
'            Case "ㅂ", "ㅃ", "ㅕ", "ㅖ": 번호 = 번호 & "4"
'            Case "ㅅ", "ㅆ", "ㅗ": 번호 = 번호 & "5"
'            Case "ㅇ", "ㅘ", "ㅙ", "ㅚ", "ㅛ": 번호 = 번호 & "6"
'            Case "ㅈ", "ㅉ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ": 번호 = 번호 & "7"
'            Case "ㅡ", "ㅢ": 번호 = 번호 & "8"
'            Case "ㅎ", "ㅣ": 번호 = 번호 & "9"
'            Case "ㄴ": 번호 = 번호 & "21"
'            Case "ㄷ", "ㄸ": 번호 = 번호 & "22"
'            Case "ㄹ": 번호 = 번호 & "23"
'            Case "ㅊ": 번호 = 번호 & "81"
'            Case "ㅋ": 번호 = 번호 & "82"
'            Case "ㅌ": 번호 = 번호 & "83"
'            Case "ㅍ": 번호 = 번호 & "84"
'        End Select
'    Next
    
    '자음 = IIf(Left(도서명, 1) Like "*[a-zA-Z]*", Left(도서명, 1), fn한글(IIf(IsNumeric(Left(도서명, 1)), 도서명(1, 7), Left(도서명, 1)))(0))
    '자음 = IIf(Left(도서명, 1) Like "[a-zA-Z]", Left(도서명, 1), fn한글(IIf(IsNumeric(Left(도서명, 1)), 도서명(1, 7), Left(도서명, 1)))(0))
    If IsNumeric(Left(도서명, 1)) Then
        자음 = fn한글(도서명(1, 7))(0)
    ElseIf Left(도서명, 1) Like "[a-zA-Z]" Then
        자음 = Left(도서명, 1)
    Else
        자음 = fn한글(Left(도서명, 1))(0)
    End If
    
    부가기호 = ""
    파싱 = Split(도서명)
    끝idx = UBound(파싱): 끝len = Len(파싱(끝idx))
    끝내용 = 파싱(끝idx): 앞내용 = Left(도서명, Len(도서명) - 끝len)
    
    If IsNumeric(끝내용) Then
        권수 = WorksheetFunction.CountIf(Range("B:B"), 앞내용 & "*")
        If 권수 > 1 Then 부가기호 = "-" & 파싱(끝idx)
    End If
    
    권수 = WorksheetFunction.CountIf(Range("B:B"), 도서명)
    If 권수 > 1 Then 부가기호 = 부가기호 & "=" & WorksheetFunction.CountIf(Range("B2:B" & 도서명.row), 도서명)
    
    fnCallNM = 내용분류기호 & " " & 도서기호 & 번호 & " " & 자음 & 부가기호
    
End Function

Sub fncallnm테스트()
    iMsg "L" Like "*[a-zA-Z]*"
    'f = fnCallNM(Range("B10"), Range("C10"), Range("F10"))
End Sub

Function fn한글(kor)
    
    초성 = Split("ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ")
    중성 = Split("ㅏ ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ ㅣ")
    
    If kor Like "*[가-힣]*" Then
        n = AscW(kor) + 21504
        k1 = Int(n / (21 * 28))
        k2 = Int((n Mod 21 * 28) / 28)
        
        fn한글 = Split(초성(k1) & " " & 중성(k2))
        
    Else
        Exit Function
    End If
    
End Function

Sub strtophon테스트()
    
    
    
End Sub

Sub case테스트()
    
    i = 3
    
    Select Case i
        Case Split("1 2 3 4 5"): iMsg "5이하"
        Case Split("5 6 7 8 9"): iMsg "5이상"
    End Select
    
End Sub
 
