Attribute VB_Name = "Module2"
Public flag As Boolean
Public stopTime As Date
Public selTree
Public rType

Public Sub chgImg()
    
    Debug.Print flag
    flag = Not flag

    If flag Then
        �α���.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\image\�α���\image1.jpg")
        �α���.Repaint
    Else
        �α���.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\image\�α���\image2.jpg")
        �α���.Repaint
    End If
    
    stopTime = Now + TimeValue("0:0:3")
    Application.OnTime stopTime, "chgImg"
    
End Sub

Public Sub stopchgImg()
    On Error Resume Next
    Application.OnTime stopTime, "chgImg", Schedule:=False
End Sub
