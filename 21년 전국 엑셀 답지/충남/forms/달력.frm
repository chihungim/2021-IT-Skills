VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 달력 
   Caption         =   "UserForm1"
   ClientHeight    =   5205
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4635
   OleObjectBlob   =   "달력.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "달력"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(42) As New cls달력
Public 년, 월

Private Sub btnNext_Click()
    If Val(월) = 12 Then
        년 = Val(년) + 1: 월 = "1"
    Else
        월 = Val(월) + 1
    End If
    
    Label1 = 년 & "년 " & 월 & "월"
    
    날짜설정
End Sub

Private Sub btnPrev_Click()
    If Val(월) = 1 Then
        년 = Val(년) - 1: 월 = "12"
    Else
        월 = Val(월) - 1
    End If
    
    Label1 = 년 & "년 " & 월 & "월"
    
    날짜설정
End Sub

Private Sub UserForm_Initialize()
    For i = 1 To 42
        Set cls(i).btn = Me.Controls("CommandButton" & i)
    Next
    
    Label1 = Format(Date, "yyyy년 m월")
    
    날짜설정
End Sub

Sub 날짜설정()
    
    년 = Left(Label1, 4)
    월 = Mid(Label1, 7, 2)
    
    startd = WorksheetFunction.Weekday(DateSerial(년, Val(월), 1))
    
    For i = 1 To 42
        d = DateSerial(년, Val(월), i - startd + 1)
        cls(i).btn.Caption = Day(d)
        cls(i).btn.Visible = False
        
        If month(CDate(Label1.Caption)) = month(d) Then cls(i).btn.Visible = True
    Next
End Sub
