VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �������� 
   Caption         =   "UserForm1"
   ClientHeight    =   6075
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7545
   OleObjectBlob   =   "��������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(5) As cls���������
Public åī��Ʈ As Integer

Private Sub CommandButton1_Click()
    If Val(Label4) = 0 Then eMsg "������ ������ ���õ��� �ʾҽ��ϴ�.": Exit Sub
    If Val(Label3) < Val(Label4) Then eMsg "���� �� ���� �ʰ��Ͽ����ϴ�.": Exit Sub
    
    If MsgBox(Val(Label4) & "�� ������ �����Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
        
    End If
End Sub

Private Sub Frame1_Click()

End Sub

Private Sub UserForm_Initialize()
    �ڷ�ó���ʱ�ȭ Sheet11.[e1], Sheet11, Sheet4.Range("A1")
    mkdic dic���, Sheet1.[a2]
    
    TextBox1.Value = Format(Date + 14, "yyyy�� mm�� dd��") & " (" & WeekdayName(Weekday(Date + 14)) & ")"
    
    cnt = 5
    
    For i = 0 To cnt - 1
        Set cls(i) = New cls���������
        cls(i).����init Frame1, ThisWorkbook.Path & "\�����ڷ�\�̹���\����\3 2 1.jpg", "�α׾ƿ�", 10, (i * 120) + 10
    Next
    
    Max = (125 * cnt) + 10
    
    If 3 < cnt Then
        Frame1.ScrollBars = fmScrollBarsHorizontal
        Frame1.ScrollWidth = Max
    End If
    
    Label10 = dic���(Sheet11.[ȸ�����̵�].Value).Cells(1, 3)
End Sub

