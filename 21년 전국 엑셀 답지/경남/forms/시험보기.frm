VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���躸�� 
   Caption         =   "UserForm1"
   ClientHeight    =   2190
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   10815
   OleObjectBlob   =   "���躸��.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "���躸��"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim r

Private Sub CommandButton1_Click()
    [���������ȣ] = r - 1
    
End Sub

Private Sub UserForm_Activate()
    With Sheet5
        For i = 2 To .Range("A10000").End(3).Row
            If .Range("B" & i) = [��ȣ] And .Range("G" & i) = "" Then
                r = i
                Exit For
            End If
        Next
        
        Label1 = Format(Now, "yyyy�� MM�� dd�� HH:mm:ss")
        Label2 = .Range("I" & r) & " / " & .Range("E" & r) & " / " & Format(.Range("K" & r), "h:mm")
        testableTime = DateValue(Sheet5.Range("L" & r)) + TimeValue(Sheet5.Range("L" & r)) - TimeValue("00:10:00")
        
        If Now > DateValue(Sheet5.Range("L" & r)) + TimeValue(Sheet5.Range("L" & r)) + TimeValue("0:" & Val(Sheet5.Range("M" & r)) & ":0") Then
            eMsg "���� �ð��� ���� ��� ó�� �Ǿ����ϴ�."
            Unload ���躸��
        Else
            Call sTime(Val(.Range("M" & r)))
            Call ���躸��_�ð�
        End If
 
        
    End With
End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()

    

    
    
    
    
End Sub

Private Sub UserForm_Terminate()
    Call �ð����߱�("���躸��_�ð�")
End Sub
