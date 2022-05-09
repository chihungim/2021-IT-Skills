Attribute VB_Name = "ī����"
Public ��ī�� As New ArrayList

Function fnī�װ�(rg, key)
    flag = False
    arr = Split(rg, ",")
    For Each i In arr
        If InStr(Sheet3.Cells(Val(i) + 1, 4), key) > 0 Then ' 4 = ����ī�װ�
            flag = True
            Exit For
        End If
    Next
    
    fnī�װ� = flag
    
End Function

Function fnī���̸�(rg, key)
    flag = False
    arr = Split(rg, ",")
    For Each i In arr
        If InStr(Sheet3.Cells(Val(i) + 1, 9), key) > 0 Then ' 9 = ī���̸�
            flag = True
            Exit For
        End If
    Next
    
    fnī���̸� = flag
End Function

Function li(nums, t)
    
    Set ��� = New ArrayList
    num = Split(nums, ",")
    
    If t = "����" Then
        
        For i = 0 To UBound(num)
            txt = Sheets("ī������").Range("D" & num(i) + 1)
            If txt = "�ܽ�,����,����,����" Then
                ���.Add txt
                Exit For
            Else
                arrTxt = Split(txt, ",")
                For Each rg In arrTxt
                    If Not ���.Contains(rg) Then
                        ���.Add rg
                    End If
                Next
            End If
        Next
        
    Else
        
        For i = 0 To UBound(num)
            txt = Sheets("ī������").Range("I" & num(i) + 1)
            If Not ���.Contains(txt) Then
                ���.Add txt
            End If
        Next
        
    End If
    
    li = Join(���.ToArray, ",")
    
End Function

Sub ī����ϱ�(card) ''ī���� ��ȣ�� ����
    If ��ī��.Count = 3 Then
        If MsgBox("�� ����� �ʱ�ȭ �Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
            ��ī��.Clear
        End If
        Exit Sub
    End If
    
    If Not ��ī��.Contains(card) Then
        ��ī��.Add card
        If MsgBox(Sheet2.Range("B" & card + 1) & vbCrLf & "ī�带 �� ��Ͽ� �߰��߽��ϴ�." & vbCrLf & "���� �̵��Ͻðڽ��ϱ�?", vbInformation + vbYesNo) = vbYes Then
            Sheet9.Select
        End If
    Else
        ��ī��.Remove card
        MsgBox "������ ī�带 �� ��Ͽ��� �����߽��ϴ�.", vbInformation
    End If
End Sub
Sub ī���û�ϱ�(card)
    iMsg card
End Sub

Sub ī���ʱ�ȭ()
    Sheet2.Range("AF2:AW2").ClearContents
    For i = 1 To 15
        With Sheet8.Shapes("check" & i)
            .OnAction = "'action""" & Sheet8.Shapes("check" & i).Name & """'"
            With .TextFrame2.TextRange
                .Characters = ""
                .Font.Fill.ForeColor.ObjectThemeColor = msoThemeColorText1
                .Font.Name = "Wingdings 2"
                .Font.Size = 16
            End With
        End With
    Next
    
    Application.ScreenUpdating = False
    
    Call ī�������
    Call ī�庹��
    Call ī�嵥����
    Call shtAction
    
    Application.ScreenUpdating = True
    
End Sub

Sub ī�������()
    On Error Resume Next
    For Each shp In Sheet8.Shapes
        If shp.Name Like "*ī��*" Then
            shp.Delete
        End If
    Next
End Sub

Sub shtAction()
    k = 1
    With Sheet8
        For i = 2 To Sheet2.Range("AD1000").End(xlUp).Row
            If Sheet2.Range("AD" & i) > 0 Then
                .Shapes("ī�� " & k).GroupItems("�� " & Sheet2.Range("A" & i)).OnAction = "'ī����ϱ�""" & Sheet2.Range("A" & i) & """'"
                k = k + 1
            End If
        Next
        
        k = 1
        
        For i = 2 To Sheet2.Range("AD1000").End(3).Row
            If Sheet2.Range("AD" & i) > 0 Then
                .Shapes("ī�� " & k).GroupItems("��û " & Sheet2.Range("A" & i)).OnAction = "'ī���û�ϱ� """ & Sheet2.Range("A" & i) & """'"
                k = k + 1
            End If
        Next
    End With
End Sub

Sub ī�庹��()
    cnt = 0
    On Error Resume Next
    With Sheet2
        For i = 2 To .Range("AD1000").End(xlUp).Row
            If .Range("AD" & i) > 0 Then
                Sheet13.Shapes("ī�� 1").Copy
                Sheet8.Range("D" & 21 + (15 * cnt)).Select
                Sheet8.Paste
                cnt = cnt + 1
            End If
        Next
    End With
    
    Range("D17") = "��" & cnt & "���� ���"
    
    i = 1
    
    For Each shp In Sheet8.Shapes
        If shp.Name Like "*ī��*" Then
            shp.Name = "ī�� " & i
            i = i + 1
        End If
    Next
End Sub

Sub action(���Ǹ� As String)
    With Sheet8.Shapes(���Ǹ�).TextFrame2.TextRange.Characters
        If .Text = ChrW(80) Then
            .Text = ""
            �� = ""
        Else
            .Text = ChrW(80)
            �� = True
        End If
    End With
    
    Select Case ���Ǹ�
        Case "check1": Sheet2.Range("AF2") = ��
        Case "check2": Sheet2.Range("AG2") = ��
        Case "check3": Sheet2.Range("AH2") = ��
        
        Case "check4": Sheet2.Range("AL2") = ��
        Case "check5": Sheet2.Range("AK2") = ��
        Case "check6": Sheet2.Range("AM2") = ��
        Case "check7": Sheet2.Range("AJ2") = ��
        
        Case "check8": Sheet2.Range("AO2") = ��
        Case "check9": Sheet2.Range("AP2") = ��
        
        Case "check10": Sheet2.Range("AW2") = ��
        Case "check11": Sheet2.Range("AR2") = ��
        Case "check12": Sheet2.Range("AV2") = ��
        Case "check13": Sheet2.Range("AT2") = ��
        Case "check14": Sheet2.Range("AS2") = ��
        Case "check15": Sheet2.Range("AU2") = ��
    End Select
    
    Application.ScreenUpdating = False
    
    Call ī�������
    Call ī�庹��
    Call ī�嵥����
    Call shtAction
    
    Application.ScreenUpdating = True

End Sub

Sub ī�嵥����()
    k = 1
    With Sheet2
        For i = 2 To .Range("AD1000").End(xlUp).Row
            If .Range("AD" & i) > 0 Then
                Sheet8.Shapes("ī�� " & k).GroupItems("ī���̹���").Fill.UserPicture ThisWorkbook.Path & "\�����ڷ�\�̹���\" & .Range("A" & i) & ".jpg"
                Sheet8.Shapes("ī�� " & k).GroupItems("����").TextFrame.Characters.Text = .Range("B" & i)
                Sheet8.Shapes("ī�� " & k).GroupItems("����").TextFrame.Characters.Text = .Range("C" & i)
                Sheet8.Shapes("ī�� " & k).GroupItems("����").TextFrame.Characters.Text = Format(.Range("F" & i), "#.#0%")
                Sheet8.Shapes("ī�� " & k).GroupItems("����").TextFrame.Characters.Text = .Range("D" & i) & vbCrLf & li(.Range("E" & i), "����")
                Sheet8.Shapes("ī�� " & k).GroupItems("��ȸ��").TextFrame.Characters.Text = Format(.Range("I" & i), "#,##0") & "��"
                Sheet8.Shapes("ī�� " & k).GroupItems("�ؿ�").TextFrame.Characters.Text = li(.Range("G" & i), "�ؿ�")
                
                Sheet8.Shapes("ī�� " & k).GroupItems("��").Name = "�� " & .Range("A" & i)
                Sheet8.Shapes("ī�� " & k).GroupItems("��û").Name = "��û " & .Range("A" & i)
                k = k + 1
            End If
        Next
    End With
End Sub
