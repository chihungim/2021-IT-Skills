VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls시험"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public frm As MSForms.Frame
Public WithEvents lbl1 As MSForms.Label
Attribute lbl1.VB_VarHelpID = -1
Public WithEvents lbl2 As MSForms.Label
Attribute lbl2.VB_VarHelpID = -1
Public WithEvents lbl3 As MSForms.Label
Attribute lbl3.VB_VarHelpID = -1
Public WithEvents lbl4 As MSForms.Label
Attribute lbl4.VB_VarHelpID = -1
Public WithEvents opt1 As MSForms.OptionButton
Attribute opt1.VB_VarHelpID = -1
Public WithEvents opt2 As MSForms.OptionButton
Attribute opt2.VB_VarHelpID = -1
Public WithEvents opt3 As MSForms.OptionButton
Attribute opt3.VB_VarHelpID = -1
Public WithEvents opt4 As MSForms.OptionButton
Attribute opt4.VB_VarHelpID = -1

Public sans, cnt

Public Sub boxInit(t, l)
    Set frm = 시험.Frame2.Controls.Add("Forms.Frame.1")
    With frm
        .Top = t: .Left = l: .Width = 354: .Height = 180
    End With
End Sub

Public Sub init(tit, cap)
    
    With frm
        
        
        Set lbl = .Controls.Add("Forms.Label.1")
        With lbl
            .Top = 0: .Left = 0: .Width = 354: .Height = 180
            .Caption = tit
            .Font.Size = 12: .Font.Bold = True
        End With
        
        Set lbl1 = .Controls.Add("Forms.Label.1")
        With lbl1
            .Top = 36: .Left = 0: .Width = 354: .Height = 36
            .Caption = "1. " & cap(1, 1)
            .Font.Size = 10
        End With
        
        Set lbl2 = .Controls.Add("Forms.Label.1")
        With lbl2
            .Top = 36 * 2: .Left = 0: .Width = 354: .Height = 36
            .Caption = "2. " & cap(1, 2)
            .Font.Size = 10
        End With
        
        Set lbl3 = .Controls.Add("Forms.Label.1")
        With lbl3
            .Top = 36 * 3: .Left = 0: .Width = 354: .Height = 36
            .Caption = "3. " & cap(1, 3)
            .Font.Size = 10
        End With
        
        Set lbl4 = .Controls.Add("Forms.Label.1")
        With lbl4
            .Top = 36 * 4: .Left = 0: .Width = 354: .Height = 36
            .Caption = "4. " & cap(1, 4)
            .Font.Size = 10
            
        End With
        
    End With
    
    
End Sub

Public Sub opInit(t, i, c)
    With 시험.Frame1
        Set optit = .Controls.Add("Forms.Label.1")
        With optit
            .Top = t: .Left = 6: .Width = 24: .Height = 24
            .Caption = c
            .Font.Bold = True: .Font.Size = 11
            .TextAlign = fmTextAlignCenter
        End With
        
        Set opt1 = .Controls.Add("Forms.OptionButton.1")
        With opt1
            .Top = t: .Left = 42
            .Caption = 1
            .AutoSize = True
            .GroupName = c
        End With
        
        Set opt2 = .Controls.Add("Forms.OptionButton.1")
        With opt2
            .Top = t: .Left = 42 + 36
            .Caption = 2
            .AutoSize = True
            .GroupName = c
        End With
        
        Set opt3 = .Controls.Add("Forms.OptionButton.1")
        With opt3
            .Top = t: .Left = 42 + (36 * 2)
            .Caption = 3
            .AutoSize = True
            .GroupName = c
        End With
        
        Set opt4 = .Controls.Add("Forms.OptionButton.1")
        With opt4
            .Top = t: .Left = 42 + (36 * 3)
            .Caption = 4
            .AutoSize = True
            .GroupName = c
        End With
        
    End With
    
End Sub

Sub 문제클릭(번호)
    lbl1.ForeColor = vbBlack: lbl2.ForeColor = vbBlack: lbl3.ForeColor = vbBlack: lbl4.ForeColor = vbBlack
    sans = 번호
    If cnt = 0 Then cnt = 1: 시험.남은문항수 = Val(시험.남은문항수) - 1
    
    Select Case 번호
        Case 1: lbl1.ForeColor = vbRed: opt1.Value = True
        Case 2: lbl2.ForeColor = vbRed: opt2.Value = True
        Case 3: lbl3.ForeColor = vbRed: opt3.Value = True
        Case 4: lbl4.ForeColor = vbRed: opt4.Value = True
    End Select
End Sub

Private Sub lbl1_Click()
    Call 문제클릭(1)
End Sub
Private Sub lbl2_Click()
    Call 문제클릭(2)
End Sub
Private Sub lbl3_Click()
    Call 문제클릭(3)
End Sub
Private Sub lbl4_Click()
    Call 문제클릭(4)
End Sub

Private Sub opt1_Click()
    Call 문제클릭(1)
End Sub
Private Sub opt2_Click()
    Call 문제클릭(2)
End Sub
Private Sub opt3_Click()
    Call 문제클릭(3)
End Sub
Private Sub opt4_Click()
    Call 문제클릭(4)
End Sub
