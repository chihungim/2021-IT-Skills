VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ����̺�Ʈ 
   Caption         =   "����̺�Ʈ"
   ClientHeight    =   3525
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9165
   OleObjectBlob   =   "����̺�Ʈ.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����̺�Ʈ"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim idx As Integer
Dim chkImg
Private Sub ComboBox1_Change()
    
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton1_Click()
    
    If TextBox1 = "" Then eMsg "�����ڵ带 �Է��ϼ���.": Exit Sub
    
    Call mkDic(dic����, Sheet4.Range("B2"))
    
    If Not dic����.exists(TextBox1.Value) Then eMsg "�������� �ʴ� �����Դϴ�": Exit Sub
    
    idx = 0
    With Sheet7
        For i = 2 To .Range("A10000").End(3).row
            If .Range("B" & i) = TextBox1 Then
                idx = i
                Exit For
            End If
        Next
        
        For i = 2 To 4
            Me.Controls("Textbox" & i).Locked = False
        Next
        If idx = 0 Then
            Me.Caption = "��� ���"
            CommandButton2.Caption = "���"
            Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\����ǰ\�⺻�̹���.jpg")
            TextBox2 = "": TextBox3 = "": TextBox4 = "": ComboBox1 = ""
        Else
            Me.Caption = "������"
            CommandButton2.Caption = "����"
            Set rg = .Range("A" & idx)
            TextBox2 = rg(1, 3)
            TextBox3 = rg(1, 4)
            TextBox4 = rg(1, 5)
            ComboBox1 = rg(1, 6)
            
            On Error Resume Next
            Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\����ǰ\" & rg(1, 3) & ".jpg")
        End If
    End With
    
    
    
    
End Sub

Private Sub CommandButton2_Click()
'    iMsg idx

    If TextBox1 = "" Or TextBox2 = "" Or TextBox3 = "" Or TextBox4 = "" Or ComboBox1 = "" Then eMsg "��ĭ�� �����մϴ�.": Exit Sub
    
    With Sheet7
        lrow = IIf(idx = 0, .Range("A1000").End(3).row + 1, idx)
        
        .Range("A" & lrow & ":F" & lrow) = Array(idx - 1, TextBox1, TextBox2, TextBox3, TextBox4, ComboBox1)
        If chkImg Then
            Call SavePicture(ThisWorkbook.Path & "\�����ڷ�\����ǰ\" & TextBox2 & ".jpg")
        End If
        
    End With
    
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        
        If .Show <> 0 Then imgpath = .SelectedItems(1)
        
        If imgpath = "" Then
            Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\����ǰ\�⺻�̹���.jpg")
            chkImg = False
        Else
            Image1.Picture = LoadPicture(imgpath)
            chkImg = True
        End If
        
        
        
        Repaint
        
        
    End With
End Sub

Private Sub TextBox2_BeforeUpdate(ByVal Cancel As MSForms.ReturnBoolean)
    
    For i = 2 To Sheet7.Range("A10000").End(3).row
        If Sheet7.Range("C" & i) = TextBox2 Then
           Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\����ǰ\" & TextBox2 & ".jpg")
        End If
    Next
    
End Sub

Private Sub TextBox2_Change()
    
End Sub

Private Sub TextBox3_Change()

End Sub

Private Sub TextBox3_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal x As Single, ByVal y As Single)
    �޷�.������ = ""
    �޷�.Show
End Sub

Private Sub TextBox4_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal x As Single, ByVal y As Single)
    If TextBox3 = "" Then eMsg "�������� �����ϼ���.": Exit Sub
    
    �޷�.������ = DateValue(TextBox3)
    �޷�.Show
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    ComboBox1.List = Split("���,10��,20��,30��,40��,50��,60��,70�� �̻�", ",")
    ComboBox1.Style = fmStyleDropDownList
End Sub

