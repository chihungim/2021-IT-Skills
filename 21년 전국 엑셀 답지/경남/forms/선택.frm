VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} ���� 
   Caption         =   "����"
   ClientHeight    =   4635
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   7515
   OleObjectBlob   =   "����.frx":0000
   StartUpPosition =   1  '������ ���
End
Attribute VB_Name = "����"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False


Private Sub CheckBox1_BeforeUpdate(ByVal Cancel As MSForms.ReturnBoolean)
    For i = 1 To 6
        Me.Controls("ToggleButton" & i) = True
    Next
    
    Debug.Print CheckBox1
    If Not CheckBox1 Then
        For i = 1 To 6
            Me.Controls("ToggleButton" & i) = False
        Next
    End If
    
End Sub

Private Sub CheckBox1_Click()
    
End Sub

Private Sub CommandButton1_Click()
    With Sheet4
        
        Set arr = New ArrayList
        For i = 1 To 6
            If Me.Controls("ToggleButton" & i) Then
                arr.Add i
            End If
        Next
        
        .ListObjects("ǥ3").Range.AutoFilter Field:=2, Criteria1:=arr.toArray, Operator:=xlFilterValues
        .Select
    End With
End Sub

Private Sub ToggleButton1_Click()
    Call ��üüũ
End Sub
Private Sub ToggleButton2_Click()
    Call ��üüũ
End Sub
Private Sub ToggleButton3_Click()
    Call ��üüũ
End Sub
Private Sub ToggleButton4_Click()
    Call ��üüũ
End Sub
Private Sub ToggleButton5_Click()
    Call ��üüũ
End Sub
Private Sub ToggleButton6_Click()
    Call ��üüũ
End Sub

Private Sub UserForm_Click()

End Sub

Sub ��üüũ()
    If ToggleButton1 And ToggleButton2 And ToggleButton3 And ToggleButton4 And ToggleButton5 And ToggleButton6 Then
        CheckBox1 = True
    Else
        CheckBox1 = False
    End If
End Sub
