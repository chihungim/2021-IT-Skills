VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ��ٱ��� 
   Caption         =   "��ٱ���"
   ClientHeight    =   5160
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5820
   OleObjectBlob   =   "��ٱ���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��ٱ���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    mkDic dic��ٱ���, Sheet6.Range("A2")
    
    For i = 1 To ListView1.ListItems.Count
        If ListView1.ListItems(i).Selected Then
            �� = Val(ListView1.ListItems(i))
            dic��ٱ���(��).Cells(1, 1).EntireRow.Delete xlShiftUp
        End If
    Next
    
    Call reload
End Sub

Private Sub CommandButton2_Click()
    
    mkDic dic��ٱ���, Sheet6.Range("A2")
    Set arr���� = New ArrayList
    For i = 1 To ListView1.ListItems.Count
        If ListView1.ListItems(i).Checked Then
            �� = Val(ListView1.ListItems(i))
            �����ڵ� = ListView1.ListItems(i).SubItems(1)
            �� = dic����(�����ڵ�).Cells(1, 4)
            �ǸŰ� = dic��ٱ���(��).Cells(1, 5) / dic��ٱ���(��).Cells(1, 4)
            ���� = dic��ٱ���(��).Cells(1, 4)
            ���� = dic��ٱ���(��).Cells(1, 5)
            arr����.Add Array(�����ڵ�, ��, �ǸŰ�, ����, ����)
            MsgBox i
        End If
    Next
    
    Unload Me
    
    ����.Show
End Sub

Private Sub UserForm_Initialize()
    mkDic dic����, Sheet4.Range("B2")
    With ListView1
        .Gridlines = True
        .View = lvwReport
        With .ColumnHeaders
            .Add , , "��ȣ", 30
            .Add , , "�����ڵ�", 50
            .Add , , "������", 100
            .Add , , "����", 30
        End With
    End With
    Call reload
End Sub

Sub reload()
    ListView1.ListItems.Clear

    With Sheet6
        For i = 2 To .Range("A1000").End(xlUp).row
            If Not .Cells(i, "A").Rows.Hidden Then
                
                Dim lrow As ListItem
                Set lrow = ListView1.ListItems.Add(, , .Cells(i, 1))
                lrow.SubItems(1) = .Cells(i, 3)

                lrow.SubItems(2) = dic����(.Cells(i, 3).Value).Cells(i, 4)
                lrow.SubItems(3) = .Cells(i, 4)
            End If
        Next
    End With
End Sub
