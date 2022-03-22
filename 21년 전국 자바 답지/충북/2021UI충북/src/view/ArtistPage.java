package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import db.DB;

public class ArtistPage extends BasePage {

	JPanel artistP, mainP, viewP;
	DefaultTableModel m = songModel();
	ArrayList<Integer> albumList = new ArrayList<>();
	JTable table = songTable(m);

	public ArtistPage() {
		mainP = mf.bPanel.me;
		mainP.setOpaque(true);
		mainP.setBackground(Color.BLACK);
		mainP.setLayout(new BorderLayout());
		setData();
		setUI();

		mainP.revalidate();
		mainP.repaint();

	}

	void setData() {
		try {
			var rs = stmt.executeQuery("select * from artist where serial = " + a_serial);
			if (rs.next()) {
				a_name = rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("select * from album where artist = " + a_serial);
			while (rs.next()) {
				albumList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addSongRow("SELECT \r\n" + "    if(s.titlesong = 1 , 1, 0),\r\n" + "	s.name,\r\n"
				+ "   if(s.serial in  ( select f.song from user u , favorite f where u.serial = f.user and u.serial = "
				+ u_serial + "), true, false) isFavorite\r\n" + "    ,time_format(s.length, '%i:%S') \r\n"
				+ "	,s.serial\r\n" + "FROM\r\n" + "    song s,\r\n" + "    album al,\r\n" + "    artist ar,\r\n"
				+ "    history h\r\n" + "WHERE\r\n" + "	s.album = al.serial\r\n" + "    and al.artist = ar.serial\r\n"
				+ "    and h.song = s.serial\r\n" + "    and ar.serial = " + a_serial + "\r\n"
				+ "	group by s.serial\r\n" + "	order by count(h.serial) desc limit 5", m);
	}

	private void setUI() {
		artistP = new JPanel(new BorderLayout());
		viewP = new JPanel();
		viewP.setLayout(new BoxLayout(viewP, BoxLayout.Y_AXIS));
		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new BorderLayout(5, 5));
		var c = new JPanel(new BorderLayout());
		var c_c = new JPanel(new BorderLayout());
		var c_n = new JPanel(new BorderLayout());
		var c_n_c = new JPanel(new FlowLayout(FlowLayout.LEFT));

		artistP.add(n, "North");
		artistP.add(c);

		// N
		n.add(n_c);
		n_c.add(imglbl("./지급자료/images/artist/" + a_serial + ".jpg", 180, 180), "West");
		n_c.add(lbl(a_name, JLabel.LEFT, 0, 20));
		// C
		c.add(c_c);

		c.add(viewP, "South");
		c.add(c_n, "North");
		c_n.add(c_n_c);

		for (var bcap : "개요,소개".split(",")) {
			c_n_c.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("개요")) {
//					viewP.add(favoriteM);
					for (var al : albumList) {
						mainP.add(new AlbumItem(al));
					}
					mainP.repaint();
					mainP.revalidate();
				} else {
					for (var v : mainP.getComponents()) {
						if (v instanceof AlbumItem) {
							mainP.remove(v);
						}
					}

					mainP.repaint();
					mainP.revalidate();
				}
			}));
		}

		c_c.add(lbl("인기있는 음악", JLabel.LEFT, 0, 20), "North");
		c_c.add(size(new Chart(), 220, 200), "East");
		c_c.add(table);

		mainP.add(artistP);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				albumList.forEach(a -> {
					viewP.add(new AlbumItem(a));
				});
			}
		});

		n.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
		n_c.setBorder(new EmptyBorder(5, 5, 5, 5));
		n_c.setOpaque(false);
		n.setOpaque(false);
		c.setOpaque(false);
		c_n.setOpaque(false);
		viewP.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));
		viewP.setOpaque(false);
		c_n_c.setOpaque(false);
		c_c.setOpaque(false);
		c_c.setBorder(new MatteBorder(2, 0, 2, 0, Color.WHITE));
		artistP.setOpaque(false);
		artistP.setBorder(new EmptyBorder(5, 5, 5, 20));
		viewP.setLayout(new BoxLayout(viewP, BoxLayout.Y_AXIS));
		artistP.setMaximumSize(new Dimension(mainP.getWidth(), 420));

		mainP.revalidate();
		mainP.repaint();
	}

	class Review extends JPanel {

		String u_name;
		int rate;
		String date;
		String content;

		public Review(String u_name, int rate, String date, String content) {
			this.u_name = u_name;
			this.rate = rate;
			this.date = date;
			this.content = content;
		}

		void init() {
			JPanel n = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JTextArea txt = new JTextArea();

			setOpaque(false);
			n.setOpaque(false);

			setLayout(new BorderLayout());
			txt.setBackground(Color.gray);

			add(n, "North");
			add(BasePage.size(txt, 700, 100));

			txt.setEditable(false);
			txt.setBorder(new LineBorder(Color.white));
			txt.setForeground(Color.white);
			txt.setBackground(Color.gray);
			txt.setText(content);
			txt.setLineWrap(true);

			n.add(lbl(u_name + " - ", JLabel.LEFT, 0, 15));
			for (int i = 0; i < 5; i++) {
				JLabel l;
				if (i <= rate - 1) {
					l = lbl("★", JLabel.LEFT, 0, 15);
				} else {
					l = lbl("☆", JLabel.LEFT, 0, 15);
				}
				l.setForeground(Color.orange);
				n.add(l);
			}
			n.add(lbl(date + " 게시", JLabel.LEFT, 0, 15));
		}
	}

	class AlbumItem extends JPanel {
		DefaultTableModel m = songModel();
		JTable t = songTable(m);
		String info = "<html>", alName, release;
		JLabel showAll;
		int al_serial;
		Statement stmt = DB.stmt;

		public AlbumItem(int al_serial) {
			super(new BorderLayout());
			this.al_serial = al_serial;
			setData();
			ui();
			addSongRow("SELECT \r\n" + "	if(s.titlesong = 1 , 1, 0),\r\n" + "	s.name,\r\n"
					+ "   if(s.serial in  ( select f.song from user u , favorite f where u.serial = f.user and u.serial = 1), true, false) isFavorite\r\n"
					+ "    ,time_format(s.length, '%i:%S') \r\n" + "	,s.serial\r\n" + "FROM\r\n" + "    song s,\r\n"
					+ "    album al,\r\n" + "    artist ar\r\n" + "WHERE\r\n" + "	s.album = al.serial\r\n"
					+ "    and al.artist = ar.serial\r\n" + "    and al.serial = " + al_serial + "\r\n"
					+ "	group by s.serial", m);

		}

		void ui() {
			var n = new JPanel(new BorderLayout());
			var n_c = new JPanel(new BorderLayout(5, 5));
			add(n, "North");
			n.add(n_c);

			n.add(imglbl("./지급자료/images/album/" + al_serial + ".jpg", 180, 180), "West");
			n_c.add(lbl(info, JLabel.LEFT, 0, 10));

			n_c.add(showAll = lbl("모두 보기", JLabel.LEFT, 0, 10), "South");

			n.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE), new EmptyBorder(5, 5, 5, 5)));
			add(this.t);
			setOpaque(false);
			n.setOpaque(false);
			n_c.setOpaque(false);
		}

		void setData() {
			try {
				var rs = stmt.executeQuery(
						"SELECT al.name, c.name, al.release FROM album al, category c where al.category = c.serial and al.serial = "
								+ al_serial);

				if (rs.next()) {
					info += "<left>" + rs.getString(1) + "<br>" + a_name + "<br>" + rs.getString(2) + "·"
							+ rs.getString(3);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class Chart extends BasePage {

		JPanel c = new JPanel(new BorderLayout()), chart;

		public Chart() {
			c.setBackground(Color.black);
			c.setOpaque(false);
			add(c);

			drawChart();
		}

		private void drawChart() {
			c.removeAll();
			chart = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.black);
					g2.fillRect(0, 0, getWidth(), getHeight());
					g2.setColor(Color.WHITE);

					try {
						ArrayList<Integer> listValues = new ArrayList<>();

						for (int i = 10; i + 10 < 70; i += 10) {
							var rs = stmt.executeQuery(
									"select count(*) FROM artist ar, album al, user u, song s, history h WHERE ar.serial =  "
											+ a_serial
											+ " AND h.song = s.serial AND h.user = u.serial AND s.album = al.serial AND al.artist = ar.serial  and datediff(now(), u.birth)/365 >= '"
											+ i + "' and datediff(now(), u.birth)/365 < '"
											+ ((i + 10) == 60 ? 100 : (i + 10)) + "'");

							if (rs.next()) {
								listValues.add(rs.getInt(1));
							}
						}

						int max = Collections.max(listValues);
						int mvalue = (int) (((double) max / max) * 150); // 사실상 100
						for (int i = 0; i < 5; i++) {
							int value = (int) (((double) listValues.get(i) / max) * 100);
							if (listValues.get(i) == max)
								g2.setColor(Color.RED);
							else
								g2.setColor(Color.WHITE);
							g2.fillRect(i * 40 - 10 + 20, (mvalue - value) - 20, 30, value);
							g2.setColor(Color.WHITE);
							g2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
							g2.drawString(((i + 1) * 10) + "대", (i * 40) + 10, mvalue);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			c.add(chart);
		}
	}

	public static void main(String[] args) {
		a_serial = 26;
		u_serial = 1;
		mf = new MainFrame();
		new ArtistPage();
	}
}

class FillPainter implements Painter<JComponent> {
	private final Color color;

	public FillPainter(Color c) {
		color = c;
	}

	@Override
	public void paint(Graphics2D g, JComponent object, int width, int height) {
		g.setColor(color);
		g.fillRect(0, 0, width - 1, height - 1);
	}
}
