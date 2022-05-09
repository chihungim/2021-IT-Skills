Attribute VB_Name = "메인모듈"
Sub 관리자로그인()
    
    With Sheet1
        For i = 1 To 5
            .Shapes("shp_" & i).TextFrame.Characters.Font.Color = vbBlack
            .Shapes("shp_" & i).Fill.ForeColor.ObjectThemeColor = msoThemeColorText2
        Next
    
        .Shapes("shp_1").TextFrame.Characters.Text = "로그아웃"
        .Shapes("shp_2").TextFrame.Characters.Text = "패키지 생성"
        .Shapes("shp_3").TextFrame.Characters.Text = "쿠폰 관리"
        .Shapes("shp_4").TextFrame.Characters.Text = "종 료"
        .Shapes("shp_4").Visible = msoTrue
        .Shapes("shp_5").Visible = msoFalse
    End With
End Sub

Sub 유저로그인()
    With Sheet1
        For i = 1 To 5
            .Shapes("shp_" & i).TextFrame.Characters.Font.Color = vbBlack
            .Shapes("shp_" & i).Fill.ForeColor.ObjectThemeColor = msoThemeColorAccent4
        Next
        .Shapes("shp_1").TextFrame.Characters.Text = "로그아웃"
        .Shapes("shp_2").TextFrame.Characters.Text = "예약하기"
        .Shapes("shp_3").TextFrame.Characters.Text = "쿠폰 시트"
        .Shapes("shp_4").TextFrame.Characters.Text = "리뷰 확인"
        .Shapes("shp_5").TextFrame.Characters.Text = "종 료"
        .Shapes("shp_4").Visible = msoTrue
        .Shapes("shp_5").Visible = msoTrue
    End With
End Sub

Sub 로그아웃()
    [번호] = ""
    With Sheet1
        For i = 1 To 5
            .Shapes("shp_" & i).TextFrame.Characters.Font.Color = vbWhite
            .Shapes("shp_" & i).Fill.ForeColor.ObjectThemeColor = msoThemeColorAccent5
        Next
        .Shapes("shp_1").TextFrame.Characters.Text = "로그인"
        .Shapes("shp_2").TextFrame.Characters.Text = "회원가입"
        .Shapes("shp_3").TextFrame.Characters.Text = "종 료"
        .Shapes("shp_4").Visible = msoFalse
        .Shapes("shp_5").Visible = msoFalse
    End With
End Sub

Sub shp1_Click()
    With Sheet1
        If .Shapes("shp_1").TextFrame.Characters.Text = "로그인" Then
            로그인.Show
        Else
            로그아웃
        End If
    End With
End Sub
Sub shp2_Click()
    With Sheet1
        If .Shapes("shp_2").TextFrame.Characters.Text = "회원가입" Then
            회원가입.Show
        ElseIf .Shapes("shp_2").TextFrame.Characters.Text = "예약하기" Then
            검색예약.Show
        Else
            패키지생성.Show
        End If
    End With
End Sub
Sub shp3_Click()
    With Sheet1
        If .Shapes("shp_3").TextFrame.Characters.Text = "종 료" Then
            ThisWorkbook.Save
            ThisWorkbook.Close
        ElseIf .Shapes("shp_3").TextFrame.Characters.Text = "쿠폰 시트" Then
            Sheet9.Select
        Else
            Sheet9.Select
        End If
    End With
End Sub
Sub shp4_Click()
    With Sheet1
        If .Shapes("shp_4").TextFrame.Characters.Text = "리뷰 확인" Then
            Sheet8.Select
        Else
            ThisWorkbook.Save
            ThisWorkbook.Close
        End If
    End With
End Sub
Sub shp5_Click()
    With Sheet1
        ThisWorkbook.Save
        ThisWorkbook.Close
    End With
End Sub
