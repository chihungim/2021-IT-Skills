VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} UserForm1 
   Caption         =   "UserForm1"
   ClientHeight    =   6705
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   12510
   OleObjectBlob   =   "UserForm1.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "UserForm1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Private Sub Frame1_Click()

End Sub

Private Sub UserForm_Initialize()
    cnt = 5
    For i = 0 To cnt - 1
        Dim cls As cls���������: Set cls = New cls���������
        cls.����init Frame1, ThisWorkbook.Path & "\�����ڷ�\�̹���\����\3 2 1.jpg", "�α׾ƿ�", 10, (i * 120) + 10
    Next
    
    Max = (125 * cnt) + 10
    
    If 3 < cnt Then
        Frame1.ScrollBars = fmScrollBarsHorizontal
        Frame1.ScrollWidth = Max
    End If
End Sub
