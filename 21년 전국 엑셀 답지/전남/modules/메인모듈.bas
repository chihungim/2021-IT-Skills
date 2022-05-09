Attribute VB_Name = "메인모듈"
Sub 유저로그인()
    With Sheet1
        .CommandButton1.Caption = "로그아웃"
        .CommandButton2.Caption = "정보수정"
        .CommandButton3.Enabled = True
        .CommandButton4.Enabled = True
        .CommandButton5.Enabled = True
        .CommandButton6.Enabled = True
    End With
End Sub


Sub 로그아웃()
    [번호] = ""
    With Sheet1
        .CommandButton1.Caption = "로그인"
        .CommandButton2.Caption = "회원가입"
        .CommandButton3.Caption = "도서검색"
        .CommandButton4.Caption = "장바구니"
        .CommandButton5.Caption = "영수증보기"
        .CommandButton6.Caption = "구매내역"
        .CommandButton7.Visible = True
        .CommandButton8.Visible = True
        .CommandButton3.Enabled = False
        .CommandButton4.Enabled = False
        .CommandButton5.Enabled = False
        .CommandButton6.Enabled = False
        .Range("B14:G14").Font.Color = vbBlack
    End With
End Sub

Sub 관리자로그인()
    [번호] = "관"
    With Sheet1
        .CommandButton1.Caption = "로그아웃"
        .CommandButton2.Caption = "회원정보"
        .CommandButton3.Caption = "도서목록"
        .CommandButton4.Caption = "분석"
        .CommandButton5.Caption = "행사"
        .CommandButton6.Caption = "종료"
        .CommandButton7.Visible = False
        .CommandButton8.Visible = False
        .CommandButton3.Enabled = True
        .CommandButton4.Enabled = True
        .CommandButton5.Enabled = True
        .CommandButton6.Enabled = True
        .Range("B14:G14").Font.Color = vbWhite
    End With
End Sub
