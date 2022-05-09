VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "cls버튼"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public WithEvents btn As CommandButton
Attribute btn.VB_VarHelpID = -1

Private Sub btn_Click()
    Select Case btn.Caption
        Case "로그인": 로그인.Show
        Case "로그아웃": Call 로그아웃
        Case "회원가입":
            Set Sign = 회원가입수정
            Sign.mode = "회원가입"
            Sign.Show
        Case "정보수정": 회원가입수정.Show
        Case "도서검색":
        Case "도서목록": Sheet4.Select
        Case "장바구니":
        Case "영수증보기":
        Case "구매내역":
        Case "순위":
        Case "분석": Sheet8.Select
        Case "행사": 행사이벤트.Show
        Case "종료": ThisWorkbook.Save: ThisWorkbook.Close
    
    End Select
End Sub
