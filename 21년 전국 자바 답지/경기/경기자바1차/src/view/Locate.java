package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import db.DBManager;

public class Locate extends BaseFrame {

	JTabbedPane tab = new JTabbedPane(JTabbedPane.LEFT);
	JPanel floor[] = new JPanel[5];
	String cap[] = "1F,2F,3F,4F,외부".split(",");
	JComboBox<String> combo = new JComboBox<String>();
	JLabel lblImg[] = new JLabel[cap.length];
	Vector<Point> vec = new Vector<Point>();
	String fcap[][];
	HashMap<String, ArrayList<String>> ride = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<JLabel>> icon = new HashMap<String, ArrayList<JLabel>>();
	int point[][][] = {
			{ { 105, 409 }, { 222, 327 }, { 694, 274 }, { 262, 527 }, { 317, 492 }, { 792, 262 }, { 734, 331 },
					{ 580, 232 }, { 574, 383 }, { 662, 336 } },
			{ { 782, 353 }, { 710, 224 } }, { { 636, 302 }, { 260, 372 } },
			{ { 355, 313 }, { 613, 319 }, { 753, 346 } },
			{ { 298, 294 }, { 519, 172 }, { 297, 432 }, { 731, 283 }, { 226, 349 }, { 459, 447 }, { 390, 440 },
					{ 427, 509 }, { 400, 299 }, { 295, 335 }, { 196, 458 }, { 358, 246 }, { 648, 229 } } };
	int idx = 0;
	String character[] = { "로티", "로리" };
	Random r = new Random();
	Thread iconThread;
	JLabel l;

	public Locate() {
		super("위치정보", 1250, 650);

		for (int i = 0; i < cap.length; i++) {
			ride.put(cap[i], new ArrayList<String>());
			icon.put(cap[i], new ArrayList<JLabel>());
		}

		try {
			var rs = DBManager.rs("select * from ride");
			while (rs.next()) {
				ride.get(rs.getString(3)).add(rs.getString(2));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.add(c = new JPanel(new BorderLayout()));
		this.add(e = new JPanel(new BorderLayout()), "East");

		e.add(combo, "North");
		sz(e, 230, 1);
		setEmpty(e, 0, 10, 0, 10);
		e.add(new JLabel(img("./datafiles/캐릭터/" + character[r.nextInt(2)] + (r.nextInt(3) + 1) + ".jpg", 200, 200)),
				"South");

		c.setOpaque(false);
		e.setOpaque(false);
		tab.setOpaque(true);
		tab.setBackground(Color.white);

		c.add(tab);
		for (int i = 0; i < cap.length; i++) {
			int j = i;
			lblImg[i] = new JLabel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					Graphics2D g2 = (Graphics2D) g;
					Image img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./datafiles/지도/" + cap[j] + ".jpg"))
							.getImage();

					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
					g.drawImage(img, 0, 0, 950, 600, null);
				}

			};

			for (int k = 0; k < point[i].length; k++) {
				boolean enb = (uheight >= val(
						DBManager.getOne("select r_height from ride where r_name ='" + ride.get(cap[i]).get(k) + "'"))
						&& uold >= val(DBManager
								.getOne("select r_old from ride where r_name ='" + ride.get(cap[i]).get(k) + "'"))
						&& !(val(DBManager.getOne(
								"select r_disable from ride where r_name ='" + ride.get(cap[i]).get(k) + "'")) == 1
								&& udisable == 1));
				IconLabel ip = new IconLabel(ride.get(cap[i]).get(k), (enb));
				icon.get(cap[i]).add(ip);
				lblImg[i].add(ip);
				ip.setBounds(point[i][k][0] - 25, point[i][k][1] - 50, 50, 50);
				repaint();
			}

			tab.add(cap[i], lblImg[i]);

			lblImg[i].setOpaque(true);
			lblImg[i].setBackground(Color.white);
		}

		tab.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				combo.removeAllItems();
				for (int i = 0; i < ride.get(cap[tab.getSelectedIndex()]).size(); i++) {
					combo.addItem(ride.get(cap[tab.getSelectedIndex()]).get(i));
				}
				combo.setSelectedIndex(-1);

				while (iconThread.isAlive())
					iconThread.interrupt();

			}
		});
		for (int i = 0; i < ride.get(cap[tab.getSelectedIndex()]).size(); i++) {
			combo.addItem(ride.get(cap[tab.getSelectedIndex()]).get(i));
		}
		combo.setSelectedIndex(-1);

		combo.addItemListener(a -> {
			if (combo.getSelectedIndex() != -1) {
				l = icon.get(cap[tab.getSelectedIndex()]).get(combo.getSelectedIndex());

				iconThread = new IconThread();
				iconThread.start();
			} else {
				while (iconThread.isAlive())
					iconThread.interrupt();
			}
		});

		this.setVisible(true);
	}

	class IconThread extends Thread {

		@Override
		public void run() {
			while (true) {
				l.setVisible(false);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				l.setVisible(true);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Thread.interrupted()) {
					l.setVisible(true);
					break;
				}

			}
		}

	}

	class IconLabel extends JLabel {
		public IconLabel(String tit, boolean enabled) {
			super("", img("./datafiles/아이콘.png", 25, 30), 0);
			this.setEnabled(enabled);

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!enabled) {
						eMsg("회원님이 이용할 수 없는 놀이기구입니다.");
						return;
					}
					new MoreInform(tit).addWindowListener(new Before(Locate.this));
				}
			});
		}
	}

	public static void main(String[] args) {
		BaseFrame.setLogin(4);
		new Locate();
	}

}
