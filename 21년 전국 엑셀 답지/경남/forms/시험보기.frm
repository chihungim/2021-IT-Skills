VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 시험보기 
   Caption         =   "UserForm1"
   ClientHeight    =   2190
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10815
   OleObjectBlob   =   "시험보기.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "시험보기"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim r

Private Sub CommandButton1_Click()
    [시험종목번호] = r - 1
    
End Sub

Private Sub UserForm_Activate()
    With Sheet5
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("B" & i) = [번호] And .Range("G" & i) = "" Then
                r = i
                Exit For
            End If
        Next
        
        Label1 = Format(Now, "yyyy년 MM월 dd일 HH:mm:ss")
        Label2 = .Range("I" & r) & " / " & .Range("E" & r) & " / " & Format(.Range("K" & r), "h:mm")
        testableTime = DateValue(Sheet5.Range("L" & r)) + TimeValue(Sheet5.Range("L" & r)) - TimeValue("00:10:00")
        
        If Now > DateValue(Sheet5.Range("L" & r)) + TimeValue(Sheet5.Range("L" & r)) + TimeValue("0:" & Val(Sheet5.Range("M" & r)) & ":0") Then
            eMsg "시험 시간이 지나 결시 처리 되었습니다."
            Unload 시험보기
        Else
            Call sTime(Val(.Range("M" & r)))
            Call 시험보기_시간
        End If
 
        
    End With
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()

    

    
    
    
    
End Sub

Private Sub UserForm_Terminate()
    Call 시간멈추기("시험보기_시간")
End Sub
