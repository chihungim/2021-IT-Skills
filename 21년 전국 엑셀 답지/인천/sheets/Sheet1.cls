VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Dim num
Dim 작품분류 As Object
Dim img(5) As Class1

Private Sub ComboBox1_Change()
    If ComboBox1.ListIndex = 0 Then
        Sheet3.Range("T2") = ""
    Else
        Sheet3.Range("T2") = ComboBox1
    End If

    With Sheet3
        .Range("X2:AD10000").ClearContents
        .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("T1:T2"), .Range("X1:AF1"), False
        .Range("X2:AF10000").Sort key1:=.Range("AF1"), order1:=xlDescending
    End With

    setImg
End Sub

Private Sub CommandButton1_Click()
    If CommandButton1.Caption = "로그인" Then
        로그인.Show
    Else
        CommandButton1.Caption = "로그인"
        CommandButton2.Caption = "회원가입"
        CommandButton3.Enabled = False
        CommandButton4.Enabled = False
        CommandButton5.Enabled = False
        Range("A1:C1") = ""
        Range("N4") = "로그인 필요"
    End If
End Sub

Sub setImg()
    Application.ScreenUpdating = False
    
    For i = 1 To 5
        Set img(i) = New Class1
        Set img(i).img = OLEObjects.Item("Image" & i).Object
    Next
    
    
    If ComboBox1.ListIndex = 0 Then
        Set 작품분류 = CreateObject("Scripting.Dictionary")
        
        With Sheet3
            For i = 2 To .Range("A1000").End(xlUp).Row
                If Not 작품분류.exists(.Range("B" & i).Value) Then
                    작품분류.Add .Range("B" & i).Value, ""
                End If
            Next
            
            Imageidx = 1
            
            For Each 분류명 In 작품분류.keys
                .Range("T2") = 분류명
                .Range("A1").CurrentRegion.AdvancedFilter xlFilterCopy, .Range("T1:T2"), .Range("X1:AF1"), False
                .Range("X2:AF10000").Sort key1:=.Range("AF1"), order1:=xlDescending
                OLEObjects.Item("Image" & Imageidx).Object.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & .Range("Y2").Value & "\" & .Range("X2").Value & ".jpg")
                nm = .Range("X2") & "," & .Range("Y2") & "," & .Range("Z2") & "," & .Range("AA2") & "," & .Range("AC2") & "," & .Range("AE2") & "," & .Range("AF2")
                img(Imageidx).nm = nm
                OLEObjects.Item("Label" & Imageidx).Object.Caption = "<" & .Range("Y2") & "> " & .Range("Z2")
                Imageidx = Imageidx + 1
            Next
            
            .Range("T2") = ""
        End With
        
        Else
            With Sheet3
                For i = 2 To 6
                    번호 = .Range("X" & i): 분류 = .Range("Y" & i): 이름 = .Range("Z" & i)
                    OLEObjects.Item("Image" & (i - 1)).Object.Picture = LoadPicture(ThisWorkbook.Path & "\지급자료\이미지\" & 분류 & "\" & 번호 & ".jpg")
                    OLEObjects.Item("Label" & (i - 1)).Object.Caption = "<" & (i - 1) & "위> " & 이름
                Next
            End With
    End If
End Sub

Private Sub CommandButton2_Click()
    회원가입수정.Show
End Sub

Private Sub CommandButton3_Click()
    If CommandButton3.Caption = "작품 제작" Then
        작품제작.Show
    Else
        구매내역.Show
    End If
End Sub

Private Sub CommandButton4_Click()
    검색.Show
End Sub

Private Sub CommandButton5_Click()
    전시회일정.Show
End Sub

Private Sub CommandButton6_Click()
    ThisWorkbook.Save
    ThisWorkbook.Close
End Sub

Private Sub Worksheet_Activate()
    ComboBox1.ListIndex = 0
    setImg
End Sub
