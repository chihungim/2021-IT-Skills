VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �޷� 
   Caption         =   "�޷�"
   ClientHeight    =   4995
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4800
   OleObjectBlob   =   "�޷�.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�޷�"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim ��¥ As Date
Dim cls(42) As New Class1
Public ������

Private Sub btnNext_Click()
    ��¥ = DateAdd("m", 1, ��¥)
    Label2 = Month(��¥)
    Call setCal
End Sub

Private Sub btnPrev_Click()
    ��¥ = DateAdd("m", -1, ��¥)
    Label2 = Month(��¥)
    Call setCal
End Sub

Private Sub ComboBox1_Change()

End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub UserForm_Activate()
    ��¥ = DateSerial(Year(Date), Month(Date), 1)
    
    ComboBox1.AddItem Year(��¥)
    ComboBox1.ListIndex = 0
    Label2 = Month(��¥)
    
    For i = 1 To 42
        Set cls(i).cb = Me.Controls("CommandButton" & i)
    Next
    
    Call setCal
End Sub

Sub setCal()
    
    week = Weekday(��¥, 0)
    e = WorksheetFunction.EoMonth(��¥, 0)
    
    For i = 1 To 42
        cls(i).cb.Visible = False
        cls(i).cb.Caption = ""
        cls(i).cb.Enabled = False
    Next
    
    
    k = 1
    For i = ��¥ - week + 1 To e
                
        If i >= Date Then cls(k).cb.Enabled = True
        If ������ <> "" Then
            cls(k).cb.Enabled = False
            If i > ������ Then cls(k).cb.Enabled = True
        End If
                
        If Month(i) = Month(��¥) Then
            cls(k).cb.Visible = True
            cls(k).cb.Caption = Day(i)
         
        End If
        k = k + 1
        
    Next
    
End Sub

