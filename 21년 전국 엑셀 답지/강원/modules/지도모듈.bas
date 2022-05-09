Attribute VB_Name = "�������"
Dim n, k, chk As Boolean
Dim arr(100), locarr(100)
Dim minPr
Dim dep As Integer, arrv As Integer, result() As Variant, curCnt
Public dic1 As Object, dic2 As Object

Sub shp�߰�_Click()
    
    With Sheet11
        If .ListBox1.ListIndex = -1 Then Exit Sub
        
        If .ListBox2.ListCount = 8 Then eMsg "�õ������� �ִ� 8������ ����� �� �ֽ��ϴ�.:exit sub"
        
        For i = 0 To .ListBox2.ListCount - 1
            If .ListBox1.List(.ListBox1.ListIndex) = .ListBox2.List(i) Then eMsg "�̹� �߰��� �õ�/���� �Դϴ�!": Exit Sub
        Next
        
        Set img = .Shapes(.ListBox1.List(.ListBox1.ListIndex))
        With img
            t = .Top
            l = .Left
            w = .Width
            h = .Height
        End With
        .Shapes(.ListBox1.List(.ListBox1.ListIndex)).Delete
    
        Set img = .Shapes.AddPicture(ThisWorkbook.Path & "\�����ڷ�\pick_green.png", msoFalse, msoTrue, l, t, w, h)
        img.Name = .ListBox1.List(.ListBox1.ListIndex)
        .ListBox2.AddItem .ListBox1.List(.ListBox1.ListIndex)
        
        .Shapes("shp�Ÿ�").TextFrame.Characters.Text = ""
        .Shapes("shp����").TextFrame.Characters.Text = ""
        �ѰŸ� = 0: �Ѱ��� = 0
        For i = 0 To .ListBox2.ListCount - 2
            
            r = [���].Find(.ListBox2.List(i)).row
            c = [����].Find(.ListBox2.List(i + 1)).Column
            
            �ѰŸ� = �ѰŸ� + [�Ÿ�].Cells(r - 1, c - 1): �Ѱ��� = �Ѱ��� + [�����].Cells(r - 1, c - 1) + [�⸧��].Cells(r - 1, c - 1)
            .Shapes("shp�Ÿ�").TextFrame.Characters.Text = .Shapes("shp�Ÿ�").TextFrame.Characters.Text & " " & [�Ÿ�].Cells(r - 1, c - 1)
            .Shapes("shp����").TextFrame.Characters.Text = .Shapes("shp����").TextFrame.Characters.Text & " " & Format([�����].Cells(r - 1, c - 1) + [�⸧��].Cells(r - 1, c - 1), "#,##0")
        
            x1 = .Shapes(.ListBox2.List(i)).Left + 15
            y1 = .Shapes(.ListBox2.List(i)).Top + 15
            x2 = .Shapes(.ListBox2.List(i + 1)).Left + 15
            y2 = .Shapes(.ListBox2.List(i + 1)).Top + 15
            With .Shapes.AddConnector(msoConnectorStraight, x1, y1, x2, y2)
                .ZOrder msoSendToBack
                .ZOrder msoBringForward
                .Line.Weight = 2.25
                .Name = "Line" & (i + 1)
            End With
            With .Shapes.AddLabel(msoTextOrientationHorizontal, ((x2 - x1) / 2) + x1 - 30, ((y2 - y1) / 2) + y1, 100, 10)
                .Name = "Distance" & (i + 1)
                .TextFrame.Characters.Text = [�Ÿ�].Cells(r - 1, c - 1) & "km"
            End With
            With .Shapes.AddLabel(msoTextOrientationHorizontal, ((x2 - x1) / 2) + x1 - 30, ((y2 - y1) / 2) + y1 + 10, 100, 10)
                .Name = "Price" & (i + 1)
                .TextFrame.Characters.Text = Format([�����].Cells(r - 1, c - 1) + [�⸧��].Cells(r - 1, c - 1), "#,##0��")
            End With
        Next
        
        .Shapes("shp�Ÿ�").TextFrame.Characters.Text = Replace(Trim(.Shapes("shp�Ÿ�").TextFrame.Characters.Text), " ", " + ")
        .Shapes("shp����").TextFrame.Characters.Text = Replace(Trim(.Shapes("shp����").TextFrame.Characters.Text), " ", " + ")
        .Shapes("shp�ѰŸ�").TextFrame.Characters.Text = "�� : " & �ѰŸ� & "km"
        .Shapes("shp�Ѱ���").TextFrame.Characters.Text = "�� : " & Format(�Ѱ���, "#,##0��")
        
    End With
End Sub

Sub shp����_Click()
    
    With Sheet11
        If .ListBox2.ListIndex < 0 Then Exit Sub
        Set img = .Shapes(.ListBox2.List(.ListBox2.ListIndex))
        With img
            t = .Top
            l = .Left
            w = .Width
            h = .Height
        End With
        .Shapes(.ListBox2.List(.ListBox2.ListIndex)).Delete
    
        Set img = .Shapes.AddPicture(ThisWorkbook.Path & "\�����ڷ�\pick_red.png", msoFalse, msoTrue, l, t, w, h)
        img.Name = .ListBox2.List(.ListBox2.ListIndex)
        .ListBox2.RemoveItem .ListBox2.ListIndex
    End With
End Sub

Sub shp������ΰ��_Click()
    If Sheet11.ListBox2.ListCount = 0 Then eMsg "�õ�/������ �߰����ּ���.": Exit Sub
    
    k = Sheet11.ListBox2.ListCount
    n = k
    
    tCnt = Evaluate("Fact(" & k & ")")
    Erase result
    ReDim Preserve result((tCnt - 1), k)
    
    For i = 0 To k - 1
        locarr(i) = Sheet11.ListBox2.List(i)
    Next
    
    curCnt = 0
    Call permutation(0)
    
    dist = Sheet13.Range("B2:S19")
    pass = Sheet14.Range("B2:S19")
    oil = Sheet15.Range("B2:S19")
    Call mkdic3(Sheet13.Range("A2:A19"), dic1)
    Call mkdic3(Sheet13.Range("B1:S1"), dic2)
    
    minPr = 100000000
    For i = 0 To UBound(result)
        h = 0: ����� = 0: �⸧�� = 0
        For J = 0 To k - 2
            h = h + dist(dic1(result(i, J)).row - 1, dic2(result(i, J + 1)).Column - 1)
        Next
        result(i, k) = h
        
        If minPr > Val(result(i, k)) Then minPr = Val(result(i, k)): idx = i
    Next
    
    With Sheet11
        For Each shp In .Shapes
            If shp.Name Like "*Line*" Or shp.Name Like "*Distance*" Or shp.Name Like "*Price*" Then
                shp.Delete
            End If
        Next
        .Shapes("shp�Ÿ�").TextFrame.Characters.Text = ""
        .Shapes("shp����").TextFrame.Characters.Text = ""
        �ѰŸ� = 0: �Ѱ��� = 0
        For i = 0 To k - 2
            r = [���].Find(result(idx, i)).row
            c = [����].Find(result(idx, i + 1)).Column
            �ѰŸ� = �ѰŸ� + dist(r - 1, c - 1)
            �Ѱ��� = �Ѱ��� + pass(r - 1, c - 1) + oil(r - 1, c - 1)
            .Shapes("shp�Ÿ�").TextFrame.Characters.Text = .Shapes("shp�Ÿ�").TextFrame.Characters.Text & " " & dist(r - 1, c - 1)
            .Shapes("shp����").TextFrame.Characters.Text = .Shapes("shp����").TextFrame.Characters.Text & " " & Format(pass(r - 1, c - 1) + oil(r - 1, c - 1), "#,##0")
        
            x1 = .Shapes(result(idx, i)).Left + 15
            y1 = .Shapes(result(idx, i)).Top + 15
            x2 = .Shapes(result(idx, i + 1)).Left + 15
            y2 = .Shapes(result(idx, i + 1)).Top + 15
            With .Shapes.AddConnector(msoConnectorStraight, x1, y1, x2, y2)
                .ZOrder msoSendToBack
                .ZOrder msoBringForward
                .Line.Weight = 2.25
                .Name = "Line" & (i + 1)
            End With
            With .Shapes.AddLabel(msoTextOrientationHorizontal, ((x2 - x1) / 2) + x1 - 30, ((y2 - y1) / 2) + y1, 100, 10)
                .Name = "Distance" & (i + 1)
                .TextFrame.Characters.Text = dist(r - 1, c - 1) & "km"
            End With
            With .Shapes.AddLabel(msoTextOrientationHorizontal, ((x2 - x1) / 2) + x1 - 30, ((y2 - y1) / 2) + y1 + 10, 100, 10)
                .Name = "Price" & (i + 1)
                .TextFrame.Characters.Text = Format(pass(r - 1, c - 1) + oil(r - 1, c - 1), "#,##0��")
            End With
        Next
        
        .Shapes("shp�Ÿ�").TextFrame.Characters.Text = Replace(Trim(.Shapes("shp�Ÿ�").TextFrame.Characters.Text), " ", " + ")
        .Shapes("shp����").TextFrame.Characters.Text = Replace(Trim(.Shapes("shp����").TextFrame.Characters.Text), " ", " + ")
        .Shapes("shp�ѰŸ�").TextFrame.Characters.Text = "�� : " & �ѰŸ� & "km"
        .Shapes("shp�Ѱ���").TextFrame.Characters.Text = "�� : " & Format(�Ѱ���, "#,##0��")
    End With
    
End Sub

Sub permutation(dp)
    If dp >= k Then
        aa = ""
        For i = 0 To k
            aa = aa & " " & arr(i)
        Next
        
        bb = Split(Trim(aa), " ")
        For i = 0 To UBound(bb)
            result(curCnt, i) = bb(i)
        Next
        curCnt = curCnt + 1
    End If
    
    For i = 0 To n - 1
        chk = True
        For J = 0 To dp - 1
            If arr(J) = locarr(i) Then chk = False
        Next
        
        If chk = True Then
            arr(dp) = locarr(i)
            Call permutation(dp + 1)
            arr(dp) = 0
        End If
    Next
End Sub

Function calDistance(loc As String)
    
    sp = Split(loc, ",")
    dis = 0
    
    For i = 0 To UBound(sp) - 1
        dep = [���].Find(sp(i)).row - 1
        arrv = [����].Find(sp(i + 1)).Column - 1
        dis = dis + [�Ÿ�].Cells(dep, arrv)
    Next
    
    calDistance = dis
    
End Function

Sub ����ϱ�()
    
    iMsg calDistance("��â��,������,��ô��,�籸��")
    
End Sub
