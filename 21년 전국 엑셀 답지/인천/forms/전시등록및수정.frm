VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���õ�Ϲ׼��� 
   Caption         =   "����ȸ ���"
   ClientHeight    =   7410
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5220
   OleObjectBlob   =   "���õ�Ϲ׼���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "���õ�Ϲ׼���"
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
    ����ȸ����.Show

End Sub

Private Sub CommandButton2_Click()

    If Trim(TextBox1) = "" Or Trim(TextBox2) = "" Then
        errmsg "��ĭ�� �����մϴ�."
        Exit Sub
    End If
    
    If ComboBox1.ListIndex = 0 Then
        errmsg "��ǰ �з��� �������ּ���."
        Exit Sub
    End If
    
    If chk = False Then
        errmsg "��ǰ�� �������ּ���."
        Exit Sub
    End If

    If mode = "add" Then
        With Sheet6
            irow = .Range("A10000").End(3).Row + 1
            Range("A" & irow & ":F" & irow) = Array(irow - 1, pNo, "D", "2021-09-20", "2021-09-25")
        End With
        iMsg "����ȸ ����� �Ϸ�Ǿ����ϴ�."
    Else
        With Sheet6
            irow = .Range("A10000").End(3).Row + 1
            Range("A" & irow & ":F" & irow) = Array(irow - 1, pNo, "D", "2021-09-20", "2021-09-25")
        End With
        iMsg "����ȸ ������ �Ϸ�Ǿ����ϴ�."
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
            pic = ThisWorkbook.Path & "\�����ڷ�\�̹���\�⺻ �̹���.jpg"
        End If
        Image1.Picture = LoadPicture(pic)
        Repaint
    End With

End Sub

Private Sub UserForm_Initialize()

    Call mkdic(Sheet10.Range("A2"), picNum, "", 1)

    ComboBox1.List = Array("��������", "��¡����", "�λ�����", "��������", "����������")

    If mode = "add" Then
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹���\�⺻ �̹���.jpg")
    Else
        CommandButton2.Caption = "����"
        Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹���\" & picNum(pNo).Cells(1, 2) & "\" & pNo & ".jpg")
        TextBox1 = picNum(pNo).Cells(1, 3)
        ComboBox1 = picNum(pNo).Cells(1, 2)
        TextBox2 = picNum(pNo).Cells(1, 5)
    End If

End Sub
