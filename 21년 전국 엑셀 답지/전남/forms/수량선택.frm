VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �������� 
   Caption         =   "��������"
   ClientHeight    =   3015
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4095
   OleObjectBlob   =   "��������.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "��������"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim ����, ����, �����ڵ�

Private Sub CommandButton1_Click()
    
    Set arr���� = New ArrayList
    
    mkDic dic����, Sheet4.Range("B2")
    
    If ���Ÿ�� Then
        arr����.Add Array(�����ڵ�, ����.������, ����, Val(TextBox1), Val(TextBox1) * ����)
        Unload Me
        ����.Show
    Else
        With Sheet6
            If Val(.Range("G1")) = 20 Then
                eMsg "��ٱ��ϰ� �� á���ϴ�."
                Exit Sub
            End If
            Dim ��ٱ���row As Range
            
            For i = 2 To Sheet6.Range("A10000").End(xlUp).row
                If Not Sheet6.Cells(i, "A").Rows.Hidden Then
                    If Sheet6.Cells(i, "C") = �����ڵ� Then
                        Set ��ٱ���row = Sheet6.Range("C" & i)
                    End If
                End If
            Next
                
            If Not ��ٱ���row Is Nothing Then
                ��ٱ���row.Cells(1, 2) = ��ٱ���row.Cells(1, 2) + Val(TextBox1)
            Else
                irow = .Range("A1000").End(xlUp).row + 1
                .Range("A" & irow & ":D" & irow) = Array(irow - 1, [���̵�], �����ڵ�, Val(TextBox1))
            End If
            
            dic����(�����ڵ�).Cells(1, 8) = dic����(�����ڵ�).Cells(1, 8) - Val(TextBox1)
        
            If MsgBox("��� �˻��Ͻðڽ��ϱ�?", vbInformation + vbYesNo, "����") = vbYes Then
                Unload Me
                �����˻�.Show
            Else
                Unload Me
                ��ٱ���.Show
            End If
        End With
    End If
End Sub

Private Sub CommandButton2_Click()
    Unload Me
    �����˻�.Show
End Sub

Private Sub TextBox1_Change()
    

    If TextBox1 = "" Then Exit Sub
    
    
    If Val(TextBox1) < 1 Or TextBox1 Like "*[!0-9]*" Then eMsg "������ 1 �̻��� ���ڸ� �Է°����մϴ�.": TextBox1 = "": Exit Sub
    If Val(TextBox1) > ���� Then eMsg "����� �����ϴ�": Exit Sub
    �İ��� = Format(���� * Val(TextBox1), "#,##0")
End Sub

Private Sub TextBox1_Exit(ByVal Cancel As MSForms.ReturnBoolean)
    If TextBox1 = "" Then TextBox1 = "1"
End Sub

Private Sub UserForm_Initialize()
    Me.������ = ����.������
    
    ���� = dic����(����.������).Cells(1, 4)
    �����ڵ� = dic����(����.������).Cells(1, -2)
    ���� = dic����(����.������).Cells(1, 5)
    Me.������ = Format(����, "#,##0")
    TextBox1 = "1"
    �İ��� = Format(���� * Val(TextBox1), "#,##0")
End Sub
