VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 패키지생성 
   Caption         =   "패키지생성"
   ClientHeight    =   7185
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   9240
   OleObjectBlob   =   "패키지생성.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "패키지생성"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim sel
Dim d As DataObject

Private Sub CommandButton1_Click()
    
    Set fesArr = New ArrayList
    Set touArr = New ArrayList
    
    With TreeView1
        For i = 1 To .Nodes.Count
            If Not (.Nodes(i).key = "축제" Or .Nodes(i).key = "관광지") And .Nodes(i).Checked Then
                If .Nodes(i).Parent.Text = "축제" Then
                    fesArr.Add i
                Else
                    touArr.Add i
                End If
            End If
        Next
        
        fesArr.Reverse
        touArr.Reverse
        
        For Each i In fesArr
            .Nodes.Remove i
        Next
        For Each i In touArr
            .Nodes.Remove i
        Next
        
    End With
    
End Sub

Private Sub CommandButton2_Click()
    
    If TextBox1 = "" Then eMsg "패키지 이름을 입력해주셔야 합니다.": Exit Sub
    
    fesFlag = False: touFlag = False
    Set fesArr = New ArrayList
    Set touArr = New ArrayList
    
    With TreeView1
        For i = 1 To .Nodes.Count
            If Not (.Nodes(i).key = "축제" Or .Nodes(i).key = "관광지") And .Nodes(i).Checked Then
                If .Nodes(i).Parent.Text = "축제" Then
                    fesArr.Add Int(Split(.Nodes(i).key, ",")(1))
                    fesFlag = True
                Else
                    touArr.Add Int(Split(.Nodes(i).key, ",")(1))
                    touFlag = True
                End If
            End If
        Next
        
    End With
    
    If Not fesFlag Or Not touFlag Then eMsg "축제/관광지는 적어도 1개씩은 있어야 합니다.": Exit Sub
    
    iMsg "새로운 패키지가 생성되었습니다."
    With Sheet6
        lrow = .Range("A1000").End(3).row + 1
        .Range("A" & lrow & ":D" & lrow) = Array(lrow - 1, TextBox1, CStr(Join(fesArr.ToArray, ", ")), CStr(Join(touArr.ToArray, ", ")))
        Call 금액구하기
        Unload Me
    End With
    
    
End Sub

Private Sub ListBox1_BeforeDragOver(ByVal Cancel As MSForms.ReturnBoolean, ByVal Data As MSForms.DataObject, ByVal X As Single, ByVal Y As Single, ByVal DragState As MSForms.fmDragState, ByVal Effect As MSForms.ReturnEffect, ByVal Shift As Integer)
    Cancel = True
End Sub

Private Sub ListBox1_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    
    sel = 1
    
    If Button = 1 Then
        Set d = New DataObject
        d.SetText ListBox1.Value
        Effect = d.StartDrag
    End If
    
End Sub

Private Sub ListBox2_BeforeDragOver(ByVal Cancel As MSForms.ReturnBoolean, ByVal Data As MSForms.DataObject, ByVal X As Single, ByVal Y As Single, ByVal DragState As MSForms.fmDragState, ByVal Effect As MSForms.ReturnEffect, ByVal Shift As Integer)
    Cancel = True
End Sub

Private Sub ListBox2_MouseMove(ByVal Button As Integer, ByVal Shift As Integer, ByVal X As Single, ByVal Y As Single)
    
    sel = 2
    
    If Button = 1 Then
        Set d = New DataObject
        d.SetText ListBox2.Value
        Effect = d.StartDrag
    End If
    
End Sub

Private Sub TreeView1_DblClick()
    
    값 = TreeView1.SelectedItem.Text
    
    If TreeView1.SelectedItem.Parent Is Nothing Then Exit Sub
    
    With 사진
        If TreeView1.SelectedItem.Parent = "축제" Then
            사진.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\image\축제\" & TreeView1.SelectedItem & ".jpg")
        ElseIf TreeView1.SelectedItem.Parent = "관광지" Then
            사진.Image1.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\image\관광지\" & TreeView1.SelectedItem & ".jpg")
        Else
            Exit Sub
        End If
        .Show
    End With
    
End Sub

Private Sub TreeView1_NodeCheck(ByVal Node As MSComctlLib.Node)
    
    If Node.Index = 1 Then
    
        Set fesNode = Node.child
        For i = 1 To Node.Children
            fesNode.Checked = TreeView1.Nodes(1).Checked
            Set fesNode = fesNode.Next
        Next
    
    ElseIf Node.Index = 2 Then
    
        Set touNode = Node.child
        For i = 1 To Node.Children
            touNode.Checked = TreeView1.Nodes(2).Checked
            Set touNode = touNode.Next
        Next
    
    End If
    
End Sub

Private Sub TreeView1_OLEDragDrop(Data As MSComctlLib.DataObject, Effect As Long, Button As Integer, Shift As Integer, X As Single, Y As Single)

    Effect = 1

    If sel = 1 Then
        TreeView1.Nodes.Add "축제", tvwChild, "축제," & dic축제(d.GetText).Cells(1, -1), d.GetText
        TreeView1.Nodes(1).Expanded = True
    Else
        TreeView1.Nodes.Add "관광지", tvwChild, "관광지," & dic관광지(d.GetText).Cells(1, -1), d.GetText
        TreeView1.Nodes(2).Expanded = True
    End If
End Sub

Private Sub TreeView1_OLEDragOver(Data As MSComctlLib.DataObject, Effect As Long, Button As Integer, Shift As Integer, X As Single, Y As Single, State As Integer)

    Effect = 1

End Sub

Private Sub UserForm_Click()

End Sub

Private Sub UserForm_Initialize()
    
    For i = 2 To Sheet5.Range("A1000").End(3).row
        ListBox1.AddItem Sheet5.Range("C" & i)
    Next
    For i = 2 To Sheet3.Range("A1000").End(3).row
        ListBox2.AddItem Sheet3.Range("C" & i)
    Next
    
    With TreeView1.Nodes
        .Add key:="축제", Text:="축제"
        .Add key:="관광지", Text:="관광지"
    End With
    
    mkdic dic패키지, Sheet6.Range("B2")
    mkdic dic관광지, Sheet3.Range("C2")
    mkdic dic축제, Sheet5.Range("C2")
    
End Sub
