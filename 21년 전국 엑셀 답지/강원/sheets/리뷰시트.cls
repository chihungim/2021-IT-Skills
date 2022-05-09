VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet8"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_BeforeDoubleClick(ByVal Target As Range, Cancel As Boolean)
    r = Target.row: c = Target.Column
    
    If Cells(r, c) = "" Then
        rType = "µî·Ï"
        ¸®ºä.Show
    Else
        rType = "¼öÁ¤"
        With Sheet12
            .Range("D6") = Cells(r, 4): .Range("I6") = Cells(r, 5): .Range("M6") = Cells(r, 6)
        End With
        With ¸®ºä
            .Caption = "¸®ºä ¼öÁ¤"
            .ComboBox1.Enabled = False
            .ComboBox2.Enabled = False
            .ComboBox1.ListIndex = 0
            .ComboBox2.ListIndex = 0
            
            Select Case Left(Cells(r, 3), 1)
                Case "P": .ComboBox1.ListIndex = 0
                Case "G": .ComboBox1.ListIndex = 1
                Case "C": .ComboBox1.ListIndex = 2
            End Select
            
            .ComboBox2.ListIndex = Right(Cells(r, 3), Len(Cells(r, 3)) - 1) - 1
            
            .TextBox1 = Cells(r, 7)
            If Cells(r, 8) = "O" Then .Image1.Picture = LoadPicture(ThisWorkbook.Path & "\Áö±ÞÀÚ·á\image\¸®ºä\" & Cells(r, 1) & ".jpg")
            If Cells(r, 9) = "O" Then
                .OptionButton1.Value = True
            Else
                .OptionButton2.Value = True
            End If
            .Show
        End With
    End If
    
End Sub
