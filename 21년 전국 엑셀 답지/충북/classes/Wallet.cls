VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Wallet"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public img As MSForms.Image
Dim lbl() As MSForms.Label

Public Sub init(frm As MSForms.UserForm, idx, no, 이름, 번호, 실적, 포인트, 브랜드)
    
    ReDim Preserve lbl(4)
    
    Set img = frm.MultiPage1.Pages(idx).Controls.Add("Forms.Image.1")
    img.PictureSizeMode = fmPictureSizeModeStretch
    img.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & no & ".jpg")
    img.BorderStyle = fmBorderStyleNone
    img.Width = 220: img.Height = 125: img.Top = 5: img.Left = 95
    
    arr = Array(이름, 번호, 실적, 포인트, 브랜드)
    sz = Array(14, 12, 9, 9, 9)
    
    For i = 0 To 4
        Set lbl(i) = frm.MultiPage1.Pages(idx).Controls.Add("Forms.Label.1")
        lbl(i).Caption = arr(i)
        lbl(i).Width = 220: lbl(i).Height = 30: lbl(i).Top = 144 + (30 * i): lbl(i).Left = 95
        lbl(i).TextAlign = fmTextAlignCenter
        lbl(i).Font.Name = "맑은 고딕"
        lbl(i).Font.Size = sz(i)
    Next
    lbl(0).Font.Bold = True

    
End Sub

