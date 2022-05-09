VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 도서검색 
   Caption         =   "도서검색"
   ClientHeight    =   6165
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8070
   OleObjectBlob   =   "도서검색.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "도서검색"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    
    ListView1.ListItems.Clear
    
    For i = 2 To Sheet4.Range("A10000").End(xlUp).row
        If ComboBox1 = "전체" Then
            If [등급].Value = "준회원" Then
                If Sheet4.Range("D" & i) = "준회원" Then
                    ListView1.ListItems.Add , , Sheet4.Range("E" & i)
                End If
            Else
                ListView1.ListItems.Add , , Sheet4.Range("E" & i)
            End If
        Else
            If Sheet4.Range("C" & i) = ComboBox1 Then
                If [등급].Value = "준회원" Then
                    If Sheet4.Range("D" & i) = "준회원" Then
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
    If TextBox1 = "" Then eMsg "도서를 선택하세요": Exit Sub
    도서명 = TextBox1
    Unload Me
    도구.구매모드 = True
    수량선택.Show
End Sub

Private Sub CommandButton2_Click()
    If TextBox1 = "" Then eMsg "도서를 선택하세요": Exit Sub
    도서명 = TextBox1
    Unload Me
    도구.구매모드 = False
    수량선택.Show
End Sub

Private Sub ListView1_ItemClick(ByVal Item As MSComctlLib.ListItem)
    v = Item
    Dim 도서row As Range
    Set 도서row = dic도서(v)
    With 도서row
        TextBox1 = v
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\책이미지\" & .Cells(1, -2) & ".jpg")
        TextBox2 = .Cells(1, 2)
        TextBox3 = .Cells(1, 3)
        TextBox4 = .Cells(1, 4)
        TextBox5 = .Cells(1, 5)
    End With
    
End Sub

Private Sub UserForm_Initialize()
    Call mkDic(dic도서, Sheet4.Range("C2"))
    
    ComboBox1.AddItem "전체"
    
    For Each k In dic도서.Keys
        ComboBox1.AddItem k
    Next
    
    With ListView1
        .Gridlines = True
        .View = lvwReport
        .MultiSelect = False
        

        With .ColumnHeaders
            .Add , , "도서명", 180
        End With
    End With
    
    
    ComboBox1.ListIndex = 0
    
    mkDic dic도서, Sheet4.Range("E2")
End Sub
