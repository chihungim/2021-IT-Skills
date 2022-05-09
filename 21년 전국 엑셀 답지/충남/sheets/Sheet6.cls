VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet6"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_Activate()
    
    
    
    
End Sub

Sub shp이전_Click()
    If [T13] = 1 Then Exit Sub
    [T13] = [T13] - 1
    Call 대출인기도서
End Sub

Sub shp다음_Click()
    If [T13] = 8 Then Exit Sub
    [T13] = [T13] + 1
    Call 대출인기도서
End Sub

Sub 대출인기도서()
    idx = [T13]
    
    k = 1
    For i = idx To idx + 2
        Shapes("책" & k).Fill.UserPicture ThisWorkbook.Path & "\지급자료\이미지\도서\" & Range("U" & 13 + i) & ".jpg"
        k = k + 1
    Next
End Sub

Private Sub Worksheet_SelectionChange(ByVal Target As Range)

End Sub
