import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AdminMain extends BaseFrame {
	JTabbedPane tab = new JTabbedPane();
	JPanel p[] = { new UserManage(), new Recommend(), new Scehdule(), new ReserveManage() };

	String cap[] = "사용자 관리,추천 여행지 관리,일정 관리,예매 관리".split(",");

	public AdminMain() {
		super("관리자 메인", 1100, 700);

		ui();
		setVisible(true);
	}

	void ui() {
		add(tab);
		tab.setTabPlacement(2);

		for (int i = 0; i < cap.length; i++) {
			tab.add(p[i]);
			tab.setTitleAt(i, cap[i]);
			p[i].setOpaque(true);
//			p[i].setBackground(whiteColor);
		}

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
