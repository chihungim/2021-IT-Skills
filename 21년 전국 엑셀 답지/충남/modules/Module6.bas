Attribute VB_Name = "Module6"
Sub ��ũ��2()
Attribute ��ũ��2.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��2 ��ũ��
'

'
    ActiveSheet.Shapes.Range(Array("�˻�1")).Select
    ActiveSheet.Shapes.Range(Array("üũ1")).Select
End Sub
Sub ��ũ��3()
Attribute ��ũ��3.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��3 ��ũ��
'

'
    Range("B22").Select
    Selection.End(xlToLeft).Select
    Selection.End(xlUp).Select
    Range("ǥ3").Select
    Range("A350").Activate
    Selection.Copy
    Sheets("�ڷ�ó��").Select
    Range("Q1").Select
    Selection
    ActiveWindow.ScrollColumn = 2
    ActiveWindow.ScrollColumn = 3
    ActiveWindow.ScrollColumn = 4
    ActiveWindow.ScrollColumn = 5
    ActiveWindow.ScrollColumn = 6
    ActiveWindow.ScrollColumn = 7
    ActiveWindow.ScrollColumn = 8
    ActiveWindow.ScrollColumn = 9
End Sub
Sub ��ũ��7()
Attribute ��ũ��7.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��7 ��ũ��
'

'
    Sheets("bookrental").Select
    Range("ǥ3").Select
    Range("A320").Activate
    Selection.Copy
    Sheets("�ڷ�ó��").Select
    Range("P1").Select
    Selection.PasteSpecial Paste:=xlPasteValuesAndNumberFormats, Operation:= _
        xlNone, SkipBlanks:=False, Transpose:=False
End Sub
