Attribute VB_Name = "Module2"
Public flag As Boolean
Public stopTime As Date
Public selTree
Public rType

Public Sub chgImg()
    
    Debug.Print flag
    flag = Not flag

    If flag Then
        로그인.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\image\로그인\image1.jpg")
        로그인.Repaint
    Else
        로그인.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\image\로그인\image2.jpg")
        로그인.Repaint
    End If
    
    stopTime = Now + TimeValue("0:0:3")
    Application.OnTime stopTime, "chgImg"
    
End Sub

Public Sub stopchgImg()
    On Error Resume Next
    Application.OnTime stopTime, "chgImg", Schedule:=False
End Sub
