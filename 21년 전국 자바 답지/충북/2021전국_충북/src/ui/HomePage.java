package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import base.BasePage;

public class HomePage extends BasePage {

	JPanel master = new JPanel(new BorderLayout());
	JPanel w = new West(), s = new South();
	JPanel home = new Center();

	JScrollPane scr = new JScrollPane(home);

	public HomePage() {
		setBackground(Color.black);
		scr.setBackground(Color.black);

		add(scr);
		add(w, "West");
		add(s, "South");

		repaint();
		revalidate();
	}

	class South extends JPanel {

		JLabel img = BasePage.size(imglbl("", 1000, 10), 100, 100);
		JPanel c = new JPanel(new BorderLayout());
		JPanel c_n = new JPanel(new FlowLayout());
		JPanel c_c = new JPanel(new FlowLayout());

		boolean isPlaying = false;

		JLabel sizeBox1 = BasePage.size(new JLabel(), 300, 10);
		JLabel sizeBox2 = BasePage.size(new JLabel(), 300, 10);
		JLabel prev = imglbl("./지급자료/images/prev.png", 30, 30);
		JLabel play = imglbl(isPlaying ? "./지급자료/images/pause.png" : "./지급자료/images/play.png", 30, 30);
		JLabel next = imglbl("./지급자료/images/next.png", 30, 30);
		JLabel queue = imglbl("./지급자료/images/queue.png", 40, 40);

		public South() {
			setOpaque(false);
			c.setOpaque(false);
			c_c.setOpaque(false);
			c_n.setOpaque(false);

			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(20, 5, 0, 5));

			img.setBorder(new LineBorder(Color.white));

			c_n.setBorder(new EmptyBorder(20, 0, 0, 0));

//			BasePage.size(c_c, 1100, 50);

			add(img, "West");
			c_n.add(lbl("재생중이 아님", JLabel.CENTER, Font.BOLD, 15), "North");
			add(c);
			c.add(c_n, "North");
			c.add(c_c);
			c_c.add(prev);
			c_c.add(sizeBox1);
			c_c.add(play);
			c_c.add(sizeBox2);
			c_c.add(next);
			add(queue, "East");

			img.addMouseListener(new Click("img"));
			prev.addMouseListener(new Click("prev"));
			play.addMouseListener(new Click("play"));
			next.addMouseListener(new Click("next"));
			queue.addMouseListener(new Click("queue"));
		}

		class Click extends MouseAdapter {

			String type;

			public Click(String type) {
				this.type = type;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (type.equals("play")) {
					isPlaying = !isPlaying;
					play.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
							.getImage(isPlaying ? "./지급자료/images/pause.png" : "./지급자료/images/play.png")
							.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
					repaint();
					revalidate();
				}else if(type.equals("prev")) {
					
				}else if(type.equals("play")) {
					
				}else if(type.equals("next")) {
					
				}else if(type.equals("queue")) {
					HomePage.this.scr.setViewportView(new PlayListPage("2"));
				}
			}
		}
	}

	class Center extends JPanel {

		JPanel line = new TitleLine("카테고리 둘러보기", 500);
		JPanel n = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel c = new JPanel(new BorderLayout(10, 10));
		JPanel c_n = new JPanel(new GridLayout(1, 0, 5, 0));
		JPanel c_c = new JPanel(new GridLayout(0, 3, 5, 5));

		String cap[] = "이번달 히트 곡,최근 발매된 앨범,의 인기 음악".split(",");

		public Center() {
			setLayout(new BorderLayout());
			setBackground(Color.black);

			add(n, "North");
			add(c);
			c.add(c_n, "North");
			c.add(line);
			c.add(c_c, "South");

			c.setOpaque(false);
			n.setOpaque(false);
			c_n.setOpaque(false);
			c_c.setOpaque(false);
			line.setOpaque(false);

			c.setBorder(new EmptyBorder(10, 10, 10, 10));

			n.add(btn("로그아웃", a -> {
				mf.remove(HomePage.this);
				mf.add(new LoginPage());
				mf.repaint();
				mf.revalidate();
				u_serial = 0;
			}));

			try {
				var r = stmt.executeQuery("select name from region where serial='" + u_region + "'");
				if (r.next()) {
					cap[2] = r.getString(1) + cap[2];
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String sql[] = {
					"SELECT s.name, a.name, a.serial FROM album a, song s where `release` <= curdate() and a.serial = s.album order by `release` desc",
					"SELECT s.name, a.name, a.serial FROM album a, song s where `release` <= curdate() and a.serial = s.album order by `release` desc",
					"SELECT s.name, a.name, a.serial FROM album a, song s where `release` <= curdate() and a.serial = s.album order by `release` desc" };

			for (int i = 0; i < 3; i++) {
				JPanel b = new JPanel(new BorderLayout()) {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(3));
						g.setColor(myColor);
						RoundRectangle2D rec = new RoundRectangle2D.Float(1.5f, 5.5f, getWidth() - 3, getHeight() - 3,
								20, 20);
						g2.fill(rec);
					}
				};
				JPanel t = new JPanel(new BorderLayout(5, 5));
				b.setOpaque(false);
				t.setOpaque(false);
				t.add(lbl(cap[i], JLabel.LEFT, Font.BOLD, 10), "North");

				try {
					var rs = stmt.executeQuery(sql[i]);
					if (rs.next()) {
						System.out.println(rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getString(3));
						t.add(lbl("<html>" + rs.getString(1) + "<br>" + rs.getString(2) + "<html>", JLabel.LEFT, 0,
								10));
						t.add(imglbl("./지급자료/images/album/" + rs.getInt(3) + ".jpg", 100, 100), "West");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b.add(t);
				t.setBorder(new EmptyBorder(10, 10, 10, 10));
				c_n.add(b);
			}

			try {
				var rs = stmt.executeQuery("select name from category");
				while (rs.next()) {
					Category cate = new Category(rs.getString(1));
					JPanel tmp = new JPanel(new BorderLayout());
					tmp.setBackground(Color.WHITE);
					tmp.add(cate);
					c_c.add(tmp);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	class West extends JPanel {
		String[] menu = "MENU,홈,검색하기".split(","), lib = "LIBRARY,좋아요,재생기록".split(","),
				icon = "home,search,like,history".split(",");
		JLabel addList = lbl("재생목록 추가", JLabel.LEFT, 0, 10);
		int i = 0;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/side.png")
					.getScaledInstance(300, 650, Image.SCALE_SMOOTH));
			g.drawImage(img.getImage(), 0, 0, null);
		}

		public West() {
			setLayout(new FlowLayout());
			setBorder(new EmptyBorder(0, 30, 0, 10));
			setPreferredSize(new Dimension(250, 300));

			JPanel m = new JPanel(new GridLayout(0, 1, 0, 0));
			JPanel l = new JPanel(new GridLayout(0, 1, 0, 0));
			JPanel p = new JPanel(new GridLayout(0, 1, 0, 10));

			add(m);
			add(l);
			add(p);

			m.setOpaque(false);
			l.setOpaque(false);
			p.setOpaque(false);

			BasePage.size(m, 250, 100);
			BasePage.size(l, 250, 100);
			BasePage.size(p, 250, 250);

			for (int i = 0; i < menu.length; i++) {
				JPanel t = new JPanel(new FlowLayout(FlowLayout.LEFT));
				t.setOpaque(false);
				if (i == 0) {
					t.add(lbl(menu[i], JLabel.LEFT, Font.BOLD, 15));
				} else {
					JLabel lbl = lbl(menu[i], JLabel.LEFT, 0, 10);
					t.add(new JLabel(
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/" + icon[i - 1] + ".png")
									.getScaledInstance(15, 15, Image.SCALE_SMOOTH))));
					lbl.addMouseListener(new Click(West.this));
					t.add(lbl);
				}
				m.add(t);
			}

			for (int i = 0; i < menu.length; i++) {
				JPanel t = new JPanel(new FlowLayout(FlowLayout.LEFT));
				t.setOpaque(false);
				if (i == 0) {
					t.add(lbl(lib[i], JLabel.LEFT, Font.BOLD, 15));
				} else {
					JLabel lbl = lbl(lib[i], JLabel.LEFT, 0, 10);
					t.add(new JLabel(
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/" + icon[i + 1] + ".png")
									.getScaledInstance(25, 25, Image.SCALE_SMOOTH))));
					lbl.addMouseListener(new Click(West.this));
					t.add(lbl);
				}
				l.add(t);
			}

			p.add(lbl("PLAYLIST", JLabel.LEFT, Font.BOLD, 15));
			p.add(addList);
			addList.addMouseListener(new Click(West.this));
			try {
				var rs = stmt.executeQuery("select name, serial from playlist where user='" + u_serial + "'");
				while (rs.next()) {
					JLabel lbl = lbl(rs.getString(1), JLabel.LEFT, 0, 10);
					lbl.setName(rs.getString(2));
					lbl.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							scr.setViewportView(new PlayListPage(((JLabel) e.getSource()).getName()));
							repaint();
							revalidate();
						}
					});
					i++;
					p.add(lbl);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class Click extends MouseAdapter {

		West west;

		public Click(West west) {
			this.west = west;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (((JLabel) e.getSource()).getText().equals("홈")) {
				scr.setViewportView(home);
			} else if (((JLabel) e.getSource()).getText().equals("검색하기")) {
				scr.setViewportView(new SearchPage());
			} else if (((JLabel) e.getSource()).getText().equals("좋아요")) {
				scr.setViewportView(new LikePage());
			} else if (((JLabel) e.getSource()).getText().equals("재생기록")) {
				scr.setViewportView(new PlayHistoryPage());
			} else {
				if (west.i == 5) {
					eMsg("플레이리스트는 5개까지 만들 수 있습니다");
					return;
				} else {
					String input = JOptionPane.showInputDialog("플레이리스트 제목을 입력해주세요.");
					if (input.equals("")) {
						eMsg("플레이리스트 제목을 입력해주세요.");
						return;
					} else {
						execute("insert into playlist values(0, '" + input + "', '" + u_serial + "')");
						System.out.println(getComponentCount());
						remove(west);
						add(new West(), "West");
						repaint();
						revalidate();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		u_serial = 1;
		u_region = 1;
		homePage = new HomePage();
		mf.add(homePage);
		mf.repaint();
		mf.revalidate();
	}
}
