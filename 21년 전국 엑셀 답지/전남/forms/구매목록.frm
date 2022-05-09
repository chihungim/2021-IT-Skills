VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 구매목록 
   Caption         =   "구매내역"
   ClientHeight    =   6240
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8145
   OleObjectBlob   =   "구매목록.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "구매목록"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim v As Integer

Private Sub ComboBox1_Change()
    Call 검색
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton1_Click()
    If v = 0 Then Exit Sub
    
    With Sheet5
        Set rg = .Range("A:A").Find(What:=v, LookAt:=xlWhole)
        If rg(1, 4) = Date Then
            iMsg "반품이 완료되었습니다."
            .ListObjects("표3").Range.AutoFilter Field:=2
            Sheet5.Rows(rg.row).Delete
            Call 검색
            .ListObjects("표3").Range.AutoFilter Field:=2, Criteria1:=[아이디]
        Else
            eMsg "오늘 구매한 도서만 반품 가능합니다.": Exit Sub
        End If
    End With
    
End Sub

Private Sub ListView1_BeforeLabelEdit(Cancel As Integer)

End Sub


Private Sub ListView1_Click()
    v = ListView1.SelectedItem.Index
End Sub

Private Sub ListView1_DblClick()
    평점입력.lrow = ListView1.ListItems(ListView1.SelectedItem.Index) + 1
    평점입력.Show

End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    Label2 = [이름]
    ComboBox1.AddItem "전체"
    
    For i = 1 To Month(Date)
        ComboBox1.AddItem i & "월"
    Next
    
    With ListView1
        .Gridlines = True
        .FullRowSelect = True
        .View = lvwReport
        
        With .ColumnHeaders
            .Add , , "번호", 40
            .Add , , "도서명", 130
            .Add , , "구매일자", 80
            .Add , , "수량", 40
            .Add , , "합계", 80
        End With
    End With
    
    
    ComboBox1.ListIndex = 0
    
End Sub

Sub 검색()
    ListView1.ListItems.Clear
    
    값 = IIf(ComboBox1.ListIndex = 0, "**", "*" & Val(ComboBox1) & "*")
    cnt = 0: m = 0
    
    Call mkDic(dic도서, Sheet4.Range("B2"))
    
    With Sheet5
        For i = 2 To .Range("A100000").End(3).row
            If .Range("B" & i) = [아이디] And Month(.Range("D" & i)) Like 값 Then
                Set li = ListView1.ListItems.Add(, , .Range("A" & i))
                li.SubItems(1) = dic도서(.Range("C" & i).Value).Cells(1, 4)
                li.SubItems(2) = .Range("D" & i)
                li.SubItems(3) = .Range("E" & i)
                li.SubItems(4) = Format(dic도서(.Range("C" & i).Value).Cells(1, 7) * .Range("E" & i), "#,##0")
                cnt = cnt + 1
                m = m + dic도서(.Range("C" & i).Value).Cells(1, 7) * .Range("E" & i)
            End If
        Next
    End With
    
    Label5 = cnt & "권"
    Label7 = Format(m, "#,##0")
    
End Sub

