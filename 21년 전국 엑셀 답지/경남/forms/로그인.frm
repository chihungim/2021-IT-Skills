VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �α��� 
   Caption         =   "�α���"
   ClientHeight    =   2760
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6780
   OleObjectBlob   =   "�α���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�α���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    
    uid = TextBox1: upw = TextBox2
    
    If uid = "" Then eMsg "���̵� �Է����ּ���.": Exit Sub
    
    If upw = "" Then eMsg "��й�ȣ�� �Է����ּ���.": Exit Sub
    
    If uid = "admin" And upw = "1234" Then
        iMsg "�����ڴ� ȯ���մϴ�."
        Exit Sub
    End If
    
    mkdic dicȸ��, Sheet2.Range("C2")
    
    If Not dicȸ��.exists(uid) Then
        eMsg "�Է��Ͻ� ���̵� �Ǵ� ��й�ȣ�� �ٽ� Ȯ�����ּ���.": Exit Sub
    Else
        If dicȸ��(uid).Cells(1, 2) <> upw Then eMsg "�Է��Ͻ� ���̵� �Ǵ� ��й�ȣ�� �ٽ� Ȯ�����ּ���.": Exit Sub
    End If
    
    iMsg "ȸ�� " & dicȸ��(uid).Cells(1, 0) & "�� ȯ���մϴ�."
    
End Sub

Private Sub CommandButton2_Click()
    ȸ������.Show
End Sub

Private Sub CommandButton3_Click()
    idpwã��.Show
End Sub

Private Sub UserForm_Click()

End Sub
