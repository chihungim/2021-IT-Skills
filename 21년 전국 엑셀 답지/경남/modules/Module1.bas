Attribute VB_Name = "Module1"
Public dic회원, dic자격증, dic시험, dic접수, dic문제, dic보기
Public 개인정보r As Integer

Sub mkdic(dic, sc As Range)
    Set dic = CreateObject("Scripting.Dictionary")
    Set lc = sc.Cells(1000000, 1).End(3)
    
    For Each rg In Range(sc, lc)
        If Not dic.exists(rg.Value) Then dic.Add rg.Value, rg
    Next
    
End Sub
Sub iMsg(msg)
    MsgBox msg, vbInformation, "정보"
End Sub

Sub eMsg(msg)
    MsgBox msg, vbCritical, "경고"
End Sub

Function qMsg(msg, modd)
    qMsg = MsgBox(msg, modd + vbYesNo)
End Function

Function iFormat(v)
    iFormat = Format(v, "#,##0")
End Function

Function vFormat(s)
    vFormat = Val(Replace(s, ",", ""))
End Function
