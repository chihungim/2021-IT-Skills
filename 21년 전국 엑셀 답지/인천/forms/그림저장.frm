VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 그림저장 
   Caption         =   "그림등록"
   ClientHeight    =   4590
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6555
   OleObjectBlob   =   "그림저장.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "그림저장"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    Application.ScreenUpdating = False
    
    Dim genre
    
    Sheet3.Range("AV2") = ComboBox1
    
    Select Case ComboBox1.ListIndex
        Case 0
            genre = "RO"
        Case 1
            genre = "SY"
        Case 2
            genre = "IM"
        Case 3
            genre = "TR"
        Case 4
            genre = "SU"
    End Select
    
    사전생성 Sheet3.Range("A2")
    With Sheet3
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet3.Range("AV1:AV2"), Sheet3.Range("AX1"), False
        작품번호 = genre & .Range("AX1000").End(xlUp).Row
        rowVal = .Range("AX1000").End(xlUp).Value
        irow = 사전(rowVal).Row
    End With
End Sub


Private Sub CommandButton1_Click()
    If TextBox1 = "" Or TextBox3 = "" Then
        eMsg "빈칸이 존재합니다."
        Exit Sub
    End If
    
    If Not TextBox3 Like "*[^0-9]*" Then eMsg "가격은 숫자로만 입력해주세요.": Exit Sub
    
    Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\지급자료\이미지\" & ComboBox1 & "\" & 작품번호 & ".jpg")
    
    With Sheet3
        .Rows(irow & ":" & irow).Insert xlDown, xlFormatFromRightOrBelow
        .Range("A" & irow & ":H" & irow) = Array(작품번호, ComboBox1, TextBox1, Sheet1.[이름], Sheet1.[번호], TextBox2, "", Format(TextBox3, "#,##0"))
        .Range("I" & irow).FillDown
    End With
    
    iMsg "등록이 완료되었습니다."
    Unload Me

End Sub

Private Sub CommandButton2_Click()
    Unload Me
    그림판.Show
End Sub

Private Sub UserForm_Initialize()
    Image1.Picture = LoadPicture(ThisWorkbook.Path & "\그림.gif")
    Kill ThisWorkbook.Path & "\그림.gif"
    
    ComboBox1.List = Array("낭만주의", "상징주의", "인상주의", "전통주의", "초현실주의")
    ComboBox1.ListIndex = 0
End Sub
