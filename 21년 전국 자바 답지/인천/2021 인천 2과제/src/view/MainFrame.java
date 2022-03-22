package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import view.BasePage.JPanel;

public class MainFrame extends JFrame {

	JPanel main_n, main_c, main_s;
	JLabel logolbl;
	String icon[] = { "<html>&#127968", "<html>&#128269", "<html>&#128100" };
	String cap[] = "메인,검색,마이페이지".split(",");
	JButton btn;
	btnPanel panels[] = new btnPanel[3];

	public MainFrame() {
		super("FN Mart");
		ui();
		events();
	}

	void ui() {
		setSize(1100, 700);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.WHITE);
		add(BasePage.setBorder(main_n = new JPanel(new BorderLayout()), new LineBorder(Color.BLACK)), "North");
		add(BasePage.setBorder(main_c = new JPanel(new GridLayout()), new LineBorder(Color.BLACK)));
		add(BasePage.setBorder(main_s = new JPanel(new GridLayout(0, 3, 50, 0)), new LineBorder(Color.BLACK)), "South");

		main_n.add(logolbl = BasePage.lbl("FN", JLabel.LEFT, 30), "West");
		main_n.add(BasePage.sz(btn = BasePage.btn("Login", a -> {
			switch (a.getActionCommand()) {
			case "Login":
				swapPage(new LoginPage());
				break;
			default:
				BasePage.logout();
				swapPage(new MainPage());
				break;
			}
		}), 120, 25), "East");

		btn.setBorderPainted(false);

		for (int i = 0; i < cap.length; i++) {
			main_s.add(panels[i] = new btnPanel(icon[i], cap[i], i));
		}
		panels[0].onClicked(true);
	}

	void events() {

		logolbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (BasePage.sno != 0) {
					swapPage(new ManagePage());
				} else {
					swapPage(new MainPage());
				}
				super.mousePressed(e);
			}
		});
		Arrays.stream(panels).forEach(a -> {
			a.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

					Arrays.stream(panels).forEach(a -> a.onClicked(false));

					switch (a.idx) {
					case 0:
						if (BasePage.sno == 0) {
							swapPage(new MainPage());
						} else {
							swapPage(new ManagePage());
						}
						break;
					case 1:
						if (BasePage.uno == 0) {
							BasePage.eMsg("로그인 하지 않았습니다.");
							panels[0].onClicked(true);
							return;
						}
						swapPage(new SearchPage());
						break;

					default:
						if (BasePage.uno == 0) {
							BasePage.eMsg("로그인 하지 않았습니다.");
							panels[0].onClicked(true);
							return;
						}
						swapPage(new MyPage());
						break;
					}
					super.mousePressed(e);
					a.onClicked(true);
				}

			});
		});
	}

	void menuInit(boolean user) {
		main_s.removeAll();
		if (user) {
			main_s.setLayout(new GridLayout(0, 3, 30, 50));
			for (int i = 0; i < panels.length; i++) {
				main_s.add(panels[i]);
			}
		} else { // 판매자인경우
			main_s.setLayout(new BorderLayout());
			main_s.add(panels[0]);
		}
		panels[0].onClicked(true);
		repaint();
		revalidate();
	}

	void swapPage(BasePage page) {
		main_c.removeAll();
		main_c.add(page);
		main_c.revalidate();
		main_c.repaint();
	}

	class btnPanel extends javax.swing.JPanel {

		JLabel lbl1, lbl2;
		int idx;

		public btnPanel(String icon, String cap, int idx) {
			this.idx = idx;
			setLayout(new BorderLayout());
			add(lbl1 = BasePage.lbl(icon, JLabel.CENTER, 20));
			add(lbl2 = BasePage.lbl(cap, JLabel.CENTER, 15), "South");
			setBackground(Color.WHITE);
			lbl1.setForeground(Color.LIGHT_GRAY.darker());
			lbl2.setForeground(Color.LIGHT_GRAY.darker());
		}

		void onClicked(boolean chk) {
			lbl1.setForeground(chk ? Color.BLACK : Color.LIGHT_GRAY.darker());
			lbl2.setForeground(chk ? Color.BLACK : Color.LIGHT_GRAY.darker());
		}
	}

	public static void main(String[] args) {
		BasePage.mf.swapPage(new MainPage());
		BasePage.mf.setVisible(true);
	}
}
