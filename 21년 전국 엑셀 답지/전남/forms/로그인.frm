VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} �α��� 
   Caption         =   "�α���"
   ClientHeight    =   1440
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   4560
   OleObjectBlob   =   "�α���.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "�α���"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim cnt
Private Sub CommandButton1_Click()
    uid = TextBox1: upw = TextBox2
    
    mkDic dicȸ��, Sheet2.Range("C2")
    
    If uid = "" And upw = "" Then eMsg "��ĭ�� �����մϴ�.": Exit Sub
    If uid = "admin" Then
        
        If upw = "1234" Then
            iMsg "�����ڷ� �α��εǾ����ϴ�.": Call �����ڷα���: Unload Me: Exit Sub
        Else
            If cnt = 2 Then TextBox2 = "1234": cnt = 0
            cnt = cnt + 1
            Exit Sub
        End If
    End If
    
    If Not dicȸ��.exists(uid) Then eMsg "��ġ�ϴ� ���̵� �����ϴ�.": TextBox1 = "": TextBox2 = "": TextBox1.SetFocus: Exit Sub
    If dicȸ��(uid).Cells(1, 2) <> upw Then
        If cnt = 2 Then tmpPW = �ӽú������: eMsg "��й�ȣ�� ����Ǿ����ϴ�." & vbCrLf & "����� ��й�ȣ [" & tmpPW & "]": cnt = 0: Exit Sub
        cnt = cnt + 1
    End If
    
    iMsg dicȸ��(uid).Cells(1, 0) & "�� ȯ���մϴ�."
    [��ȣ] = dicȸ��(uid).Cells(1, -1)
    Call �����α���
    Unload Me

End Sub

Function �ӽú������()
    Set �빮�� = New ArrayList
    Set �ҹ��� = New ArrayList
    ���� = Split("0 1 2 3 4 5 6 7 8 9")
    Ư���� = Split("! @ # $ ?")
    
    For i = Asc("A") To Asc("Z")
        �빮��.Add Chr(i)
    Next
    
    For i = Asc("a") To Asc("z")
        �ҹ���.Add Chr(i)
    Next
    
    Randomize
    �ӽú�� = �빮��(Int(Rnd * 10) + 15) & �ҹ���(Int(Rnd * 10) + 15) & �ҹ���(Int(Rnd * 10) + 15) & �ҹ���(Int(Rnd * 10) + 15) & ����(Int(Rnd * 10)) & Ư����(Int(Rnd * 5))
    �ӽú������ = �ӽú��
End Function

Private Sub Label2_Click()

End Sub

Private Sub UserForm_Initialize()
    cnt = 0
End Sub
