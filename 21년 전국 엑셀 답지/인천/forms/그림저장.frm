VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �׸����� 
   Caption         =   "�׸����"
   ClientHeight    =   4590
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6555
   OleObjectBlob   =   "�׸�����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�׸�����"
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
    
    �������� Sheet3.Range("A2")
    With Sheet3
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet3.Range("AV1:AV2"), Sheet3.Range("AX1"), False
        ��ǰ��ȣ = genre & .Range("AX1000").End(xlUp).Row
        rowVal = .Range("AX1000").End(xlUp).Value
        irow = ����(rowVal).Row
    End With
End Sub


Private Sub CommandButton1_Click()
    If TextBox1 = "" Or TextBox3 = "" Then
        eMsg "��ĭ�� �����մϴ�."
        Exit Sub
    End If
    
    If Not TextBox3 Like "*[^0-9]*" Then eMsg "������ ���ڷθ� �Է����ּ���.": Exit Sub
    
    Call SavePicture(Image1.Picture, ThisWorkbook.Path & "\�����ڷ�\�̹���\" & ComboBox1 & "\" & ��ǰ��ȣ & ".jpg")
    
    With Sheet3
        .Rows(irow & ":" & irow).Insert xlDown, xlFormatFromRightOrBelow
        .Range("A" & irow & ":H" & irow) = Array(��ǰ��ȣ, ComboBox1, TextBox1, Sheet1.[�̸�], Sheet1.[��ȣ], TextBox2, "", Format(TextBox3, "#,##0"))
        .Range("I" & irow).FillDown
    End With
    
    iMsg "����� �Ϸ�Ǿ����ϴ�."
    Unload Me

End Sub

Private Sub CommandButton2_Click()
    Unload Me
    �׸���.Show
End Sub

Private Sub UserForm_Initialize()
    Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�׸�.gif")
    Kill ThisWorkbook.Path & "\�׸�.gif"
    
    ComboBox1.List = Array("��������", "��¡����", "�λ�����", "��������", "����������")
    ComboBox1.ListIndex = 0
End Sub
