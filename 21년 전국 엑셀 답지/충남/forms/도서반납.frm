VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �����ݳ� 
   Caption         =   "UserForm1"
   ClientHeight    =   7845
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7575
   OleObjectBlob   =   "�����ݳ�.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�����ݳ�"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim isOpen As Boolean

Private Sub CommandButton1_Click()
    c = 0
    For i = 1 To ListView1.ListItems.Count
        If ListView1.ListItems(i).Checked = False Then c = c + 1
    Next
    If c = ListView1.ListItems.Count Then eMsg "�ݳ��� ������ ���õ��� �ʾҽ��ϴ�.": Exit Sub
    
    iMsg ListView1.ListItems.Count - c & "���� �ݳ��Ǿ����ϴ�."
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub UserForm_Activate()
    Do While isOpen
        TextBox1 = Format(Now, "yyyy�� mm�� dd�� ") & "(" & WeekdayName(Weekday(Date)) & ") " & Format(Now, "hh:mm:ss")
        DoEvents
    Loop
End Sub

Private Sub UserForm_Initialize()
    �ڷ�ó���ʱ�ȭ Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    mkdic dicå, Sheet3.[a2]
    
    With ListView1
        
        .View = lvwReport
        .Gridlines = True
        
        With .ColumnHeaders
            .Add , , ""
            .Add , , "������"
            .Add , , "�ݳ�������"
            
            
            .item(1).Width = 30
            .item(2).Width = 230
            .item(3).Width = 60
            
        End With
        .ColumnHeaders(1).Position = 3
    End With
    
    ImageList1.ImageHeight = 150
    ImageList1.ImageWidth = 100
    
    With Sheet11
        mkdic dic�ڷ�ó��, .Range("E" & .[i100000].End(3).row + 1)
        For Each k In dic�ڷ�ó��.Keys
            If dic�ڷ�ó��(k).Cells(1, 2) = .[ȸ�����̵�] Then
                ImageList1.ListImages.Add , "img/" & k, LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹���\����\" & dicå(k).Cells(1, 2) & ".jpg")
            End If
        Next
    End With
    
    ListView1.SmallIcons = ImageList1

    For i = 1 To ImageList1.ListImages.Count
        ikey = Mid(ImageList1.ListImages(i).Key, 5, Len(ImageList1.ListImages(i).Key))
        Set li = ListView1.ListItems.Add(, , "")
        li.ListSubItems.Add , , dicå(ikey).Cells(1, 2), ImageList1.ListImages(i).Key
        li.ListSubItems.Add , , IIf(CDate("2021-10-05") <= dic�ڷ�ó��(ikey).Cells(1, 4), dic�ڷ�ó��(ikey).Cells(1, 4), DateDiff("d", dic�ڷ�ó��(ikey).Cells(1, 4), CDate("2021-10-05")) & "�� ��ü")
        
        If CDate(li.SubItems(2)) = CDate("2021-10-05") Then
            li.ListSubItems(1).ForeColor = vbRed
            li.ListSubItems(2).ForeColor = vbRed
        End If
    Next
    
    
    
    isOpen = True
End Sub

Private Sub UserForm_QueryClose(Cancel As Integer, CloseMode As Integer)
    isOpen = False
End Sub
