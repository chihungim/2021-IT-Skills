VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls대출아이템"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public img As MSForms.Image
Public lbl As MSForms.Label
Public WithEvents chk As MSForms.CheckBox
Attribute chk.VB_VarHelpID = -1

Public Sub 대출init(f As UserForm, img_path, cap, t, l, Optional iname As String = "")
    Set frm = f
    Set img = frm.Controls.Add("Forms.Image.1")
    Set lbl = frm.Controls.Add("Forms.Label.1")
    Set chk = frm.Controls.Add("Forms.CheckBox.1")
    
    img.Picture = LoadPicture(img_path)
    lbl = cap
    
    img.PictureSizeMode = fmPictureSizeModeStretch
    img.Height = 150
    img.Width = 100
    
    img.Top = t
    img.Left = l
    img.name = IIf(iname = "", img.name, name)
    
    lbl.Top = img.Height + 15
    lbl.Left = l
    
    chk.Left = l + img.Width + 5
    chk.Top = img.Height / 2
    chk.Caption = ""
    chk.Width = 15
End Sub

Private Sub chk_Click()
    If chk.Value = True Then
        도서대출.Label4 = Val(도서대출.Label4) + 1 & "권"
    Else
        도서대출.Label4 = Val(도서대출.Label4) - 1 & "권"
    End If
    
    
End Sub
