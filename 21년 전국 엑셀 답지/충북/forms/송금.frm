VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �۱� 
   Caption         =   "�۱�"
   ClientHeight    =   7080
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9015
   OleObjectBlob   =   "�۱�.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�۱�"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Public state As Boolean
Private Sub CommandButton1_Click()
    
    search TextBox1
    
End Sub

Sub search(txt)
    
    For i = 0 To 2
        With Sheet1
            .Cells(2 + i, 12 + i) = "*" & txt & "*"
            .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("L1:N4"), .Range("P1:Q1")
            ListBox1.RowSource = "ȸ�����!P2:Q" & Sheet1.Range("A1000").End(3).Row
        End With
    Next
    
End Sub

Private Sub CommandButton2_Click()
    If lbl�̸� = "" Or lbl���¹�ȣ = "" Then eMsg "�۱��� ȸ���� �����ؾ� �մϴ�.": Exit Sub
    
    If TextBox2 = "" Then eMsg "�۱��� �ݾ��� �Է��ؾ� �մϴ�.": Exit Sub
    
    If Not IsNumeric(TextBox2) Then eMsg "�ݾ��� ���ڷθ� �Է��ؾ� �մϴ�.": Exit Sub
    
    If lbl�̸�.Caption = [�̸�] Then eMsg "�ڱ� �ڽſ��� ���� ���� �� �����ϴ�.": Exit Sub
    
    If [�ܾ�] < Val(TextBox2) Then eMsg "�ܾ��� �����մϴ�.": Exit Sub
    
    pw = ""
    ��й�ȣ.Show
    
    If Not ��й�ȣflag Then eMsg "2�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.": Exit Sub
    
    If MsgBox(lbl�̸� & "�Կ��� ���� �۱��Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
        iMsg "�۱��� �Ϸ�Ǿ����ϴ�."
        mkdic dic, Sheet1.Range("D2")
        no = dic(�۱�.lbl�̸�.Caption).Cells(1, -2)
                
        Sheet1.Cells([��ȣ] + 1, "J") = Int(Sheet1.Cells([��ȣ] + 1, "J")) - Int(�۱�.TextBox2)
        Sheet1.Cells(no + 1, "J") = Int(Sheet1.Cells(no + 1, "J")) + Int(�۱�.TextBox2)
               
               
        With Sheet6
            lrow = .Range("A1000").End(3).Row + 1
            .Range("A" & lrow & ":J" & lrow) = Array(lrow - 1, Now, [��ȣ], [����], lbl���¹�ȣ, "", "", Val(TextBox2), 1, 1)
        End With
             
        Unload Me
    End If
    
End Sub

Private Sub ListBox1_Click()
    lbl�̸� = ListBox1.list(ListBox1.ListIndex, 0)
    lbl���¹�ȣ = ListBox1.list(ListBox1.ListIndex, 1)
End Sub

Private Sub UserForm_Initialize()
    
    search ("")
    state = False
    
End Sub

