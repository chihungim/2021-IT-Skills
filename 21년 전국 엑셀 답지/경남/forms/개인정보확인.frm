VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ��������Ȯ�� 
   Caption         =   "��������Ȯ��"
   ClientHeight    =   1440
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6810
   OleObjectBlob   =   "��������Ȯ��.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��������Ȯ��"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    If TextBox1 = "" Then eMsg "��ĭ�Դϴ�.": Exit Sub
    
    mkdic dicȸ��, Sheet2.Range("D2")
    
    If dicȸ��.exists(TextBox1.Value) Then
        iMsg "�����Ǿ����ϴ�."
        ��������r = dicȸ��(TextBox1.Value).Row
        Unload Me
        ������������.Show
    End If
    
End Sub
