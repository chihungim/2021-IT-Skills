Attribute VB_Name = "���θ��"
Sub �����α���()
    With Sheet1
        .CommandButton1.Caption = "�α׾ƿ�"
        .CommandButton2.Caption = "��������"
        .CommandButton3.Enabled = True
        .CommandButton4.Enabled = True
        .CommandButton5.Enabled = True
        .CommandButton6.Enabled = True
    End With
End Sub


Sub �α׾ƿ�()
    [��ȣ] = ""
    With Sheet1
        .CommandButton1.Caption = "�α���"
        .CommandButton2.Caption = "ȸ������"
        .CommandButton3.Caption = "�����˻�"
        .CommandButton4.Caption = "��ٱ���"
        .CommandButton5.Caption = "����������"
        .CommandButton6.Caption = "���ų���"
        .CommandButton7.Visible = True
        .CommandButton8.Visible = True
        .CommandButton3.Enabled = False
        .CommandButton4.Enabled = False
        .CommandButton5.Enabled = False
        .CommandButton6.Enabled = False
        .Range("B14:G14").Font.Color = vbBlack
    End With
End Sub

Sub �����ڷα���()
    [��ȣ] = "��"
    With Sheet1
        .CommandButton1.Caption = "�α׾ƿ�"
        .CommandButton2.Caption = "ȸ������"
        .CommandButton3.Caption = "�������"
        .CommandButton4.Caption = "�м�"
        .CommandButton5.Caption = "���"
        .CommandButton6.Caption = "����"
        .CommandButton7.Visible = False
        .CommandButton8.Visible = False
        .CommandButton3.Enabled = True
        .CommandButton4.Enabled = True
        .CommandButton5.Enabled = True
        .CommandButton6.Enabled = True
        .Range("B14:G14").Font.Color = vbWhite
    End With
End Sub
