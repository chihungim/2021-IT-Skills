VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 송금 
   Caption         =   "송금"
   ClientHeight    =   7080
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9015
   OleObjectBlob   =   "송금.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "송금"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public state As Boolean
Private Sub CommandButton1_Click()
    
    search TextBox1
    
End Sub

Sub search(txt)
    
    For i = 0 To 2
        With Sheet1
            .Cells(2 + i, 12 + i) = "*" & txt & "*"
            .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("L1:N4"), .Range("P1:Q1")
            ListBox1.RowSource = "회원목록!P2:Q" & Sheet1.Range("A1000").End(3).Row
        End With
    Next
    
End Sub

Private Sub CommandButton2_Click()
    If lbl이름 = "" Or lbl계좌번호 = "" Then eMsg "송금할 회원을 선택해야 합니다.": Exit Sub
    
    If TextBox2 = "" Then eMsg "송금할 금액을 입력해야 합니다.": Exit Sub
    
    If Not IsNumeric(TextBox2) Then eMsg "금액을 숫자로만 입력해야 합니다.": Exit Sub
    
    If lbl이름.Caption = [이름] Then eMsg "자기 자신에게 돈을 보낼 수 없습니다.": Exit Sub
    
    If [잔액] < Val(TextBox2) Then eMsg "잔액이 부족합니다.": Exit Sub
    
    pw = ""
    비밀번호.Show
    
    If Not 비밀번호flag Then eMsg "2차 비밀번호가 일치하지 않습니다.": Exit Sub
    
    If MsgBox(lbl이름 & "님에게 정말 송금하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
        iMsg "송금이 완료되었습니다."
        mkdic dic, Sheet1.Range("D2")
        no = dic(송금.lbl이름.Caption).Cells(1, -2)
                
        Sheet1.Cells([번호] + 1, "J") = Int(Sheet1.Cells([번호] + 1, "J")) - Int(송금.TextBox2)
        Sheet1.Cells(no + 1, "J") = Int(Sheet1.Cells(no + 1, "J")) + Int(송금.TextBox2)
               
               
        With Sheet6
            lrow = .Range("A1000").End(3).Row + 1
            .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, Now, [번호], [계좌], lbl계좌번호, "", "", Val(TextBox2), 1, 1)
        End With
             
        Unload Me
    End If
    
End Sub

Private Sub ListBox1_Click()
    lbl이름 = ListBox1.list(ListBox1.ListIndex, 0)
    lbl계좌번호 = ListBox1.list(ListBox1.ListIndex, 1)
End Sub

Private Sub UserForm_Initialize()
    
    search ("")
    state = False
    
End Sub

