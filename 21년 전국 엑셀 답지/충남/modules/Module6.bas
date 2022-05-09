Attribute VB_Name = "Module6"
Sub 매크로2()
Attribute 매크로2.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로2 매크로
'

'
    ActiveSheet.Shapes.Range(Array("검색1")).Select
    ActiveSheet.Shapes.Range(Array("체크1")).Select
End Sub
Sub 매크로3()
Attribute 매크로3.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로3 매크로
'

'
    Range("B22").Select
    Selection.End(xlToLeft).Select
    Selection.End(xlUp).Select
    Range("표3").Select
    Range("A350").Activate
    Selection.Copy
    Sheets("자료처리").Select
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
Sub 매크로7()
Attribute 매크로7.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로7 매크로
'

'
    Sheets("bookrental").Select
    Range("표3").Select
    Range("A320").Activate
    Selection.Copy
    Sheets("자료처리").Select
    Range("P1").Select
    Selection.PasteSpecial Paste:=xlPasteValuesAndNumberFormats, Operation:= _
        xlNone, SkipBlanks:=False, Transpose:=False
End Sub
