VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 쿠폰생성 
   Caption         =   "쿠폰생성기"
   ClientHeight    =   2940
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6405
   OleObjectBlob   =   "쿠폰생성.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "쿠폰생성"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim chk As Boolean
Dim arr As ArrayList
Dim sel

Private Sub ComboBox1_Change()

End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)

    KeyAscii = 0

End Sub

Private Sub ComboBox2_Change()

    Select Case ComboBox2.ListIndex
        Case 0
            sel = "[총금액 할인]"
        Case 1
            sel = "[성인할인율]"
        Case 2
            sel = "[청소년할인율]"
        Case 3
            sel = "[어린이할인율]"
    End Select

End Sub

Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)

    KeyAscii = 0

End Sub

Private Sub CommandButton1_Click()
    CommandButton1.Enabled = False
    coupon = "E" & Format(Date, "yyMMdd") & "T" & Format(Time, "HHmm") & "-"
    dis = "[" & Replace(combobo2, " ", "") & "율] = " & TextBox1
    For i = 1 To Slider1.Value
        If WorksheetFunction.CountIf(Sheet9.Range("C:C"), coupon & "*") > 9 Then
            eMsg "더 이상 쿠폰을 생성할 수 없습니다. 1분 뒤에 시도하세요."
            CommandButton1.Enabled = True
            Exit Sub
        End If
    
        Call waitFor(0.5)
        
        With Sheet9
            lrow = .Range("A1000").End(3).row + 1
            .Range("A" & lrow & ":G" & lrow) = Array(lrow - 1, [번호], coupon & WorksheetFunction.CountIf(Sheet9.Range("C:C"), coupon & "*"), Date, DateAdd("m", 1, Date), sel & " = " & TextBox1, "X")
        End With
        
        ProgressBar1.Value = (i / Slider1.Value) * 100
        
    Next
    CommandButton1.Enabled = True
End Sub

Private Sub Slider1_Change()
    Label5 = Slider1.Value & "장"
End Sub

Private Sub Slider1_Click()
    Label5 = Slider1.Value & "장"
End Sub

Sub waitFor(sec As Single)

    Dim s As Single
    
    s = Timer + sec
    
    Do While Timer < s
        DoEvents
    Loop

End Sub

Private Sub UserForm_Initialize()
    
    For i = 2 To Sheet4.Range("A10000").End(3).row
        ComboBox1.AddItem Sheet4.Range("A" & i) & " - " & Sheet4.Range("D" & i)
    Next
    
    ComboBox1.ListIndex = 0
    
    ComboBox2.List = Array("총금액 할인", "성인 할인", "청소년 할인", "어린이 할인")
    chk = True
    
End Sub

Private Sub UserForm_QueryClose(Cancel As Integer, CloseMode As Integer)
    If CloseMode = 0 And CommandButton1.Enabled = False Then
        eMsg "생성 중에는 폼을 닫을 수 없습니다.": Cancel = True
    End If
End Sub
