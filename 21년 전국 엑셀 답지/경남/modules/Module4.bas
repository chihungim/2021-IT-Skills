Attribute VB_Name = "Module4"
Sub ��ũ��1()
Attribute ��ũ��1.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��1 ��ũ��
'

'
    Range("ǥ3[[#Headers],[ȸ��]]").Select
    Selection.AutoFilter
    ActiveSheet.ListObjects("ǥ3").Range.AutoFilter Field:=2, Criteria1:=Array( _
        "1", "2", "3"), Operator:=xlFilterValues
End Sub
