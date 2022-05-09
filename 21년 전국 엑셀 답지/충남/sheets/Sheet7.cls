VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet7"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True

Dim tcnt As Integer
Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub Label9_Click()

End Sub

Private Sub Label11_Click()

End Sub

Private Sub Label1_Click()
    Call FindBook_PrevPage
End Sub

Private Sub Label7_Click()
    Call FindBook_NextPage
End Sub

Private Sub Worksheet_Activate()
    
    For i = 1 To 5
        Set bookChk(i) = New clsFindBook
        Set bookChk(i).체크 = OLEObjects("체크" & i).Object
        Set bookChk(i).위치출력 = OLEObjects("위치출력" & i).Object
        Set bookChk(i).페이지 = OLEObjects("페이지" & i).Object
    Next
    
    Call FindBook검색
    
End Sub

Sub FindBook_PrevPage()

    첫페이지 = Val(bookChk(1).페이지.Caption)
    
    If 첫페이지 = 1 Then
        eMsg "더 이상 표시할 페이지가 없습니다.": Exit Sub
    Else
        curPage = 첫페이지 - 5
        bookChk(1).페이지.Caption = curPage
        startBookidx = (curPage - 1) * 5 + 1
    End If
    
End Sub

Sub FindBook_NextPage()
    
    첫페이지 = Val(bookChk(1).페이지.Caption)
    
    If (첫페이지 + 4) * 5 >= totSearchCnt Then
        eMsg "더 이상 표시할 페이지가 없습니다.": Exit Sub
    Else
        curPage = 첫페이지 + 5
        bookChk(1).페이지.Caption = curPage
        startBookidx = (curPage - 1) * 5 + 1
    End If

End Sub


Sub shp전체선택_Click()
    
    ComboBox1.ListIndex = 0
    TextBox1 = ""
    
    For i = 1 To 5
        Set lbl = OLEObjects("체크" & i).Object
        If lbl.Caption = ChrW(163) Then
            Range("AQ" & Shapes("번호" & i).TextFrame.Characters.Text + 1) = "O"
            lbl.Caption = ChrW(82)
            lbl.ForeColor = vbBlue
        Else
            Range("Z" & Shapes("번호" & i).TextFrame.Characters.Text + 1) = ""
            lbl.Caption = ChrW(163)
            lbl.ForeColor = vbBlack
        End If
    Next
    
End Sub

Sub asdfasdsafasd()
    
    For i = 1 To Shapes("검색1").GroupItems.Count
        MsgBox i & ", " & Shapes("검색1").GroupItems(i).name
    Next
    
End Sub

Private Sub Worksheet_SelectionChange(ByVal Target As Range)

End Sub

Private Sub 체크1_Click()

End Sub
