VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet7"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Private Sub Worksheet_Activate()


    

    ListObjects("표5").Range.AutoFilter Field:=6, Criteria1:=[나이대]
    ListObjects("표5").Range.AutoFilter Field:=4, Criteria1:="<=" & Date
    ListObjects("표5").Range.AutoFilter Field:=5, Criteria1:=">=" & Date
    
End Sub

