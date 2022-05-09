VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ȸ������ 
   Caption         =   "ȸ������"
   ClientHeight    =   6495
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9315
   OleObjectBlob   =   "ȸ������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "ȸ������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim rg


Private Sub ComboBox1_Change()
    ComboBox2.ListIndex = -1
    ComboBox3.ListIndex = -1
End Sub

Private Sub ComboBox2_Change()
    If ComboBox2.ListIndex <> -1 Then
        ComboBox3.Clear
        ���� = Day(WorksheetFunction.EoMonth(DateValue(ComboBox1.Text & "-" & ComboBox2 & "-" & 1), 0))
        For i = 1 To ����
            ComboBox3.AddItem i
        Next
    End If
End Sub

Function setAccount(start)
    r = WorksheetFunction.RandBetween(100000000000#, 1000000000000# - 1)
    setAccount = Format(r, start & "00-0000-0000-00")
End Function

Private Sub CommandButton1_Click()
    For i = 1 To 6
        If Me.Controls("TextBox" & i) = "" Then eMsg "��ĭ�� �����մϴ�.": Exit Sub
    Next
    
    If ComboBox1 = "" Or ComboBox2 = "" Or ComboBox3 = "" Then eMsg "��ĭ�� �����մϴ�.": Exit Sub
    
    If TextBox2 Like "*[0-9]*" = False Or TextBox2 Like "*[a-zA-Z]*" = False Or TextBox2 Like "*[\!\@\#\$]*" = False Or Not (Len(TextBox2) >= 8 And Len(TextBox2) <= 16) Then eMsg "��й�ȣ�� Ư������, ������, ���ڸ� ������ 8~16�� �̳��� �����ؾ� �մϴ�.": Exit Sub
    
    If Len(TextBox3) < 4 Then eMsg "2�� ��й�ȣ�� �ּ� 4�ڸ� �̻����� �����ؾ� �մϴ�.": Exit Sub
    
    If Not (TextBox6 Like "[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]" Or TextBox6 Like "[0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]") Then eMsg "��ȭ��ȣ ������ �ùٸ��� �����ؾ��մϴ�.": Exit Sub
    
    Set ���� = mkdic(Sheet1.Range("B2"))
    If ����.exists(TextBox1.Value) Then eMsg "�̹� ��ϵ� ���̵� �Դϴ�.": Exit Sub
    
    Set ���� = mkdic(Sheet1.Range("H2"))
    If ����.exists(TextBox6.Value) Then eMsg "�̹� ��ϵ� ��ȭ��ȣ �Դϴ�.": Exit Sub
    Frame1.Visible = True
End Sub

Private Sub CommandButton2_Click()
    If Not (����1.Value Or ����2.Value Or ����3.Value) Then eMsg "���� ���� �׸��� �����ؾ� �մϴ�.": Exit Sub
    If Not (ī��0.Value Or ī��1.Value Or ī��2.Value Or ī��3.Value) Then eMsg "ī�� ��û �׸��� �����ؾ��մϴ�.": Exit Sub
    
    If Not (CheckBox1 And CheckBox2) Then eMsg "Ÿ�����κ��� ���� ���� ��û�� ����� �մϴ�.": Exit Sub
    
    If ī��0.Value Then
        If MsgBox("���� ī�带 ��û�ϸ� ��ȸ�� �����˴ϴ�." & vbCrLf & "�׷��� ��û���� �����ðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
            Call ȸ������
        Else
            Exit Sub
        End If
    Else
        With ī���û
            .Image1.Picture = LoadPicture(ThisWorkbook.Path & "\�����ڷ�\�̹���\" & rg.Cells(1, 0) & ".jpg")
            .ī��� = rg
            ��ȸ�� = " (����)"
            
            �ؿ� = Split(rg.Cells(1, 6), ",")
            For i = 0 To UBound(�ؿ�)
                .ComboBox1.AddItem Sheet3.Range("I" & �ؿ�(i) + 1)
            Next
            .ComboBox1.ListIndex = 0
            
            Erase ����ī��
            ��ûflag = False
            .Show
        End With
        If ��ûflag Then
            Call ȸ������
            With Sheet4
                lrow = .Range("A10000").End(xlUp).Row + 1
                .Range("A" & lrow & ":E" & lrow) = Array(����ī��(0), ����ī��(1), ����ī��(2), Sheet1.Range("A1000").End(xlUp).Row - 1, 0)
            End With
        Else
            Exit Sub
        End If
    End If
End Sub

Sub ȸ������()
    Set dic = mkdic(Sheet1.Range("L2"))
    Do While True
        account = setAccount(IIf(����1, "3", IIf(����2, "7", "9")))
        If Not dic.exists(account) Then Exit Do
    Loop
    
    With Sheet1
        lrow = .Range("A1000").End(xlUp).Row + 1
        �ܾ� = IIf(����1, 200000, 25000)
        
        .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, TextBox1, TextBox2, TextBox4, ComboBox1 & "-" & ComboBox2 & "-" & ComboBox3, TextBox3, TextBox5, TextBox6, account, �ܾ�)
        Unload Me
    End With
End Sub

Private Sub TextBox3_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub TextBox3_MouseUp(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    ��й�ȣ.Show
End Sub

Private Sub UserForm_Initialize()
    For i = 1 To 3
        Me.Controls("ComboBox" & i).Style = fmStyleDropDownList
    Next

    For i = 1960 To Year(Now())
        ComboBox1.AddItem i
    Next
    
    For i = 1 To 12
        ComboBox2.AddItem i
    Next
    
    Sheet12.Range("B10:C" & Sheet12.Range("B1000").End(xlUp).Row - 1).Copy Sheet13.Range("AX2")
    Sheet13.Range("AX1").CurrentRegion.AdvancedFilter xlFilterCopy, Sheet13.Range("BA1:BB2"), Sheet13.Range("BD1:BE1"), False


    For i = 1 To 3
        Me.Controls("ī��" & i).Caption = Sheet13.Range("BD" & i + 1)
    Next

    Frame1.Visible = False
End Sub

Private Sub ComboBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox2_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub
Private Sub ComboBox3_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub ī��1_Click()
    Set rg = Sheet2.Range("B:B").Find(What:=ī��1.Caption, LookAt:=xlWhole)
End Sub

Private Sub ī��2_Click()
    Set rg = Sheet2.Range("B:B").Find(What:=ī��2.Caption, LookAt:=xlWhole)
End Sub

Private Sub ī��3_Click()
    Set rg = Sheet2.Range("B:B").Find(What:=ī��3.Caption, LookAt:=xlWhole)
End Sub
