package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import additional.Util;

public class MainFrame extends JFrame {
	// 2021-07-09 수정
	JPanel main_n, main_c, main_s;
	JLabel lbl;
	String icon[] = { "<html>&#127968", "<html>&#128269", "<html>&#128100" };
	String cap[] = "메인,검색,마이페이지".split(",");
	JButton btn;
	BtnPanel panels[] = new BtnPanel[3];

	public MainFrame() {
		super("FN Mart");

		this.setSize(1100, 700);
		this.setDefaultCloseOperation(2);
		this.setLocationRelativeTo(null);

		this.getContentPane().setBackground(Color.white);

		this.add(main_n = new JPanel(new BorderLayout()), "North");
		this.add(main_c = new JPanel(new GridLayout()));
		this.add(main_s = new JPanel(new GridLayout(0, 3, 50, 0)), "South");

		main_n.setOpaque(false);
		main_c.setOpaque(false);
		main_s.setOpaque(false);

		Util.setLine(main_n);
		Util.setLine(main_c);
		Util.setLine(main_s);
		main_n.add(lbl = Util.lbl("FN", 0, 30), "West");
		Util.setEmpty(lbl, 0, 5, 0, 0);
		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addPage(new MainPage());
			}
		});

		main_n.add(btn = Util.btn("Login", a -> {
			if (a.getActionCommand().contentEquals("Login")) {
				addPage(new LoginPage());
			} else {
				Util.iMsg("로그아웃 하셨습니다.");
				btnInit(true);
				BasePage.uno = 0;
				BasePage.uname = "";
				BasePage.sno = 0;
				BasePage.sname = "";
				btn.setText("Login");
				addPage(new MainPage());
				return;
			}
		}), "East");
		Util.sz(btn, 120, 25);
		btn.setBorderPainted(false);
		for (int i = 0; i < cap.length; i++) {
			main_s.add(panels[i] = new BtnPanel(icon[i], cap[i], i));
			panels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					for (int j = 0; j < panels.length; j++) {
						panels[j].onClicked(false);
					}
					BtnPanel p = (BtnPanel) e.getSource();

					switch (p.idx) {
					case 0:
						if (BasePage.sno == 0) {
							addPage(new MainPage());
						} else {
							addPage(new ManagePage());
						}
						break;

					case 1:
						if (BasePage.uno == 0) {
							Util.eMsg("로그인 하지 않았습니다.");
							panels[0].onClicked(true);
							return;
						} else
							addPage(new SearchPage());
						break;

					default:
						if (BasePage.uno == 0) {
							Util.eMsg("로그인 하지 않았습니다.");
							panels[0].onClicked(true);
							return;
						} else
							addPage(new MyPage());
						break;
					}

					p.onClicked(true);
				}
			});
		}
		panels[0].onClicked(true);

	}

	void btnInit(boolean user) {
		main_s.removeAll();
		if (user) {
			main_s.setLayout(new GridLayout(0, 3, 50, 0));
			for (int i = 0; i < panels.length; i++) {
				main_s.add(panels[i]);
			}
		} else {
			main_s.setLayout(new BorderLayout());
			main_s.add(panels[0]);
		}
		panels[0].onClicked(true);
		repaint();
		revalidate();
	}

	void addPage(BasePage page) {
		main_c.removeAll();
		main_c.add(page);
		main_c.repaint();
		main_c.revalidate();
	}

	class BtnPanel extends JPanel {
		JLabel lbl1, lbl2;
		int idx;

		public BtnPanel(String icon, String cap, int idx) {
			this.idx = idx;
			this.setLayout(new BorderLayout());
			this.add(lbl1 = Util.lbl(icon, 0, 25));
			this.add(lbl2 = Util.lbl(cap, 0, 15), "South");
			this.setBackground(Color.white);
			lbl1.setForeground(Color.LIGHT_GRAY.darker());
			lbl2.setForeground(Color.LIGHT_GRAY.darker());
		}

		public void onClicked(boolean chk) {
			lbl1.setForeground(chk ? Color.black : Color.LIGHT_GRAY.darker());
			lbl2.setForeground(chk ? Color.black : Color.LIGHT_GRAY.darker());
		}

	}

	public static void main(String[] args) {
		BasePage.mf.addPage(new MainPage());
		BasePage.mf.setVisible(true);
	}

}
