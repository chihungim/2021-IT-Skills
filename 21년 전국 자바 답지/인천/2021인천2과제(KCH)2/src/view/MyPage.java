package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import tools.Tools;

public class MyPage extends BasePage {

	JPanel page;

	String bcap[] = "���ų���,��������".split(",");

	public MyPage() {
		add(page = new JPanel(new GridBagLayout()));
		page.setBackground(Color.WHITE);

		var c = new JPanel(new GridLayout(1, 0, 10, 5));
		page.add(c);

		for (int i = 0; i < bcap.length; i++) {
			c.add(Tools.size(Tools.btn(bcap[i], a -> {
				if (a.getActionCommand().equals("���ų���")) {
					mf.addPage(new OrderLogPage());
				} else {
					mf.addPage(new EditProfilePage());
				}
			}), 150, 150));
		}

	}
}
