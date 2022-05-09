VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Class2"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents imglbl0 As MSForms.Label
Attribute imglbl0.VB_VarHelpID = -1
Public WithEvents imglbl1 As MSForms.Label
Attribute imglbl1.VB_VarHelpID = -1
Public WithEvents imglbl2 As MSForms.Label
Attribute imglbl2.VB_VarHelpID = -1
Public WithEvents imglbl3 As MSForms.Label
Attribute imglbl3.VB_VarHelpID = -1

Dim dtlbl As MSForms.Label, imglbl(3) As MSForms.Label, nmlbl(3) As MSForms.Label
Dim fm As MSForms.UserForm
Dim idx, curDate, curDay

Sub init(form, i)
    Set fm = form
    idx = i
    
    aa = Weekday(DateSerial([년], [월], 1), 2) Mod 7
    curDate = DateSerial([년], [월], idx - aa + 1)
    curDay = Day(curDate)
End Sub

Sub mkUI()
    '날짜 박스
    Set dtlbl = fm.Controls.Add("Forms.Label.1")
    dtlbl.top = 60 + 72 * (idx \ 7)
    dtlbl.left = 6 + 102 * (idx Mod 7)
    dtlbl.width = 102
    dtlbl.height = 72
    dtlbl.Caption = curDay
    dtlbl.BorderStyle = fmBorderStyleSingle
    dtlbl.Visible = True
    
    '날짜 박스 속성
    dtlbl.Font.Bold = True
    dtlbl.ForeColor = vbBlack
    If Weekday(curDate, 2) Mod 7 = 0 Then dtlbl.ForeColor = vbRed
    
    If Month(curDate) <> [월] Then
        If dtlbl.ForeColor = vbRed Then
            dtlbl.ForeColor = &H8080FF
        Else
            dtlbl.ForeColor = &H80000010
        End If
    End If

    '작가명
    For i = 0 To 3
        Set nmlbl(i) = fm.Controls.Add("Forms.Label.1")
        nmlbl(i).width = 30
        nmlbl(i).height = 10
        nmlbl(i).left = dtlbl.left + 20 + (i Mod 2) * 40
        nmlbl(i).top = dtlbl.top + 2 + Int(i / 2) * 35
        nmlbl(i).Visible = True
    
        nmlbl(i).Caption = "작가명"
        nmlbl(i).Font.Size = 7
        nmlbl(i).BorderStyle = fmBorderStyleNone
    Next
    
    '이미지
    For i = 0 To 3
        Set imglbl(i) = fm.Controls.Add("Forms.Label.1")
    
        imglbl(i).width = 23
        imglbl(i).height = 23
        imglbl(i).left = dtlbl.left + 20 + (i Mod 2) * 40
        imglbl(i).top = dtlbl.top + 10 + Int(i / 2) * 35
        imglbl(i).Visible = True
        imglbl(i).BorderStyle = 1
    Next
    
    Set imglbl0 = imglbl(0)
    Set imglbl1 = imglbl(1)
    Set imglbl2 = imglbl(2)
    Set imglbl3 = imglbl(3)
    
End Sub

Sub calUpdate()
    On Error GoTo e1
    
    dtlbl.Caption = curDay
    
    dtlbl.Font.Bold = True
    dtlbl.ForeColor = vbBlack
    If Weekday(curDate, 2) Mod 7 = 0 Then dtlbl.ForeColor = vbRed
    
    If Month(curDate) <> [월] Then
        If dtlbl.ForeColor = vbRed Then
            dtlbl.ForeColor = &H8080FF
        Else
            dtlbl.ForeColor = &H80000010
        End If
    End If

    For i = 0 To 3
        nmlbl(i).Caption = ""
        Set imglbl(i).Picture = Nothing
    Next
    
    erridx = -1
    artists = Split(Mid(calData(idx), 2), ",")
    
    For i = 0 To UBound(artists) - 1 Step 2
        erridx = i
        nmlbl(Int(i / 2)).Caption = artists(i)
      
        코드 = Mid(artists(i + 1), 1, 2)
        화풍 = IIf(코드 = "IM", "인상", IIf(코드 = "RO", "낭만", IIf(코드 = "SU", "초현실", IIf(코드 = "SY", "상징", "전통")))) & "주의"
        imglbl(Int(i / 2)).Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & 화풍 & "\" & artists(i + 1) & ".jpg")
        imglbl(Int(i / 2)).name = artists(i + 1) & "," & artists(i)
        
    Next
    
    Exit Sub
e1:
    'MsgBox "파일 없음: " & ThisWorkbook.Path & "\지급자료\이미지\" & 화풍 & "\" & artists(erridx + 1) & ".jpg"
End Sub

Sub imglbl0_Click()
    
    pr = Split(imglbl0.name, ",")
    pic = imglbl0.Picture
    Call act(pr, pic)
    
End Sub


Sub imglbl1_Click()

    pr = Split(imglbl1.name, ",")
    pic = imglbl1.Picture
    Call act(pr, pic)
    
End Sub

Sub imglbl2_Click()

    pr = Split(imglbl2.name, ",")
    pic = imglbl2.Picture
    Call act(pr, pic)
    
End Sub

Sub imglbl3_Click()

    pr = Split(imglbl3.name, ",")
    pic = imglbl3.Picture
    Call act(pr, pic)
    
End Sub

Sub act(pr, pic)
    
    pNo = pr(0)

    If [분류] = "일반" Then
        If pic = 0 Then eMsg "비어있는 전시회입니다.": Exit Sub
        Unload 전시회일정
        전시회정보.Show
    Else
    
        If pic = 0 Then
            If curDate < Now() Then eMsg "오늘 이후부터 등록이 가능합니다.": Exit Sub
            mode = "add"
            전시등록및수정.Caption = "전시회 등록"
            전시등록및수정.CommandButton2.Caption = "등록"
        Else
            On Error Resume Next
            If pr(1) <> [이름] And pr(1) <> "" And [분류] = "화가" Then
                eMsg "자신의 전시회만 수정이 가능합니다."
                Exit Sub
            End If
            
            If curDate < Now() Then eMsg "이미 종료된 전시 일정입니다.": Exit Sub
            
            mode = "modi"
            전시등록및수정.Caption = "전시회 수정"
            전시등록및수정.CommandButton2.Caption = "수정"
        End If
        
        Unload 전시회일정
        전시등록및수정.Show
    End If
End Sub
