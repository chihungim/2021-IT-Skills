package view;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AdminMain extends BaseFrame {
	JTabbedPane tab = new JTabbedPane();
	String cap[] = "사용자 관리,추천 여행지 관리,일정 관리,예매 관리".split(",");
	JPanel p[] = { new UserManage(), new Recommend(), new Scehdule(), new ReserveManage() };

	public AdminMain() {
		super("관리자", 1000, 800);
		ui();
		setVisible(true);
	}

	void ui() {
		add(tab);
		for (int i = 0; i < cap.length; i++) {
			tab.add(p[i]);
			tab.setTitleAt(i, cap[i]);
			p[i].setOpaque(true);
		}

		tab.setTabPlacement(JTabbedPane.LEFT);
		tab.add("테마", null);
		tab.add("로그아웃", null);

		if (tMode == true) {
			tab.setBackgroundAt(4, Color.DARK_GRAY);
			tab.setForegroundAt(4, Color.WHITE);
		}

		tab.addChangeListener(e -> {
			if (tab.getSelectedIndex() == 4) {
				tMode = !tMode;
				themeMode();

				repaint();
				revalidate();
			}

			if (tab.getSelectedIndex() == 5) {
				dispose();
			}
		});
	}

	public static void main(String[] args) {
		new AdminMain();
	}
}
