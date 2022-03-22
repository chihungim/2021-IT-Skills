package View;

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
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class HomePage extends BasePage {
	JPanel m;
	West w;
	South s;
	JPanel home;
	JScrollPane pane;

	public HomePage() {
		setBackground(Color.BLACK);
		queuePage = new QueuePage(HomePage.this.s);
		add(pane = new JScrollPane(home = new Center()));
		pane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = Color.darkGray;
				this.trackColor = Color.GRAY;
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			private JButton createZeroButton() {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(0, 0));
				button.setMinimumSize(new Dimension(0, 0));
				button.setMaximumSize(new Dimension(0, 0));
				return button;
			}
		});

		pane.setBackground(Color.BLACK);
		add(w = new West(), "West");
		add(s = new South(), "South");
		setData();
	}

	class Center extends JPanel {

		JPanel n = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel c = new JPanel(new BorderLayout(10, 10));
		JPanel c_n = new JPanel(new GridLayout(1, 0, 5, 0));
		JPanel c_c = new JPanel(new GridLayout(0, 3, 5, 5));
		TitledBorder tb;
		String cap[] = "�̹��� ��Ʈ ��,�ֱ� �߸ŵ� �ٹ�,�� �α� ����".split(",");

		public Center() {
			setLayout(new BorderLayout());
			setBackground(Color.black);
			tb = new TitledBorder(new MatteBorder(1, 0, 0, 0, Color.WHITE),
					"<html><font color = \"white\"; size = \"5\">ī�װ� �ѷ�����");
			tb.setTitlePosition(TitledBorder.ABOVE_TOP);

			add(n, "North");
			add(c);

			c.add(c_n, "North");
			c.add(c_c, "South");

			c.setOpaque(false);
			n.setOpaque(false);
			c_n.setOpaque(false);
			c_c.setBackground(Color.BLACK);

			c_c.setBorder(tb);

			c.setBorder(new EmptyBorder(10, 10, 10, 10));

			n.add(btn("�α׾ƿ�", a -> {
				mf.swapPage(new LoginPage());
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
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
						t.add(lbl("<html>" + rs.getString(1) + "<br>" + rs.getString(2) + "<html>", JLabel.LEFT, 0,
								10));
						t.add(imglbl("./�����ڷ�/images/album/" + rs.getInt(3) + ".jpg", 100, 100), "West");
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
		String[] menu = "MENU,Ȩ,�˻��ϱ�".split(","), lib = "LIBRARY,���ƿ�,������".split(","),
				icon = "home,search,like,history".split(",");
		JLabel addList = lbl("������ �߰�", JLabel.LEFT, 0, 10);
		int i = 0;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./�����ڷ�/images/side.png")
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
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("./�����ڷ�/images/" + icon[i - 1] + ".png")
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
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("./�����ڷ�/images/" + icon[i + 1] + ".png")
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
							pane.setViewportView(new PlayListPage(((JLabel) e.getSource()).getName()));
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
			if (((JLabel) e.getSource()).getText().equals("Ȩ")) {
				pane.setViewportView(home);
			} else if (((JLabel) e.getSource()).getText().equals("�˻��ϱ�")) {
				pane.setViewportView(new SearchPage());
			} else if (((JLabel) e.getSource()).getText().equals("���ƿ�")) {
				pane.setViewportView(new FavoritePage());
			} else if (((JLabel) e.getSource()).getText().equals("������")) {
				pane.setViewportView(new PlayHistoryPage());
			} else {
				if (west.i == 5) {
					eMsg("�÷��̸���Ʈ�� 5������ ���� �� �ֽ��ϴ�");
					return;
				} else {
					String input = JOptionPane.showInputDialog("�÷��̸���Ʈ ������ �Է����ּ���.");

					if (input.equals("")) {
						eMsg("�÷��̸���Ʈ ������ �Է����ּ���.");
						return;
					} else {
						execute("insert into playlist values(0, '" + input + "', '" + u_serial + "')");
						remove(west);
						add(new West(), "West");
						repaint();
						revalidate();
					}
				}
			}
		}
	}

	class South extends JPanel {

		JLabel img = BasePage.size(imglbl("", 1000, 10), 100, 100);
		JPanel m = new JPanel(new BorderLayout());
		JPanel c = new JPanel(new BorderLayout());
		JPanel c_n = new JPanel(new FlowLayout());
		JPanel c_c = new JPanel(new FlowLayout());

		JLabel prev = imglbl("./�����ڷ�/images/prev.png", 30, 30);
		JLabel play = imglbl(isPlaying ? "./�����ڷ�/images/pause.png" : "./�����ڷ�/images/play.png", 30, 30);
		JLabel next = imglbl("./�����ڷ�/images/next.png", 30, 30);
		JLabel queue = imglbl("./�����ڷ�/images/queue.png", 40, 40);
		JLabel songName;

		public South() {
			setOpaque(false);
			c.setOpaque(false);
			c_c.setOpaque(false);
			c_n.setOpaque(false);
			m.setOpaque(false);

			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(20, 5, 0, 5));
			img.setBorder(new LineBorder(Color.white));
			c_n.setBorder(new EmptyBorder(20, 0, 0, 0));

			add(m);
			m.add(BasePage.size(bar = new Timebar(), 1, 20), "North");

			c.add(img, "West");
			c_n.add(songName = lbl("������� �ƴ�", JLabel.CENTER, Font.BOLD, 15), "North");
			m.add(c);
			c.add(c_n, "North");
			c.add(c_c);
			c_c.add(prev);
			c_c.add(Box.createHorizontalStrut(300));
			c_c.add(play);
			c_c.add(Box.createHorizontalStrut(300));
			c_c.add(next);
			c.add(queue, "East");
			progress = new Timer(1, a -> {
				time += 1;
				bar.setValue(time);
			});

			img.addMouseListener(new Click("img"));
			prev.addMouseListener(new Click("prev"));
			play.addMouseListener(new Click("play"));
			next.addMouseListener(new Click("next"));
			queue.addMouseListener(new Click("queue"));
		}

		void setPlaying() {
			play.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
					.getImage(isPlaying ? "./�����ڷ�/images/pause.png" : "./�����ڷ�/images/play.png")
					.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		}

		class Click extends MouseAdapter {

			String type;

			public Click(String type) {
				this.type = type;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (type.equals("play")) {
					isPlaying = !isPlaying;

					if (isPlaying) {
						if (songName.getText().equals("������� �ƴ�"))
							return;
						progress.start();
					} else {
						progress.stop();
					}
					play.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
							.getImage(isPlaying ? "./�����ڷ�/images/pause.png" : "./�����ڷ�/images/play.png")
							.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
					repaint();
					revalidate();
				} else if (type.equals("prev")) {
					prev();
				} else if (type.equals("next")) {
					next();
				} else if (type.equals("queue")) {
					homePage.pane.setViewportView(queuePage);
				}
				super.mousePressed(e);
			}
		}

	}

	public static void main(String[] args) {
		u_serial = 1;
		mf.swapPage(homePage = new HomePage());
	}
}
