package view;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MyPage extends BasePage {
	JPanel page;
	String bcap[] = "���ų���,��������".split(",");

	public MyPage() {
		add(page = new JPanel(new GridBagLayout()));
		page.add(c = new JPanel(new GridLayout(1, 0, 10, 5)));
		for (int i = 0; i < bcap.length; i++) {
			c.add(btn(bcap[i], a -> {
				if (a.getActionCommand().equals("���ų���")) {
					mf.swapPage(new OrderLogPage());
				} else {
					mf.swapPage(new EditProfilePage());
				}
			})); 
		}
	}
}
