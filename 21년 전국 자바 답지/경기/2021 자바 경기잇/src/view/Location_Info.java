package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

public class Location_Info extends BaseFrame {

	String floor[] = "1F,2F,3F,4F,외부".split(",");
	String character[] = "로티,로리".split(",");
	JLabel floor_img[] = new JLabel[floor.length];
	JLabel ch_img;

	JTabbedPane tab = new JTabbedPane(JTabbedPane.LEFT);

	IconLabel iconlbl[][];
	JComboBox<String> combo = new JComboBox<String>();
	HashMap<String, ArrayList<String>> ride = new HashMap<String, ArrayList<String>>();
	Random r = new Random();
	int point[][][];
	Timer 깜빡거리게하기;

	IconLabel prevLabel;

	public Location_Info() {
		super("위치정보", 1250, 650);

		data();

		ui();

		events();
		setVisible(true);
	}

	void ui() {
		var c = new JPanel(new BorderLayout());
		var e = new JPanel(new BorderLayout());

		add(c);
		add(e, "East");

		e.add(combo, "North");
		e.add(ch_img = new JLabel(
				img("./datafiles/캐릭터/" + character[r.nextInt(2)] + (r.nextInt(3) + 1) + ".jpg", 200, 200)), "South");

		c.add(tab);

		for (int i = 0; i < floor.length; i++) {
			int j = i;
			floor_img[i] = new JLabel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					Graphics2D g2 = (Graphics2D) g;
					Image img = new ImageIcon(
							Toolkit.getDefaultToolkit().getImage("./datafiles/지도/" + floor[j] + ".jpg")).getImage();

					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
					g.drawImage(img, 0, 0, 950, 600, null);
				}

			};
			tab.add(floor[i], floor_img[i]);
			floor_img[i].setOpaque(true);
			floor_img[i].setBackground(Color.white);
			floor_img[i].setLayout(null);

			for (int k = 0; k < point[i].length; k++) {
				floor_img[i].add(iconlbl[i][k]);
			}
		}

		tab.setBackground(Color.white);
		tab.setOpaque(true);

		e.setOpaque(false);
		c.setOpaque(false);

		sz(e, 230, 1);
		setEmpty(e, 0, 10, 0, 10);
	}

	void data() {
		point = new int[floor.length][][];

		iconlbl = new IconLabel[point.length][];

		for (int i = 0; i < floor.length; i++) {
			ride.put(floor[i], new ArrayList<String>());
		}

		try {
			var rs = stmt.executeQuery("select * from ride");
			while (rs.next()) {
				ride.get(rs.getString(3)).add(rs.getString(2));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < floor.length; i++) {
			point[i] = new int[ride.get(floor[i]).size()][2];
			try {
				var rs = stmt.executeQuery("SELECT r_explation from ride where r_floor ='" + floor[i] + "'");
				int idx = 0;
				while (rs.next()) {
					point[i][idx][0] = val(rs.getString(1).split("#")[1]);
					point[i][idx][1] = val(rs.getString(1).split("#")[2]);
					idx++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 0; i < point.length; i++) {
			iconlbl[i] = new IconLabel[point[i].length];
			for (int j = 0; j < point[i].length; j++) {
				boolean enb = (uheight >= val(
						getOne("select r_height from ride where r_name ='" + ride.get(floor[i]).get(j) + "'"))
						&& uold >= val(
								getOne("select r_old from ride where r_name ='" + ride.get(floor[i]).get(j) + "'"))
						&& !(val(getOne(
								"select r_disable from ride where r_name ='" + ride.get(floor[i]).get(j) + "'")) == 1
								&& udisable == 1));
				iconlbl[i][j] = new IconLabel(ride.get(floor[i]).get(j), (enb));
				iconlbl[i][j].setBounds(point[i][j][0] - 25, point[i][j][1] - 50, 50, 50);
			}
		}
	}

	void events() {
		// data이긴 한데

		for (int i = 0; i < ride.get(floor[tab.getSelectedIndex()]).size(); i++) {
			combo.addItem(ride.get(floor[tab.getSelectedIndex()]).get(i));
		}

		combo.setSelectedIndex(-1);
		tab.addChangeListener(a -> {

			combo.removeAllItems();
			for (int i = 0; i < ride.get(floor[tab.getSelectedIndex()]).size(); i++) {
				combo.addItem(ride.get(floor[tab.getSelectedIndex()]).get(i));
			}
			combo.setSelectedIndex(-1);
			try {
				깜빡거리게하기.stop();
				prevLabel.setVisible(true);
			} catch (NullPointerException e) {
				System.out.println("처음에는 다들 그렇죠");
			}
			ch_img.setIcon(img("./datafiles/캐릭터/" + character[r.nextInt(2)] + (r.nextInt(3) + 1) + ".jpg", 200, 200));
		});

		combo.addItemListener(i -> {
			if (combo.getSelectedIndex() == -1)
				return;

			final int Y = tab.getSelectedIndex();
			final int X = combo.getSelectedIndex();

			try {
				깜빡거리게하기.stop();
				prevLabel.setVisible(true);
			} catch (NullPointerException e) {
				System.out.println("처음에는 다들 그렇죠");
			}

			prevLabel = iconlbl[Y][X];

			깜빡거리게하기 = new Timer(1000, a -> {
				iconlbl[Y][X].setVisible(iconlbl[Y][X].isVisible() ? false : true);
			});

			깜빡거리게하기.start();
		});
	}

	public static void main(String[] args) {
		BaseFrame.setLogin(4);
		new Location_Info();
	}
}
