VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���� 
   Caption         =   "����"
   ClientHeight    =   13410
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   20115
   OleObjectBlob   =   "����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim ���� As New ArrayList
Dim cls����(10) As cls����
Dim page, flag

Private Sub CommandButton2_Click()
    If page = 0 Then Exit Sub
    page = page - 1
    ȭ��Init
    Label16 = "- 1 -"
End Sub

Private Sub CommandButton3_Click()
    If page = 1 Then Exit Sub
    page = page + 1
    ȭ��Init
    Label16 = "- 2 -"
End Sub

Private Sub CommandButton4_Click()
    If Val(�������׼�) <> 0 Then
        If qMsg("Ǯ�� ���� ������ �ֽ��ϴ�." & vbCrLf & "�����Ͻðڽ��ϱ�?", vbCritical) = vbYes Then
            iMsg "�����ϼ̽��ϴ�."
            iMsg �հݿ���(0) & ", " & �հݿ���(1)
            ���� = �հݿ���(0): ���� = IIf(�հݿ���(1) = 0, "���հ�", "�հ�")
            Sheet5.Range("G" & [���������ȣ]) = ����
            Sheet5.Range("H" & [���������ȣ]) = ����
            �հ���ȸ_��泻��.Show
        End If
    Else
        If qMsg("�����Ͻðڽ��ϱ�?", vbInformation) = vbYes Then
            iMsg "�����ϼ̽��ϴ�."
            iMsg �հݿ���(0) & ", " & �հݿ���(1)
            ���� = �հݿ���(0): ���� = IIf(�հݿ���(1) = 0, "���հ�", "�հ�")
            Sheet5.Range("G" & [���������ȣ]) = ����
            Sheet5.Range("H" & [���������ȣ]) = ����
            �հ���ȸ_��泻��.Show
        End If
    End If
End Sub

Function �հݿ���()
    ���� = 0
    k = 0
    For i = 1 To 10
        If cls����(i).sans = Val(����(k)(1, 4)) Then
            ���� = ���� + 10
        End If
        k = k + 1
    Next
    
    �հ� = 0
    If ���� >= Val(Sheet3.Range("G" & ���� + 1)) Then �հݿ��� = 1
    
    �հݿ��� = Array(����, �հ�)
End Function

Private Sub Frame2_Click()

End Sub

Private Sub UserForm_Activate()
     ���� = [���������ȣ].Cells(1, 2) + 1

    Call sTime(Val(Sheet10.Range("Q1")))
    Call ����_�ð�
    
    Frame1.Caption = Sheet3.Range("B" & ����)
    With Sheet7
        For i = 2 To .Range("A1000").End(3).Row
            If .Range("B" & i) = ���� Then ����.Add .Range("A" & i)
        Next
    End With
    
    shuffle
    
    �������׼� = Val(Sheet3.Range("F" & ����))
    
    k = 0
    For i = 1 To Val(Sheet3.Range("F" & ����))
        Set cls����(i) = New cls����
        
        Call cls����(i).boxInit(12 + ((k Mod 3) * 192), 12 + ((k \ 3) * 390))
        Call cls����(i).init(i & ". " & ����(i - 1)(1, 3), Sheet8.Range("B" & Val(����(i - 1))))
        
        k = IIf(k = 5, 0, k + 1)
    Next
    
    k = 0
    For i = 1 To Val(Sheet3.Range("F" & ����))
        Call cls����(i).opInit(90 + (k * 36), 42, i)
        k = k + 1
    Next
End Sub

Private Sub UserForm_Initialize()
    
   
    
End Sub

Sub ȭ��Init()
    
    idx = (page) * 6 + 1
    
    For i = 1 To 10
        cls����(i).frm.Visible = False
    Next
    
    For i = idx To idx + 5
        cls����(i).frm.Visible = True
        If i >= 10 Then Exit For
    Next
    
End Sub

Sub shuffle()
    Randomize
    cnt = ����.Count - 1
    For i = 0 To cnt
        k = Int((cnt - i) * Rnd + i)
        Dim tmp As Range
        Set tmp = ����(i)
        ����(i) = ����(k)
        ����(k) = tmp
    Next
End Sub

Private Sub UserForm_Terminate()
    Call �ð����߱�("����_�ð�")
End Sub
