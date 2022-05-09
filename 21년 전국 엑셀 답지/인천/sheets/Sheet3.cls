VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet3"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Dim 작품분류 As Object
Dim 작품사전 As Object

Private Sub CommandButton1_Click()
    Sheet1.Select
End Sub
Private Sub Worksheet_Activate()
    Application.ScreenUpdating = False

    Shapes.SelectAll
    Selection.Delete
    setPicture
    
    Application.ScreenUpdating = True
    
    
    Set 작품분류 = CreateObject("Scripting.Dictionary")
    
    For i = 2 To Sheet3.Range("A1000").End(xlUp).Row
        If Not 작품분류.exists(Sheet3.Range("B" & i).Value) Then 작품분류.Add Sheet3.Range("B" & i).Value, ""
    Next
    
    
    Set 작품사전 = CreateObject("Scripting.Dictionary")
    
    For Each k In 작품분류.keys
        For i = 2 To Sheet3.Range("A1000").End(xlUp).Row
            If Sheet3.Range("B" & i).Value = k Then
                If max < Sheet3.Range("H" & i).Value Then max = Sheet3.Range("H" & i).Value
            End If
        Next
        작품사전.Add k, max
        max = 0
    Next
    
    
    idx = 3
    
    For Each k In 작품사전.keys
        Sheet3.Range("O" & idx) = 작품사전(k)
        Sheet3.Range("N" & idx) = k
        idx = idx + 1
    Next
    
    
End Sub

Sub setPicture()
        For i = 2 To Range("A10000").End(3).Row
            Range("G" & i + 2).RowHeight = 80
            With ActiveSheet.Pictures.Insert(ThisWorkbook.Path & "\지급자료\이미지\" & Range("B" & i) & "\" & Range("A" & i) & ".jpg").ShapeRange
                .LockAspectRatio = msoFalse
                .height = 80
                .width = 130
                .top = Sheet3.Range("G" & i).top
                .left = Sheet3.Range("G" & i).left
                .name = "그림 " & i - 1
            End With
        Next
End Sub

Private Sub Worksheet_BeforeRightClick(ByVal Target As Range, Cancel As Boolean)
    For Each i In Application.CommandBars("Cell").Controls
        i.Delete
    Next
    
    If Cells(Target.Row, Target.Column) <> "" Then
        If Cells(Target.Row, 4) = [이름] Then
            If Target.Column < 11 Then
                With Application.CommandBars("Cell").Controls.Add
                    .FaceId = 548
                    irow = Target.Row
                    .Caption = "작품 수정"
                    .OnAction = "수정"
                End With
                
                With Application.CommandBars("Cell").Controls.Add
                    .FaceId = 358
                    irow = Target.Row
                    .Caption = "작품 삭제"
                    .OnAction = "삭제"
                End With
            End If
        Else
            Application.CommandBars("Cell").Reset
        End If
    Else
        Application.CommandBars("Cell").Reset
    End If
End Sub
