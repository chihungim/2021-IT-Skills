VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �α��� 
   Caption         =   "UserForm1"
   ClientHeight    =   3300
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6810
   OleObjectBlob   =   "�α���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�α���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()
    If TextBox1 = "" Or TextBox2 = "" Then eMsg "�� ĭ ���� �Է��ϼ���.": Exit Sub
    If TextBox1 = "admin" And TextBox2 = "1234" Then
        iMsg "�����ڴ� �α��� �Ǿ����ϴ�"
        Sheet11.[ȸ�����̵�] = TextBox1
        Exit Sub
    End If
    If Not dic���.exists(TextBox1.Value) Then eMsg "�߸��� �����Դϴ�.": Exit Sub
    If Not dic���(TextBox1.Value).Cells(1, 2) = TextBox2 Then eMsg "�߸��� �����Դϴ�.": Exit Sub
    
    iMsg dic���(TextBox1.Value).Cells(1, 3) & "�� �α��� �Ǿ����ϴ�."
    Sheet11.[ȸ�����̵�] = TextBox1
    Unload Me
End Sub

Private Sub CommandButton2_Click()
    Unload Me
End Sub

Private Sub UserForm_Initialize()
    mkdic dic���, Sheet1.[a2]
    
    TextBox1.SetFocus
End Sub
