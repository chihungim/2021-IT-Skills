VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���Ÿ�� 
   Caption         =   "���ų���"
   ClientHeight    =   6240
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8145
   OleObjectBlob   =   "���Ÿ��.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "���Ÿ��"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim v As Integer

Private Sub ComboBox1_Change()
    Call �˻�
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton1_Click()
    If v = 0 Then Exit Sub
    
    With Sheet5
        Set rg = .Range("A:A").Find(What:=v, LookAt:=xlWhole)
        If rg(1, 4) = Date Then
            iMsg "��ǰ�� �Ϸ�Ǿ����ϴ�."
            .ListObjects("ǥ3").Range.AutoFilter Field:=2
            Sheet5.Rows(rg.row).Delete
            Call �˻�
            .ListObjects("ǥ3").Range.AutoFilter Field:=2, Criteria1:=[���̵�]
        Else
            eMsg "���� ������ ������ ��ǰ �����մϴ�.": Exit Sub
        End If
    End With
    
End Sub

Private Sub ListView1_BeforeLabelEdit(Cancel As Integer)

End Sub


Private Sub ListView1_Click()
    v = ListView1.SelectedItem.Index
End Sub

Private Sub ListView1_DblClick()
    �����Է�.lrow = ListView1.ListItems(ListView1.SelectedItem.Index) + 1
    �����Է�.Show

End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    Label2 = [�̸�]
    ComboBox1.AddItem "��ü"
    
    For i = 1 To Month(Date)
        ComboBox1.AddItem i & "��"
    Next
    
    With ListView1
        .Gridlines = True
        .FullRowSelect = True
        .View = lvwReport
        
        With .ColumnHeaders
            .Add , , "��ȣ", 40
            .Add , , "������", 130
            .Add , , "��������", 80
            .Add , , "����", 40
            .Add , , "�հ�", 80
        End With
    End With
    
    
    ComboBox1.ListIndex = 0
    
End Sub

Sub �˻�()
    ListView1.ListItems.Clear
    
    �� = IIf(ComboBox1.ListIndex = 0, "**", "*" & Val(ComboBox1) & "*")
    cnt = 0: m = 0
    
    Call mkDic(dic����, Sheet4.Range("B2"))
    
    With Sheet5
        For i = 2 To .Range("A100000").End(3).row
            If .Range("B" & i) = [���̵�] And Month(.Range("D" & i)) Like �� Then
                Set li = ListView1.ListItems.Add(, , .Range("A" & i))
                li.SubItems(1) = dic����(.Range("C" & i).Value).Cells(1, 4)
                li.SubItems(2) = .Range("D" & i)
                li.SubItems(3) = .Range("E" & i)
                li.SubItems(4) = Format(dic����(.Range("C" & i).Value).Cells(1, 7) * .Range("E" & i), "#,##0")
                cnt = cnt + 1
                m = m + dic����(.Range("C" & i).Value).Cells(1, 7) * .Range("E" & i)
            End If
        Next
    End With
    
    Label5 = cnt & "��"
    Label7 = Format(m, "#,##0")
    
End Sub

