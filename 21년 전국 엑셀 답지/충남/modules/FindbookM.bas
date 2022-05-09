Attribute VB_Name = "FindbookM"
Public bookChk(5) As clsFindBook, pages(7) As clsFindBook
Public totSearchCnt
Public curPage, startBookidx, endBookidx
Public ��ü���� As Object, �������� As Object

Sub aasdfasdf()
    ' box 163, chkbox= 82
    'Sheet7.üũ1 = ChrW(163)
    With Sheet7.Shapes("���簢�� 3")
        .TextFrame.Characters.Text = ChrW(168) & " ��������Ұ�"
        '.TextFrame.Characters(1, 2).Font.Name = "Webdings"
        
    End With
   
    
End Sub

Sub �۾����ٲ��()
    Call �۾����ٲٱ�(5, 4, vbBlue)
End Sub

Sub changeRgColor(rg, s, l, col)
    rg.Characters(s, l).Font.color = col
End Sub

Sub changeShpColor(shp, s, l, col)
    shp.TextFrame2.TextRange.Characters(s, l).Font.Fill.ForeColor.RGB = col
End Sub

Sub FindBook�˻�()
    Application.ScreenUpdating = False
    
    With Sheet7
        .Range("Z2:AB2").ClearContents
        Select Case .ComboBox1
            Case "����": .[Z2] = "*" & .TextBox1 & "*"
            Case "����": .[AA2] = "*" & .TextBox1 & "*"
            Case "���ǻ�": .[AB2] = "*" & .TextBox1 & "*"
        End Select
        
        Sheet3.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("Z1:AB2"), .Range("AD1"), False
        Sheet3.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("Z1:AB2"), .Range("AR1:AX1"), False
        Sheet4.Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("AF1:AF2"), .Range("AH1")
        
        .ListObjects("�˻����̺�").Sort.SortFields.Clear
        .ListObjects("�˻����̺�").Sort.SortFields.Add Key:=.Range("AV1"), Order:=xlDescending
        '.ListObjects("�˻����̺�").Sort.SortFields.Add Key:=[�˻����̺�[������]], Order:=xlAscending
        .ListObjects("�˻����̺�").Sort.Apply
        
        curPage = 1
        startBookidx = 1
        totSearchCnt = .Range("AD1000").End(3).row - 1
        If totSearchCnt = 0 Then eMsg "�˻��� �ڷᰡ �����ϴ�.": Exit Sub
        
        .ListObjects("�˻����̺�").Resize .Range("AQ1:BB" & .Range("AD1000").End(3).row)
        Call bookUI
        
        
    End With
    Application.ScreenUpdating = True
End Sub

Public Sub bookUI()
    With Sheet7
    
        totSearchCnt = .Range("AD10000").End(3).row - 1
        .Range("B11") = "�� �� " & totSearchCnt & "���� å�� �����մϴ�."
        Call changeRgColor(.Range("B11"), 5, Len(CStr(totSearchCnt)) + 1, vbBlue)
        
        endBookidx = IIf(startBookidx + 4 <= totSearchCnt, startBookidx + 4, totSearchCnt)
        
            
        For i = startBookidx To endBookidx
            Call showBookInfo(i)
            Sheet7.Shapes("�˻�" & (i - 1) Mod 5 + 1).Visible = True
        Next
        For i = endBookidx + 1 To 5
            Sheet7.Shapes("�˻�" & (i - 1) Mod 5 + 1).Visible = False
        Next
        
        ù������ = Val(bookChk(1).������.Caption)
        ���������� = Round((totSearchCnt - (ù������ - 1) * 5) / 5 + 0.5)
        
        If ���������� >= 4 Then
            �������� = ù������ + 4
        Else
            �������� = ù������ + ���������� - 1
        End If
        
        For i = 1 To 5
            bookChk(i).������.Visible = False
        Next
        For i = ù������ To ��������
            bookChk((i - 1) Mod 5 + 1).������.Caption = i
            bookChk((i - 1) Mod 5 + 1).������.Visible = True
        Next
        
    End With
    
End Sub

Sub showBookInfo(i)
    
    idx = (i - 1) Mod 5 + 1
    
    With Sheet7
        Dim shp As Shape
        Set shp = .Shapes("�˻�" & idx)
        shp.GroupItems(9).TextFrame.Characters.Text = i
        
        Set obj = .OLEObjects("üũ" & idx).Object
        If .Range("AQ2").Cells(idx, 1) Then
            obj.Caption = ChrW(82)
            obj.ForeColor = vbBlue
        Else
            obj.Caption = ChrW(163)
            obj.ForeColor = vbBlack
        End If
        
        'Debug.Print ThisWorkbook.Path & "\�����ڷ�\�̹���\����\" & .Range("AQ2").Cells(i, 3) & ".jpg"
        shp.GroupItems("����").TextFrame.Characters.Text = "������ " & .Range("AQ2").Cells(i, 3)
        shp.GroupItems("����").TextFrame.Characters.Text = "���� : " & .Range("AQ2").Cells(i, 4) & " / ���ǻ� : " & .Range("AQ2").Cells(i, 5) & " / �Ⱓ�⵵ : " & year(.Range("AQ2").Cells(i, 6))
        shp.GroupItems("isbn").TextFrame.Characters.Text = "ISBN : " & Mid(.Range("AQ2").Cells(i, 7), 1, 17)
        shp.GroupItems("û����ȣ").TextFrame.Characters.Text = "û����ȣ : " & .Range("AQ2").Cells(i, 8)
        
        If .Range("AQ2").Cells(i, 10) Then
            shp.GroupItems("���⿩��").TextFrame.Characters.Text = "���Ⱑ��[��ġ��]"
            shp.GroupItems("���࿩��").TextFrame.Characters.Text = .Range("O2")
            Call changeShpColor(shp.GroupItems("���⿩��"), 1, 4, vbGreen)
            Call changeShpColor(shp.GroupItems("���⿩��"), 5, 5, vbBlack)
            Call changeShpColor(shp.GroupItems("���࿩��"), 1, 1, vbButtonFace)
        Else
            shp.GroupItems("���⿩��").TextFrame.Characters.Text = "����Ұ�[������] (���� : " & .Range("AQ2").Cells(i, 11) & "��) (�ݳ�������:" & .Range("AQ2").Cells(i, 9) & ")"
            shp.GroupItems("���࿩��").TextFrame.Characters.Text = .Range("O3")
            Call changeShpColor(shp.GroupItems("���⿩��"), 1, 4, vbRed)
            Call changeShpColor(shp.GroupItems("���⿩��"), 5, 34, vbBlack)
            Call changeShpColor(shp.GroupItems("���࿩��"), 1, 1, vbRed)
        End If
        
        
    End With
    
End Sub
