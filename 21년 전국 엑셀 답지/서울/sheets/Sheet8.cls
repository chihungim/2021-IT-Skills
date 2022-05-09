VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet8"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True



Private Sub Worksheet_Activate()
     Shapes("shp내").Visible = ([타입] = "주문자")
        
    yTop = 1: yWidth = 25
    xTop = 1: xWidth = 30
    Call init
End Sub

Sub init()
    
    Application.ScreenUpdating = False
    
    Shapes("shp예상시간").TextFrame.Characters.text = "예상시간: 0분"
    
    With Range("맵")
        .ClearContents
        .Interior.Color = vbWhite
    
    
        mkList arr회원, Sheet3.Range("C2")
        For Each v In arr회원
            n = v.Cells(1, -1)
            x = Val(Split(v, ",")(0)) + 1
            y = Val(Split(v, ",")(1)) + 1
            .Cells(y, x) = n
            With .Cells(y, x)
                If Left(n, 1) = "A" Then
                    .Interior.Color = vbGreen
                Else
                    .Interior.Color = vbRed
                End If
            End With
        Next
        
        mkList arr금지, Sheet6.Range("B2")
        
        For Each v In arr금지
            n = v.Cells(1, 0)
            x = Val(Split(v, ",")(0)) + 1
            y = Val(Split(v, ",")(1)) + 1
            .Cells(y, x) = n
            .Cells(y, x).Interior.Color = RGB(200, 200, 200)
        Next
        
    End With
    Call 영역전개
    Application.ScreenUpdating = True
End Sub

Sub 길찾기()
    
    Set 탐색경로 = New ArrayList
    Set 이전위치 = New ArrayList
    Set 경로수 = New ArrayList
    Set 최단경로 = New ArrayList
    Set 최단방향 = New ArrayList
    Set 방향 = New ArrayList
    
    Set 시작 = Range("맵").Find([번호])
    Set 끝 = Range("맵").Find([구매할놈])
    
    If 시작.Cells(1, 2) = "" And 시작.Cells(1, 2) <> 1 Then 탐색경로.add 시작.Cells(1, 2).Address: 이전위치.add -1: 경로수.add 1: 방향.add "←"
    If 시작.Cells(2, 1) = "" And 시작.Cells(2, 1) <> 1 Then 탐색경로.add 시작.Cells(2, 1).Address: 이전위치.add -1: 경로수.add 1: 방향.add "↑"
    If 시작.Cells(1, 0) = "" And 시작.Cells(1, 0) <> 1 Then 탐색경로.add 시작.Cells(1, 0).Address: 이전위치.add -1: 경로수.add 1: 방향.add "→"
    If 시작.Cells(0, 1) = "" And 시작.Cells(0, 1) <> 1 Then 탐색경로.add 시작.Cells(0, 1).Address: 이전위치.add -1: 경로수.add 1: 방향.add "↓"
    
    
'    If 탐색경로.Count = 0 Then eMsg "배달 할 수 없는 곳입니다. 직접 방문해주십시오.": Exit Sub
    deqidx = -1
    Do While 1
        deqidx = deqidx + 1
        Set 현재 = Range(탐색경로(deqidx))
        pnt = 경로수(deqidx) + 1
        
        
        If 현재.Cells(1, 2) = 끝 Or 현재.Cells(2, 1) = 끝 Or 현재.Cells(1, 0) = 끝 Or 현재.Cells(0, 1) = 끝 Then Exit Do
        If 현재.Cells(1, 2) = "" And 현재.Cells(1, 2) <> 1 And Not 탐색경로.Contains(현재.Cells(1, 2).Address) Then 탐색경로.add 현재.Cells(1, 2).Address: 이전위치.add deqidx: 경로수.add pcnt: 방향.add "←"
        If 현재.Cells(2, 1) = "" And 현재.Cells(2, 1) <> 1 And Not 탐색경로.Contains(현재.Cells(2, 1).Address) Then 탐색경로.add 현재.Cells(2, 1).Address: 이전위치.add deqidx: 경로수.add pcnt: 방향.add "↑"
        If 현재.Cells(1, 0) = "" And 현재.Cells(1, 0) <> 1 And Not 탐색경로.Contains(현재.Cells(1, 0).Address) Then 탐색경로.add 현재.Cells(1, 0).Address: 이전위치.add deqidx: 경로수.add pcnt: 방향.add "→"
        If 현재.Cells(0, 1) = "" And 현재.Cells(0, 1) <> 1 And Not 탐색경로.Contains(현재.Cells(0, 1).Address) Then 탐색경로.add 현재.Cells(0, 1).Address: 이전위치.add deqidx: 경로수.add pcnt: 방향.add "↓"
        If deqidx > 100000 Then eMsg "배달 할 수 없는 곳입니다. 직접 방문해주십시오": Exit Sub
    Loop
    
    idx = deqidx
    
    Do While 1
        최단경로.add Range(탐색경로(idx))
        최단방향.add 방향(idx)
        idx = 이전위치(idx)
        If idx = -1 Then Exit Do
    Loop
    
    For i = 0 To 최단경로.Count - 1
        최단경로(i).Interior.Color = vbYellow
        최단경로(i).Value = 최단방향(i)
    Next
    
     Shapes("shp예상시간").TextFrame.Characters.text = "예상시간: " & Round((최단경로.Count - 2) / 3, 0) & "분"
    
End Sub

Sub test()
    Call 길찾기
End Sub


Sub 영역전개()
    With Range("맵")
        .Rows.Hidden = True
        .Columns.Hidden = True
        .Range(Cells(1, xTop), Cells(1, xWidth)).Columns.Hidden = False
        .Range(Cells(yTop, 1), Cells(yWidth, 1)).Rows.Hidden = False
    End With
End Sub

Sub w()
    If yTop - 1 > 0 Then
        yWidth = yWidth - 1
        yTop = yTop - 1
        Call 영역전개
    End If
End Sub

Sub a()
    If xTop - 1 > 0 Then
        xTop = xTop - 1
        xWidth = xWidth - 1
        Call 영역전개
    End If
End Sub

Sub s()
    If yWidth + 1 < 100 Then
        yWidth = yWidth + 1
        yTop = yTop + 1
        Call 영역전개
    End If
End Sub
Sub d()
    If xWidth + 1 < 100 Then
        xTop = xTop + 1
        xWidth = xWidth + 1
        Call 영역전개
    End If
End Sub

Private Sub Worksheet_BeforeDoubleClick(ByVal Target As Range, Cancel As Boolean)
    Set isIn = Intersect(Range("맵"), Target)
    If Not isIn Is Nothing Then
        If [타입] <> "주문자" Then eMsg "구매가 불가능한 계정입니다.": Exit Sub
        If Left(Target.Value, 1) <> "Z" Then eMsg "매장이 아닙니다.": Exit Sub
        [구매할놈] = Target.Value
        
        mkDic dic회원, Sheet3.Range("A2")
        
        If Range("A2") >= 1 Then eMsg "오늘 이미 구매하신 가계입니다.": Exit Sub
        
         
        With 구매
            .Caption = dic회원([구매할놈].Value).Cells(1, 2)
            .Show
        End With
    End If
End Sub

Private Sub Worksheet_BeforeRightClick(ByVal Target As Range, Cancel As Boolean)
    For i = 1 To Application.CommandBars("cell").Controls.Count
        Application.CommandBars("cell").Controls(1).Delete
    Next
    
    Set isIn = Intersect(Range("맵"), Target)
    If isIn Is Nothing Then
        Application.CommandBars("cell").Reset: Exit Sub
    End If
    
    If [타입] = "주문자" Then
        If Target.Interior.Color <> vbRed Then Application.CommandBars("Cell").Reset
        Set btn = Application.CommandBars("Cell").Controls.add(msoControlButton)
        With btn
            .Caption = "리뷰 쓰기"
            .FaceId = 201
            .OnAction = "'리뷰 """ & Target.Value & """'"
        End With
        
    ElseIf [타입] = "관리자" Then
    
    
    End If
End Sub
