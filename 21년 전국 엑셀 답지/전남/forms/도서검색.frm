VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �����˻� 
   Caption         =   "�����˻�"
   ClientHeight    =   6165
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8070
   OleObjectBlob   =   "�����˻�.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�����˻�"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    
    ListView1.ListItems.Clear
    
    For i = 2 To Sheet4.Range("A10000").End(xlUp).row
        If ComboBox1 = "��ü" Then
            If [���].Value = "��ȸ��" Then
                If Sheet4.Range("D" & i) = "��ȸ��" Then
                    ListView1.ListItems.Add , , Sheet4.Range("E" & i)
                End If
            Else
                ListView1.ListItems.Add , , Sheet4.Range("E" & i)
            End If
        Else
            If Sheet4.Range("C" & i) = ComboBox1 Then
                If [���].Value = "��ȸ��" Then
                    If Sheet4.Range("D" & i) = "��ȸ��" Then
                        ListView1.ListItems.Add , , Sheet4.Range("E" & i)
                    End If
                Else
                    ListView1.ListItems.Add , , Sheet4.Range("E" & i)
                End If
            End If
        End If
    Next
End Sub

Private Sub CommandButton1_Click()
    If TextBox1 = "" Then eMsg "������ �����ϼ���": Exit Sub
    ������ = TextBox1
    Unload Me
    ����.���Ÿ�� = True
    ��������.Show
End Sub

Private Sub CommandButton2_Click()
    If TextBox1 = "" Then eMsg "������ �����ϼ���": Exit Sub
    ������ = TextBox1
    Unload Me
    ����.���Ÿ�� = False
    ��������.Show
End Sub

Private Sub ListView1_ItemClick(ByVal Item As MSComctlLib.ListItem)
    v = Item
    Dim ����row As Range
    Set ����row = dic����(v)
    With ����row
        TextBox1 = v
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\å�̹���\" & .Cells(1, -2) & ".jpg")
        TextBox2 = .Cells(1, 2)
        TextBox3 = .Cells(1, 3)
        TextBox4 = .Cells(1, 4)
        TextBox5 = .Cells(1, 5)
    End With
    
End Sub

Private Sub UserForm_Initialize()
    Call mkDic(dic����, Sheet4.Range("C2"))
    
    ComboBox1.AddItem "��ü"
    
    For Each k In dic����.Keys
        ComboBox1.AddItem k
    Next
    
    With ListView1
        .Gridlines = True
        .View = lvwReport
        .MultiSelect = False
        

        With .ColumnHeaders
            .Add , , "������", 180
        End With
    End With
    
    
    ComboBox1.ListIndex = 0
    
    mkDic dic����, Sheet4.Range("E2")
End Sub
