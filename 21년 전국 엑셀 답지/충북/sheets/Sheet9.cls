VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet9"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_Activate()
    Application.ScreenUpdating = False
    For i = 0 To 2
        Shapes("그림 " & i).Fill.Visible = msoFalse
        Cells(10, 9 + i) = vbNullString
        For k = 13 To 23 Step 2
            Cells(k, 9 + i) = vbNullString
        Next
    Next
    
    For i = 0 To 비교카드.Count - 1
        With Shapes("그림 " & i).Fill
            .Visible = msoTrue
            .UserPicture (ThisWorkbook.Path & "\지급자료\이미지\" & 비교카드(i) & ".jpg")
        End With
        
        With Sheet2
            Cells(10, 9 + i) = .Range("B" & 비교카드(i) + 1)
            Cells(13, 9 + i) = .Range("C" & 비교카드(i) + 1)
            Cells(15, 9 + i) = .Range("D" & 비교카드(i) + 1)
            Cells(17, 9 + i) = Join(Split(li(.Range("E" & 비교카드(i) + 1), "혜택"), ","), ", ")
            Cells(19, 9 + i) = Join(Split(li(.Range("G" & 비교카드(i) + 1), "해외"), ","), ", ")
            Cells(21, 9 + i) = Format(.Range("F" & 비교카드(i) + 1), "#.#0%")
            Cells(23, 9 + i) = Format(.Range("I" & 비교카드(i) + 1), "#,##0원")
        End With
    Next
    Application.ScreenUpdating = True
End Sub

