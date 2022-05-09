VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 접수내역 
   Caption         =   "접수내역"
   ClientHeight    =   7560
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8460
   OleObjectBlob   =   "접수내역.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "접수내역"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    With ListView1
        .Gridlines = True
        .View = lvwReport
        .MultiSelect = False
        .FullRowSelect = True
        
        With .ColumnHeaders
            .Add , , "종목", 100
            .Add , , "시험일자", 80
            .Add , , "시험시간", 60
            .Add , , "접수일자", 80
            .Add , , "응시료", 60
        End With
        
    End With
    
    mkdic dic시험, Sheet4.Range("A2")
    
    With Sheet5
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("B" & i) = [번호] Then
                Set li = ListView1.ListItems.Add(, , .Range("I" & i))
                li.SubItems(1) = .Range("E" & i)
                li.SubItems(2) = Format(dic시험(.Range("C" & i).Value).Cells(1, 6), "h:mm")
                li.SubItems(3) = .Range("D" & i)
                li.SubItems(4) = Sheet3.Range("H" & dic시험(.Range("C" & i).Value).Cells(1, 2) + 1)
                
            End If
        Next
    End With
    
End Sub
