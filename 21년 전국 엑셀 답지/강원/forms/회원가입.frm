VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ȸ������ 
   Caption         =   "ȸ������"
   ClientHeight    =   5445
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7320
   OleObjectBlob   =   "ȸ������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ȸ������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim id����

Private Sub CommandButton1_Click()
        
    If TextBox2 = "" Then eMsg "���̵� �Է����ּ���.": Exit Sub
    
    mkdic dicȸ��, Sheet4.Range("B2")
    If dic.exists(TextBox2.Value) Then eMsg "�̹� �����ϴ� ���̵��Դϴ�.": TextBox2 = "": TextBox2.SetFocus: Exit Sub
    
    iMsg "��� ������ ���̵��Դϴ�."
    id���� = True
    
End Sub

Private Sub CommandButton2_Click()
    
    If Not id���� Then eMsg "�ߺ� Ȯ���� ���ּ���.": Exit Sub
    
    If TextBox3 <> TextBox4 Then eMsg "��й�ȣ�� Ȯ�����ּ���.": Exit Sub
    
    If Not email(TextBox10.Value & "@" & TextBox11.Value) Then eMsg "�̸��� ������ ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    If Not IsDate(TextBox5) Then eMsg "������� ������ ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    If IsNumeric(TextBox7) = False Or IsNumeric(TextBox8) = False Or IsNumeric(TextBox9) = False Then eMsg "��ȭ��ȣ�� ���ڸ� �ԷµǾ�� �մϴ�.": Exit Sub
    
    iMsg "ȸ�������� �Ϸ�Ǿ����ϴ�."
    With Sheet4
        lrow = .Range("A1000").End(3).row + 1
        .Range("A" & lrow & ":G" & lrow) = Array(lrow - 1, TextBox2, TextBox3, TextBox1, TextBox7 & "-" & TextBox8 & "-" & TextBox9, TextBox5 & "-" & TextBox6 & Label9, TextBox10 & "@" & TextBox11)
        
        Unload Me
        �α���.Show
    End With
    
End Sub

Private Sub CommandButton3_Click()
    Unload Me
End Sub

Private Sub TextBox2_Change()
    id���� = False
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    
    
End Sub
