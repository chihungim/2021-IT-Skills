Attribute VB_Name = "Module2"
Public ÇÑ±Û»çÀü As Object
Sub mkÇÑ±Û»çÀü(»çÀü, ¹è¿­)
    Set »çÀü = CreateObject("Scripting.Dictionary")
    For Each s In ¹è¿­
        t = Split(s)
        eidx = UBound(t)
        For i = 0 To eidx - 1
            »çÀü.Add t(i), t(eidx)
        Next
    Next
End Sub


Function fnCallNM(µµ¼­¸í, ÀúÀÚ¸í, ºÎ°¡±âÈ£)
    
    ³»¿ëºÐ·ù±âÈ£ = Mid(ºÎ°¡±âÈ£, 21, 3)
    µµ¼­±âÈ£ = Left(ÀúÀÚ¸í, 1) & " "
    
    ¹øÈ£ = ""
    Call mkÇÑ±Û»çÀü(ÇÑ±Û»çÀü, Split("¤¡ ¤¢ ¤¿ 1,¤À ¤Á ¤Â 2,¤± ¤Ã ¤Ä 3,¤² ¤³ ¤Å ¤Æ 4,¤µ ¤¶ ¤Ç 5,¤· ¤È ¤É ¤Ê ¤Ë 6,¤¸ ¤¹ ¤Ì ¤Í ¤Î ¤Ï ¤Ð 7,¤Ñ ¤Ò 8,¤¾ ¤Ó 9,¤¤ 21,¤§ ¤¨ 22,¤© 23,¤º 81,¤» 82,¤¼ 83,¤½ 84", ","))
    For Each i In fnÇÑ±Û(Mid(ÀúÀÚ¸í, 2, 1))
        ¹øÈ£ = ¹øÈ£ & ÇÑ±Û»çÀü(i)
    Next
'    For Each i In fnÇÑ±Û(Mid(ÀúÀÚ¸í, 2, 1))
'        Select Case i
'            Case "¤¡", "¤¢", "¤¿": ¹øÈ£ = ¹øÈ£ & "1"
'            Case "¤À", "¤Á", "¤Â": ¹øÈ£ = ¹øÈ£ & "2"
'            Case "¤±", "¤Ã", "¤Ä": ¹øÈ£ = ¹øÈ£ & "3"
'            Case "¤²", "¤³", "¤Å", "¤Æ": ¹øÈ£ = ¹øÈ£ & "4"
'            Case "¤µ", "¤¶", "¤Ç": ¹øÈ£ = ¹øÈ£ & "5"
'            Case "¤·", "¤È", "¤É", "¤Ê", "¤Ë": ¹øÈ£ = ¹øÈ£ & "6"
'            Case "¤¸", "¤¹", "¤Ì", "¤Í", "¤Î", "¤Ï", "¤Ð": ¹øÈ£ = ¹øÈ£ & "7"
'            Case "¤Ñ", "¤Ò": ¹øÈ£ = ¹øÈ£ & "8"
'            Case "¤¾", "¤Ó": ¹øÈ£ = ¹øÈ£ & "9"
'            Case "¤¤": ¹øÈ£ = ¹øÈ£ & "21"
'            Case "¤§", "¤¨": ¹øÈ£ = ¹øÈ£ & "22"
'            Case "¤©": ¹øÈ£ = ¹øÈ£ & "23"
'            Case "¤º": ¹øÈ£ = ¹øÈ£ & "81"
'            Case "¤»": ¹øÈ£ = ¹øÈ£ & "82"
'            Case "¤¼": ¹øÈ£ = ¹øÈ£ & "83"
'            Case "¤½": ¹øÈ£ = ¹øÈ£ & "84"
'        End Select
'    Next
    
    'ÀÚÀ½ = IIf(Left(µµ¼­¸í, 1) Like "*[a-zA-Z]*", Left(µµ¼­¸í, 1), fnÇÑ±Û(IIf(IsNumeric(Left(µµ¼­¸í, 1)), µµ¼­¸í(1, 7), Left(µµ¼­¸í, 1)))(0))
    'ÀÚÀ½ = IIf(Left(µµ¼­¸í, 1) Like "[a-zA-Z]", Left(µµ¼­¸í, 1), fnÇÑ±Û(IIf(IsNumeric(Left(µµ¼­¸í, 1)), µµ¼­¸í(1, 7), Left(µµ¼­¸í, 1)))(0))
    If IsNumeric(Left(µµ¼­¸í, 1)) Then
        ÀÚÀ½ = fnÇÑ±Û(µµ¼­¸í(1, 7))(0)
    ElseIf Left(µµ¼­¸í, 1) Like "[a-zA-Z]" Then
        ÀÚÀ½ = Left(µµ¼­¸í, 1)
    Else
        ÀÚÀ½ = fnÇÑ±Û(Left(µµ¼­¸í, 1))(0)
    End If
    
    ºÎ°¡±âÈ£ = ""
    ÆÄ½Ì = Split(µµ¼­¸í)
    ³¡idx = UBound(ÆÄ½Ì): ³¡len = Len(ÆÄ½Ì(³¡idx))
    ³¡³»¿ë = ÆÄ½Ì(³¡idx): ¾Õ³»¿ë = Left(µµ¼­¸í, Len(µµ¼­¸í) - ³¡len)
    
    If IsNumeric(³¡³»¿ë) Then
        ±Ç¼ö = WorksheetFunction.CountIf(Range("B:B"), ¾Õ³»¿ë & "*")
        If ±Ç¼ö > 1 Then ºÎ°¡±âÈ£ = "-" & ÆÄ½Ì(³¡idx)
    End If
    
    ±Ç¼ö = WorksheetFunction.CountIf(Range("B:B"), µµ¼­¸í)
    If ±Ç¼ö > 1 Then ºÎ°¡±âÈ£ = ºÎ°¡±âÈ£ & "=" & WorksheetFunction.CountIf(Range("B2:B" & µµ¼­¸í.row), µµ¼­¸í)
    
    fnCallNM = ³»¿ëºÐ·ù±âÈ£ & " " & µµ¼­±âÈ£ & ¹øÈ£ & " " & ÀÚÀ½ & ºÎ°¡±âÈ£
    
End Function

Sub fncallnmÅ×½ºÆ®()
    iMsg "L" Like "*[a-zA-Z]*"
    'f = fnCallNM(Range("B10"), Range("C10"), Range("F10"))
End Sub

Function fnÇÑ±Û(kor)
    
    ÃÊ¼º = Split("¤¡ ¤¢ ¤¤ ¤§ ¤¨ ¤© ¤± ¤² ¤³ ¤µ ¤¶ ¤· ¤¸ ¤¹ ¤º ¤» ¤¼ ¤½ ¤¾")
    Áß¼º = Split("¤¿ ¤À ¤Á ¤Â ¤Ã ¤Ä ¤Å ¤Æ ¤Ç ¤È ¤É ¤Ê ¤Ë ¤Ì ¤Í ¤Î ¤Ï ¤Ð ¤Ñ ¤Ò ¤Ó")
    
    If kor Like "*[°¡-ÆR]*" Then
        n = AscW(kor) + 21504
        k1 = Int(n / (21 * 28))
        k2 = Int((n Mod 21 * 28) / 28)
        
        fnÇÑ±Û = Split(ÃÊ¼º(k1) & " " & Áß¼º(k2))
        
    Else
        Exit Function
    End If
    
End Function

Sub strtophonÅ×½ºÆ®()
    
    
    
End Sub

Sub caseÅ×½ºÆ®()
    
    i = 3
    
    Select Case i
        Case Split("1 2 3 4 5"): iMsg "5ÀÌÇÏ"
        Case Split("5 6 7 8 9"): iMsg "5ÀÌ»ó"
    End Select
    
End Sub
 
