Attribute VB_Name = "���θ��"
Sub �����ڷα���()
    
    With Sheet1
        For i = 1 To 5
            .Shapes("shp_" & i).TextFrame.Characters.Font.Color = vbBlack
            .Shapes("shp_" & i).Fill.ForeColor.ObjectThemeColor = msoThemeColorText2
        Next
    
        .Shapes("shp_1").TextFrame.Characters.Text = "�α׾ƿ�"
        .Shapes("shp_2").TextFrame.Characters.Text = "��Ű�� ����"
        .Shapes("shp_3").TextFrame.Characters.Text = "���� ����"
        .Shapes("shp_4").TextFrame.Characters.Text = "�� ��"
        .Shapes("shp_4").Visible = msoTrue
        .Shapes("shp_5").Visible = msoFalse
    End With
End Sub

Sub �����α���()
    With Sheet1
        For i = 1 To 5
            .Shapes("shp_" & i).TextFrame.Characters.Font.Color = vbBlack
            .Shapes("shp_" & i).Fill.ForeColor.ObjectThemeColor = msoThemeColorAccent4
        Next
        .Shapes("shp_1").TextFrame.Characters.Text = "�α׾ƿ�"
        .Shapes("shp_2").TextFrame.Characters.Text = "�����ϱ�"
        .Shapes("shp_3").TextFrame.Characters.Text = "���� ��Ʈ"
        .Shapes("shp_4").TextFrame.Characters.Text = "���� Ȯ��"
        .Shapes("shp_5").TextFrame.Characters.Text = "�� ��"
        .Shapes("shp_4").Visible = msoTrue
        .Shapes("shp_5").Visible = msoTrue
    End With
End Sub

Sub �α׾ƿ�()
    [��ȣ] = ""
    With Sheet1
        For i = 1 To 5
            .Shapes("shp_" & i).TextFrame.Characters.Font.Color = vbWhite
            .Shapes("shp_" & i).Fill.ForeColor.ObjectThemeColor = msoThemeColorAccent5
        Next
        .Shapes("shp_1").TextFrame.Characters.Text = "�α���"
        .Shapes("shp_2").TextFrame.Characters.Text = "ȸ������"
        .Shapes("shp_3").TextFrame.Characters.Text = "�� ��"
        .Shapes("shp_4").Visible = msoFalse
        .Shapes("shp_5").Visible = msoFalse
    End With
End Sub

Sub shp1_Click()
    With Sheet1
        If .Shapes("shp_1").TextFrame.Characters.Text = "�α���" Then
            �α���.Show
        Else
            �α׾ƿ�
        End If
    End With
End Sub
Sub shp2_Click()
    With Sheet1
        If .Shapes("shp_2").TextFrame.Characters.Text = "ȸ������" Then
            ȸ������.Show
        ElseIf .Shapes("shp_2").TextFrame.Characters.Text = "�����ϱ�" Then
            �˻�����.Show
        Else
            ��Ű������.Show
        End If
    End With
End Sub
Sub shp3_Click()
    With Sheet1
        If .Shapes("shp_3").TextFrame.Characters.Text = "�� ��" Then
            ThisWorkbook.Save
            ThisWorkbook.Close
        ElseIf .Shapes("shp_3").TextFrame.Characters.Text = "���� ��Ʈ" Then
            Sheet9.Select
        Else
            Sheet9.Select
        End If
    End With
End Sub
Sub shp4_Click()
    With Sheet1
        If .Shapes("shp_4").TextFrame.Characters.Text = "���� Ȯ��" Then
            Sheet8.Select
        Else
            ThisWorkbook.Save
            ThisWorkbook.Close
        End If
    End With
End Sub
Sub shp5_Click()
    With Sheet1
        ThisWorkbook.Save
        ThisWorkbook.Close
    End With
End Sub
