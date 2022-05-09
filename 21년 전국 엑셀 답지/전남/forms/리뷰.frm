VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 리뷰 
   Caption         =   "평점등록"
   ClientHeight    =   3015
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4560
   OleObjectBlob   =   "리뷰.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "리뷰"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public lrow

Private Sub CommandButton1_Click()
    Sheet5.Range("F" & lrow) = SpinButton1.Value
    Unload Me
End Sub

Private Sub SpinButton1_Change()
    TextBox1 = SpinButton1
End Sub

Private Sub UserForm_Activate()
    Set rg = Sheet5.Range("A" & lrow)
    Call mkDic(dic도서, Sheet4.Range("B2"))
    Me.Caption = rg(1, 3)
    Label2 = dic도서(rg(1, 3).Value).Cells(1, 4)
    TextBox1 = 1
    TextBox1.Locked = True
    SpinButton1.Max = 5
End Sub

Private Sub UserForm_Click()

End Sub


