VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 카드등록 
   Caption         =   "카드등록"
   ClientHeight    =   10665
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   14805
   OleObjectBlob   =   "카드등록.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "카드등록"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim 전월실적 As New ArrayList

Private Sub CommandButton1_Click()
    If ComboBox2.ListIndex = -1 Then eMsg "카테고리는 하나 이상 선택해야 합니다.": Exit Sub
     
    If TextBox5 = "" Or TextBox6 = "" Or TextBox7 = "" Then eMsg "빈칸을 모두 입력해야 합니다.": Exit Sub
    
    If Not Right(TextBox6, 1) = "%" Then eMsg "비율은 퍼센트 단위로 입력해야 합니다.": Exit Sub
    
    If Not IsNumeric(TextBox7) Then eMsg "전월실적은 숫자로 입력해야 합니다.": Exit Sub
    
    Set l = ListView1.ListItems.Add(, , ListView1.ListItems.Count + 1)
    l.ListSubItems.Add , , ComboBox2
    l.ListSubItems.Add , , TextBox6
    l.ListSubItems.Add , , TextBox5
    전월실적.Add Format(TextBox7, "#,##0")
    
    For i = 5 To 7
        Me.Controls("TextBox" & i) = ""
    Next
    
    ComboBox2.ListIndex = -1
    
End Sub

Private Sub CommandButton2_Click()
    If TextBox1 = "" Or TextBox2 = "" Or TextBox3 = "" Or TextBox4 = "" Then eMsg "카드 정보를 모두 입력해야 합니다.": Exit Sub
    
    If Not (CheckBox1 Or CheckBox2 Or CheckBox3 Or CheckBox4 Or CheckBox5 Or CheckBox6) Then eMsg "카드 정보를 모두 입력해야 합니다.": Exit Sub
    
    If ComboBox1.ListIndex = -1 Then eMsg "카드 정보를 모두 입력해야 합니다.": Exit Sub
    
    If Image1.Picture Is Nothing Then eMsg "카드 사진을 등록해야 합니다.": Exit Sub
    
    If Not Right(TextBox3, 1) = "%" Then eMsg "할부이자는 퍼센트 단위로 입력해야 합니다.": Exit Sub
    
    If Not IsNumeric(TextBox4) Then eMsg "연회비는 숫자로 입력해야 합니다.": Exit Sub
    
    If Not (CheckBox7 Or CheckBox8 Or CheckBox9 Or CheckBox10) Then eMsg "제휴 브랜드를 1개 이상 선택해야 합니다.": Exit Sub
    
    If ListView1.ListItems.Count = 0 Then eMsg "카드 혜택은 1개 이상 등록해야 합니다.": Exit Sub
    
    Set dic = mkdic(Sheet2.Range("B2"))
    
    If dic.exists(TextBox1.Value) Then eMsg "중복되는 카드 이름이 존재합니다.": exits ub
    
    
    Set list = New ArrayList
    
    With Sheet3
        For i = 7 To 10
            If Me.Controls("CheckBox" & i) Then
                If 카테고리 = "" Then
                    카테고리 = Me.Controls("CheckBox" & i).Caption
                Else
                    카테고리 = 카테고리 & "," & Me.Controls("Checkbox" & i).Caption
                End If
            End If
        Next
        
        For i = 1 To ListView1.ListItems.Count
            lrow = .Range("A1000").End(3).Row + 1
            list.Add lrow
            .Range("A" & lrow & ":F" & lrow) = Array(lrow - 1, ListView1.ListItems(i).SubItems(1), ListView1.ListItems(i).SubItems(3), 카테고리, ListView1.ListItems(i).SubItems(2), 전월실적(i - 1))
        Next
    End With
    
    With Sheet2
        For i = 1 To 6
            If Me.Controls("CheckBox" & i) Then
                If 해외 = "" Then
                    해외 = i
                Else
                    해외 = 해외 & "," & i
                End If
            End If
        Next
    
        Set 타입 = New ArrayList
        For i = 1 To ListView1.ListItems.Count
            If Not .타입.Contains(ListView1.ListItems(i).SubItems(1)) Then
                타입.Add ListView1.ListItems(i).SubItems(1)
            Next
        Next
        
        iMsg "새로운 카드를 등록했습니다."
        
        lrow = .Range("A1000").End(xlUp).Row + 1
        
        .Range("A" & lrow & ":I" & lrow) = Array(lrow - 1, TextBox1, TextBox2, Join(타입.ToArray, ","), Join(list.ToArray, ","), Format(TextBox3, "#.#0%"), 해외, Val(ComboBox1), Format(TextBox4, "#,##0"))
        SavePicture Image1.Picture, ThisWorkbook.Path & "\지급자료\이미지\" & lrow - 1 & ".jpg"
    End With
End Sub

Private Sub Image1_Click()
    With Application.FileDialog(msoFileDialogOpen)
        .AllowMultiSelect = False
        .Filters.Clear
        .Filters.Add "JPG", "*.JPG"
        
        If .Show <> 0 Then pt = .SelectedItems(1)
        
        Image1.Picture = LoadPicture(pt)
        Repaint
    End With
End Sub

Private Sub UserForm_Initialize()
    With ListView1
        .MultiSelect = False
        .FullRowSelect = False
        .View = lvwReport
        With .ColumnHeaders
            .Add , , "번호", 50
            .Add , , "항목", 50
            .Add , , "비율", 50
            .Add , , "내용", 200
        End With
    End With
    
    For i = 1 To 5
        ComboBox1.AddItem i & "등급"
    Next
    
    ComboBox2.list = Array("할인", "적립")
End Sub
