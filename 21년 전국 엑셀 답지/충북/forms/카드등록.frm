VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ī���� 
   Caption         =   "ī����"
   ClientHeight    =   10665
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   14805
   OleObjectBlob   =   "ī����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ī����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim �������� As New ArrayList

Private Sub CommandButton1_Click()
    If ComboBox2.ListIndex = -1 Then eMsg "ī�װ��� �ϳ� �̻� �����ؾ� �մϴ�.": Exit Sub
     
    If TextBox5 = "" Or TextBox6 = "" Or TextBox7 = "" Then eMsg "��ĭ�� ��� �Է��ؾ� �մϴ�.": Exit Sub
    
    If Not Right(TextBox6, 1) = "%" Then eMsg "������ �ۼ�Ʈ ������ �Է��ؾ� �մϴ�.": Exit Sub
    
    If Not IsNumeric(TextBox7) Then eMsg "���������� ���ڷ� �Է��ؾ� �մϴ�.": Exit Sub
    
    Set l = ListView1.ListItems.Add(, , ListView1.ListItems.Count + 1)
    l.ListSubItems.Add , , ComboBox2
    l.ListSubItems.Add , , TextBox6
    l.ListSubItems.Add , , TextBox5
    ��������.Add Format(TextBox7, "#,##0")
    
    For i = 5 To 7
        Me.Controls("TextBox" & i) = ""
    Next
    
    ComboBox2.ListIndex = -1
    
End Sub

Private Sub CommandButton2_Click()
    If TextBox1 = "" Or TextBox2 = "" Or TextBox3 = "" Or TextBox4 = "" Then eMsg "ī�� ������ ��� �Է��ؾ� �մϴ�.": Exit Sub
    
    If Not (CheckBox1 Or CheckBox2 Or CheckBox3 Or CheckBox4 Or CheckBox5 Or CheckBox6) Then eMsg "ī�� ������ ��� �Է��ؾ� �մϴ�.": Exit Sub
    
    If ComboBox1.ListIndex = -1 Then eMsg "ī�� ������ ��� �Է��ؾ� �մϴ�.": Exit Sub
    
    If Image1.Picture Is Nothing Then eMsg "ī�� ������ ����ؾ� �մϴ�.": Exit Sub
    
    If Not Right(TextBox3, 1) = "%" Then eMsg "�Һ����ڴ� �ۼ�Ʈ ������ �Է��ؾ� �մϴ�.": Exit Sub
    
    If Not IsNumeric(TextBox4) Then eMsg "��ȸ��� ���ڷ� �Է��ؾ� �մϴ�.": Exit Sub
    
    If Not (CheckBox7 Or CheckBox8 Or CheckBox9 Or CheckBox10) Then eMsg "���� �귣�带 1�� �̻� �����ؾ� �մϴ�.": Exit Sub
    
    If ListView1.ListItems.Count = 0 Then eMsg "ī�� ������ 1�� �̻� ����ؾ� �մϴ�.": Exit Sub
    
    Set dic = mkdic(Sheet2.Range("B2"))
    
    If dic.exists(TextBox1.Value) Then eMsg "�ߺ��Ǵ� ī�� �̸��� �����մϴ�.": exits ub
    
    
    Set list = New ArrayList
    
    With Sheet3
        For i = 7 To 10
            If Me.Controls("CheckBox" & i) Then
                If ī�װ� = "" Then
                    ī�װ� = Me.Controls("CheckBox" & i).Caption
                Else
                    ī�װ� = ī�װ� & "," & Me.Controls("Checkbox" & i).Caption
                End If
            End If
        Next
        
        For i = 1 To ListView1.ListItems.Count
            lrow = .Range("A1000").End(3).Row + 1
            list.Add lrow
            .Range("A" & lrow & ":F" & lrow) = Array(lrow - 1, ListView1.ListItems(i).SubItems(1), ListView1.ListItems(i).SubItems(3), ī�װ�, ListView1.ListItems(i).SubItems(2), ��������(i - 1))
        Next
    End With
    
    With Sheet2
        For i = 1 To 6
            If Me.Controls("CheckBox" & i) Then
                If �ؿ� = "" Then
                    �ؿ� = i
                Else
                    �ؿ� = �ؿ� & "," & i
                End If
            End If
        Next
    
        Set Ÿ�� = New ArrayList
        For i = 1 To ListView1.ListItems.Count
            If Not .Ÿ��.Contains(ListView1.ListItems(i).SubItems(1)) Then
                Ÿ��.Add ListView1.ListItems(i).SubItems(1)
            Next
        Next
        
        iMsg "���ο� ī�带 ����߽��ϴ�."
        
        lrow = .Range("A1000").End(xlUp).Row + 1
        
        .Range("A" & lrow & ":I" & lrow) = Array(lrow - 1, TextBox1, TextBox2, Join(Ÿ��.ToArray, ","), Join(list.ToArray, ","), Format(TextBox3, "#.#0%"), �ؿ�, Val(ComboBox1), Format(TextBox4, "#,##0"))
        SavePicture Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\�̹���\" & lrow - 1 & ".jpg"
    End With
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .AllowMultiSelect = False
        .Filters.Clear
        .Filters.Add "JPG", "*.JPG"
        
        If .Show <> 0 Then pt = .SelectedItems(1)
        
        Image1.Picture = LoadPicture(pt)
        Repaint
    End With
End Sub

Private Sub UserForm_Initialize()
    With ListView1
        .MultiSelect = False
        .FullRowSelect = False
        .View = lvwReport
        With .ColumnHeaders
            .Add , , "��ȣ", 50
            .Add , , "�׸�", 50
            .Add , , "����", 50
            .Add , , "����", 200
        End With
    End With
    
    For i = 1 To 5
        ComboBox1.AddItem i & "���"
    Next
    
    ComboBox2.list = Array("����", "����")
End Sub
