Attribute VB_Name = "Module2"
Sub �α׾ƿ�()
    [��ȣ] = ""
End Sub

Sub ��()
    s = 1260
    iMsg Round(s / 100) * 100
End Sub

Sub ����()
    ��������.Show
End Sub

Sub ����(r)
    ������ȣ = Val(Sheet7.Range("A" & r))
    Set rg = Sheet8.Range("A:A").Find(what:=������ȣ, LookAt:=xlWhole)
    
    Sheet8.Rows(rg.Row).Delete
    Sheet7.Rows(r).Delete
End Sub
