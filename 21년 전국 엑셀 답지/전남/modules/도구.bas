Attribute VB_Name = "도구"


Public dic우편 As Object, dic회원 As Object, dic구매 As Object, dic장바구니 As Object, dic행사 As Object, dic도서 As Object
Public arr우편, arr회원, arr구매, arr장바구니, arr행사
Public 도서명, 구매모드


Sub eMsg(msg)
    MsgBox msg, vbCritical, "경고"
End Sub

Sub iMsg(msg)
    MsgBox msg, vbInformation, "정보"
End Sub
Sub mkDic(ByRef obj, s As Range)
    Set obj = CreateObject("Scripting.Dictionary")
    Set e = s.End(xlDown)
    
    For Each c In Range(s, e)
        If Not obj.exists(c.Value) Then obj.Add c.Value, c
    Next
    
End Sub

Sub mkList(obj, s)
    Set obj = New ArrayList
    Set e = s.End(xlDown)
    For Each c In Range(s, e)
        If Not obj.Contains(c) Then obj.Add c
    Next
End Sub

Function fn나이대(Age As Range) As String
    나이 = Age.Value
    Select Case 나이
        Case Is < 9: fn나이대 = "어린이"
        Case 10 To 19: fn나이대 = "10대": Exit Function
        Case 20 To 29: fn나이대 = "20대": Exit Function
        Case 30 To 39: fn나이대 = "30대": Exit Function
        Case 40 To 49: fn나이대 = "40대": Exit Function
        Case 50 To 59: fn나이대 = "50대": Exit Function
        Case 60 To 69: fn나이대 = "60대": Exit Function
        Case Is >= 70: fn나이대 = "70대 이상"
    End Select
End Function

Function fn포인트(도서코드)
    코드 = Split(도서코드, "-")(1)
    
    Select Case (코드)
        Case "L": fn포인트 = 100
        Case "P": fn포인트 = 200
        Case "S": fn포인트 = 300
        Case "H": fn포인트 = 400
        Case "A": fn포인트 = 500
        Case "R": fn포인트 = 600
        Case "T": fn포인트 = 700
        Case "O": fn포인트 = 800
        Case "E": fn포인트 = 900
    End Select
End Function

Function fn분류(도서코드)
    코드 = Split(도서코드, "-")(1)
    
    Select Case (코드)
        Case "L": fn분류 = "문학"
        Case "P": fn분류 = "철학"
        Case "S": fn분류 = "과학"
        Case "H": fn분류 = "역사"
        Case "A": fn분류 = "예술"
        Case "R": fn분류 = "종교"
        Case "T": fn분류 = "여행"
        Case "O": fn분류 = "정치"
        Case "E": fn분류 = "경제"
    End Select
End Function
