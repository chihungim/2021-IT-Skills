Attribute VB_Name = "로그인모듈"

Sub shp로그인_Click()

    With Sheet7
        If [로그인] = "로그인" Then
        
            If .TextBox1 = "" Or .TextBox2 = "" Then eMsg "아이디와 비밀번호가 일치하지 않습니다.": Exit Sub
        
            If .TextBox1 = "admin" And .TextBox2 = "1234" Then
                iMsg "관리자님, 안녕하세요."
                시트초기화
                Sheet12.Visible = xlSheetVisible
                Sheet12.Select
                Exit Sub
            End If
            
            Set id_dic = mkdic(Sheet1.Range("B2"))
            If Not id_dic.exists(.TextBox1.Value) Then
                eMsg "아이디 또는 비밀번호가 일치하지 않습니다.": Exit Sub
                
            Else
                If id_dic(.TextBox1.Value).Cells(1, 2) <> .TextBox2.Value Then eMsg "아이디 또는 비밀번호가 일치하지 않습니다.": Exit Sub
            End If
            
            iMsg id_dic(.TextBox1.Value).Cells(1, 3) & "님, 안녕하세요."
            
            Sheet13.Range("A1") = id_dic(.TextBox1.Value).Cells(1, 0)
            .TextBox1 = ""
            .TextBox2 = ""
            .Shapes("그룹 5").Visible = msoFalse
            .Shapes("그림 46").Visible = msoTrue
            
            시트초기화
            Sheet7.Select
            Sheet8.Visible = xlSheetVisible
            Sheet9.Visible = xlSheetVisible
            Sheet10.Visible = xlSheetVisible
            Sheet11.Visible = xlSheetVisible

        Else
            [번호] = ""
            .Shapes("그룹 5").Visible = msoTrue
            .Shapes("그림 46").Visible = msoFalse
            시트초기화
            Sheet7.Select
        End If
    End With
End Sub

Sub 신찾()
     If 번호 = "" Then
        eMsg "로그인 후 이용가능합니다."
        Exit Sub
    End If

    Sheet8.Select
End Sub
Sub 쇼핑()
    If 번호 = "" Then
        eMsg "로그인 후 이용가능합니다."
        Exit Sub
    End If
    구매.Show
End Sub
Sub 카드신()
    If 번호 = "" Then
        eMsg "로그인 후 이용가능합니다."
        Exit Sub
    End If
    카드신청.Show
End Sub

Sub 계정만들기()
    If [번호] <> "" Then
        eMsg "이미 Chase회원입니다."
        Exit Sub
    End If
    
    회원가입.Show
End Sub
