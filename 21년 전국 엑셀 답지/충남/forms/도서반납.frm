VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 도서반납 
   Caption         =   "UserForm1"
   ClientHeight    =   7845
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7575
   OleObjectBlob   =   "도서반납.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "도서반납"
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
    If c = ListView1.ListItems.Count Then eMsg "반납할 도서가 선택되지 않았습니다.": Exit Sub
    
    iMsg ListView1.ListItems.Count - c & "건이 반납되었습니다."
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub UserForm_Activate()
    Do While isOpen
        TextBox1 = Format(Now, "yyyy년 mm월 dd일 ") & "(" & WeekdayName(Weekday(Date)) & ") " & Format(Now, "hh:mm:ss")
        DoEvents
    Loop
End Sub

Private Sub UserForm_Initialize()
    자료처리초기화 Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    mkdic dic책, Sheet3.[a2]
    
    With ListView1
        
        .View = lvwReport
        .Gridlines = True
        
        With .ColumnHeaders
            .Add , , ""
            .Add , , "도서명"
            .Add , , "반납예정일"
            
            
            .item(1).Width = 30
            .item(2).Width = 230
            .item(3).Width = 60
            
        End With
        .ColumnHeaders(1).Position = 3
    End With
    
    ImageList1.ImageHeight = 150
    ImageList1.ImageWidth = 100
    
    With Sheet11
        mkdic dic자료처리, .Range("E" & .[i100000].End(3).row + 1)
        For Each k In dic자료처리.Keys
            If dic자료처리(k).Cells(1, 2) = .[회원아이디] Then
                ImageList1.ListImages.Add , "img/" & k, LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\도서\" & dic책(k).Cells(1, 2) & ".jpg")
            End If
        Next
    End With
    
    ListView1.SmallIcons = ImageList1

    For i = 1 To ImageList1.ListImages.Count
        ikey = Mid(ImageList1.ListImages(i).Key, 5, Len(ImageList1.ListImages(i).Key))
        Set li = ListView1.ListItems.Add(, , "")
        li.ListSubItems.Add , , dic책(ikey).Cells(1, 2), ImageList1.ListImages(i).Key
        li.ListSubItems.Add , , IIf(CDate("2021-10-05") <= dic자료처리(ikey).Cells(1, 4), dic자료처리(ikey).Cells(1, 4), DateDiff("d", dic자료처리(ikey).Cells(1, 4), CDate("2021-10-05")) & "일 연체")
        
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
