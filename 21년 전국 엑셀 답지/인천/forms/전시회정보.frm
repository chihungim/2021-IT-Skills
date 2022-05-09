VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 전시회정보 
   Caption         =   "전시회 정보"
   ClientHeight    =   7950
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   5250
   OleObjectBlob   =   "전시회정보.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "전시회정보"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim selDate

Private Sub CommandButton1_Click()
    Unload Me
    전시회일정.Show

End Sub
Private Sub UserForm_Initialize()
    사전생성 Sheet3.Range("A2")
    
    
    With 사전(pNo)
        Me.작품명 = .Cells(1, 3)
        Me.분류 = .Cells(1, 2)
        Me.화가 = .Cells(1, 4)
        Me.설명 = .Cells(1, 6)
        Me.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & Me.분류 & "\" & pNo & ".jpg")
    End With

End Sub

