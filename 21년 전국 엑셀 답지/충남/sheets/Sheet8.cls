VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet8"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Dim 화살표, 시작행, 끝행, 시작열, 끝열, pcNum
Dim 화살표시작 As Range, 화살표끝 As Range

Private Sub Worksheet_Activate()
    Application.ScreenUpdating = False
    For i = 1 To 10
        Range("_" & Format(i, "000")).ClearContents
    Next
    
    Shapes("예약1").Visible = False
    화살표 = ""
    TextBox1 = "": TextBox2 = ""
    
    Range("CM9").Calculate
    Application.ScreenUpdating = True
End Sub

Sub 취소하기_Click()
    Worksheet_Activate
End Sub

Private Sub Worksheet_SelectionChange(ByVal Target As Range)
    
    If Target.Count <> 1 Then Exit Sub
    
    If Target.Value Like "PC*" Then
        Application.ScreenUpdating = False
        Worksheet_Activate
        pcNum = Val(Right(Target.Value, 3))
        
        Shapes.Range("예약1").Left = Target.Cells(3, 2).Left + (Target.Cells(3, 2).Left / 4)
        Shapes.Range("예약1").Top = Target.Cells(3, 2).Top
        Shapes("예약1").Visible = True
        Application.ScreenUpdating = True
    End If
    
    If Range("B" & Target.row) Like "PC*" Then
        If 화살표 = "" Then
            
            시작행 = WorksheetFunction.Match(Range("B" & Target.row).Value, Range("CK15:CK24"), 0)
            시작열 = Cells(3, Target.column)
            If [예약테이블].Cells(시작행, 시작열) = 0 Then
                화살표 = 1
                Set 화살표시작 = Target.Cells(0, 1)
                Target.Cells(0, 1) = 1
                TextBox1 = 시간포맷([예약테이블].Cells(-2, 시작열))
            End If
            
        ElseIf 화살표 = 1 And pcNum = Val(Right(Range("B" & Target.row), 3)) Then
            
            끝행 = WorksheetFunction.Match(Range("B" & Target.row).Value, Range("CK15:CK24"), 0)
            끝열 = Cells(3, Target.column)
            
            For Each rg In Range([예약테이블].Cells(시작행, 시작열), [예약테이블].Cells(끝행, 끝열 + 1))
                If rg.Value = 3 Then eMsg "해당 구간은 선택이 불가능합니다.": 초기화_Click: Exit Sub
            Next
            
            If [예약테이블].Cells(끝행, 끝열) = 0 Then
                Set 화살표끝 = Target.Cells(0, 1)
                Target.Cells(0, 1) = -1
                화살표 = -1
                If 시작열 > 끝열 Then
                    화살표시작 = -1
                    화살표끝 = 1
                    Call swap(시작행, 끝행)
                    Call swap(시작열, 끝열)
                End If
                
                
                TextBox1 = 시간포맷([예약테이블].Cells(-2, 시작열)) & " ~ " & 시간포맷([예약테이블].Cells(-2, 끝열 + 1))
                시간 = 시간포맷([예약테이블].Cells(-2, 끝열 + 1) - [예약테이블].Cells(-2, 시작열))
                TextBox2 = Hour(시간) * 60 + Minute(시간) & "분"
            End If
        Else
            시작행 = WorksheetFunction.Match(Range("B" & Target.row).Value, Range("CK15:CK24"), 0)
            시작열 = Cells(3, Target.column)
            If [예약테이블].Cells(시작행, 시작열) = 0 Then
                화살표 = 1
                Target.Cells(0, 1) = 1
                TextBox1 = 시간포맷([예약테이블].Cells(-2, 시작열))
            End If
        End If
    End If
    

'    If 화살표 = "" Then
'        시작행 = Target.Row: 시작열 = Target.Column
'        If Range("B" & 시작행) Like "PC*" Then
'            화살표 = 1
'            Target.Cells(0, 1) = 1
'        End If
'    ElseIf 화살표 = 1 Then
'        끝행 = Target.Row: 끝열 = Target.Column
'        If Range("B" & 시작행) = Range("B" & 끝행) Then
'
'        End If
'    Else
'
'    End If
    
End Sub

Sub 예약(plusTime)
    For i = 1 To 10
        Range("_" & Format(i, "000")).ClearContents
    Next
    
    '현재시간 10분 단위로 환산
    t = [CM9]
    현재 = TimeSerial(Hour(t), Int(Minute(t) / 10) * 10, 0) * 1
    
    '목표위치 계산
    If 현재 + plusTime >= TimeSerial(18, 0, 0) Then
        toTime = 54
    Else
        tmp = (현재 + plusTime) - TimeSerial(9, 0, 0)
        toTime = Hour(tmp) * 6 + Minute(tmp) / 10
    End If
    
    sidx = Evaluate("=MATCH(" & 현재 & ", $CL$12:$EN$12,0)")
    'sidx = WorksheetFunction.Match(현재.Value, Range("CL12:EN12"), 0)
    
    If Range("예약테이블").Cells(pcNum, sidx) = 0 Then
        For i = sidx To toTime
            If Range("예약테이블").Cells(pcNum, i) <> 0 Then Exit For
            eidx = i
        Next
    End If
    
    TextBox1 = 시간포맷([예약테이블].Cells(-2, sidx)) & " ~ " & 시간포맷([예약테이블].Cells(-2, eidx + 1))
    시간 = [예약테이블].Cells(-2, eidx + 1) - [예약테이블].Cells(-2, sidx)
    TextBox2 = Hour(시간) * 60 + Minute(시간) & "분"
    
    Debug.Print sidx; eidx; pcNum; sidx + Int((sidx - 1) / 6); eidx + Int((eidx - 1) / 6)
    Range("_" & Format(pcNum, "000")).Cells(1, sidx + Int((sidx - 1) / 6)) = 1
    Range("_" & Format(pcNum, "000")).Cells(1, eidx + Int((eidx - 1) / 6)) = -1
    
    
End Sub

Sub 초기화_Click()
    For i = 1 To 10
        Range("_" & Format(i, "000")).ClearContents
    Next
    
    TextBox1 = "": TextBox2 = ""
End Sub

Sub cmd30분_Click()
    Call 예약(TimeSerial(0, 30, 0) * 1)
End Sub

Sub cmd1시간_Click()
    Call 예약(TimeSerial(1, 0, 0) * 1)
End Sub

Sub cmd2시간_Click()
    Call 예약(TimeSerial(2, 0, 0) * 1)
End Sub

Sub cmd3시간_Click()
    Call 예약(TimeSerial(3, 0, 0) * 1)
End Sub

Sub cmd4시간_Click()
    Call 예약(TimeSerial(4, 0, 0) * 1)
End Sub

Sub cmd5시간_Click()
    Call 예약(TimeSerial(5, 0, 0) * 1)
End Sub

Sub 즉시예약_Click()
    Call 예약(TimeSerial(18, 0, 0) * 1)
End Sub

Sub 예약하기_Click()
    If Val(TextBox2) > 300 Then eMsg "1일 최대 예약시간을 초과하였습니다.": Exit Sub
    
    iMsg "예약이 완료되었습니다."
    With Sheet5
        lrow = .Range("A1000").End(3).row + 1
        .Range("A" & lrow & ":E" & lrow) = Array([아이디], "PC" & Format(pcNum, "000"), 시간포맷([예약테이블].Cells(-2, 시작열)), TextBox2, "")
    End With
    
End Sub

Function 시간포맷(t)
    시간포맷 = Format(t, "HH:mm")
End Function
