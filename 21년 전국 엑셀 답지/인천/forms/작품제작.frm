VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ��ǰ���� 
   Caption         =   "��ǰ ����"
   ClientHeight    =   8250
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   12525
   OleObjectBlob   =   "��ǰ����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��ǰ����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim pic As New Class1
Dim cls(7) As New Class1
Dim mode As Boolean



Private Sub CommandButton1_Click()
    InkPicture1.EditingMode = IOEM_Ink
    Label4 = "��"
End Sub


Private Sub CommandButton14_Click()
    For i = 1 To 7
        cls(i).btn.Visible = True
    Next
    
    CommandButton18.Visible = True
    
End Sub

Private Sub CommandButton15_Click()
    InkPicture1.EditingMode = IOEM_Ink
    
    mode = Not mode
    
    If mode Then
        InkPicture1.DefaultDrawingAttributes.PenTip = IPT_Rectangle
        Label4 = "������"
    Else
        InkPicture1.DefaultDrawingAttributes.PenTip = IPT_Ball
        Label4 = "��"
    End If
    
End Sub

Private Sub CommandButton16_Click()
    InkPicture1.Ink.DeleteStroke
    Me.Repaint
End Sub

Private Sub CommandButton17_Click()
    If Not Dir(ThisWorkbook.Path & "\�׸�.gif") = "" Then Kill ThisWorkbook.Path & "\�׸�.gif"
    Unload Me
End Sub
Private Sub CommandButton18_Click()
    Dim colorcode: colorcode = Sheet1.Range("A50").Interior.Color
    
    red = colorcode Mod 256: green = (colorcode \ 256) Mod 256: blue = colorcode \ 65536
    
    If Application.Dialogs(xlDialogEditColor).Show(1, red, green, blue) = True Then
        colorcode = ActiveWorkbook.Colors(1)
        InkPicture1.DefaultDrawingAttributes.Color = colorcode
        Label7.BackColor = colorcode
        Sheet1.Range("A50").Interior.Color = colorcode
    End If
End Sub

Private Sub CommandButton2_Click()
    CommandButton5.Visible = True
    CommandButton6.Visible = True
End Sub

Private Sub CommandButton3_Click()
    InkPicture1.EditingMode = IOEM_Select
    Label4 = "���� ����"
End Sub

Private Sub CommandButton4_Click()
    If InkPicture1.Ink.Strokes.Count = 0 Then
        eMsg "������ ��ǰ�� �����ϴ�."
        Exit Sub
    End If
    
    If MsgBox("���� ������ �Ұ��մϴ�." & vbCrLf & "�����Ͻðڽ��ϱ�?", vbExclamation + vbYesNo) = vbYes Then
        fpath = ThisWorkbook.Path & "\�׸�.gif"
        Dim byt() As Byte
        
        If InkPicture1.Ink.Strokes.Count > 0 Then
            byt = InkPicture1.Ink.Save(2)
            Open fpath For Binary As #1
            Put #1, , byt
            Close #1
        End If
        
            
    Unload Me
    
    �׸�����.Show
    End If
End Sub

Private Sub CommandButton5_Click()
    InkPicture1.EditingMode = 1
    InkPicture1.EraserMode = IOERM_StrokeErase
    CommandButton5.Visible = False
    CommandButton6.Visible = False
    Label4 = "����"
End Sub

Private Sub CommandButton6_Click()
    InkPicture1.EditingMode = 1
    InkPicture1.EraserMode = IOERM_PointErase
    CommandButton5.Visible = False
    CommandButton6.Visible = False
    Label4 = "����Ʈ"
End Sub

Private Sub ScrollBar1_Change()
    InkPicture1.DefaultDrawingAttributes.width = ScrollBar1.Value
    InkPicture1.EraserWidth = ScrollBar1.Value
    Label5 = ScrollBar1.Value
End Sub

Private Sub InkPicture1_MouseDown(ByVal Button As MSINKAUTLib.InkMouseButton, ByVal Shift As MSINKAUTLib.InkShiftKeyModifierFlags, ByVal pX As Long, ByVal pY As Long, Cancel As Boolean)

    CommandButton5.Visible = False
    CommandButton6.Visible = False
    For i = 1 To 7
        cls(i).btn.Visible = False
    Next
    
    CommandButton18.Visible = False

End Sub

Private Sub UserForm_Initialize()
     For i = 1 To 7
        Set cls(i).btn = Me.Controls("CommandButton" & i + 6)
    Next
    
    ScrollBar1.Value = 53
    
    Sheet1.Range("A50").Interior.Color = vbBlack
    
    Label4 = "��"
End Sub


Private Sub UserForm_QueryClose(Cancel As Integer, CloseMode As Integer)

    If Not Dir(ThisWorkbook.Path & "\�׸�.gif") = "" & CloseMode = 0 Then
        Kill ThisWorkbook.Path & "\�׸�.gif"
    End If

End Sub
