Attribute VB_Name = "Module3"
Public stopTime As Date
Public startTime As Date
Public 제한시간 As Integer
Public testableTime

Sub sTime(t)
    startTime = Now
    제한시간 = t
End Sub

Sub 시험보기_시간()
    
    stopTime = Now + TimeValue("0:0:1")
    시험보기.Label1 = Format(stopTime, "yyyy년 MM월 dd일 HH:mm:ss")
    If stopTime >= testableTime Then
        시험보기.CommandButton1.Enabled = True
    Else
        시험보기.CommandButton1.Enabled = False
    End If
 
    Application.OnTime stopTime, "시험보기_시간"
End Sub

Sub 시험_시간()
    stopTime = Now + TimeValue("0:0:1")
    If Minute(stopTime - startTime) >= 제한시간 Then eMsg "시험시간이 종료되어 답안이 제출되었습니다.": iMsg "수고하셨습니다.": 합격조회_취득내역.Show: Exit Sub
    시험.lbl시간.Caption = "경과시간 : " & Format(Minute(stopTime - startTime), "00분 ") & Format(Second(stopTime - startTime), "00초")
    'Debug.Print Format(stopTime - startTime, "MM:ss")
    Application.OnTime stopTime, "시험_시간"
End Sub

Sub 시간멈추기(modname)
    On Error Resume Next
    Application.OnTime stopTime, modname, Schedule:=False
End Sub
