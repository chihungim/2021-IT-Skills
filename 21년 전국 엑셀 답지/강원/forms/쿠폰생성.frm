VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �������� 
   Caption         =   "����������"
   ClientHeight    =   2940
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   6405
   OleObjectBlob   =   "��������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��������"
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
            sel = "[�ѱݾ� ����]"
        Case 1
            sel = "[����������]"
        Case 2
            sel = "[û�ҳ�������]"
        Case 3
            sel = "[���������]"
    End Select

End Sub

Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)

    KeyAscii = 0

End Sub

Private Sub CommandButton1_Click()
    CommandButton1.Enabled = False
    coupon = "E" & Format(Date, "yyMMdd") & "T" & Format(Time, "HHmm") & "-"
    dis = "[" & Replace(combobo2, " ", "") & "��] = " & TextBox1
    For i = 1 To Slider1.Value
        If WorksheetFunction.CountIf(Sheet9.Range("C:C"), coupon & "*") > 9 Then
            eMsg "�� �̻� ������ ������ �� �����ϴ�. 1�� �ڿ� �õ��ϼ���."
            CommandButton1.Enabled = True
            Exit Sub
        End If
    
        Call waitFor(0.5)
        
        With Sheet9
            lrow = .Range("A1000").End(3).row + 1
            .Range("A" & lrow & ":G" & lrow) = Array(lrow - 1, [��ȣ], coupon & WorksheetFunction.CountIf(Sheet9.Range("C:C"), coupon & "*"), Date, DateAdd("m", 1, Date), sel & " = " & TextBox1, "X")
        End With
        
        ProgressBar1.Value = (i / Slider1.Value) * 100
        
    Next
    CommandButton1.Enabled = True
End Sub

Private Sub Slider1_Change()
    Label5 = Slider1.Value & "��"
End Sub

Private Sub Slider1_Click()
    Label5 = Slider1.Value & "��"
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
    
    ComboBox2.List = Array("�ѱݾ� ����", "���� ����", "û�ҳ� ����", "��� ����")
    chk = True
    
End Sub

Private Sub UserForm_QueryClose(Cancel As Integer, CloseMode As Integer)
    If CloseMode = 0 And CommandButton1.Enabled = False Then
        eMsg "���� �߿��� ���� ���� �� �����ϴ�.": Cancel = True
    End If
End Sub
