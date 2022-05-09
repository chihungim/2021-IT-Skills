VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls시험접수"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public frm As MSForms.Frame

Dim seld, t
Dim lbls() As MSForms.Label, cmds() As cls시험접수btn
Dim tit As MSForms.Label


Sub init(t, k, txt)
    '&H0080C0FF&
    seld = k
    Set frm = 시험접수.Frame1.Controls.Add("Forms.Frame.1")
    With frm
        .Top = t: .Left = 0: .Width = 270: .Height = 94
        .BorderStyle = 1: .BorderStyle = 0
        
        Set tit = .Controls.Add("Forms.Label.1")
        With tit
            .Top = 6: .Left = 6: .Width = 100: .Height = 30
            .Caption = Format(k, "yyyy.MM.dd (aaaa)")
            
        End With
        
        arr = Split(txt)
        ReDim Preserve lbls(UBound(arr))
        ReDim Preserve cmds(UBound(arr))
        
        For i = 0 To UBound(arr)
            Set cmds(i) = New cls시험접수btn
            Set cmds(i).btn = .Controls.Add("Forms.Commandbutton.1")
            Set lbls(i) = .Controls.Add("Forms.Label.1")
            
            With cmds(i).btn
                .Top = 42: .Left = 18 + (48 * i): .Width = 42: .Height = 24
                .Caption = Split(arr(i), ",")(0)
                .Tag = k & " " & Split(arr(i), ",")(0) & " " & IIf(Split(arr(i), ",")(1) >= 10, "마감", 10 - Split(arr(i), ",")(1) & "/10석")
                .BackColor = &H80C0FF
            End With
            
            With lbls(i)
                .Top = 72: .Left = 18 + (48 * i): .Width = 42: .Height = 24
                .Caption = IIf(Split(arr(i), ",")(1) >= 10, "마감", 10 - Split(arr(i), ",")(1) & "/10석")
                .TextAlign = fmTextAlignCenter
            End With
            
        Next
'        For i = 2 To Sheet4.Range("M1000").End(3).Row
'            If Sheet4.Range("N" & i) = d Then
'                Set cmds(i).btn = .Controls.Add("Forms.CommandButton.1")
'                Set lbls(i) = .Controls.Add("Forms.Label.1")
'
'                With cmds(i).btn
'                    .Top = 42: .Left = 18 + (48 * k): .Width = 42: .Height = 24
'                    .Caption = Sheet4.Range("O" & i).Value
'                    .BackColor = &H80C0FF
'                End With
'
'                With lbls(i)
'                    좌석 = Sheet4.Range("P" & i)
'                    .Top = 42 + 30: .Left = 18 + (48 * k): .Width = 42: .Height = 24
'                    .Caption = IIf(좌석 >= 10, "마감", 10 - 좌석 & "/" & 10 & "석")
'                    .TextAlign = fmTextAlignCenter
'                End With
'
'                k = k + 1
'            End If
'        Next
        
    End With
    
End Sub
