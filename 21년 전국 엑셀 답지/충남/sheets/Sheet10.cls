VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet10"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True

Private Sub ComboBox1_Change()
    Range("J1, K1") = ComboBox1.Value
End Sub

Private Sub Worksheet_Activate()
    mkdic dic책, Sheet3.[a2]
    ComboBox1.Clear
    ComboBox1.List = Split("대출일,반납일", ",")
End Sub

Private Sub Worksheet_BeforeDoubleClick(ByVal Target As Range, Cancel As Boolean)
    With Sheet10
        If .Cells(Target.row, Target.column) = "`" Then
            If cMsg("반납 연기하시겠습니까?(반납예정일 : " & Format(.Cells(Target.row, Target.column - 2) + 7, "yyyy-mm-dd") & ")") = vbYes Then
                .Cells(Target.row, Target.column - 2) = .Cells(Target.row, Target.column - 2) + 7
            End If
        End If
    End With
End Sub
