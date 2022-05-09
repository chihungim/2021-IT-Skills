VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ������� 
   Caption         =   "UserForm1"
   ClientHeight    =   7395
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9720
   OleObjectBlob   =   "�������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim ��������
Dim ����з���ȣ

Private Sub CommandButton1_Click()
    For i = 1 To 4
        If Me.Controls("Textbox" & i) = "" Then eMsg "������ �׸��� �ֽ��ϴ�.": Exit Sub
    Next
    If Label14 = "" Or Label15 = "" Then eMsg "������ �׸��� �ֽ��ϴ�.": Exit Sub
    
    If ���ڴ���ȣ = "" Or �������±�ȣ = "" Then eMsg "������ �׸��� �ֽ��ϴ�.": Exit Sub
    
    Call mkdic(dicå, Sheet3.Range("A2"))
    For Each k In dicå.Keys
        If dicå(k).Cells(1, 2) = TextBox1 Then eMsg "�ش� ������ �̹� ��ϵ� �����Դϴ�.": Exit Sub
    Next
    
    With Sheet3
        lrow = .Range("A10000").End(3).row + 1
        For i = 1 To SpinButton1.Value
            .Range("B" & lrow & ":F" & lrow) = Array(TextBox1, TextBox2, TextBox3, Date, Label15)
            lrow = lrow + 1
        Next
        iMsg "���� ����� �Ϸ�Ǿ����ϴ�."
        
    End With
    
    
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("image file", "*.jpg")
        If .Show <> 0 Then Image1.Picture = LoadPicture(.SelectedItems(1)): �������� = True: Repaint
        
    End With
End Sub

Private Sub ListBox1_Click()
    setû����ȣisbn
End Sub

Private Sub ListBox2_Click()
    setû����ȣisbn
End Sub

Private Sub SpinButton1_Change()
    TextBox4 = SpinButton1
End Sub

Private Sub TextBox1_Change()
    setû����ȣisbn
End Sub

Private Sub TextBox2_Change()
    setû����ȣisbn
End Sub

Private Sub TextBox3_Change()
    setû����ȣisbn
End Sub

Private Sub TextBox4_Change()
    setû����ȣisbn
End Sub

Private Sub TreeView1_BeforeLabelEdit(Cancel As Integer)

End Sub

Private Sub TreeView1_NodeClick(ByVal Node As MSComctlLib.Node)
    If Not Node.Parent Is Nothing Then
        ����з���ȣ = Split(Node.Key)(1)
        setû����ȣisbn
    End If
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹���\����\�⺻ȭ��.jpg")
    
    SpinButton1.Min = 1
    SpinButton1.Max = 5
    SpinButton1.Value = 1
    
    For Each i In Split("����,�ǿ�,û�ҳ�,�н����� 1(��.����),�н����� 2(�ʵ��л���),�Ƶ�,����", ",")
        ListBox1.AddItem i
    Next
    For Each i In Split("���� ���� �ż��� ���ົ �������Ѽ����ٱǺ����ø��� �������ǹ� ���� �׸�å.��ȭ ȥ���ڷ�,�����ڷ�,����ũ���ڷ�")
        ListBox2.AddItem i
    Next
    
    
    With TreeView1.Nodes
        For i = 0 To 9
            .Add Key:=Sheet2.Range("B" & i + 3), Text:=Sheet2.Range("B" & i + 3)
            Call addChild(i + 3, Sheet2.Range("B" & i + 3))
        Next
    End With
    
End Sub

Sub addChild(r, cont)
    
    cnt = 1
    
    For i = 2 To Sheet2.Range("B" & r).End(xlToRight).column
        k = cont & " " & Sheet2.Range("A" & r) & Sheet2.Cells(2, i) & "0"
        TreeView1.Nodes.Add Sheet2.Range("B" & r).Value, tvwChild, k, Sheet2.Cells(r, i)
        cnt = cnt + 1
    Next
    
End Sub


Function ���ڴ���ȣ()
    For i = 0 To ListBox1.ListCount - 1
        If ListBox1.Selected(i) Then
            Select Case ListBox1.List(i)
                Case "����": ���ڴ���ȣ = 0
                Case "�ǿ�": ���ڴ���ȣ = 1
                Case "û�ҳ�": ���ڴ���ȣ = 4
                Case "�н����� 1(��.����)": ���ڴ���ȣ = 5
                Case "�н����� 2(�ʵ��л���)": ���ڴ���ȣ = 6
                Case "�Ƶ�": ���ڴ���ȣ = 7
                Case "����": ���ڴ���ȣ = 9
            End Select
        End If
    Next
End Function

Function �������±�ȣ()
    For i = 0 To ListBox2.ListCount - 1
        If ListBox1.Selected(i) Then
            �������±�ȣ = i
            Exit Function
        End If
    Next
End Function

Sub setû����ȣisbn()
    If Not (TextBox1 <> "" And TextBox2 <> "" And TextBox3 <> "" And TextBox4 <> "" And ListBox1.ListIndex <> -1 And ListBox2.ListIndex <> -1 And ����з���ȣ <> "") Then Exit Sub
    
    Call mkISBN
    Label14 = fnCallNM(TextBox1, TextBox2, Label15)
    
End Sub

Sub mkISBN()
    
    Randomize
    
    idx1 = Int(Rnd * 2)
    
    ���κ� = Split("978 979")(idx1)
    ������ȣ = Split("89 11")(idx1)
    
    �����ڹ�ȣ = Int(Rnd * 900000) + 100000
    ����ĺ���ȣ = Int(Rnd * 10)
    
    isbn = ���κ� & "-" & ������ȣ & "-" & �����ڹ�ȣ & "-" & ����ĺ���ȣ
    üũ��ȣ = ���κ� & ������ȣ & �����ڹ�ȣ & ����ĺ���ȣ
    
    s = 0
    Debug.Print üũ��ȣ
    For i = 1 To Len(üũ��ȣ)
        v = Val(Mid(üũ��ȣ, i, 1))
        s = s + IIf(v Mod 2 = 0, v, v * 3)
    Next
    
    üũ��ȣ = IIf((s Mod 10) = 0, 0, 10 - (s Mod 10))
    �ΰ���ȣ = ���ڴ���ȣ & �������±�ȣ & ����з���ȣ
    
    Label15 = ���κ� & "-" & ������ȣ & "-" & �����ڹ�ȣ & "-" & ����ĺ���ȣ & "-" & üũ��ȣ & "[" & �ΰ���ȣ & "]"
    
End Sub
