VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ��� 
   Caption         =   "����̺�Ʈ"
   ClientHeight    =   3735
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   8415
   OleObjectBlob   =   "���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub CommandButton1_Click()

End Sub

Private Sub UserForm_Initialize()
    ImageList1.ImageHeight = 50
    ImageList1.ImageWidth = 50
    
    Call mkDic(dic���, Sheet7.Range("C2"))
    
    For Each k In dic���.Keys
        ImageList1.ListImages.Add , k, LoadPicture(ThisWorkbook.Path & "\�����ڷ�\����ǰ\" & k & ".jpg")
    Next
    
    With ListView1
        .View = lvwReport
        .SelectedItem = Nothing
        .MultiSelect = False
        .Gridlines = True
        
        With .ColumnHeaders
            .Add , , "", 60
            .Add , , "�����ڵ�", 60
            .Add , , "����ǰ", 60
            .Add , , "������", 60
            .Add , , "������", 60
            .Add , , "�������", 60
            
        End With
            
        .SmallIcons = ImageList1
    End With
    
    
    For Each arr In arr���
        With ListView1.ListItems.Add(, , "", , arr.Cells(1, 3))
            .SubItems(1) = arr.Cells(1, 2)
            .SubItems(2) = arr.Cells(1, 3)
            .SubItems(3) = arr.Cells(1, 4)
            .SubItems(4) = arr.Cells(1, 5)
            .SubItems(4) = arr.Cells(1, 6)
        End With
    Next
    
    
End Sub
