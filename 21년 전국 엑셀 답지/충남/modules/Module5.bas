Attribute VB_Name = "Module5"
Sub ��ũ��1()
Attribute ��ũ��1.VB_ProcData.VB_Invoke_Func = " \n14"
'
' ��ũ��1 ��ũ��
'

'
    
    Sheet8.Shapes.Range("����1").Left = Sheet8.Range("C8").Left + (Sheet8.Range("C8").Left / 4)
    Sheet8.Shapes.Range("����1").Top = Sheet8.Range("C8").Top
    
End Sub

Sub �ð��׽�Ʈ()
    iMsg Format(Time, "HH:mm")
End Sub

Sub �����׽�Ʈ()
    X = 1: Y = 2
    Call swap(X, Y)
    Debug.Print X; Y
End Sub

Sub isbn�׽�Ʈ()
    
End Sub

Sub �ڷ�ó���ʱ�ȭ(clear_r As Range, paste_s As Worksheet, copy_r As Range)
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

Sub ���ÿ���()
    For i = 1 To 5
        'Debug.Print ActiveSheet.Shapes.Range(Array("üũ" & i)).Caption = ChrW(82)
    Next
End Sub
