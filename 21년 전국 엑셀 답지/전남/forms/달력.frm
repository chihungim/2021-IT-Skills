VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 달력 
   Caption         =   "달력"
   ClientHeight    =   4995
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4800
   OleObjectBlob   =   "달력.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "달력"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim 날짜 As Date
Dim cls(42) As New Class1
Public 시작일

Private Sub btnNext_Click()
    날짜 = DateAdd("m", 1, 날짜)
    Label2 = Month(날짜)
    Call setCal
End Sub

Private Sub btnPrev_Click()
    날짜 = DateAdd("m", -1, 날짜)
    Label2 = Month(날짜)
    Call setCal
End Sub

Private Sub ComboBox1_Change()

End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub UserForm_Activate()
    날짜 = DateSerial(Year(Date), Month(Date), 1)
    
    ComboBox1.AddItem Year(날짜)
    ComboBox1.ListIndex = 0
    Label2 = Month(날짜)
    
    For i = 1 To 42
        Set cls(i).cb = Me.Controls("CommandButton" & i)
    Next
    
    Call setCal
End Sub

Sub setCal()
    
    week = Weekday(날짜, 0)
    e = WorksheetFunction.EoMonth(날짜, 0)
    
    For i = 1 To 42
        cls(i).cb.Visible = False
        cls(i).cb.Caption = ""
        cls(i).cb.Enabled = False
    Next
    
    
    k = 1
    For i = 날짜 - week + 1 To e
                
        If i >= Date Then cls(k).cb.Enabled = True
        If 시작일 <> "" Then
            cls(k).cb.Enabled = False
            If i > 시작일 Then cls(k).cb.Enabled = True
        End If
                
        If Month(i) = Month(날짜) Then
            cls(k).cb.Visible = True
            cls(k).cb.Caption = Day(i)
         
        End If
        k = k + 1
        
    Next
    
End Sub

