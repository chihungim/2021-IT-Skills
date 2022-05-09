VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 전시등록및수정 
   Caption         =   "전시회 등록"
   ClientHeight    =   7410
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5220
   OleObjectBlob   =   "전시등록및수정.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "전시등록및수정"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Public peri
Dim chk, picNum As Object

Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)

    KeyAscii = 0

End Sub

Private Sub CommandButton1_Click()

    Unload Me
    전시회일정.Show

End Sub

Private Sub CommandButton2_Click()

    If Trim(TextBox1) = "" Or Trim(TextBox2) = "" Then
        errmsg "빈칸이 존재합니다."
        Exit Sub
    End If
    
    If ComboBox1.ListIndex = 0 Then
        errmsg "작품 분류를 선택해주세요."
        Exit Sub
    End If
    
    If chk = False Then
        errmsg "작품을 선택해주세요."
        Exit Sub
    End If

    If mode = "add" Then
        With Sheet6
            irow = .Range("A10000").End(3).Row + 1
            Range("A" & irow & ":F" & irow) = Array(irow - 1, pNo, "D", "2021-09-20", "2021-09-25")
        End With
        iMsg "전시회 등록이 완료되었습니다."
    Else
        With Sheet6
            irow = .Range("A10000").End(3).Row + 1
            Range("A" & irow & ":F" & irow) = Array(irow - 1, pNo, "D", "2021-09-20", "2021-09-25")
        End With
        iMsg "전시회 수정이 완료되었습니다."
    End If
    
    Unload Me

End Sub

Private Sub Image1_Click()

    With Application.FileDialog(msoFileDialogOpen)
        .Filters.Clear
        .AllowMultiSelect = False
        Call .Filters.Add("image file", "*.jpg")
        cho = .Show
        If cho <> 0 Then
            chk = True
            pic = .SelectedItems(1)
        Else
            pic = ThisWorkbook.Path & "\지급자료\이미지\기본 이미지.jpg"
        End If
        Image1.Picture = LoadPicture(pic)
        Repaint
    End With

End Sub

Private Sub UserForm_Initialize()

    Call mkdic(Sheet10.Range("A2"), picNum, "", 1)

    ComboBox1.List = Array("낭만주의", "상징주의", "인상주의", "전통주의", "초현실주의")

    If mode = "add" Then
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\기본 이미지.jpg")
    Else
        CommandButton2.Caption = "수정"
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & picNum(pNo).Cells(1, 2) & "\" & pNo & ".jpg")
        TextBox1 = picNum(pNo).Cells(1, 3)
        ComboBox1 = picNum(pNo).Cells(1, 2)
        TextBox2 = picNum(pNo).Cells(1, 5)
    End If

End Sub
