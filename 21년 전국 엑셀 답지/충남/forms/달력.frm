VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �޷� 
   Caption         =   "UserForm1"
   ClientHeight    =   5205
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4635
   OleObjectBlob   =   "�޷�.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�޷�"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(42) As New cls�޷�
Public ��, ��

Private Sub btnNext_Click()
    If Val(��) = 12 Then
        �� = Val(��) + 1: �� = "1"
    Else
        �� = Val(��) + 1
    End If
    
    Label1 = �� & "�� " & �� & "��"
    
    ��¥����
End Sub

Private Sub btnPrev_Click()
    If Val(��) = 1 Then
        �� = Val(��) - 1: �� = "12"
    Else
        �� = Val(��) - 1
    End If
    
    Label1 = �� & "�� " & �� & "��"
    
    ��¥����
End Sub

Private Sub UserForm_Initialize()
    For i = 1 To 42
        Set cls(i).btn = Me.Controls("CommandButton" & i)
    Next
    
    Label1 = Format(Date, "yyyy�� m��")
    
    ��¥����
End Sub

Sub ��¥����()
    
    �� = Left(Label1, 4)
    �� = Mid(Label1, 7, 2)
    
    startd = WorksheetFunction.Weekday(DateSerial(��, Val(��), 1))
    
    For i = 1 To 42
        d = DateSerial(��, Val(��), i - startd + 1)
        cls(i).btn.Caption = Day(d)
        cls(i).btn.Visible = False
        
        If month(CDate(Label1.Caption)) = month(d) Then cls(i).btn.Visible = True
    Next
End Sub
