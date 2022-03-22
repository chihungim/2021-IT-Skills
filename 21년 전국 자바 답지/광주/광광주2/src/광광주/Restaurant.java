package ±¤±¤ÁÖ;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Line.Info;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Restaurant extends BaseFrame {
	JLabel prev, orderCnt, like, bg;
	Menu m;
	Review r;
	int sno;
	String about, sname, rate, fee, time;

	public Restaurant(String sname, int sno, String rate, String fee, String time) {
		super(sname, 1200, 700);
		this.sname = sname;
		this.sno = sno;
		this.rate = rate;
		this.fee = fee;
		this.time = time;
		data();
		ui();
		addEvents();
		setVisible(true);
	}

	void data() {
		try {
			var rs = stmt.executeQuery("select about from seller where no = " + sno);
			if (rs.next()) {
				about = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void addEvents() {
		like.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (like.getText().equals("¢¾")) {
					like.setText("¢½");
					execute("delete from favorite where seller = " + sno + " and user = " + uno);
				} else {
					like.setText("¢¾");
					execute("insert favorite values(0," + sno + "," + uno + ")");
				}

				super.mouseReleased(e);
			}
		});

		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dispose();
				super.mousePressed(e);
			}
		});

		orderCnt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new OrderTable(Restaurant.this).addWindowListener(new Before(Restaurant.this));
				super.mouseReleased(e);
			}
		});
	}

	void ui() {
		var n = new JPanel(new BorderLayout());
		var bg_n = new JPanel(new BorderLayout());
		var bg_s = new JPanel(new BorderLayout());
		var c = new JTabbedPane(2);

		n.setBackground(Color.BLACK);

		add(n, "North");
		add(c);

		n.add(bg = new JLabel(
				new ImageIcon(getImage("./Áö±ÞÀÚ·á/¹è°æ/" + sno + ".png").getScaledInstance(1200, 300, Image.SCALE_SMOOTH))) {

			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				super.paint(g);
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

		bg_n.add(prev = lbl("µÚ·Î°¡±â", JLabel.LEFT), "West");
		bg_n.add(orderCnt = lbl("ÁÖ¹®Ç¥(0)", JLabel.RIGHT), "East");

		prev.setForeground(Color.WHITE);
		orderCnt.setForeground(Color.WHITE);
		bg_s.add(lbl("<html><left><font size = \"6\"; font color = \"white\">" + sname
				+ "</font><font size = \"4\"; font color = \"WHITE\"><br>" + fee + "/" + time + "/" + rate + "<br>"
				+ about, JLabel.LEFT));

		bg_s.add(like = lbl("¢½", JLabel.RIGHT, 20), "East");
		like.setForeground(Color.RED);

		bg_n.setOpaque(false);
		bg_s.setOpaque(false);

		c.setTabPlacement(JTabbedPane.TOP);

		try {
			var rs = stmt.executeQuery("select * from favorite where seller = " + sno + " and user=" + uno);
			if (rs.next()) {
				like.setText("¢¾");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.addTab("¸Þ´º", m = new Menu());
		c.addTab("¸®ºä", r = new Review());
	}

	class Menu extends JPanel {
		JScrollPane pane;
		ArrayList<ItemRow> rows = new ArrayList<>();
		JPanel c;

		public Menu() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(pane = new JScrollPane(c = new JPanel()));
			c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

			try {
				var rs = stmt.executeQuery("select no, name from type where seller = " + sno);
				while (rs.next()) {
					rows.add(new ItemRow(rs.getInt(1), rs.getString(2)));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rows.forEach(a -> {
				a.init();
				a.setAlignmentX(Component.LEFT_ALIGNMENT);
				c.add(a);
				a.setMaximumSize(new Dimension(1200, a.c.getComponents().length * 150));
			});
			pane.setHorizontalScrollBar(null);
		}

		class ItemRow extends JPanel {

			JPanel c;
			int type;

			public ItemRow(int type, String typeN) {
				this.type = type;
				setLayout(new BorderLayout());
				add(lbl(typeN, JLabel.LEFT, 20), "North");
				add(c = new JPanel());
				c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
				c.setAlignmentX(Component.LEFT_ALIGNMENT);
			}

			void init() {
				try {
					var rs = stmt.executeQuery(
							"select no, name ,description, price, minute(cooktime) from menu where type = " + type);
					var hBox = Box.createHorizontalBox();
					while (rs.next()) {
						Item i = new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
								rs.getString(5));
						hBox.add(i);
						hBox.add(Box.createHorizontalStrut(5));
						if (rs.getRow() % 3 == 0) {
							c.add(hBox);
							hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
							hBox = Box.createHorizontalBox();
						}
					}

					if (hBox.getComponents().length > 0) {
						hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
						c.add(hBox);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			class Item extends JPanel {
				JLabel img;
				public int no, price;
				public String name, dsp, cookTime;

				public Item(int no, String name, String dsp, int price, String cookTime) {
					this.no = no;
					this.price = price;
					this.name = name;
					this.dsp = dsp;
					this.cookTime = cookTime;

					setBorder(new LineBorder(Color.GREEN, 5));
					setLayout(new BorderLayout());

					var c = new JPanel(new BorderLayout());

					add(c);

					c.add(lbl(name, JLabel.LEFT, 20), "North");
					c.add(lbl("<html><font face =\"¸¼Àº °íµñ\"; size = \"3\">" + dsp, JLabel.LEFT));
					c.add(lbl(df.format(price) + "¿ø/" + cookTime + "ºÐ ¼Ò¿ä", JLabel.LEFT), "South");

					add(BaseFrame.size(img = new JLabel(new ImageIcon(getImage("./Áö±ÞÀÚ·á/¸Þ´º/" + no + ".png", 120, 120))),
							120, 120), "East");

					addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2)
								new OptionMenu(Restaurant.this, Item.this).setVisible(true);
							super.mouseClicked(e);
						}
					});

					setMinimumSize(new Dimension(380, 120));
					setMaximumSize(new Dimension(380, 120));
				}
			}
		}
	}

	class Review extends JPanel {
		JPanel c;
		JScrollPane pane;

		public Review() {
			setLayout(new BorderLayout());
			add(pane = new JScrollPane(c = new JPanel()));
			c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

			try {
				var rs = stmt.executeQuery(
						"select  r.title, u.name, r.content, r.rate from eats.review r, user u where r.user = u.no and seller = "
								+ sno);
				while (rs.next()) {
					c.add(new ReviewItem(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
					c.add(Box.createVerticalStrut(5));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		class ReviewItem extends JPanel {
			String stars = "";
			JLabel rate;

			public ReviewItem(String title, String uname, String content, int rate) {
				super(new BorderLayout());
				var n = new JPanel(new BorderLayout());
				add(n, "North");
				n.add(lbl("<html><font size = \"5\"; font face = \"¸¼Àº °íµñ\">" + title + "/<b>" + uname + "</b>ÀÛ¼ºÇÔ",
						JLabel.LEFT), "West");

				for (int i = 1; i <= rate; i++) {
					stars += "¡Ú";
				}

				n.add(this.rate = lbl(stars, JLabel.RIGHT, 20), "East");
				this.rate.setForeground(Color.YELLOW);
				add(lbl("<html><font face = \"¸¼Àº °íµñ\"; font size = \"4\">" + content, JLabel.LEFT));

				setMinimumSize(new Dimension(1100, 120));
				setMaximumSize(new Dimension(1100, 120));

				setBorder(new LineBorder(Color.LIGHT_GRAY));
			}

		}
	}

}