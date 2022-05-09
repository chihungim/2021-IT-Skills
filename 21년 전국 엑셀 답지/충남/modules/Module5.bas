Attribute VB_Name = "Module5"
Sub 매크로1()
Attribute 매크로1.VB_ProcData.VB_Invoke_Func = " \n14"
'
' 매크로1 매크로
'

'
    
    Sheet8.Shapes.Range("예약1").Left = Sheet8.Range("C8").Left + (Sheet8.Range("C8").Left / 4)
    Sheet8.Shapes.Range("예약1").Top = Sheet8.Range("C8").Top
    
End Sub

Sub 시간테스트()
    iMsg Format(Time, "HH:mm")
End Sub

Sub 스왑테스트()
    X = 1: Y = 2
    Call swap(X, Y)
    Debug.Print X; Y
End Sub

Sub isbn테스트()
    
End Sub

Sub 자료처리초기화(clear_r As Range, paste_s As Worksheet, copy_r As Range)
    Application.ScreenUpdating = False
    If Not clear_r = "" Then clear_r.CurrentRegion.Clear

    copy_r.CurrentRegion.Copy
    paste_s.Select
    clear_r.Select
    Selection.PasteSpecial Paste:=xlPasteValuesAndNumberFormats, Operation:=xlNone, SkipBlanks _
        :=False, Transpose:=False
    Application.CutCopyMode = False
    ActiveSheet.Range("A1").Select
    
    Application.ScreenUpdating = True
End Sub

Sub 선택예약()
    For i = 1 To 5
        'Debug.Print ActiveSheet.Shapes.Range(Array("체크" & i)).Caption = ChrW(82)
    Next
End Sub
