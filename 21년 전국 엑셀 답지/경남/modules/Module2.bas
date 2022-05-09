Attribute VB_Name = "Module2"
Sub 로그아웃()
    [번호] = ""
End Sub

Sub 텟()
    s = 1260
    iMsg Round(s / 100) * 100
End Sub

Sub 출제()
    문제출제.Show
End Sub

Sub 삭제(r)
    문제번호 = Val(Sheet7.Range("A" & r))
    Set rg = Sheet8.Range("A:A").Find(what:=문제번호, LookAt:=xlWhole)
    
    Sheet8.Rows(rg.Row).Delete
    Sheet7.Rows(r).Delete
End Sub
