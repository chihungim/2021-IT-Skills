package 광광주;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Main extends BaseFrame {

	Category category[] = new Category[10];
	String categoryCap[] = "모두,베이커리,디저트,편의점,중식,일식,멕시칸,아메리칸,한식,알코홀".split(",");
	JLabel lbl[] = { lbl("로그아웃", JLabel.RIGHT), lbl("마이페이지", JLabel.RIGHT) };
	SlideImage s;
	Timer slideMover;
	double speed = 10;

	public Main() {
		super("기능 배달", 1200, 800);
		setLayout(new BorderLayout(0, 0));

		var n = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var c = new JPanel(null);
		var s = new JPanel(new BorderLayout());
		var s_c = new JPanel(new GridLayout(1, 0, 10, 10));

		add(n, "North");
		add(c);
		add(s, "South");
		c.add(this.s = new SlideImage());

		this.s.setBounds(0, 0, 7080, 500);
		s.add(lbl("카테고리별로 탐색", JLabel.LEFT, 20), "North");
		s.add(s_c);

		n.add(lbl[0]);
		n.add(lbl[1]);

		lbl[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				orderList.clear();
				dispose();
			}
		});

		lbl[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MyPage().addWindowListener(new Before(Main.this));
				super.mouseClicked(e);
			}
		});

		s_c.add(size(category[0] = new Category(categoryCap[0], 0), 100, 200));

		category[0].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < 10; i++) {
					category[i].bye();
				}
				if (e.getClickCount() == 2) {
					new Search(0).addWindowListener(new Before(Main.this));
				}
				((Category) e.getSource()).Clicked();
				revalidate();
				repaint();
			}
		});

		for (int i = 1; i < 10; i++) {
			s_c.add(size(category[i] = new Category(categoryCap[i], i), 100, 200));
			category[i].setName(i + "");
			category[i].addMouseListener(new MouseAdapter() {
				int cnt = 0;

				@Override
				public void mouseClicked(MouseEvent e) {
					for (int i = 0; i < 10; i++) {
						category[i].bye();
					}
					cnt++;
					if (cnt == 2) {
						new Search(toInt(((Category) e.getSource()).getName()))
								.addWindowListener(new Before(Main.this));
						cnt = 0;
					}
					((Category) e.getSource()).Clicked();
					revalidate();
					repaint();
				}
			});
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				new Login();
				orderList.clear();
				super.windowClosed(e);
			}
		});

		((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 10, 10, 10));
		setVisible(true);
	}

	class Category extends JLabel {

		Color color = Color.LIGHT_GRAY;
		String category;
		RoundImg img;

		public Category(String category, int idx) {
			this.category = category;
			setLayout(null);
			add(img = new RoundImg("./지급자료/카테고리/" + (idx == 0 ? "all" : idx) + ".png"));
			img.setBounds(5, 10, 101, 101);
		}

		void bye() {
			color = Color.lightGray;
		}

		void Clicked() {
			color = Color.ORANGE;
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(color);
			RoundRectangle2D rec = new RoundRectangle2D.Float(3.0f, 3.0f, getWidth() - 3, getHeight() - 3, 100, 100);
			g2.fill(rec);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("맑은 고딕", Font.TYPE1_FONT, 12));
			FontMetrics m = getFontMetrics(g2.getFont());
			int w = m.stringWidth(category);
			g2.drawString(category, (getWidth() - w) / 2, 140);
			super.paintComponent(g2);
		}

		class RoundImg extends JLabel {

			String path;

			public RoundImg(String path) {
				super(new ImageIcon(getImage(path, 100, 100)));
				this.path = path;
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.WHITE);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.fillOval(0, 0, 100, 100);
				super.paintComponent(g2);
			}
		}
	}

	class SlideImage extends JPanel {

		JLabel img[] = new JLabel[5];
		ArrayList<JLabel> list = new ArrayList<>();
		int idx = 0;
		String names[] = new String[5];

		public SlideImage() {
			setLayout(null);
			try {
				// 이미지 누락발견
				var rs = stmt.executeQuery(
						"SELECT count(*) , s.no, s.name FROM eats.receipt r, seller s where r.seller = s.No group by s.no order by count(*) desc limit 5");
				int idx = 0, width = 0;
				while (rs.next()) {
					img[idx] = new JLabel(new ImageIcon(getImage("./지급자료/배경/" + rs.getInt(2) + ".PNG")));
					names[idx] = rs.getString(3);
					System.out.println(rs.getInt(2));
					list.add(img[idx]);
					add(img[idx]);

					img[idx].setBounds(width, 0, 1180, 400);
					width += 1180;
					idx++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < 5; i++) {
				img[i].setLayout(new BorderLayout());
				img[i].add(new GradationLabel());
				// 여따가 라벨몇게만 넣으면 됨
			}

			slideMove();
		}

		void slideMove() {
			new Thread(() -> {
				while (true) {
					for (int i = 0; i < list.size(); i++) {
						int x = list.get(i).getX();
						list.get(i).setLocation(x -= speed, 0);
						if (x == -1180) {
							list.get(i).setLocation(4720, 0);
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();
		}

		class GradationLabel extends JLabel {
			float opacity = 0.1f;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				GradientPaint gp = new GradientPaint(0, 0, new Color(255, 0, 0, 50).brighter(), 1000, 1000,
						new Color(0, 0, 255, 50).brighter());
				g2.setPaint(gp);
				g2.fillRect(0, 0, 1180, 400);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
				super.paintComponent(g2);
			}

		}

	}

	public static void main(String[] args) {
		uno = 1;
		new Main();
	}
}
