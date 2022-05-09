VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 합격조회_취득내역 
   Caption         =   "합격조회취득내역"
   ClientHeight    =   8085
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6900
   OleObjectBlob   =   "합격조회_취득내역.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "합격조회_취득내역"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub ComboBox1_Change()
    
    Call 검색
    
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub ListView2_BeforeLabelEdit(Cancel As Integer)

End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()

    ComboBox1.AddItem "전체"
    For i = 2 To 6
        ComboBox1.AddItem Sheet3.Range("J" & i)
    Next
    ComboBox1.Style = fmStyleDropDownList
    ComboBox1.ListIndex = 0

    
    With ListView1
        .View = lvwReport
        .FullRowSelect = True
        .MultiSelect = False
        
        With .ColumnHeaders
            .Add , , "종목", 90
            .Add , , "시험일", 90
            .Add , , "점수", 60
            .Add , , "합격유무", 60
        End With
    End With
    
    With ListView2
        .View = lvwReport
        .FullRowSelect = True
        .MultiSelect = False
        
        With .ColumnHeaders
            .Add , , "종목", 90
            .Add , , "시험일", 90
        End With
    End With
    
    검색
    
End Sub

Sub 검색()
    ListView1.ListItems.Clear
    ListView2.ListItems.Clear
    
    k = IIf(ComboBox1.ListIndex = 0, "**", ComboBox1)
    
    With Sheet5
        For i = 2 To .Range("A1000").End(3).Row
        
            If .Range("I" & i) Like k Then
                If .Range("B" & i) = [번호] And .Range("G" & i) <> "" Then
                    Set li1 = ListView1.ListItems.Add(, , .Range("I" & i))
                    li1.ListSubItems.Add , , .Range("E" & i)
                    li1.ListSubItems.Add , , .Range("G" & i)
                    li1.ListSubItems.Add , , .Range("H" & i)
                    
                    If .Range("H" & i) = "합격" Then
                        Set li2 = ListView2.ListItems.Add(, , .Range("I" & i))
                        li2.ListSubItems.Add , , .Range("F" & i)
                    End If
                    
                End If
            End If
            
        Next
    End With
End Sub
