VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} Ű�е� 
   Caption         =   "Ű�е�"
   ClientHeight    =   5640
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5115
   OleObjectBlob   =   "Ű�е�.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "Ű�е�"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(10) As New clsŰ�е�

Private Sub CommandButton11_Click()
    If TextBox1 = "" Then eMsg "�� �̻� ���� ���ڰ� �����ϴ�.": Exit Sub
    TextBox1 = Left(TextBox1, Len(TextBox1) - 1)
End Sub

Private Sub CommandButton12_Click()
    TextBox1 = ""
    shuffle
End Sub

Private Sub CommandButton13_Click()
    shuffle
End Sub

Private Sub CommandButton14_Click()
    If TextBox1 = "" Then eMsg "��ĭ�Դϴ�.": Exit Sub
    
    If Len(TextBox1) <> 6 Then eMsg "6�ڸ��� �Է����ּ���.": TextBox1 = "": Exit Sub
    
    Unload Me
    ��������.TextBox1.ForeColor = vbBlack
    ��������.TextBox1 = Me.TextBox1
    
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    k = 11
    For Each i In Split("�� �ʱ�ȭ ��迭 Ȯ��")
        Me.Controls("CommandButton" & k).Caption = i
        k = k + 1
    Next
    
    For i = 1 To 10
        Set cls(i).cb = Me.Controls("CommandButton" & i)
    Next
    TextBox1.Locked = True
    
    shuffle
    
End Sub

Sub shuffle()
    
    Randomize
    arr = Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    For i = 0 To UBound(arr)
        J = ((UBound(arr) - i) * Rnd) + i
        tmp = arr(i)
        arr(i) = arr(J)
        arr(J) = tmp
    Next
    
    For i = 1 To 10
        Me.Controls("CommandButton" & i).Caption = arr(i - 1)
    Next
    
End Sub
