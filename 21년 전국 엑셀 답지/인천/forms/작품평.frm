VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ��ǰ�� 
   Caption         =   "��ǳ��"
   ClientHeight    =   4590
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5100
   OleObjectBlob   =   "��ǰ��.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��ǰ��"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public imgNo

Private Sub CommandButton1_Click()
    If CommandButton1.Caption = "�ۼ� �Ϸ�" Then
        irow = Sheet4.Range("A1000").End(xlUp).Row + 1
        Sheet4.Range("A" & irow & ":E" & irow) = Array(irow - 1, imgNo, Sheet1.Range("��ȣ"), ����, TextBox3.Text)
        Sheet4.Range("F" & irow).FillDown
    Else
        Sheet4.Range("A" & irow & ":E" & irow) = Array(irow - 1, imgNo, Sheet1.Range("��ȣ"), ����, TextBox3.Text)
        Sheet4.Range("F" & irow).FillDown
    End If
    
    Unload Me
    
End Sub

Private Sub CommandButton2_Click()
      Unload Me
End Sub

Private Sub SpinButton1_SpinDown()
    ���� = SpinButton1.Value / 2
End Sub

Private Sub SpinButton1_SpinUp()
    ���� = SpinButton1.Value / 2
End Sub

Private Sub UserForm_Initialize()
    ���� = SpinButton1.Value
      
End Sub

Private Sub TextBox3_Enter()
    If TextBox3 = "�̰��� �򰡸� �ۼ��ϼ���." Then
        TextBox3 = ""
    End If
    
    SpinButton1.SetFocus
End Sub

Private Sub TextBox3_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If TextBox3 = "" Then
        TextBox3 = "�̰��� �򰡸� �ۼ��ϼ���."
        TextBox3.ForeColor = &H80000000
    End If
End Sub


