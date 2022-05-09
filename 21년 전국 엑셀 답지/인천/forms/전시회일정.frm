VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 전시회일정 
   Caption         =   "전시회 일정"
   ClientHeight    =   9300
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   14505
   OleObjectBlob   =   "전시회일정.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "전시회일정"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim evt(42) As Class2

Private Sub CommandButton1_Click()

    [년] = Year(DateSerial([년], [월] - 1, 1))
    [월] = Month(DateSerial([년], [월] - 1, 1))
    Label1 = [년] & "년 " & [월] & "월"
    
    Call update

End Sub

Private Sub CommandButton2_Click()

    [년] = Year(DateSerial([년], [월] + 1, 1))
    [월] = Month(DateSerial([년], [월] + 1, 1))
    Label1 = [년] & "년 " & [월] & "월"
    
    Call update

End Sub

Private Sub CommandButton3_Click()

    Unload Me

End Sub


Private Sub UserForm_Initialize()

    [년] = 2021: [월] = 9
'
    For i = 0 To 41

        Set evt(i) = New Class2
        evt(i).init Me, i
        evt(i).mkUI
    Next
    
    Call update
    
End Sub

Sub update()
    
    For i = 0 To 41
        calData(i) = ""
    Next
    
    S = Weekday(DateSerial([년], [월], 1), 2) Mod 7
    
    For Each rg In [표1[시작일]]
        For i = rg.Cells(1, 1) To rg.Cells(1, 2)
            stSerial = DateSerial([년], [월], 1 - S)
            edSerial = DateSerial([년], [월], 1 - S + 41)
            If i >= stSerial And i <= edSerial Then
                calData(i - stSerial) = calData(i - stSerial) & "," & rg.Cells(1, 3) & "," & rg.Cells(1, 0)
            End If
        Next
    Next
    
    For i = 0 To 41
        evt(i).init Me, i
        evt(i).calUpdate
    Next

End Sub

