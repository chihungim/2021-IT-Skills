VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �����ȣã�� 
   Caption         =   "�����ȣã��"
   ClientHeight    =   5355
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5505
   OleObjectBlob   =   "�����ȣã��.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�����ȣã��"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim mn

Private Sub ComboBox1_Change()
    Sheet3.Range("L2:Q100000").ClearContents
    Sheet3.Range("J2") = ComboBox1
    Sheet3.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet3.Range("J1:J2"), Sheet3.Range("L1:Q1"), False
    ListBox1.ColumnHeads = True
    ListBox1.RowSource = "�����ȣ!L2:Q" & Sheet3.Range("Q100000").End(xlUp).row
End Sub

Private Sub Frame1_Click()

End Sub

Private Sub ListBox1_Click()
    For i = 1 To ListBox1.ListCount - 1
        If ListBox1.Selected(i) = True Then mn = ListBox1.List(i): Exit For
    Next
    
    If IsEmpty(mn) Then Exit Sub
    TextBox1.Text = dic����(mn).Cells(1, 2) & " " & dic����(mn).Cells(1, 3) & " " & dic����(mn).Cells(1, 4) & " " & dic����(mn).Cells(1, 5)
End Sub

Private Sub TextBox1_Change()

End Sub

Private Sub UserForm_Initialize()
    ComboBox1.List = Array("�����", "����", "����", "������", "�߱�")
    ListBox1.MultiSelect = fmMultiSelectSingle
End Sub

Private Sub UserForm_QueryClose(Cancel As Integer, CloseMode As Integer)
    ȸ�����Լ���.TextBox5 = mn
    ȸ�����Լ���.TextBox6 = TextBox1.Text
End Sub

