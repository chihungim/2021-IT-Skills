VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Class1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents img As Image
Attribute img.VB_VarHelpID = -1
Public WithEvents lbl As MSForms.Label
Attribute lbl.VB_VarHelpID = -1
Public nm

Public WithEvents btn As CommandButton
Attribute btn.VB_VarHelpID = -1

Private Sub btn_Click()
    작품제작.InkPicture1.DefaultDrawingAttributes.Color = btn.BackColor
    작품제작.Label7.BackColor = btn.BackColor
    Sheet1.Range("A50").Interior.Color = btn.BackColor
End Sub


Sub imgInit(ctrl, cap, t, l, w, h)
    Set img = ctrl.Add("Forms.Image.1", cap)
    img.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & Split(cap, ",")(1) & "\" & Split(cap, ",")(0) & ".jpg")
    With img
        .top = t: .left = l: .width = w: .height = h
        .PictureSizeMode = fmPictureSizeModeStretch
    End With
End Sub

Sub labelInit(ctrl, cap, t, l, w, h)
    Set lbl = ctrl.Add("Forms.Label.1", cap)
    With lbl
        .Caption = cap
        .top = t: .left = l: .width = w: .height = h
        .TextAlign = fmTextAlignCenter
        .BackColor = vbWhite
        .BorderStyle = fmBorderStyleSingle
        .BorderColor = &H8000000A
        .name = i
    End With
End Sub

Private Sub img_Click()
    If nm = "" Then
        infoarr = Split(img.name, ",")
    Else
        infoarr = Split(nm, ",")
    End If
    ''RO05,낭만주의,새의 찻잔1,양태숙,구름이 떠있는 새들의 찻잔,2250000,3.125
    
    With 상세정보
        .Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & infoarr(1) & "\" & infoarr(0) & ".jpg")
        .imgNo = infoarr(0)
        .TextBox1 = infoarr(4) ''작품명
        .TextBox2 = infoarr(3) ''작가
        .TextBox3 = infoarr(2) ''설명
        .TextBox4 = Format(infoarr(5), "#,##0")
        .TextBox5 = Round(infoarr(6), 2)
        .Show
    End With
End Sub



