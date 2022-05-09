VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls구매"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Dim img As MSForms.Image
Dim lbl As MSForms.Label
Dim frm As MSForms.Frame
Dim WithEvents btn As CommandButton
Attribute btn.VB_VarHelpID = -1
Dim ref
Sub init(fr As MSForms.Frame, t, l, name, cnt, price, no)
    Set frm = fr.Controls.add("Forms.Frame.1")
    Set lbl = frm.Controls.add("Forms.Label.1")
    Set img = frm.Controls.add("Forms.Image.1")
    Set btn = frm.Controls.add("Forms.CommandButton.1")
    ref = no
    With frm
        .Top = t
        .Left = l
        .Width = 165
        .Height = 70
    End With
    
    With img
        .Top = 5
        .Left = 5
        .Width = 60
        .Height = 60
        .Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\음식사진\" & name & ".jpg")
    End With
    
    With lbl
        .Top = 5
        .Left = 70
        .Width = 70
        .Height = 60
        .Caption = name & vbCrLf & vbCrLf & cnt & "개" & vbCrLf & vbCrLf & Format(price, "#,##0원")
    End With
    
    With btn
        .Top = 5
        .Left = 135
        .Width = 20
        .Height = 60
        .Caption = "X"
    End With
    
End Sub


Private Sub btn_Click()
    Sheet10.Range(Sheet10.Range("I1:I10000").Find(ref), Sheet10.Range("I1:I10000").Find(ref).Cells(1, 4)).Delete xlShiftUp
    구매.reFreash
End Sub
