VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} idpwã�� 
   Caption         =   "idpwã��"
   ClientHeight    =   3360
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5460
   OleObjectBlob   =   "idpwã��.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "idpwã��"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    
    Dim clip As MSForms.DataObject
    Set clip = New MSForms.DataObject
    
    
    txt1 = TextBox1: txt2 = TextBox2
    If txt1 = "" Or txt2 = "" Then eMsg "��ĭ�� �ֽ��ϴ�.": Exit Sub
    
    If CommandButton1.Caption = "���̵� ã��" Then
        mkdic dicȸ��, Sheet2.Range("B2")
        If Not dicȸ��.exists(txt1) Then
            eMsg "��ġ�ϴ� ������ �����ϴ�": Exit Sub
        Else
            If dicȸ��(txt1).Cells(1, 4) <> txt2 Then eMsg "��ġ�ϴ� ������ �����ϴ�": Exit Sub
        End If
        
        uid = dicȸ��(txt1).Cells(1, 2)
        If MsgBox("ȸ���Բ��� �����Ͻ� ���̵�� " & uid & "�Դϴ�." & vbCrLf & "�����Ͻðڽ��ϱ�?", vbInformation + vbYesNo, "����") = vbYes Then
            iMsg uid
            
            
            
        End If
        Unload Me
        
    Else
        mkdic dicȸ��, Sheet2.Range("C2")
        If Not dicȸ��.exists(txt1) Then
            eMsg "��ġ�ϴ� ������ �����ϴ�": Exit Sub
        Else
            If dicȸ��(txt1).Cells(1, 3) <> txt2 Then eMsg "��ġ�ϴ� ������ �����ϴ�": Exit Sub
        End If
        Randomize
        �ӽú�� = Format(Int(Rnd * 1000000), "0#####")
        dicȸ��(txt1).Cells(1, 2) = �ӽú��
        
        If MsgBox("�ӽ� ��й�ȣ�� " & �ӽú�� & "�Դϴ�." & vbCrLf & "�����Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
            
        End If
        
        Unload Me
    End If
    
End Sub

Private Sub OptionButton1_Click()
    Label1 = "�̸�"
    CommandButton1.Caption = "���̵� ã��"
End Sub

Private Sub OptionButton2_Click()
    Label1 = "���̵�"
    CommandButton1.Caption = "��й�ȣ ã��"
End Sub

Private Sub UserForm_Click()

End Sub
