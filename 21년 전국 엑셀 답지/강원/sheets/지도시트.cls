VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet11"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_Activate()

    Dim 이름 As Object
    mkValuelist 이름, Sheet13.Range("A2")
    
    이름.Sort
    
    ListBox1.Clear
    ListBox2.Clear
    For Each i In 이름
        ListBox1.AddItem i
        Set img = Shapes(i)
        With img
            t = .Top: l = .Left: w = .Width: h = .Height
            .Delete
        End With
        
        Set img = Shapes.AddPicture(ThisWorkbook.Path & "\지급자료\pick_red.png", msoFalse, msoTrue, l, t, w, h)
        img.Name = i
        
    Next
    
    Shapes("shp거리").TextFrame.Characters.Text = ""
    Shapes("shp총거리").TextFrame.Characters.Text = ""
    Shapes("shp가격").TextFrame.Characters.Text = ""
    Shapes("shp총가격").TextFrame.Characters.Text = ""
    
    For Each shp In Shapes
        If shp.Name Like "*Line*" Or shp.Name Like "*Distance*" Or shp.Name Like "*Price*" Then
            shp.Delete
        End If
    Next
    

End Sub



Sub setNama()
    
    For Each shp In Shapes
        If Right(shp.Name, 1) = "t" Then
            Dim s As Shape
            Set s = shp
            s.Name = "txt" & s.TextFrame.Characters.Text
        End If
    Next
    
End Sub

Sub changeImg()
    
    Shapes("철원군").Fill.UserPicture ThisWorkbook.Path & "\지급자료\pick_green.png"
    
End Sub
