VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���� 
   Caption         =   "����"
   ClientHeight    =   3825
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10575
   OleObjectBlob   =   "����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim hap

Private Sub CheckBox1_Click()
    If CheckBox1 Then
        TextBox1 = [����Ʈ]
    Else
        TextBox1 = "0"
    End If
    
    Call update
End Sub

Sub update()
    lbl����Ʈ = Val(TextBox1)
    �ѱݾ� = hap - Val(TextBox1)
    lbl�Ѱ����� = Format(�ѱݾ�, "#,##0")
End Sub

Private Sub CommandButton1_Click()

    mkDic dic����, Sheet4.Range("B2")
    mkDic dic���, Sheet7.Range("B2")

    Set arr��� = New ArrayList
    
    With ListView1
        For i = 1 To ListView1.ListItems.Count
            irow = Sheet5.Range("A1000").End(xlUp).row + 1
            MsgBox irow
            �����ڵ� = .ListItems(i).Text
            ���� = .ListItems(i).SubItems(3)
            
            If dic���.exists(�����ڵ�) Then
                Dim ���row As Range
                Set ���row = dic���(�����ڵ�)
                If Not ���row.Rows.Hidden Then
                    arr���.Add ���row
                    MsgBox ���row.Cells(1, 1)

                End If
            End If
            
            dic����(�����ڵ�).Cells(1, 8) = dic����(�����ڵ�).Cells(1, 8) - .ListItems(i).SubItems(3)
            
            If ���Ÿ�� Then
                
                For J = 2 To Sheet5.Range("A1000").End(xlUp).row
                    If Not Sheet5.Cells(J, "A").Rows.Hidden Then
                        If Sheet5.Cells(J, "C") = �����ڵ� Then
                            Sheet5.Cells(J, "C").EntireRow.Delete xlShiftUp
                        End If
                    End If
                Next
            End If
            
            Sheet5.Range("A" & irow & ":I" & irow) = Array(irow - 1, [���̵�], �����ڵ�, Date, Val(����), "", "", lbl�Ѱ�����, IIf(Val(TextBox1) > 0, "0", dic����(�����ڵ�).Cells(1, 9) * ����))
        Next
    End With
    
    If arr���.Count > 0 Then
        If MsgBox("���Ÿ���� ����ǰ�� �ֽ��ϴ�.", vbInformation + vbYesNo, "�������") Then
            ���.Show
            Exit Sub
        End If
    End If
    
    
    
    If MsgBox("������ ���", vbInformation + vbYesNo, "������ ���") = vbYes Then
     
    Else
        Sheet1.Select
    End If
End Sub

Private Sub TextBox1_Change()
    If TextBox1 Like "*[!0-9]*" Then eMsg "���ڴ� �Է��� �Ұ����մϴ�.": TextBox1 = "0": Exit Sub
    If Val(TextBox1) > [����Ʈ] Then eMsg "����Ʈ�� �����մϴ�.": TextBox1 = "0": Exit Sub
    If Val(TextBox1) > hap Then TextBox1 = hap & ""
    Call update
End Sub

Private Sub UserForm_Initialize()
    With ListView1
        .View = lvwReport
        .Gridlines = True
        With .ColumnHeaders
            .Add , , "�����ڵ�", 50
            .Add , , "������", 80
            .Add , , "�ǸŰ�", 50
            .Add , , "����", 50
            .Add , , "����", 50
        End With
    End With
    
    
    For Each v In arr����
        arr = v
        With ListView1.ListItems.Add(, , arr(0))
            For i = 1 To 4
                .SubItems(i) = arr(i)
            Next
        End With
    Next
    
    
    For i = 1 To ListView1.ListItems.Count
        hap = hap + Val(ListView1.ListItems(i).SubItems(4))
    Next
    
    TextBox1 = "0"
    
    lbl�ֹ��� = Format(hap, "#,##0")
    lbl����Ʈ = "0"
    lbl�Ѱ����� = lbl�ֹ���
    
End Sub
