VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ����ȸ���� 
   Caption         =   "����ȸ ����"
   ClientHeight    =   7950
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5250
   OleObjectBlob   =   "����ȸ����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����ȸ����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim selDate

Private Sub CommandButton1_Click()
    Unload Me
    ����ȸ����.Show

End Sub
Private Sub UserForm_Initialize()
    �������� Sheet3.Range("A2")
    
    
    With ����(pNo)
        Me.��ǰ�� = .Cells(1, 3)
        Me.�з� = .Cells(1, 2)
        Me.ȭ�� = .Cells(1, 4)
        Me.���� = .Cells(1, 6)
        Me.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹���\" & Me.�з� & "\" & pNo & ".jpg")
    End With

End Sub

