VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���� 
   Caption         =   "����"
   ClientHeight    =   7515
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   12555
   OleObjectBlob   =   "����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(1 To 1000) As cls����
Dim foodList As New ArrayList
Dim sum

Private Sub CommandButton1_Click()
    
    If Image1.Picture = Null Then eMsg "��ǰ�� �������ּ���.": Exit Sub
    
    lrow = Sheet10.Range("I1000").End(xlUp).Row + 1: idx = ListView1.SelectedItem.Index - 1
    
    �� = foodList(idx).fprice
    ��ǰ��ȣ = foodList(idx).fno
    �̸� = foodList(idx).fname
    ���� = Val(TextBox3)
    P = �� * ����
    
    If SpinButton1.Value = 0 Then eMsg "������ 1�� �̻��̾�� �մϴ�.": Exit Sub
    If Not Sheet10.Range("I1:I1000").Find(��ǰ��ȣ) Is Nothing Then eMsg "��ǰ�� �̹� ��ٱ��Ͽ� ����ֽ��ϴ�.": Exit Sub
    iMsg "��ٱ��Ͽ� �����ϴ�."
    Sheet10.Range("I" & lrow & ":L" & lrow) = Array(��ǰ��ȣ, �̸�, ����, P)
    Call reFreash
End Sub

Sub reFreash()
    On Error Resume Next
    For Each v In Frame4.Controls
        If v.name <> "sumlbl" Then Frame4.Controls.Remove v.name
    Next
    Set tmpList = New ArrayList
    
    For i = 2 To Sheet10.Range("I1000").End(xlUp).Row
        tmpList.add Sheet10.Range("I" & i)
    Next
    
    idx = 0

    For Each v In tmpList
        Dim item As cls����
        Set item = New cls����
        item.init Frame4, (idx * 70) + 5, 5, v.Cells(1, 2), v.Cells(1, 3), v.Cells(1, 4), v.Cells(1, 1)
        idx = idx + 1
        Set cls(idx) = item
    Next
    
    Frame4.ScrollHeight = idx * 70 + 5
    If Frame4.Height < Frame4.ScrollHeight Then
        Frame4.ScrollBars = fmScrollBarsVertical
    Else
        Frame4.ScrollBars = fmScrollBarsNone
    End If
    
    sum = Sheet10.Range("M1")
    sumlbl.Top = (idx * 70) + 10
    sumlbl.Caption = Format(sum, "#,##0��")
End Sub

Private Sub CommandButton2_Click()
    lrow = Sheet7.Range("A10000").End(xlUp).Row + 1
    
    n = Sheet10.Range("J2:J" & Sheet10.Range("J1000").End(xlUp).Row)
    C = Sheet10.Range("K2:K" & Sheet10.Range("K1000").End(xlUp).Row)

    
    If TypeName(n) = "String" Then
        �̸��� = n
        ������ = C
    Else
        �̸��� = Join(WorksheetFunction.Transpose(n), ",")
        ������ = Join(WorksheetFunction.Transpose(C), ",")
    End If


    
    If TextBox2.text = "" Or ComboBox1.ListIndex = -1 Or ComboBox2.ListIndex = -1 Then eMsg "��ĭ�� Ȯ�����ּ���.": Exit Sub
    
    discount = 1

    If ComboBox1.ListIndex = 1 Then
        discount = 0.9
    ElseIf ComboBox1.ListIndex = 2 Then
        discount = 0.8
    End If
    
    sum = sum * discount
    
    If [���] <> "VIP" And OptionButton1 Then eMsg "VIP�� �̿밡���� �����Դϴ�.": Exit Sub
    
    If MsgBox(Me.Caption & vbCrLf & vbCrLf & "�������:" & ComboBox1 & vbCrLf & "�����ݾ�:" & Format(sum, "#,##0��") & "�� �����Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
        Sheet7.Range("A" & lrow & ":H" & lrow) = Array(lrow - 1, [��ȣ], Me.Caption, �̸���, ������, Date, IIf(OptionButton1, 0, 3000), sum)
        iMsg "������ �Ϸ�Ǿ����ϴ�."
        Unload Me
        Call Sheet8.init
        Call Sheet8.��ã��
    End If
End Sub

Private Sub CommandButton3_Click()
    
    If CommandButton3.Caption = "��" Then
        Do While Frame3.Left > 400
            Frame3.Left = Frame3.Left - 1
            DoEvents
        Loop
        CommandButton3.Caption = "��"
    Else
        Do While Frame3.Left < 588
            Frame3.Left = Frame3.Left + 1
            DoEvents
        Loop
        CommandButton3.Caption = "��"
    End If
    
End Sub


Private Sub ListView1_Click()
    Image1.Picture = ImageList1.ListImages(ListView1.SelectedItem.Icon).Picture
    ���� = Format(foodList(ImageList1.ListImages(ListView1.SelectedItem.Icon).Index - 1).fprice, "#,##0��")
    �̸� = foodList(ImageList1.ListImages(ListView1.SelectedItem.Icon).Index - 1).fname
    TextBox1 = foodList(ImageList1.ListImages(ListView1.SelectedItem.Icon).Index - 1).fexp
End Sub

Private Sub SpinButton1_SpinDown()
    TextBox3.Value = SpinButton1.Value
End Sub

Private Sub SpinButton1_SpinUp()
    If Val(TextBox3) = SpinButton1.Max Then eMsg "�ִ� �����Դϴ�.": Exit Sub
    TextBox3.Value = SpinButton1.Value
End Sub

Private Sub TextBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub UserForm_Initialize()
    
    mkDic dicȸ��, Sheet3.Range("A2")
    
    Sheet10.Range("I2:l1000").ClearContents
    
    ComboBox1.List = Split("�������,�� �տ� �ΰ� ���ּ���,�� �տ� ���ø� ��ȭ�ּ���.,���տ� ���ø� �޽��� �ּ���", ",")
    ComboBox2.List = Split("������ ���� ����,������ ī�� ����,ī�� ����,������ü", ",")
    
    For i = 2 To Sheet5.Range("A10000").End(xlUp).Row
        With Sheet5
            If .Cells(i, "B") = [�����ҳ�] Then
                Dim item As New cls����item
                Set item = New cls����item
                item.init .Cells(i, 1), .Cells(i, 3), .Cells(i, 4), .Cells(i, 5)
                foodList.add item
            End If
        End With
    Next
    
    For Each i In foodList
        ImageList1.ListImages.add Key:="����" & i.fno, Picture:=LoadPicture(ThisWorkbook.Path & "\�����ڷ�\���Ļ���\" & i.fname & ".jpg")
    Next

    ListView1.View = lvwIcon
    ListView1.Icons = ImageList1
    ListView1.OLEDragMode = ccOLEDragAutomatic
    
    For Each i In foodList
        ListView1.ListItems.add , , i.fname, "����" & i.fno
    Next
End Sub
