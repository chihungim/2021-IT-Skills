VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} È¸¿ø°¡ÀÔ¼öÁ¤ 
   Caption         =   "È¸¿ø°¡ÀÔ"
   ClientHeight    =   5760
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5985
   OleObjectBlob   =   "È¸¿ø°¡ÀÔ¼öÁ¤.frx":0000
   StartUpPosition =   1  '¼ÒÀ¯ÀÚ °¡¿îµ¥
End
Attribute VB_Name = "È¸¿ø°¡ÀÔ¼öÁ¤"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim max As Integer, chk As Boolean

Private Sub ComboBox1_Change()
    TextBox7.Visible = IIf(ComboBox1.ListIndex = ComboBox1.ListCount - 1, True, False)
    max = IIf(ComboBox1.ListIndex = ComboBox1.ListIndex - 1, 6, 5)
End Sub

Private Sub CommandButton1_Click()
    createRandomCharacters
End Sub

Sub createRandomCharacters()
    max = 0
    Randomize
    
    È®ÀÎ¹®ÀÚ = ""
    
    For i = 0 To 5
        ch = Int(Rnd * 2)
        max = max + ch
        
        If ch = 1 Then
            È®ÀÎ¹®ÀÚ = È®ÀÎ¹®ÀÚ + Chr(Int(Rnd * 26) + 65)
        Else
            È®ÀÎ¹®ÀÚ = È®ÀÎ¹®ÀÚ & Int(Rnd * 10)
        End If
    Next
    
    If max <> 4 Then createRandomCharacters
    
End Sub

Private Sub CommandButton2_Click()
    For i = 1 To max
        If Me.Controls("TextBox" & i) = "" Then
            eMsg "ºóÄ­ÀÌ ÀÖ½À´Ï´Ù."
            Exit Sub
        End If
    Next
    
    Call »çÀü»ý¼º(Sheet2.Range("C2"))
    
    If »çÀü.exists(TextBox1.Value) Then eMsg "ÀÌ¹ÌÀÖ´Â ¾ÆÀÌµðÀÔ´Ï´Ù.": Exit Sub
    
    If Not (TextBox3 Like "*[0-9]*" And TextBox3 Like "*[a-zA-Z]*" And TextBox3 Like "*[!0-9a-zA-Z¤¡-ÆR]*") Or Len(TextBox3) < 6 Then
        eMsg "ºñ¹Ð¹øÈ£´Â ¼ýÀÚ, ¿µ¹®ÀÚ, Æ¯¼ö¹®ÀÚ¸¦ Æ÷ÇÔÇÏ¿© 6±ÛÀÚ ÀÌ»óÀ¸·Î ÀÔ·ÂÇØÁÖ¼¼¿ä."
        Exit Sub
    End If
    
    If TextBox3 <> TextBox4 Then
        eMsg "ºñ¹Ð¹øÈ£°¡ ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù."
        Exit Sub
    End If
    
    If OptionButton1 = False And OptionButton2 = False Then
        eMsg "ºÐ·ù¸¦ ¼±ÅÃÇØÁÖ¼¼¿ä": Exit Sub
    End If

    
    If Not (LCase(È®ÀÎ¹®ÀÚ) = LCase(È®ÀÎ¹®ÀÚ) Or UCase(È®ÀÎ¹®ÀÚ) = UCase(È®ÀÎ¹®ÀÚ)) Then
        eMsg "È®ÀÎ¹®ÀÚ¸¦ Àß¸øÀÔ·ÂÇÏ¼Ì½À´Ï´Ù."
        Exit Sub
    End If
    
    If CommandButton2.Caption = "¼öÁ¤ ¿Ï·á" Then
        iMsg "¼öÁ¤ÀÌ ¿Ï·áµÇ¾ú½À´Ï´Ù."
        irow = Sheet1.Range("¹øÈ£").Value + 1
        Sheet2.Range("A" & irow & ":E" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, IIf(max = 5, TextBox5 & "@" & ComboBox1, TextBox5 & "@" & TextBox6))
    Else
        iMsg "È¸¿ø°¡ÀÔÀÌ ¿Ï·áµÇ¾ú½À´Ï´Ù."
        irow = Sheet2.Range("A1000").End(xlUp).Row + 1
        Sheet2.Range("A" & irow & ":E" & irow) = Array(irow - 1, TextBox1, TextBox2, TextBox3, IIf(max = 5, TextBox5 & "@" & ComboBox1, TextBox5 & "@" & TextBox6))
    End If
    
    Sheet2.Range("F" & irow) = IIf(OptionButton1 = True, "ÀÏ¹Ý", "È­°¡")
    Sheet2.Range("G" & irow).FillDown
    
    If chk Then
        ¹øÈ£ = Sheet1.Range("¹øÈ£").Value
        Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\Áö±ÞÀÚ·á\È¸¿ø»çÁø\" & ¹øÈ£ & ".jpg")
        With Sheet2.Range("B" & irow).AddComment
            .Shape.Fill.UserPicture ThisWorkbook.Path & "\Áö±ÞÀÚ·á\È¸¿ø»çÁø\" & ¹øÈ£ & ".jpg"
        End With
    End If
End Sub

Private Sub CommandButton3_Click()
    Unload Me
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("image file", "*.jpg")
        cho = .Show
        If cho <> 0 Then
            chk = True
            pic = .SelectedItems(1)
        Else
            pic = ThisWorkbook.Path & "\Áö±ÞÀÚ·á\È¸¿ø»çÁø\±âº».jpg"
        End If
        Image1.Picture = LoadPicture(pic)
        Repaint
    End With
End Sub

Private Sub OptionButton1_Click()

End Sub

Private Sub OptionButton1_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    OptionButton1.ControlTipText = "ÀÏ¹Ý È¸¿øÀº ÀÛÇ° Á¦ÀÛÀÌ ºÒ°¡´ÉÇÕ´Ï´Ù."
End Sub


Private Sub OptionButton2_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    OptionButton2.ControlTipText = "È­°¡ È¸¿øÀº ÀÛÇ° ±¸¸ÅÄ¢ Æò°¡°¡ ºÒ°¡´É ÇÕ´Ï´Ù."
End Sub

Private Sub UserForm_Initialize()
    createRandomCharacters
    ComboBox1.List = Array("naver.com", "gmail.com", "hanmail.net", "icloude.com", "Á÷Á¢ÀÔ·Â")
    ComboBox1.ListIndex = 0
    TextBox1.SetFocus

    If Sheet1.CommandButton2.Caption = "È¸¿øÁ¤º¸ ¼öÁ¤" Then
        Me.Caption = "È¸¿øÁ¤º¸ ¼öÁ¤"
        CommandButton2.Caption = "¼öÁ¤ ¿Ï·á"
        
        °æ·Î = ThisWorkbook.Path & "\Áö±ÞÀÚ·á\È¸¿ø»çÁø\" & Sheet1.Range("¹øÈ£").Value & ".jpg"

        If Not Dir(°æ·Î) = "" Then
            Image1.Picture = LoadPicture(°æ·Î)
        End If
        TextBox1 = Sheet1.Range("ÀÌ¸§")
        »çÀü»ý¼º Sheet2.Range("B2")
        
        With »çÀü(TextBox1.Value)
            TextBox2 = .Cells(1, 2)
            ÀÌ¸ÞÀÏ = Split(.Cells(1, 4), "@")
            TextBox5 = ÀÌ¸ÞÀÏ(0)
            For i = 0 To ComboBox1.ListCount
                ComboBox1.ListIndex = i
                If ÀÌ¸ÞÀÏ(1) = ComboBox1 Then Exit For
            Next
            Debug.Print .Cells(1, 5)
            OptionButton1 = IIf(.Cells(1, 5) = "ÀÏ¹Ý", True, False)
            OptionButton2 = IIf(.Cells(1, 5) = "È­°¡", True, False)
        End With
    
        TextBox1.Enabled = False
        TextBox2.Enabled = False
    End If
End Sub

