Attribute VB_Name = "�α��θ��"

Sub shp�α���_Click()

    With Sheet7
        If [�α���] = "�α���" Then
        
            If .TextBox1 = "" Or .TextBox2 = "" Then eMsg "���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
        
            If .TextBox1 = "admin" And .TextBox2 = "1234" Then
                iMsg "�����ڴ�, �ȳ��ϼ���."
                ��Ʈ�ʱ�ȭ
                Sheet12.Visible = xlSheetVisible
                Sheet12.Select
                Exit Sub
            End If
            
            Set id_dic = mkdic(Sheet1.Range("B2"))
            If Not id_dic.exists(.TextBox1.Value) Then
                eMsg "���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
                
            Else
                If id_dic(.TextBox1.Value).Cells(1, 2) <> .TextBox2.Value Then eMsg "���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
            End If
            
            iMsg id_dic(.TextBox1.Value).Cells(1, 3) & "��, �ȳ��ϼ���."
            
            Sheet13.Range("A1") = id_dic(.TextBox1.Value).Cells(1, 0)
            .TextBox1 = ""
            .TextBox2 = ""
            .Shapes("�׷� 5").Visible = msoFalse
            .Shapes("�׸� 46").Visible = msoTrue
            
            ��Ʈ�ʱ�ȭ
            Sheet7.Select
            Sheet8.Visible = xlSheetVisible
            Sheet9.Visible = xlSheetVisible
            Sheet10.Visible = xlSheetVisible
            Sheet11.Visible = xlSheetVisible

        Else
            [��ȣ] = ""
            .Shapes("�׷� 5").Visible = msoTrue
            .Shapes("�׸� 46").Visible = msoFalse
            ��Ʈ�ʱ�ȭ
            Sheet7.Select
        End If
    End With
End Sub

Sub ��ã()
     If ��ȣ = "" Then
        eMsg "�α��� �� �̿밡���մϴ�."
        Exit Sub
    End If

    Sheet8.Select
End Sub
Sub ����()
    If ��ȣ = "" Then
        eMsg "�α��� �� �̿밡���մϴ�."
        Exit Sub
    End If
    ����.Show
End Sub
Sub ī���()
    If ��ȣ = "" Then
        eMsg "�α��� �� �̿밡���մϴ�."
        Exit Sub
    End If
    ī���û.Show
End Sub

Sub ���������()
    If [��ȣ] <> "" Then
        eMsg "�̹� Chaseȸ���Դϴ�."
        Exit Sub
    End If
    
    ȸ������.Show
End Sub
