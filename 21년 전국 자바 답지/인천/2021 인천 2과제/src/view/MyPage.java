package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class MyPage extends BasePage {
	JPanel page;
	String bcap[] = "구매내역,정보수정".split(",");

	public MyPage() {
		add(page = new JPanel(new GridBagLayout()));
		page.setBackground(Color.WHITE);

		var c = new JPanel(new GridLayout(1, 0, 10, 5));
		page.add(c);

		for (int i = 0; i < bcap.length; i++) {
			c.add(sz(btn(bcap[i], a -> {
				if (a.getActionCommand().equals("구매내역")) {
					mf.swapPage(new OrderLogPage());
				} else {
					mf.swapPage(new EditProfilePage());
				}
			}), 150, 150));
		}
	}
}
