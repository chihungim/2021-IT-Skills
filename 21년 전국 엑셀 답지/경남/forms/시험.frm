VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} 시험 
   Caption         =   "시험"
   ClientHeight    =   13410
   ClientLeft      =   120
   ClientTop       =   465
   ClientWidth     =   20115
   OleObjectBlob   =   "시험.frx":0000
   StartUpPosition =   1  '소유자 가운데
End
Attribute VB_Name = "시험"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim 문제 As New ArrayList
Dim cls시험(10) As cls시험
Dim page, flag

Private Sub CommandButton2_Click()
    If page = 0 Then Exit Sub
    page = page - 1
    화면Init
    Label16 = "- 1 -"
End Sub

Private Sub CommandButton3_Click()
    If page = 1 Then Exit Sub
    page = page + 1
    화면Init
    Label16 = "- 2 -"
End Sub

Private Sub CommandButton4_Click()
    If Val(남은문항수) <> 0 Then
        If qMsg("풀지 않은 문제가 있습니다." & vbCrLf & "제출하시겠습니까?", vbCritical) = vbYes Then
            iMsg "수고하셨습니다."
            iMsg 합격여부(0) & ", " & 합격여부(1)
            점수 = 합격여부(0): 여부 = IIf(합격여부(1) = 0, "불합격", "합격")
            Sheet5.Range("G" & [시험종목번호]) = 점수
            Sheet5.Range("H" & [시험종목번호]) = 여부
            합격조회_취득내역.Show
        End If
    Else
        If qMsg("제출하시겠습니까?", vbInformation) = vbYes Then
            iMsg "수고하셨습니다."
            iMsg 합격여부(0) & ", " & 합격여부(1)
            점수 = 합격여부(0): 여부 = IIf(합격여부(1) = 0, "불합격", "합격")
            Sheet5.Range("G" & [시험종목번호]) = 점수
            Sheet5.Range("H" & [시험종목번호]) = 여부
            합격조회_취득내역.Show
        End If
    End If
End Sub

Function 합격여부()
    점수 = 0
    k = 0
    For i = 1 To 10
        If cls시험(i).sans = Val(문제(k)(1, 4)) Then
            점수 = 점수 + 10
        End If
        k = k + 1
    Next
    
    합격 = 0
    If 점수 >= Val(Sheet3.Range("G" & 종목 + 1)) Then 합격여부 = 1
    
    합격여부 = Array(점수, 합격)
End Function

Private Sub Frame2_Click()

End Sub

Private Sub UserForm_Activate()
     종목 = [시험종목번호].Cells(1, 2) + 1

    Call sTime(Val(Sheet10.Range("Q1")))
    Call 시험_시간
    
    Frame1.Caption = Sheet3.Range("B" & 종목)
    With Sheet7
        For i = 2 To .Range("A1000").End(3).Row
            If .Range("B" & i) = 종목 Then 문제.Add .Range("A" & i)
        Next
    End With
    
    shuffle
    
    남은문항수 = Val(Sheet3.Range("F" & 종목))
    
    k = 0
    For i = 1 To Val(Sheet3.Range("F" & 종목))
        Set cls시험(i) = New cls시험
        
        Call cls시험(i).boxInit(12 + ((k Mod 3) * 192), 12 + ((k \ 3) * 390))
        Call cls시험(i).init(i & ". " & 문제(i - 1)(1, 3), Sheet8.Range("B" & Val(문제(i - 1))))
        
        k = IIf(k = 5, 0, k + 1)
    Next
    
    k = 0
    For i = 1 To Val(Sheet3.Range("F" & 종목))
        Call cls시험(i).opInit(90 + (k * 36), 42, i)
        k = k + 1
    Next
End Sub

Private Sub UserForm_Initialize()
    
   
    
End Sub

Sub 화면Init()
    
    idx = (page) * 6 + 1
    
    For i = 1 To 10
        cls시험(i).frm.Visible = False
    Next
    
    For i = idx To idx + 5
        cls시험(i).frm.Visible = True
        If i >= 10 Then Exit For
    Next
    
End Sub

Sub shuffle()
    Randomize
    cnt = 문제.Count - 1
    For i = 0 To cnt
        k = Int((cnt - i) * Rnd + i)
        Dim tmp As Range
        Set tmp = 문제(i)
        문제(i) = 문제(k)
        문제(k) = tmp
    Next
End Sub

Private Sub UserForm_Terminate()
    Call 시간멈추기("시험_시간")
End Sub
