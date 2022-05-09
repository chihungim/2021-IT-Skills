VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �˻����� 
   Caption         =   "�˻�/����"
   ClientHeight    =   9660
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7785
   OleObjectBlob   =   "�˻�����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�˻�����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim dic�ñ��� As Object
Dim n, v, spinMax
Dim �⺻������(4)
Dim ��Ű����ȣ
Function fn������(��Ű��, ������, �ñ���, Ű����)
    üũ1 = ��Ű�� Like "*" & Ű���� & "*"
    
    ��ȣ�� = Split(Replace(Trim(������), " ", ""), ",")
    �� = ""
    For Each n In ��ȣ��
        Set c = dic������(Val(n))
        üũ2 = c.Cells(1, 3) Like "*" & Ű���� & "*"
        üũ3 = c.Cells(1, 5) Like "*" & Ű���� & "*"
        üũ4 = c.Cells(1, 6) Like "*" & Ű���� & "*"
        
        If (c.Cells(1, 2) = �ñ��� Or �ñ��� = "��ü") And (üũ1 Or üũ2 Or üũ3 Or üũ4) Then
            �� = �� & " " & n
        End If
    Next
    
    fn������ = Join(Split(Trim(��), " "), ", ")
End Function

Function fn����(��Ű��, ����, �ñ���, ����, ��, Ű����)
    üũ1 = ��Ű�� Like "*" & Ű���� & "*"
    
    ��ȣ�� = Split(Replace(Trim(����), " ", ""), ",")
    �� = ""
    For Each n In ��ȣ��
        Set c = dic����(Val(n))
        üũ2 = c.Cells(1, 3) Like "*" & Ű���� & "*"
        üũ3 = c.Cells(1, 4) Like "*" & Ű���� & "*"
        üũ4 = c.Cells(1, 8) Like "*" & Ű���� & "*"
        
        üũ5 = DateValue(c.Cells(1, 6)) >= DateValue(IIf(���� = "", "2021-01-01", ����)) And DateValue(c.Cells(1, 7)) <= DateValue(IIf(�� = "", "2022-12-31", ��))
        
        If (c.Cells(1, 2) = �ñ��� Or �ñ��� = "��ü") And (üũ1 Or üũ2 Or üũ3 Or üũ4) And üũ5 Then
            �� = �� & " " & n
        End If
    Next
    
    fn���� = Join(Split(Trim(��), " "), ", ")
End Function

Private Sub CommandButton1_Click()
    TreeView1.Nodes.Clear ' �ʱ�ȭ
    setNode
End Sub

Private Sub CommandButton3_Click()
    MultiPage1.Value = 0
    CommandButton3.Enabled = False
    CommandButton4.Enabled = True
End Sub

Private Sub CommandButton4_Click()
    If TreeView1.SelectedItem Is Nothing Then eMsg "������ ���� ��Ű���� �������ּ���.": Exit Sub
    ��Ű����ȣ = TreeView1.SelectedItem.Index
    Label5 = IIf(TreeView1.SelectedItem.Parent Is Nothing, TreeView1.SelectedItem, TreeView1.SelectedItem.Parent)
    Label12 = Format(Sheet6.Range("E" & ��Ű����ȣ + 1), "#,##0��")
    
    Set �̹��� = New ArrayList
    For Each i In TreeView1.Nodes
        If i.key Like "*" & Label5 & "*" And (Not i.Parent Is Nothing) Then
            ��Ű���� = Replace(i.key, Label5, "")
            �̹���.Add Split(��Ű����, ",")(0) & "\" & Split(��Ű����, ",")(2)
        End If
    Next
    
    For i = 1 To IIf(�̹���.Count < 4, �̹���.Count, 4)
        Me.Controls("Image" & i).Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\image\" & �̹���(i - 1) & ".jpg")
    Next
    
    MultiPage1.Value = 1
    CommandButton3.Enabled = True
    CommandButton4.Enabled = False
    
End Sub

Sub set����()
    
    Label20 = Int(unformat(Label12) * spinMax)
    Label22 = Int(((unformat(Label12) - (unformat(Label12) * percent(TextBox6))) * SpinButton1) + ((unformat(Label12) - (unformat(Label12) * percent(TextBox7))) * SpinButton2) + ((unformat(Label12) - (unformat(Label12) * percent(TextBox8))) * SpinButton3)) * percent(TextBox5)
    Label21 = Int((Val(Label20) - Val(Label22)))
    
End Sub


Private Sub CommandButton5_Click()
    With CommandButton5
        If Not TextBox12.Visible Then
            .Left = 330
            .Width = 42
            .Caption = "����"
            TextBox12.Visible = True
            CommandButton7.Visible = True
            Exit Sub
        End If
    End With
    
    If TextBox12 = "" Then eMsg "����� ������ �Է����ּ���.": Exit Sub
    If Not dic����.exists(TextBox12.Value) Then eMsg "�������� �ʴ� ���� �Դϴ�.": Exit Sub
    If dic����(TextBox12.Value).Cells(1, 0) <> [��ȣ] Then eMsg "������ �����ڰ� �ƴմϴ�!": Exit Sub
    If dic����(TextBox12.Value).Cells(1, 5) = "O" Then eMsg "�̹� ���� ���� �Դϴ�!": Exit Sub
    
    If dic����(TextBox12.Value).Cells(1, 4) Like "*�ѱݾ�*" Then
        TextBox5 = Format(�⺻������(0) + Val(Split(dic����(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    ElseIf dic����(TextBox12.Value).Cells(1, 4) Like "*����*" Then
        TextBox6 = Format(�⺻������(1) + Val(Split(dic����(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    ElseIf dic����(TextBox12.Value).Cells(1, 4) Like "*û�ҳ�*" Then
        TextBox7 = Format(�⺻������(2) + Val(Split(dic����(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    Else
        TextBox8 = Format(�⺻������(3) + Val(Split(dic����(TextBox12.Value).Cells(1, 4), " = ")(1)), "0%")
    End If
    set����
End Sub

Private Sub CommandButton6_Click()
    If spinMax < 1 Then eMsg "�ּ� 1���� �����Ͽ��� �մϴ�!": Exit Sub
    
    With Sheet7
        lrow = .Range("A1000").End(3).row + 1
        .Range("A" & lrow & ":G" & lrow) = Array(lrow - 1, [��ȣ], Date, ��Ű����ȣ, SpinButton1, SpinButton2, SpinButton3)
        .Range("I" & lrow & ":L" & lrow) = Array(TextBox6, TextBox7, TextBox8, TextBox5)
        If TextBox12.Visible Then
            dic����(TextBox12.Value).Cells(1, 5) = "O"
        End If
        iMsg "����Ǿ����ϴ�."
        Unload Me
    End With
    
End Sub

Private Sub CommandButton7_Click()
    With CommandButton5
        If .Caption = "����" Then
            .Left = 300
            .Width = 72
            .Caption = "���� �����ϱ�"
            TextBox12.Visible = False
            CommandButton7.Visible = False
            For i = 5 To 8
                Me.Controls("Textbox" & i) = Format(�⺻������(i - 5), "0%")
            Next
            set����
        End If
    End With
    
    
End Sub

Private Sub MultiPage1_Change()
    
End Sub

Private Sub SpinButton1_Change()
     
    If spinMax >= 100 Then eMsg "�� �ο��� 100���� �ʰ��� �� �����ϴ�.": Exit Sub
    
    TextBox9 = SpinButton1
    spinMax = SpinButton1 + SpinButton2 + SpinButton3
    set����
    
End Sub
Private Sub SpinButton2_Change()
     
    If spinMax >= 100 Then eMsg "�� �ο��� 100���� �ʰ��� �� �����ϴ�.": Exit Sub
    
    TextBox10 = SpinButton2
    spinMax = SpinButton1 + SpinButton2 + SpinButton3
    set����
    
End Sub
Private Sub SpinButton3_Change()
     
    If spinMax >= 100 Then eMsg "�� �ο��� 100���� �ʰ��� �� �����ϴ�.": Exit Sub
    
    TextBox11 = SpinButton3
    spinMax = SpinButton1 + SpinButton2 + SpinButton3
    set����
    
End Sub


Private Sub TreeView1_DblClick()
    �� = TreeView1.SelectedItem.Text
    
    If dic��Ű��.exists(��) Then Exit Sub
    
    With ����
        .Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\image\" & Split(TreeView1.SelectedItem.key, ",")(0) & "\" & TreeView1.SelectedItem.Text & ".jpg")
        .Caption = ��
        .Show
    End With
        
End Sub

Private Sub UserForm_Initialize()
    
    MultiPage1.Style = fmTabStyleNone
    
    ComboBox1.AddItem "��ü"
    
    With Sheet13
        For i = 2 To .Range("A1000").End(3).row
            ComboBox1.AddItem .Range("A" & i)
        Next
    End With
    ComboBox1.ListIndex = 0
    
    With TreeView1
        .LineStyle = tvwRootLines
        
    End With
    
    
    
    TextBox4 = Date
    With Sheet2
        For i = get��� To 2 Step -1
            Set c = .Cells(1, i)
            For k = 2 To 4
                If c.Cells(k, 1) = "" Then Exit For
                If c.Cells(k, 1) Like "*�ѱݾ�*" Then
                    TextBox5 = Val(TextBox5) + Val(Split(c.Cells(k, 1), " = ")(1))
                ElseIf c.Cells(k, 1) Like "*����*" Then
                    TextBox6 = Val(TextBox6) + Val(Split(c.Cells(k, 1), " = ")(1))
                ElseIf c.Cells(k, 1) Like "*û�ҳ�*" Then
                    TextBox7 = Val(TextBox7) + Val(Split(c.Cells(k, 1), " = ")(1))
                Else
                    TextBox8 = Val(TextBox8) + Val(Split(c.Cells(k, 1), " = ")(1))
                End If
            Next
        Next
    End With
    For i = 5 To 8
        �⺻������(i - 5) = Me.Controls("Textbox" & i)
        Me.Controls("Textbox" & i) = Format(Me.Controls("Textbox" & i), "0%")
    Next
    
    mkdic dic��Ű��, Sheet6.Range("B2")
    mkdic dic������, Sheet3.Range("A2")
    mkdic dic����, Sheet5.Range("A2")
    mkdic dic����, Sheet9.Range("C2")
    Set dic�ñ��� = CreateObject("Scripting.Dictionary")
    setNode
    
End Sub

Function get���()
    Select Case [���]
        Case "���̾�": get��� = 5
        Case "���": get��� = 4
        Case "�ǹ�": get��� = 3
        Case "�����": get��� = 2
    End Select
End Function

Sub setNode()
    
    With TreeView1.Nodes
        For i = 2 To Sheet6.Range("A1000").End(3).row
            .Add key:=Sheet6.Range("B" & i), Text:=Sheet6.Range("B" & i)
        Next
        For i = 2 To Sheet6.Range("A1000").End(3).row
            Call child(i, Sheet6.Range("B" & i).Value)
        Next
    End With
    
End Sub

Sub child(row, cont)
    
    With Sheet6
        fes = Split(fn����(.Range("B" & row).Value, .Range("C" & row).Value, ComboBox1, TextBox1, TextBox2, TextBox3), ", ")
        tou = Split(fn������(.Range("B" & row).Value, .Range("D" & row).Value, ComboBox1, TextBox3), ", ")
        
        cnt = 1

        For i = 0 To UBound(fes)
            TreeView1.Nodes.Add .Range("B" & row).Value, tvwChild, "����," & cont & CStr(cnt) & "," & dic����(Int(fes(i))).Cells(1, 3), dic����(Int(fes(i))).Cells(1, 3)
            cnt = cnt + 1
        Next
        
        For i = 0 To UBound(tou)
            TreeView1.Nodes.Add .Range("B" & row).Value, tvwChild, "������," & cont & CStr(cnt) & "," & dic������(Int(tou(i))).Cells(1, 3), dic������(Int(tou(i))).Cells(1, 3)
            cnt = cnt + 1
        Next
    End With
    
End Sub
