VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Activity"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
Public title, rdate, deal, amount, pay, detail, delivery

Public Sub init(title, rdate, deal, amount, pay, detail, delivery)
    Me.title = title
    Me.rdate = rdate
    Me.deal = deal
    Me.amount = amount
    Me.pay = pay
    Me.detail = detail
    Me.delivery = delivery
End Sub

