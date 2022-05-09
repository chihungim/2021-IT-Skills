VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 검색 
   Caption         =   "검색"
   ClientHeight    =   9255
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10185
   OleObjectBlob   =   "검색.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "검색"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(200) As Class1

Private Sub ComboBox1_Change()
    If ComboBox1.ListIndex = 0 Then
        Sheet3.Range("AH2") = ""
    Else
        Sheet3.Range("AH2") = ComboBox1
    End If
End Sub



Private Sub CommandButton1_Click()
    
    Sheet3.Range("AI2:AJ2") = Array(TextBox1, TextBox2)
    Sheet3.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet3.Range("AH1:AJ2"), Sheet3.Range("AL1:AT1"), False
    
    On Error Resume Next
    For i = 0 To 200
        Me.Controls.Remove cls(i).img.name
        Me.Controls.Remove cls(i).lbl.name
    Next
    
    setList
End Sub

Sub setList()
    t = 0: l = 0: w = 90: h = 60
    irow = Sheet3.Range("AL1000").End(xlUp).Row - 2
    If irow < 0 Then iMsg "검색결과가 없습니다."
    
    For i = 0 To irow
        Set cls(i) = New Class1
        ft = (t + 55) + (h + 40) * Int(i / 5): fl = l + (w + 5) * (i Mod 5): fw = w: fh = 40
        labelText = vbCrLf & Sheet3.Range("AN" & i + 2) & vbCrLf & vbCrLf & Sheet3.Range("AO" & i + 2)
        
        With Sheet3
            imageName = .Range("AL" & i + 2) & "," & .Range("AM" & i + 2) & "," & .Range("AN" & i + 2) & "," & .Range("AO" & i + 2) & "," & .Range("AQ" & i + 2) & "," & .Range("AS" & i + 2) & "," & .Range("AT" & i + 2)
        End With

        Call cls(i).imgInit(MultiPage1.Pages(0), imageName, ft - 50, fl, fw, fh + 10)
        Call cls(i).labelInit(MultiPage1.Pages(0), labelText, ft, fl, fw, fh)
    Next

    max = (Int(irow / 5) + 1) * 100
    
    If max <= 400 Then
        MultiPage1.Pages(0).ScrollBars = 0
    Else
        MultiPage1.Pages(0).ScrollBars = 2
        MultiPage1.Pages(0).ScrollHeight = max
    End If
    
End Sub


Private Sub MultiPage1_Change()

End Sub

Private Sub UserForm_Initialize()
    ComboBox1.List = Array("전체", "낭만주의", "상징주의", "인상주의", "전통주의", "초현실주의")
    ComboBox1.ListIndex = 0
    MultiPage1.Style = fmTabStyleNone
    
End Sub


