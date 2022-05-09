VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 리뷰 
   Caption         =   "UserForm1"
   ClientHeight    =   6645
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8115
   OleObjectBlob   =   "리뷰.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "리뷰"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim arr As Variant
Dim spoint(3)
Dim blag(3)

Private Sub ComboBox1_Change()
    ComboBox2.Clear
    If ComboBox1 = "패키지" Then
        For Each k In dic패키지
            ComboBox2.AddItem dic패키지(k).Cells(1, 2)
        Next
    ElseIf ComboBox1 = "관광지" Then
        For Each k In dic관광지
            ComboBox2.AddItem dic관광지(k).Cells(1, 3)
        Next
    Else
        For Each k In dic축제
            ComboBox2.AddItem dic축제(k).Cells(1, 3)
        Next
    End If
End Sub

Private Sub CommandButton1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .AllowMultiSelect = False
        .Filters.Clear
        
        If .Show <> 0 Then pt = .SelectedItems(1)
        Image1.Picture = LoadPicture(pt)
        Repaint
    End With
End Sub

Private Sub CommandButton2_Click()
    If Not Image1.Picture Is Nothing Then
        Image1.Picture = Nothing
    End If
End Sub

Private Sub CommandButton3_Click()
    If Me.Caption = "리뷰 등록" Then
        If ComboBox1 = "" Or ComboBox2 = "" Or TextBox1 = "" Then eMsg "모든 항목을 작성해주세요.": Exit Sub
        If OptionButton1.Value = False And OptionButton2.Value = False Then eMsg "공개 여부를 확인해주세요.": Exit Sub
        
        iMsg "등록 완료되었습니다."
        cate = Array("P", "G", "C")
        
        With Sheet8
            lrow = .Range("A1000").End(3).row + 1
            .Range("A" & lrow & ":I" & lrow) = Array(lrow - 1, [번호], cate(ComboBox1.ListIndex) & ComboBox2.ListIndex + 1, Sheet12.Range("D6"), Sheet12.Range("I6"), Sheet12.Range("M6"), TextBox1, IIf(Image1.Picture Is Nothing, "X", "O"), IIf(OptionButton1.Value, "O", "X"))
            Unload Me
        End With
        
    Else
        
    End If
End Sub

Private Sub CommandButton4_Click()
    Unload Me
End Sub

Private Sub star1_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    blag(0) = Not blag(0)
    Sheet12.Range("D6") = spoint(0)
    Call starInit("차트 9", "그림 7", "star1")
    Repaint
End Sub

Private Sub star1_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    If Not blag(0) Then
        spoint(0) = arr(Int(X / 24)) + IIf(X / 24 - arr(Int(X / 24)) + 1 > 0.5, 0.5, 0) - 1
        Sheet12.Range("D6") = arr(Int(X / 24)) + X / 24 - arr(Int(X / 24))
        Call starInit("차트 9", "그림 7", "star1")
        Repaint
    End If
    
End Sub

Private Sub star2_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    blag(1) = Not blag(1)
    Sheet12.Range("I6") = spoint(1)
    Call starInit("차트 22", "그림 21", "star2")
    Repaint
End Sub

Private Sub star2_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    If Not blag(1) Then
        spoint(1) = arr(Int(X / 24)) + IIf(X / 24 - arr(Int(X / 24)) + 1 > 0.5, 0.5, 0) - 1
        Sheet12.Range("I6") = arr(Int(X / 24)) + X / 24 - arr(Int(X / 24))
        Call starInit("차트 22", "그림 21", "star2")
        Repaint
    End If
    
End Sub

Private Sub star3_MouseDown(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    blag(2) = Not blag(2)
    Sheet12.Range("M6") = spoint(2)
    Call starInit("차트 27", "그림 26", "star3")
    Repaint
End Sub

Private Sub star3_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    If Not blag(2) Then
        spoint(2) = arr(Int(X / 24)) + IIf(X / 24 - arr(Int(X / 24)) + 1 > 0.5, 0.5, 0) - 1
        Sheet12.Range("M6") = arr(Int(X / 24)) + X / 24 - arr(Int(X / 24))
        Call starInit("차트 27", "그림 26", "star3")
        Repaint
    End If
    
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    arr = Array(1, 2, 3, 4, 5)
    mkdic dic관광지, Sheet3.Range("A2")
    mkdic dic패키지, Sheet6.Range("A2")
    mkdic dic축제, Sheet5.Range("A2")
    For i = 0 To 2
        blag(i) = False
    Next
    
    ComboBox1.List = Array("패키지", "관광지", "축제")
    
    
    If rType = "등록" Then
        With Sheet12
            .Range("D6") = 5: .Range("I6") = 5: .Range("M6") = 5
        End With
        Me.Caption = "리뷰 등록"
    Else
        
        
    End If
    
    
    
    
    
    Call starInit("차트 9", "그림 7", "star1")
    Call starInit("차트 22", "그림 21", "star2")
    Call starInit("차트 27", "그림 26", "star3")
    
End Sub

Sub starInit(chtName, picName, star)
    
    
    With Sheet12
        
        For Each shp In .Shapes(chtName).Chart.Shapes
            shp.Delete
        Next
    
        .Shapes(picName).Copy
        .Shapes(chtName).Chart.Paste
    
        .Shapes(chtName).Chart.Export ThisWorkbook.Path & "\shp.jpg", "jpg"
        
        Me.Controls(star).Picture = LoadPicture(ThisWorkbook.Path & "\shp.jpg")
        
        Kill ThisWorkbook.Path & "\shp.jpg"
    End With
    
    
End Sub
