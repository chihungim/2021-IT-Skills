VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 도서등록 
   Caption         =   "UserForm1"
   ClientHeight    =   7395
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9720
   OleObjectBlob   =   "도서등록.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "도서등록"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim 사진여부
Dim 내용분류기호

Private Sub CommandButton1_Click()
    For i = 1 To 4
        If Me.Controls("Textbox" & i) = "" Then eMsg "누락된 항목이 있습니다.": Exit Sub
    Next
    If Label14 = "" Or Label15 = "" Then eMsg "누락된 항목이 있습니다.": Exit Sub
    
    If 독자대상기호 = "" Or 발행형태기호 = "" Then eMsg "누락된 항목이 있습니다.": Exit Sub
    
    Call mkdic(dic책, Sheet3.Range("A2"))
    For Each k In dic책.Keys
        If dic책(k).Cells(1, 2) = TextBox1 Then eMsg "해당 도서는 이미 등록된 도서입니다.": Exit Sub
    Next
    
    With Sheet3
        lrow = .Range("A10000").End(3).row + 1
        For i = 1 To SpinButton1.Value
            .Range("B" & lrow & ":F" & lrow) = Array(TextBox1, TextBox2, TextBox3, Date, Label15)
            lrow = lrow + 1
        Next
        iMsg "도서 등록이 완료되었습니다."
        
    End With
    
    
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("image file", "*.jpg")
        If .Show <> 0 Then Image1.Picture = LoadPicture(.SelectedItems(1)): 사진여부 = True: Repaint
        
    End With
End Sub

Private Sub ListBox1_Click()
    set청구기호isbn
End Sub

Private Sub ListBox2_Click()
    set청구기호isbn
End Sub

Private Sub SpinButton1_Change()
    TextBox4 = SpinButton1
End Sub

Private Sub TextBox1_Change()
    set청구기호isbn
End Sub

Private Sub TextBox2_Change()
    set청구기호isbn
End Sub

Private Sub TextBox3_Change()
    set청구기호isbn
End Sub

Private Sub TextBox4_Change()
    set청구기호isbn
End Sub

Private Sub TreeView1_BeforeLabelEdit(Cancel As Integer)

End Sub

Private Sub TreeView1_NodeClick(ByVal Node As MSComctlLib.Node)
    If Not Node.Parent Is Nothing Then
        내용분류기호 = Split(Node.Key)(1)
        set청구기호isbn
    End If
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\도서\기본화면.jpg")
    
    SpinButton1.Min = 1
    SpinButton1.Max = 5
    SpinButton1.Value = 1
    
    For Each i In Split("교양,실용,청소년,학습참고서 1(중.고교용),학습참고서 2(초등학생용),아동,전문", ",")
        ListBox1.AddItem i
    Next
    For Each i In Split("문고본 사전 신서판 단행본 전집·총서·다권본·시리즈 전자출판물 도감 그림책.만화 혼합자료,점자자료,마이크로자료")
        ListBox2.AddItem i
    Next
    
    
    With TreeView1.Nodes
        For i = 0 To 9
            .Add Key:=Sheet2.Range("B" & i + 3), Text:=Sheet2.Range("B" & i + 3)
            Call addChild(i + 3, Sheet2.Range("B" & i + 3))
        Next
    End With
    
End Sub

Sub addChild(r, cont)
    
    cnt = 1
    
    For i = 2 To Sheet2.Range("B" & r).End(xlToRight).column
        k = cont & " " & Sheet2.Range("A" & r) & Sheet2.Cells(2, i) & "0"
        TreeView1.Nodes.Add Sheet2.Range("B" & r).Value, tvwChild, k, Sheet2.Cells(r, i)
        cnt = cnt + 1
    Next
    
End Sub


Function 독자대상기호()
    For i = 0 To ListBox1.ListCount - 1
        If ListBox1.Selected(i) Then
            Select Case ListBox1.List(i)
                Case "교양": 독자대상기호 = 0
                Case "실용": 독자대상기호 = 1
                Case "청소년": 독자대상기호 = 4
                Case "학습참고서 1(중.고교용)": 독자대상기호 = 5
                Case "학습참고서 2(초등학생용)": 독자대상기호 = 6
                Case "아동": 독자대상기호 = 7
                Case "전문": 독자대상기호 = 9
            End Select
        End If
    Next
End Function

Function 발행형태기호()
    For i = 0 To ListBox2.ListCount - 1
        If ListBox1.Selected(i) Then
            발행형태기호 = i
            Exit Function
        End If
    Next
End Function

Sub set청구기호isbn()
    If Not (TextBox1 <> "" And TextBox2 <> "" And TextBox3 <> "" And TextBox4 <> "" And ListBox1.ListIndex <> -1 And ListBox2.ListIndex <> -1 And 내용분류기호 <> "") Then Exit Sub
    
    Call mkISBN
    Label14 = fnCallNM(TextBox1, TextBox2, Label15)
    
End Sub

Sub mkISBN()
    
    Randomize
    
    idx1 = Int(Rnd * 2)
    
    접두부 = Split("978 979")(idx1)
    국별번호 = Split("89 11")(idx1)
    
    발행자번호 = Int(Rnd * 900000) + 100000
    서명식별번호 = Int(Rnd * 10)
    
    isbn = 접두부 & "-" & 국별번호 & "-" & 발행자번호 & "-" & 서명식별번호
    체크기호 = 접두부 & 국별번호 & 발행자번호 & 서명식별번호
    
    s = 0
    Debug.Print 체크기호
    For i = 1 To Len(체크기호)
        v = Val(Mid(체크기호, i, 1))
        s = s + IIf(v Mod 2 = 0, v, v * 3)
    Next
    
    체크기호 = IIf((s Mod 10) = 0, 0, 10 - (s Mod 10))
    부가기호 = 독자대상기호 & 발행형태기호 & 내용분류기호
    
    Label15 = 접두부 & "-" & 국별번호 & "-" & 발행자번호 & "-" & 서명식별번호 & "-" & 체크기호 & "[" & 부가기호 & "]"
    
End Sub
