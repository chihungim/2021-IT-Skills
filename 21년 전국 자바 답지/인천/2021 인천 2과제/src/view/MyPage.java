package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class MyPage extends BasePage {
	JPanel page;
	String bcap[] = "���ų���,��������".split(",");

	public MyPage() {
		add(page = new JPanel(new GridBagLayout()));
		page.setBackground(Color.WHITE);

		var c = new JPanel(new GridLayout(1, 0, 10, 5));
		page.add(c);

		for (int i = 0; i < bcap.length; i++) {
			c.add(sz(btn(bcap[i], a -> {
				if (a.getActionCommand().equals("���ų���")) {
					mf.swapPage(new OrderLogPage());
				} else {
					mf.swapPage(new EditProfilePage());
				}
			}), 150, 150));
		}
	}
}
