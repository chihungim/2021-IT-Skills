VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet2"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_Activate()

    Application.CalculateFullRebuild
    
    For i = 2 To Sheet2.Range("A1000").End(xlUp).Row
        On Error Resume Next
        Sheet2.Range("B" & i).Comment.Delete
    Next
    
    For i = 2 To Sheet2.Range("A10000").End(xlUp).Row
        If Dir(ThisWorkbook.Path & "\지급자료\회원사진\" & Range("A" & i) & ".jpg") <> "" Then
            With Sheet2.Range("B" & i).AddComment
                
                .Shape.Fill.UserPicture ThisWorkbook.Path & "\지급자료\회원사진\" & Range("A" & i) & ".jpg"
            End With
        End If
    Next
End Sub

