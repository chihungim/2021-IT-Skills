Attribute VB_Name = "Module3"
Public stopTime As Date
Public startTime As Date
Public ���ѽð� As Integer
Public testableTime

Sub sTime(t)
    startTime = Now
    ���ѽð� = t
End Sub

Sub ���躸��_�ð�()
    
    stopTime = Now + TimeValue("0:0:1")
    ���躸��.Label1 = Format(stopTime, "yyyy�� MM�� dd�� HH:mm:ss")
    If stopTime >= testableTime Then
        ���躸��.CommandButton1.Enabled = True
    Else
        ���躸��.CommandButton1.Enabled = False
    End If
 
    Application.OnTime stopTime, "���躸��_�ð�"
End Sub

Sub ����_�ð�()
    stopTime = Now + TimeValue("0:0:1")
    If Minute(stopTime - startTime) >= ���ѽð� Then eMsg "����ð��� ����Ǿ� ����� ����Ǿ����ϴ�.": iMsg "�����ϼ̽��ϴ�.": �հ���ȸ_��泻��.Show: Exit Sub
    ����.lbl�ð�.Caption = "����ð� : " & Format(Minute(stopTime - startTime), "00�� ") & Format(Second(stopTime - startTime), "00��")
    'Debug.Print Format(stopTime - startTime, "MM:ss")
    Application.OnTime stopTime, "����_�ð�"
End Sub

Sub �ð����߱�(modname)
    On Error Resume Next
    Application.OnTime stopTime, modname, Schedule:=False
End Sub
