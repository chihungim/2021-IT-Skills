VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �α��� 
   Caption         =   "�α���"
   ClientHeight    =   1305
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4275
   OleObjectBlob   =   "�α���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�α���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Dim loginCnt

Private Sub CommandButton1_Click()

    Set ȸ������ = mkdic(Sheet2.Range("C2"))
        
    If TextBox1 = "" Or TextBox2 = "" Then
        eMsg "��ĭ�� �ֽ��ϴ�"
        Exit Sub
    End If
    
    If Not ȸ������.exists(TextBox1.Text) Then
        eMsg "���̵� ��ġ���� �ʽ��ϴ�."
        Exit Sub
    End If
    
    If Not ȸ������(TextBox1.Text).Cells(1, 2) = TextBox2.Text Then
        If loginCnt = 3 Then
            loginCnt = 0
            Dim yn: yn = MsgBox("��й�ȣ�� 3ȸ �̻� �߸��Է��ϼ̽��ϴ�." & vbLf & "�α����� ���� �ӽ� ��й�ȣ�� �߱޹����ðڽ��ϱ�?", vbCritical + vbOKCancel)
  
            If yn = vbOK Then
                ��� = randPW
                �̸� = ȸ������(TextBox1.Text).Cells(1, 0)
                ȸ������(TextBox1.Text).Cells(1, 2) = ���
                iMsg �̸� & "���� �ӽ� ��й�ȣ�� " & ��� & "�Դϴ�."
            End If
            
            Exit Sub
        End If
        eMsg "��й�ȣ�� ��ġ���� �ʽ��ϴ�."
        loginCnt = loginCnt + 1
        Exit Sub
    End If
    
    With Sheet1
        .CommandButton1.Caption = "�α׾ƿ�"
        .CommandButton2.Caption = "ȸ������ ����"
        .CommandButton3.Enabled = True
        .CommandButton4.Enabled = True
        .CommandButton5.Enabled = True
        
        Dim ��ȣ
        Dim ����
        
        With ȸ������(TextBox1.Text)
            ��ȣ = .Cells(1, -1): �̸� = .Cells(1, 0): ���� = .Cells(1, 4)
        End With
    
        With Sheet1
            .Cells(1, "A") = �̸�
            .Cells(1, "B") = ��ȣ
            .Cells(1, "C") = ����
        End With
    
    
        If .Cells(1, 3) = "ȭ��" Then
            Sheet1.CommandButton3.Caption = "��ǰ ����"
        Else
            Sheet1.CommandButton3.Caption = "���ų��� ����"
        End If
        
    End With
    
    iMsg �̸� & "������ �α��εǾ����ϴ�."
    
    stateChange
    
    Unload Me
End Sub

Function randPW() As String

   passw = ""
    
    For i = 0 To 3
        passw = passw + Chr(Int(Rnd * 26) + 97)
    Next
    
    For i = 0 To 2
        passw = passw & Int(Rnd * 10)
    Next
    
    a = Int(Rnd * 8)
    
    specialCh = Array("!", "@", "#", "$", "%", "^", "&", "*")
    
    randPW = passw & specialCh(a)
End Function


Private Sub Label2_Click()

End Sub

Private Sub UserForm_Click()

End Sub
