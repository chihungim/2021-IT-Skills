VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Class3"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents img As MSForms.Image '
Attribute img.VB_VarHelpID = -1
Public WithEvents lbl As MSForms.Label
Attribute lbl.VB_VarHelpID = -1

Sub display(obj, t, l, w, h, fpath, ltext)
    Set img = obj.Controls.Add("Forms.Image.1", fpath)
    Set lbl = obj.Controls.Add("Forms.Label.1", ltext)
    
    With img
        .top = t
        .left = l
        .width = w
        .height = h
        .Picture = LoadPicture(fpath)
        .PictureSizeMode = fmPictureSizeModeStretch
    End With
    
    With lbl
        .top = img.top
        .left = img.left + img.width + 10
        .width = img.width + 100
        .height = img.height
        .Caption = ltext
    End With
End Sub
