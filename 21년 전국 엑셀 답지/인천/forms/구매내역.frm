VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���ų��� 
   Caption         =   "���ų���"
   ClientHeight    =   7395
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5280
   OleObjectBlob   =   "���ų���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "���ų���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim max
Dim imgLabel(0 To 100) As New Class3

Private Sub CommandButton1_Click()
    Unload Me
End Sub

Private Sub MultiPage1_Change()

End Sub

Private Sub UserForm_Initialize()
    MultiPage1.Style = fmTabStyleNone
    Sheet6.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet6.Range("N1:N2"), Sheet6.Range("P1:U1")
    
    irow = Sheet6.Range("P1000").End(xlUp).Row
    
    �������� Sheet3.Range("A2")
    
    For i = 2 To irow
        Set imgLabel(i) = New Class3
        
        Dim fpath, ltext
        
        With ����(Sheet6.Range("Q" & i).Value)
            ��ȣ = .Cells(1, 1)
            �з� = .Cells(1, 2)
            �� = .Cells(1, 3)
            ���� = Format(.Cells(1, 8), "#,##0")
            
            fpath = ThisWorkbook.Path & "\�����ڷ�\�̹���\" & �з� & "\" & ��ȣ & ".jpg"
            ltext = vbLf & "��ǰ��:" & �� & vbLf & vbLf & vbLf & vbLf & "����:" & ���� & "��"
        End With
            
        max = max + 60
        Call imgLabel(i).display(Me.MultiPage1.Pages(0), ((i - 2) * 70), 0, 70, 60, fpath, ltext)
    Next
    
    If max < 300 Then
        MultiPage1.Pages(0).ScrollBars = 0
    Else
        MultiPage1.Pages(0).ScrollBars = 2
        MultiPage1.Pages(0).ScrollHeight = max + 60
    End If
    

End Sub
