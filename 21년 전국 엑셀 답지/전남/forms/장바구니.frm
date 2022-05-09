VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 장바구니 
   Caption         =   "장바구니"
   ClientHeight    =   5160
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5820
   OleObjectBlob   =   "장바구니.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "장바구니"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    mkDic dic장바구니, Sheet6.Range("A2")
    
    For i = 1 To ListView1.ListItems.Count
        If ListView1.ListItems(i).Selected Then
            열 = Val(ListView1.ListItems(i))
            dic장바구니(열).Cells(1, 1).EntireRow.Delete xlShiftUp
        End If
    Next
    
    Call reload
End Sub

Private Sub CommandButton2_Click()
    
    mkDic dic장바구니, Sheet6.Range("A2")
    Set arr구매 = New ArrayList
    For i = 1 To ListView1.ListItems.Count
        If ListView1.ListItems(i).Checked Then
            열 = Val(ListView1.ListItems(i))
            도서코드 = ListView1.ListItems(i).SubItems(1)
            명 = dic도서(도서코드).Cells(1, 4)
            판매가 = dic장바구니(열).Cells(1, 5) / dic장바구니(열).Cells(1, 4)
            수량 = dic장바구니(열).Cells(1, 4)
            가격 = dic장바구니(열).Cells(1, 5)
            arr구매.Add Array(도서코드, 명, 판매가, 수량, 가격)
            MsgBox i
        End If
    Next
    
    Unload Me
    
    결제.Show
End Sub

Private Sub UserForm_Initialize()
    mkDic dic도서, Sheet4.Range("B2")
    With ListView1
        .Gridlines = True
        .View = lvwReport
        With .ColumnHeaders
            .Add , , "번호", 30
            .Add , , "도서코드", 50
            .Add , , "도서명", 100
            .Add , , "수량", 30
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

                lrow.SubItems(2) = dic도서(.Cells(i, 3).Value).Cells(i, 4)
                lrow.SubItems(3) = .Cells(i, 4)
            End If
        Next
    End With
End Sub
