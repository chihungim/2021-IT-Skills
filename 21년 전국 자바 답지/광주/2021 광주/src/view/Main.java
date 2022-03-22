package view;

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
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class Main extends BaseDialog {

	Category category[] = new Category[10];
	String ccap[] = "모두,베이커리,디저트,편의점,중식,일식,멕시칸,아메리칸,한식,알코홀".split(",");
	Slide slide;
	JLabel lbl[] = { lbl("로그아웃", JLabel.RIGHT), lbl("마이페이지", JLabel.RIGHT) };

	public Main() {
		super("기능배달", 1200, 700);
		ui();
		setVisible(true);
	}

	void ui() {
		setLayout(new BorderLayout(0, 0));

		var n = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var c = new JPanel(null);
		var s = new JPanel(new BorderLayout());
		var s_c = new JPanel(new GridLayout(1, 0, 10, 10));
		n.add(lbl[0]);
		n.add(lbl[1]);
		add(n, "North");
		add(c);
		add(s, "South");
		s.add(lbl("카테고리별로 탐색", JLabel.LEFT, 20), "North");
		s.add(s_c);
		c.add(slide = new Slide());

		for (int i = 0; i < 10; i++) {
			s_c.add(size(category[i] = new Category(ccap[i]), 100, 200));
		}

		lbl[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				super.mouseClicked(e);
			}
		});

		lbl[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				super.mouseClicked(e);
			}
		});

		slide.showAnimation();
		slide.setBounds(0, 0, 7080, 500);
		((javax.swing.JPanel) getContentPane()).setBorder(new EmptyBorder(20, 10, 10, 10));
	}

	public static void main(String[] args) {
		uno = 1;
		new Main();
	}

	class Slide extends JPanel {
		JLabel img[] = new JLabel[5];
		ArrayList<JLabel> list = new ArrayList<>();

		public Slide() {
			setLayout(null);

			try {
				var rs = stmt.executeQuery(
						"SELECT count(*) , s.no, s.name, s.category FROM eats.receipt r, seller s where r.seller = s.No group by s.no order by count(*) desc limit 5");
				int idx = 0, width = 0;
				while (rs.next()) {
					img[idx] = new JLabel(getIcon("./지급자료/배경/" + rs.getInt(2) + ".PNG", 1180, 400)) {
						@Override
						protected void paintComponent(Graphics g) {
							Graphics2D g2 = (Graphics2D) g;
							var gp = new GradientPaint(0, 0, new Color(255, 0, 0, 160).brighter(), 1000, 1000,
									new Color(0, 0, 255, 160).brighter());
							g2.setPaint(gp);
							g2.fillRect(0, 0, 1200, 400);
							g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
							super.paintComponent(g2);
						}

						@Override
						protected void paintChildren(Graphics g) {
							Graphics2D g2 = (Graphics2D) g;

							g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1.0f));
							super.paintChildren(g2);
						}
					};
					list.add(img[idx]);
					img[idx].setLayout(null);
					add(img[idx]);
					var lbl = new JLabel("<html><font face = '맑은고딕', size = '60', Color = 'WHITE'>#" + (idx + 1)
							+ "<br><font size = '6'>" + rs.getString(3) + "<br><font size = '4'>" + ccap[rs.getInt(4)],
							JLabel.LEFT);
					img[idx].add(lbl);

					lbl.setBounds(100, 0, 1000, 350);
					img[idx].setBounds(width, 0, 1180, 400);
					width += 1180;
					idx++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		void showAnimation() {
			new Thread(() -> {
				while (true) {
					for (int i = 0; i < list.size(); i++) {
						int x = list.get(i).getX();
						list.get(i).setLocation(x -= 10, 0);
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
	}

	class Category extends JLabel {
		Color col = Color.LIGHT_GRAY;
		String category;

		public Category(String category) {
			this.category = category;
			setLayout(null);
			var img = new JLabel(Main.getIcon(
					"./지급자료/카테고리/" + (category.equals("모두") ? "all" : Arrays.asList(ccap).indexOf(category)) + ".png",
					100, 100)) {

				@Override
				protected void paintComponent(Graphics g) {
					var g2 = (Graphics2D) g;
					g2.setColor(Color.WHITE);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.fillOval(0, 0, 100, 100);
					super.paintComponent(g);
				}
			};
			add(img);
			img.setBounds(5, 10, 101, 101);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					for (int i = 0; i < Main.this.category.length; i++) {
						Main.this.category[i].col = Color.LIGHT_GRAY;
						Main.this.category[i].repaint();
					}
					col = Color.ORANGE;
					if (e.getClickCount() == 2) {
						Search s = new Search();
						s.box.setSelectedItem(category);
						s.addWindowListener(new Before(Main.this));
						s.search();
						repaint();
					}
				}
			});

		}

		@Override
		protected void paintComponent(Graphics g) {
			var g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(col);
			RoundRectangle2D rec = new RoundRectangle2D.Float(3.0f, 3.0f, getWidth() - 3, getHeight() - 3, 100, 100);
			g2.fill(rec);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("맑은 고딕", Font.TYPE1_FONT, 12));
			FontMetrics m = getFontMetrics(g2.getFont());
			int w = m.stringWidth(category);
			g2.drawString(category, (getWidth() - w) / 2, 140);
			super.paintComponent(g2);
		}

	}
}
