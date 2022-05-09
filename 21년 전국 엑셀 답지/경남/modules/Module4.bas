Attribute VB_Name = "Module4"
Sub 매크로1()
Attribute 매크로1.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로1 매크로
'

'
    Range("표3[[#Headers],[회차]]").Select
    Selection.AutoFilter
    ActiveSheet.ListObjects("표3").Range.AutoFilter Field:=2, Criteria1:=Array( _
        "1", "2", "3"), Operator:=xlFilterValues
End Sub
