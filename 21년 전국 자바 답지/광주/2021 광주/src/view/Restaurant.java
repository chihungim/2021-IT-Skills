package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Restaurant extends BaseDialog {
	JLabel prev, orderCnt, like, bg;
	int sno;
	String about, sname, rate, fee, time;
	JPanel menu, review;
	JScrollPane pane1, pane2;
	HashMap<Integer, ArrayList<Item>> itemMap = new HashMap<>();
	ArrayList<Review> reviewList = new ArrayList<>();
	HashMap<Integer, String> ItemName = new HashMap<>();

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
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery(
					"select t.no ,m.no, m.name, m.DESCRIPTION, m.price, minute(m.cooktime) from  menu m,type t where m.TYPE = t.no and m.seller = "
							+ sno);
			while (rs.next()) {
				if (itemMap.containsKey(rs.getInt(1))) {
					itemMap.get(rs.getInt(1)).add(
							new Item(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)));
				} else {
					itemMap.put(rs.getInt(1), new ArrayList<>());
					itemMap.get(rs.getInt(1)).add(
							new Item(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("select no, name from type");
			while (rs.next()) {
				ItemName.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery(
					"select  r.title, u.name, r.content, r.rate from eats.review r, user u where r.user = u.no and seller = "
							+ sno);
			while (rs.next()) {
				reviewList.add(new Review(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void ui() {
		var n = new JPanel(new BorderLayout());
		var bg_n = new JPanel(new BorderLayout());
		var bg_s = new JPanel(new BorderLayout());
		var c = new JTabbedPane(2);

		n.setBackground(Color.BLACK);
		n.setOpaque(true);
		add(n, "North");
		add(c);

		n.add(bg = new JLabel(getIcon("./지급자료/배경/" + sno + ".png", 1200, 300)) {
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

		bg_n.add(prev = lbl("뒤로가기", JLabel.LEFT), "West");
		bg_n.add(orderCnt = lbl("주문표(0)", JLabel.RIGHT), "East");

		prev.setForeground(Color.WHITE);
		orderCnt.setForeground(Color.WHITE);
		bg_s.add(lbl("<html><left><font size = \"6\"; font color = \"white\"><b>" + sname
				+ "</b></font><font size = \"4\"; font color = \"WHITE\"><br>" + fee + "/" + time + "/" + rate + "<br>"
				+ about, JLabel.LEFT));

		bg_s.add(like = lbl("♡", JLabel.RIGHT, 20), "East");
		like.setForeground(Color.RED);

		bg_n.setOpaque(false);
		bg_s.setOpaque(false);

		c.setTabPlacement(JTabbedPane.TOP);

		try {
			var rs = stmt.executeQuery("select * from favorite where seller = " + sno + " and user=" + uno);
			if (rs.next()) {
				like.setText("♥");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.addTab("메뉴", pane1 = new JScrollPane(menu = new JPanel()));
		c.addTab("리뷰", pane2 = new JScrollPane(review = new JPanel()));
		// menu
		for (var k : itemMap.keySet()) {
			var itemP = new JPanel(new BorderLayout());
			final var itemC = new JPanel();

			itemP.add(lbl(ItemName.get(k), JLabel.LEFT, 20), "North");
			itemP.add(itemC);

			itemC.setLayout(new BoxLayout(itemC, BoxLayout.Y_AXIS));

			var hbox = Box.createHorizontalBox();
			hbox.setAlignmentX(LEFT_ALIGNMENT);
			for (var element : itemMap.get(k)) {
				var i = new JPanel(new BorderLayout());
				var ic = new JPanel(new BorderLayout());
				var img = new JLabel(getIcon(element.imgPath, 120, 120));
				if (hbox.getComponents().length == 3) {
					itemC.add(hbox);
					hbox = Box.createHorizontalBox();
					hbox.setAlignmentX(LEFT_ALIGNMENT);
				}

				ic.add(lbl(element.name, JLabel.LEFT, 15), "North");
				ic.add(lbl("<html>" + element.description, JLabel.LEFT));
				ic.add(lbl(df.format(element.price) + "원/" + element.cooktime + "분 소요", JLabel.LEFT), "South");
				i.add(ic);
				i.add(size(img, 120, 120), "East");
				hbox.add(i);
				size(i, 380, 120);
				i.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new LineBorder(Color.GREEN)));
				i.setMinimumSize(new Dimension(380, 120));
				i.setMaximumSize(new Dimension(380, 120));
				i.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getClickCount() == 2) {
							new OptionMenu(Restaurant.this, element).setVisible(true);
						}
					}
				});
				img.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getClickCount() == 2) {
							new OptionMenu(Restaurant.this, element).setVisible(true);
						}
						super.mousePressed(e);
					}
				});
			}

			if (hbox.getComponents().length > 0) {
				itemC.add(hbox);
			}
			menu.add(itemP);
		}

		for (var r : reviewList) {
			var rc = new JPanel(new BorderLayout());
			var rn = new JPanel(new BorderLayout());
			rc.add(rn, "North");
			rn.add(lbl(r.title, JLabel.LEFT), "West");
			rn.add(lbl("<html><font color = \"YELLOW\">" + r.stars, JLabel.RIGHT, 20), "East");
			rc.add(lbl(r.content, JLabel.LEFT));
			rc.setBorder(new LineBorder(Color.LIGHT_GRAY));
			review.add(size(rc, 1100, 100));
			rc.setMinimumSize(new Dimension(1100, 100));
			rc.setMaximumSize(new Dimension(1100, 100));
			rc.setAlignmentY(TOP_ALIGNMENT);
		}
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		review.setLayout(new GridLayout(0, 1, 5, 5));
		review.setBorder(new EmptyBorder(20, 20, 20, 20));
	}

	class Item {
		int no, price;
		String name, description, cooktime, imgPath;

		public Item(int no, String name, String description, int price, String cooktime) {
			this.no = no;
			this.price = price;
			this.name = name;
			this.description = description;
			this.price = price;
			this.cooktime = cooktime;
			imgPath = "./지급자료/메뉴/" + no + ".png";
		}
	}

	class Review {
		String stars = "";
		String title = "";
		String content;

		public Review(String title, String uname, String content, int rate) {
			this.title = "<html><font size = \"5\"; font face = \"맑은 고딕\">" + title + "/<b>" + uname + "</b>작성함";
			this.content = "<html>" + content;
			for (int i = 1; i <= rate; i++) {
				stars += "★";
			}
		}
	}

	void addEvents() {
		like.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (like.getText().equals("♥")) {
					like.setText("♡");
					execute("delete from favorite where seller = " + sno + " and user = " + uno);
				} else {
					like.setText("♥");
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

}