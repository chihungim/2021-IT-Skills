package ������;

import java.awt.GridLayout;

public class MyPage extends BaseFrame {

	String cap[] = "�ֹ�����,�������,�������ܰ���,���� ������".split(",");

	public MyPage() {
		super("���� ������", 300, 300);
		setLayout(new GridLayout(0, 1));
		for (String b : cap) {
			add(btn(b, a -> {
				if (a.getActionCommand().equals("�ֹ�����")) {
					new OrderList().addWindowListener(new Before(this));
				} else if (a.getActionCommand().equals("�������")) {
					new Review().addWindowListener(new Before(this));

				} else if (a.getActionCommand().equals("�������ܰ���")) {
					new PaymentManage().addWindowListener(new Before(this));

				} else if (a.getActionCommand().equals("���� ������")) {
					new Favorite().addWindowListener(new Before(this));

				}
			}));
		}
		setVisible(true);
	}

	public static void main(String[] args) {
		new MyPage();
	}
}
