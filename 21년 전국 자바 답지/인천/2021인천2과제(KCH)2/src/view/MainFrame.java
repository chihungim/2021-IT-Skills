package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.Tools;

public class MainFrame extends JFrame {
	JPanel n, c, s;
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
		getContentPane().setBackground(Color.white);

		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new GridLayout()));
		this.add(s = new JPanel(new GridLayout(0, 3, 50, 0)), "South");

		n.setOpaque(false);
		c.setOpaque(false);
		s.setOpaque(false);

		Tools.setLine(n);
		Tools.setLine(c);
		Tools.setLine(s);

		n.add(lbl = Tools.lbl("FN", 0, 30), "West");

		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addPage(new MainPage());
			}
		});

		n.add(btn = Tools.btn("Login", a -> {
			if (a.getActionCommand().equals("Login")) {
				addPage(new LoginPage());
			} else {
				Tools.iMsg("로그아웃 하셨습니다.");
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

		Tools.size(btn, 120, 25);
		btn.setBorderPainted(false);

		for (int i = 0; i < cap.length; i++) {
			s.add(panels[i] = new BtnPanel(icon[i], cap[i], i));
			panels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					for (int j = 0; j < panels.length; j++) {
						panels[j].onClicked(false);
					}

					BtnPanel p = (BtnPanel) e.getSource();
					switch (p.idx) {
					case 0:
						if (BasePage.sno == 0)
							addPage(new MainPage());
						else
							addPage(new ManagePage());
						break;
					case 1:
						if (BasePage.uno == 0) {
							Tools.eMsg("로그인 하지 않았습니다.");
							panels[0].onClicked(true);
							return;
						} else
							addPage(new SearchPage());
						break;
					default:
						if (BasePage.uno == 0) {
							Tools.eMsg("로그인 하지 않았습니다.");
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

	public static void main(String[] args) {
		BasePage.mf.addPage(new MainPage());
		BasePage.mf.setVisible(true);
	}

	void btnInit(boolean user) {
		s.removeAll();
		if (user) {
			s.setLayout(new GridLayout(0, 3, 50, 0));
			for (int i = 0; i < panels.length; i++) {
				s.add(panels[i]);
			}
		} else {
			s.setLayout(new BorderLayout());
			s.add(panels[0]);
		}
		panels[0].onClicked(true);
		repaint();
		revalidate();
	}

	void addPage(BasePage bp) {
		c.removeAll();
		c.add(bp);
		c.repaint();
		c.revalidate();
	}

	class BtnPanel extends JPanel {

		JLabel lbl1, lbl2;
		protected int idx;

		public BtnPanel(String icon, String cap, int idx) {
			this.idx = idx;
			setLayout(new BorderLayout());
			add(lbl1 = Tools.lbl(icon, 0, 25));
			add(lbl2 = Tools.lbl(cap, 0, 15), "South");
			setBackground(Color.WHITE);
			lbl1.setForeground(Color.LIGHT_GRAY.darker());
			lbl2.setForeground(Color.LIGHT_GRAY.darker());
		}

		public void onClicked(boolean b) {
			lbl1.setForeground(b ? Color.BLACK : Color.LIGHT_GRAY.darker());
			lbl2.setForeground(b ? Color.BLACK : Color.LIGHT_GRAY.darker());
		}

	}
}
