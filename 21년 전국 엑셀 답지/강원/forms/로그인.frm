VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �α��� 
   Caption         =   "�α���"
   ClientHeight    =   3825
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4920
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
    
    If uid = "" Or upw = "" Then eMsg "��ĭ�� �����մϴ�.": Exit Sub
    
    If uid = "admin" And upw = "1234" Then
        [��ȣ] = "admin"
        iMsg "�����ڷ� �α��� �Ͽ����ϴ�."
        �����ڷα���
        Unload Me
        Exit Sub
    End If
    
    mkdic dicȸ��, Sheet4.Range("B2")
    
    If Not dicȸ��.exists(uid) Then
        eMsg "���̵� Ȥ�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": TextBox1 = "": TextBox2 = "": TextBox1.SetFocus: Exit Sub
    Else
        If dicȸ��(uid).Cells(1, 2) <> upw Then eMsg "���̵� Ȥ�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": TextBox1 = "": TextBox2 = "": TextBox1.SetFocus: Exit Sub
    End If
    
    iMsg dicȸ��(uid).Cells(1, 3) & "������ �α��� �Ͽ����ϴ�."
    �����α���
    Unload Me
        
    
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub Label3_Click()
    Unload Me
    ȸ������.Show
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
   chgImg
    
End Sub


Private Sub UserForm_Terminate()
    stopchgImg
End Sub
