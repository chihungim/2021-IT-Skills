VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ī���û 
   Caption         =   "ī�� ��û"
   ClientHeight    =   9240
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   11610
   OleObjectBlob   =   "ī���û.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ī���û"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim dicī���̸� As Object
Dim sum

Private Sub ComboBox1_Change()
    Set dicī���̸� = mkdic(Sheet2.Range("B2"))
    
    ī�弳�� = dicī���̸�(ī���.Caption).Cells(1, 2)
    Ÿ�� = dicī���̸�(ī���.Caption).Cells(1, 3)
    ���� = Format(dicī���̸�(ī���.Caption).Cells(1, 5), "#.#0%")
    ��� = dicī���̸�(ī���.Caption).Cells(1, 7) & "���"
    
    �⺻��ȸ�� = Format(dicī���̸�(ī���.Caption).Cells(1, 8), "#,##0��")
    
    Set dic = mkdic(Sheet3.Range("I2"))
    �߰���ȸ�� = Format(Val(Sheet3.Cells(dic(ComboBox1.Value).Row, 9 + Val(���.Caption))), "#,##0��")
    sum = dicī���̸�(ī���.Caption).Cells(1, 8) + Val(Sheet3.Cells(dic(ComboBox1.Value).Row, 9 + Val(���.Caption)))
    
    �ѿ�ȸ�� = sum
    
    ���� = Split(dicī���̸�(ī���.Caption).Cells(1, 4), ",")
    
    For i = 0 To UBound(����)
        st = "�������� " & Format(Sheet3.Range("F" & ����(i) + 1), "#,##0��") & " �̻� ������, "
        If Sheet3.Range("B" & ����(i) + 1) = "����" Then
            ���� = st & Sheet3.Range("C" & ����(i) + 1) & vbCrLf & "(" & Sheet3.Range("D" & ����(i) + 1) & " ī�װ� ����, �ִ� " & Format(Sheet3.Range("E" & ����(i) + 1), "#%") & " ����)"
        Else
            ���� = st & Sheet3.Range("C" & ����(i) + 1) & vbCrLf & "(" & Sheet3.Range("D" & ����(i) + 1) & " ī�װ� ����, �ִ� " & Format(Sheet3.Range("E" & ����(i) + 1), "#%") & " ����)"
        End If
    Next
End Sub


Private Sub UserForm_Terminate()
    If Not ��ûflag Then eMsg "ī�尡 ���������� �߱޵��� �ʾҽ��ϴ�.."
End Sub
