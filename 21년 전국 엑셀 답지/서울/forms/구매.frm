VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 구매 
   Caption         =   "구매"
   ClientHeight    =   7515
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   12555
   OleObjectBlob   =   "구매.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "구매"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cls(1 To 1000) As cls구매
Dim foodList As New ArrayList
Dim sum

Private Sub CommandButton1_Click()
    
    If Image1.Picture = Null Then eMsg "상품을 선택해주세요.": Exit Sub
    
    lrow = Sheet10.Range("I1000").End(xlUp).Row + 1: idx = ListView1.SelectedItem.Index - 1
    
    값 = foodList(idx).fprice
    상품번호 = foodList(idx).fno
    이름 = foodList(idx).fname
    개수 = Val(TextBox3)
    P = 값 * 개수
    
    If SpinButton1.Value = 0 Then eMsg "수량은 1개 이상이어야 합니다.": Exit Sub
    If Not Sheet10.Range("I1:I1000").Find(상품번호) Is Nothing Then eMsg "상품이 이미 장바구니에 담겨있습니다.": Exit Sub
    iMsg "장바구니에 담겼습니다."
    Sheet10.Range("I" & lrow & ":L" & lrow) = Array(상품번호, 이름, 개수, P)
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
        Dim item As cls구매
        Set item = New cls구매
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
    sumlbl.Caption = Format(sum, "#,##0원")
End Sub

Private Sub CommandButton2_Click()
    lrow = Sheet7.Range("A10000").End(xlUp).Row + 1
    
    n = Sheet10.Range("J2:J" & Sheet10.Range("J1000").End(xlUp).Row)
    C = Sheet10.Range("K2:K" & Sheet10.Range("K1000").End(xlUp).Row)

    
    If TypeName(n) = "String" Then
        이름들 = n
        개수들 = C
    Else
        이름들 = Join(WorksheetFunction.Transpose(n), ",")
        개수들 = Join(WorksheetFunction.Transpose(C), ",")
    End If


    
    If TextBox2.text = "" Or ComboBox1.ListIndex = -1 Or ComboBox2.ListIndex = -1 Then eMsg "빈칸을 확인해주세요.": Exit Sub
    
    discount = 1

    If ComboBox1.ListIndex = 1 Then
        discount = 0.9
    ElseIf ComboBox1.ListIndex = 2 Then
        discount = 0.8
    End If
    
    sum = sum * discount
    
    If [등급] <> "VIP" And OptionButton1 Then eMsg "VIP만 이용가능한 권한입니다.": Exit Sub
    
    If MsgBox(Me.Caption & vbCrLf & vbCrLf & "결제방식:" & ComboBox1 & vbCrLf & "결제금액:" & Format(sum, "#,##0원") & "을 결제하시겠습니까?", vbInformation + vbYesNo) = vbYes Then
        Sheet7.Range("A" & lrow & ":H" & lrow) = Array(lrow - 1, [번호], Me.Caption, 이름들, 개수들, Date, IIf(OptionButton1, 0, 3000), sum)
        iMsg "결제가 완료되었습니다."
        Unload Me
        Call Sheet8.init
        Call Sheet8.길찾기
    End If
End Sub

Private Sub CommandButton3_Click()
    
    If CommandButton3.Caption = "◀" Then
        Do While Frame3.Left > 400
            Frame3.Left = Frame3.Left - 1
            DoEvents
        Loop
        CommandButton3.Caption = "▶"
    Else
        Do While Frame3.Left < 588
            Frame3.Left = Frame3.Left + 1
            DoEvents
        Loop
        CommandButton3.Caption = "◀"
    End If
    
End Sub


Private Sub ListView1_Click()
    Image1.Picture = ImageList1.ListImages(ListView1.SelectedItem.Icon).Picture
    가격 = Format(foodList(ImageList1.ListImages(ListView1.SelectedItem.Icon).Index - 1).fprice, "#,##0원")
    이름 = foodList(ImageList1.ListImages(ListView1.SelectedItem.Icon).Index - 1).fname
    TextBox1 = foodList(ImageList1.ListImages(ListView1.SelectedItem.Icon).Index - 1).fexp
End Sub

Private Sub SpinButton1_SpinDown()
    TextBox3.Value = SpinButton1.Value
End Sub

Private Sub SpinButton1_SpinUp()
    If Val(TextBox3) = SpinButton1.Max Then eMsg "최대 수량입니다.": Exit Sub
    TextBox3.Value = SpinButton1.Value
End Sub

Private Sub TextBox1_KeyPress(ByVal KeyAscii As MSForms.ReturnInteger)
    KeyAscii = 0
End Sub

Private Sub UserForm_Initialize()
    
    mkDic dic회원, Sheet3.Range("A2")
    
    Sheet10.Range("I2:l1000").ClearContents
    
    ComboBox1.List = Split("상관없음,문 앞에 두고 가주세요,문 앞에 오시면 전화주세요.,문앞에 오시면 메시지 주세요", ",")
    ComboBox2.List = Split("만나서 현금 결제,만나서 카드 결제,카드 결제,계좌이체", ",")
    
    For i = 2 To Sheet5.Range("A10000").End(xlUp).Row
        With Sheet5
            If .Cells(i, "B") = [구매할놈] Then
                Dim item As New cls구매item
                Set item = New cls구매item
                item.init .Cells(i, 1), .Cells(i, 3), .Cells(i, 4), .Cells(i, 5)
                foodList.add item
            End If
        End With
    Next
    
    For Each i In foodList
        ImageList1.ListImages.add Key:="음식" & i.fno, Picture:=LoadPicture(ThisWorkbook.Path & "\지급자료\음식사진\" & i.fname & ".jpg")
    Next

    ListView1.View = lvwIcon
    ListView1.Icons = ImageList1
    ListView1.OLEDragMode = ccOLEDragAutomatic
    
    For Each i In foodList
        ListView1.ListItems.add , , i.fname, "음식" & i.fno
    Next
End Sub
