VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���� 
   Caption         =   "����"
   ClientHeight    =   6450
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4560
   OleObjectBlob   =   "����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim list As ArrayList

Private Sub ComboBox1_Change()
    If ComboBox1.ListIndex <> -1 Then
    
        With list(ComboBox1.ListIndex)
            TextBox1 = Format(.Cells(1, 8), "#,##0��")
            Debug.Print .Cells(1, 8)
            TextBox2 = IIf(.Cells(1, 10) = 0, "�Ͻú�", .Cells(1, 10) & "����")
            
            If TextBox2 = "�Ͻú�" Then
                If .Cells(1, 9) = 0 Then TextBox3 = "���� �ȵ�"
            Else
                TextBox3 = .Cells(1, 9) & "ȸ�� ���� / " & .Cells(1, 10) - .Cells(1, 9) & "ȸ�� ����"
            End If
            
            TextBox4 = .Cells(1, 6)
        End With
    
    End If
End Sub

Private Sub CommandButton1_Click()
    If ComboBox1.ListIndex = -1 Then eMsg "������ �������� �����ؾ� �մϴ�.": Exit Sub
    
    pw = ""
    ��й�ȣ.Show
        If Not ��й�ȣflag Then eMsg "2�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
        
        If Val(TextBox1) > [�ܾ�] Then eMsg "�ܾ��� �����մϴ�.": Exit Sub
        With list(ComboBox1.ListIndex)
        
        If TextBox2 = "�Ͻú�" Then
            iMsg "���θ� �Ϸ��߽��ϴ�."
        Else
            iMsg .Cells(1, 10) & "ȸ�� �� " & .Cells(1, 9) + 1 & "ȸ���� �����߽��ϴ�."
        End If
    End With
    Sheet1.Range("J" & [��ȣ] + 1) = Sheet1.Range("J" & [��ȣ] + 1) - Val(TextBox1)
End Sub


Private Sub UserForm_Initialize()

    Set list = New ArrayList

    With Sheet6
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("C" & i) = [��ȣ] And .Range("F" & i) <> "" And .Range("J" & i) - .Range("I" & i) >= 0 Then
                ComboBox1.AddItem Format(.Range("B" & i), "yy-MM-dd") & " " & Sheet5.Range("C" & .Range("G" & i) + 1)
                list.Add Range("A" & i)
            End If
        Next
    End With
End Sub
