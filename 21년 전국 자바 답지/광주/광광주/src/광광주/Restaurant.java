package 광광주;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Restaurant extends BaseFrame {

	JLabel prev, orderCount, favorite;
	JLabel bg;
	Menu m;
	Review r;
	int sno;

	public Restaurant(String sname, int sno, String rate, String fee, String time) {
		super(sname, 1200, 700);

		this.sno = sno;

		var n = new JPanel(new BorderLayout());
		var bg_n = new JPanel(new BorderLayout());
		var bg_s = new JPanel(new BorderLayout());
		var c = new JTabbedPane(2);

		n.setBackground(Color.BLACK);

		add(n, "North");
		add(c);

		n.add(bg = new JLabel(
				new ImageIcon(getImage("./지급자료/배경/" + sno + ".png").getScaledInstance(1200, 300, Image.SCALE_SMOOTH))) {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				super.paint(g2);
			}

			@Override
			protected void paintChildren(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				super.paintChildren(g);
			}
		}, "North");

		bg.setPreferredSize(new Dimension(1200, 300));

		bg.setLayout(new BorderLayout());

		bg.add(bg_n, "North");
		bg.add(bg_s, "South");

		bg_n.add(prev = lbl("뒤로가기", JLabel.LEFT), "West");
		bg_n.add(orderCount = lbl("주문표(0)", JLabel.RIGHT), "East");

		prev.setForeground(Color.WHITE);
		orderCount.setForeground(Color.WHITE);

		String about = "";

		try {
			var rs = stmt.executeQuery("select about from seller where no = " + sno);
			if (rs.next()) {
				about = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bg_s.add(lbl("<html><left><font size = \"6\"; font color = \"white\">" + sname
				+ "</font><font size = \"4\"; font color = \"WHITE\"><br>" + fee + "/" + time + "/" + rate + "<br>"
				+ about, JLabel.LEFT));

		bg_s.add(favorite = lbl("♡", JLabel.RIGHT, 20), "East");
		favorite.setForeground(Color.RED);

		bg_n.setOpaque(false);
		bg_s.setOpaque(false);

		c.setTabPlacement(JTabbedPane.TOP);

		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
				super.mouseReleased(e);
			}
		});

		try {
			var rs = stmt.executeQuery("select * from favorite where seller = " + sno + " and user=" + uno);
			if (rs.next()) {
				favorite.setText("♥");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.addTab("메뉴", m = new Menu());
		c.addTab("리뷰", r = new Review());

		orderCount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new OrderTable(Restaurant.this).addWindowListener(new Before(Restaurant.this));
				super.mouseReleased(e);
			}
		});

		favorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (favorite.getText().equals("♥")) {
					favorite.setText("♡");
					execute("delete from favorite where seller = " + sno + " and user = " + uno);
				} else {
					favorite.setText("♥");
					execute("insert favorite values(0," + sno + "," + uno + ")");
				}
				super.mouseReleased(e);
			}
		});

		setVisible(true);
	}

	class Menu extends JPanel {

		JScrollPane pane;
		ArrayList<ItemRow> rows = new ArrayList<>();
		JPanel c;
		int totalHeight = 0;

		public Menu() {
			setLayout(new BorderLayout());
			add(pane = new JScrollPane(c = new JPanel(new FlowLayout())));
			try {
				var rs = stmt.executeQuery("SELECT no, name FROM type where seller = " + sno);
				while (rs.next()) {
					rows.add(new ItemRow(rs.getInt(1), rs.getString(2)));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rows.forEach(a -> {
				a.init();
				c.add(a);
				totalHeight += a.height;
			});
			c.setPreferredSize(new Dimension(1150, totalHeight + 140));
		}

		class ItemRow extends JPanel {

			JPanel c;
			int type;
			int height;

			public ItemRow(int type, String typeName) {
				this.type = type;
				setLayout(new BorderLayout());
				add(lbl(typeName, JLabel.LEFT, 20), "North");
				add(c = new JPanel(new FlowLayout(FlowLayout.LEFT)));

			}

			void init() {
				int cnt = 0;
				try {
					var rs = stmt.executeQuery(
							"select no, name, description, price, minute(cooktime) from menu where type = " + type);
					while (rs.next()) {
						Item i = new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
								rs.getString(5));
						c.add(i);
						++cnt;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				height = ((cnt / 3) * 120) + 140;
				c.setPreferredSize(new Dimension(1150, height));
			}

			class Item extends JPanel {

				JPanel c;
				JLabel img;
				public int no, price;
				public String name, dsp;

				public Item(int no, String name, String dsp, int price, String cooktime) {
					this.no = no;
					this.price = price;
					this.name = name;
					this.dsp = dsp;
					setBorder(new LineBorder(Color.green, 5));
					setLayout(new BorderLayout());
					setPreferredSize(new Dimension(350, 120));
					add(c = new JPanel(new BorderLayout()));
					c.add(lbl(name, JLabel.LEFT, 20), "North");
					c.add(lbl("<html>" + dsp, JLabel.LEFT));
					c.add(lbl(df.format(price) + "원/" + cooktime + "분 소요", JLabel.LEFT), "South");
					add(img = new JLabel(new ImageIcon(
							getImage("./지급자료/메뉴/" + no + ".png").getScaledInstance(120, 120, Image.SCALE_SMOOTH))),
							"East");
					addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2) {
								new OptionMenu(Restaurant.this, Item.this).setVisible(true);
							}
							super.mouseClicked(e);
						}
					});
				}
			}
		}

	}

	class Review extends JPanel {

		JPanel c;
		JScrollPane pane;
		int totalHeight;

		public Review() {
			setLayout(new BorderLayout());

			add(pane = new JScrollPane(c = new JPanel(new FlowLayout(FlowLayout.LEFT))));

			try {
				var rs = stmt.executeQuery(
						"SELECT r.title, u.name, r.CONTENT, r.rate FROM eats.review r, user u where r.USER = u.NO and seller = "
								+ sno);
				while (rs.next()) {
					c.add(new ReviewItem(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
					totalHeight += 100;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.setPreferredSize(new Dimension(1110, totalHeight + 100));
			pane.setBorder(BorderFactory.createEmptyBorder());
			setBorder(new EmptyBorder(20, 20, 20, 20));
		}

		class ReviewItem extends JPanel {

			JPanel n;
			String stars = "";
			JLabel rate;

			public ReviewItem(String title, String uname, String content, int rate) {
				super(new BorderLayout());
				add(n = new JPanel(new BorderLayout()), "North");
				n.add(lbl("<html><font size = \"5\"; font face = \"맑은 고딕\">" + title + " /<b>" + uname + " </b>작성함",
						JLabel.LEFT), "West");

				for (int i = 1; i <= rate; i++) {
					stars += "★";
				}
				n.add(this.rate = lbl(stars, JLabel.RIGHT, 20), "East");
				this.rate.setForeground(Color.YELLOW);
				add(lbl("<html><font face = \"맑은 고딕\"; font size = \"4\">" + content, JLabel.LEFT));
				setPreferredSize(new Dimension(1100, 100));
				setBorder(new LineBorder(Color.LIGHT_GRAY));
			}
		}
	}

}
