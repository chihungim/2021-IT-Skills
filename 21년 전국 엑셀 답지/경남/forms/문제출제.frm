VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �������� 
   Caption         =   "��������"
   ClientHeight    =   8235
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6945
   OleObjectBlob   =   "��������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    If ComboBox1.ListIndex = 0 Then
        ComboBox2.AddItem "����"
    ElseIf ComboBox1.ListIndex = 1 Then
        ComboBox2.AddItem "1��"
        ComboBox2.AddItem "2��"
    Else
        ComboBox2.AddItem "1��"
        ComboBox2.AddItem "2��"
        ComboBox2.AddItem "3��"
    End If
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub CommandButton2_Click()
    If ComboBox1.ListIndex = -1 Or ComboBox2.ListIndex = -1 Then eMsg "������ �������ּ���.": Exit Sub
    
    For i = 1 To 5
        If Me.Controls("Textbox" & i) = "" Then eMsg "�� �׸��� �ֽ��ϴ�.": Exit Sub
    Next
    
    If OptionButton1 = False And OptionButton2 = False And OptionButton3 = False And OptionButton4 = False Then eMsg "������ ������ �ּ���.": Exit Sub
    
    ���� = ""
    For i = 1 To 4
        If Me.Controls("OptionButton" & i) Then
            ���� = i
            Exit For
        End If
    Next
    
    With Sheet7
        lrow = .Range("A10000").End(3).Row + 1
        .Range("A" & lrow & ":D" & lrow) = Array(lrow - 1, get�����ȣ, TextBox1, ����)
    End With
    
    With Sheet8
        lrow = .Range("A10000").End(3).Row + 1
        .Range("B" & lrow & ":E" & lrow) = Array(TextBox2, TextBox3, TextBox4, TextBox5)
    End With
    
    iMsg "���� ������ �Ϸ�Ǿ����ϴ�.": Exit Sub
    
End Sub

Function get�����ȣ()
    For i = 2 To Sheet3.Range("A1000").End(3).Row
        If Sheet3.Range("B" & i) & Sheet3.Range("C" & i) = ComboBox1 & ComboBox2 Then
            get�����ȣ = Sheet3.Range("A" & i)
            Exit Function
        End If
    Next
End Function

Private Sub UserForm_Click()
    
End Sub

Private Sub UserForm_Initialize()
    
    For i = 2 To Sheet3.Range("A100").End(3).Row
        ComboBox1.AddItem Sheet3.Range("B" & i)
    Next
    
    ComboBox1.Style = fmStyleDropDownList
    ComboBox2.Style = fmStyleDropDownList
    
End Sub
