package 광광주;

import java.awt.GridLayout;

public class MyPage extends BaseFrame {

	String cap[] = "주문내역,리뷰관리,결제수단관리,찜한 음식점".split(",");

	public MyPage() {
		super("마이 페이지", 300, 300);
		setLayout(new GridLayout(0, 1));
		for (String b : cap) {
			add(btn(b, a -> {
				if (a.getActionCommand().equals("주문내역")) {
					new OrderList().addWindowListener(new Before(this));
				} else if (a.getActionCommand().equals("리뷰관리")) {
					new Review().addWindowListener(new Before(this));

				} else if (a.getActionCommand().equals("결제수단관리")) {
					new PaymentManage().addWindowListener(new Before(this));

				} else if (a.getActionCommand().equals("찜한 음식점")) {
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
