package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ArtistPage extends BasePage {

	String ar_serial;
	String ar_name;
	North n;
	JPanel c;
	JPanel s;
	Summary sum;
	Chart chart;
	Introduce intro;

	ArrayList<String> al_serials;

	public ArtistPage(String ar_serial) {
		this.ar_serial = ar_serial;

		try {
			var rs1 = stmt.executeQuery("select name from artist where serial=" + ar_serial);
			if (rs1.next()) {
				ar_name = rs1.getString(1);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		n = new North();
		c = new JPanel(new BorderLayout());
		s = new JPanel(new GridLayout(0, 1));
		chart = new Chart();
		al_serials = new ArrayList<String>();
		sum = new Summary();
		intro = new Introduce();

		c.setOpaque(false);
		s.setOpaque(false);

		add(n, "North");
		add(c);

		c.add(sum);
		c.add(BasePage.size(chart, 250, 100), "East");
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

	class Introduce extends JPanel {

		JTextField txt = txt(0);

		JPanel w = new JPanel(new BorderLayout());
		JPanel w_n = new JPanel(new BorderLayout());
		JPanel c = new JPanel(new BorderLayout());
		JPanel c_c = new JPanel(new BorderLayout(5, 5));
		JPanel c_c_s = new JPanel(new BorderLayout());
		JPanel c_c_s_w = new JPanel();
		JPanel c_s = new JPanel(new GridLayout(0, 1));

		JLabel stars[] = { lbl("☆", 0, 0, 20), lbl("☆", 0, 0, 20), lbl("☆", 0, 0, 20), lbl("☆", 0, 0, 20),
				lbl("☆", 0, 0, 20) };

		ArrayList<String> rank = new ArrayList<String>();
		HashMap<String, String> about = new HashMap<String, String>();
		int rate;

		public Introduce() {
			setLayout(new BorderLayout());

			setOpaque(false);
			w.setOpaque(false);
			c.setOpaque(false);
			c_s.setOpaque(false);
			w_n.setOpaque(false);
			c_c.setOpaque(false);
			c_c_s.setOpaque(false);
			c_c_s_w.setOpaque(false);

			add(BasePage.size(w, 300, 150), "West");
			add(c);

			w.add(w_n, "North");
			c.add(BasePage.size(c_c, 700, 150));
			c.add(c_s, "South");
			c_c.add(c_c_s, "South");
			c_c_s.add(c_c_s_w, "West");

			data();

			w_n.add(lbl("세계 순위 " + (rank.indexOf(ar_name) + 1) + "위", JLabel.LEFT, 0, 15), "North");
			w_n.add(lbl("<html>" + about.get(ar_name) + "<html>", JLabel.LEFT, 0, 10));

			c_c.add(txt);
			for (int i = 0; i < 5; i++) {
				stars[i].setForeground(Color.orange);
				stars[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						for (int j = 0; j < stars.length; j++) {
							stars[j].setText("☆");
						}

						for (int j = 0; j < BasePage.toInt(((JLabel) e.getSource()).getName()); j++) {
							stars[j].setText("★");
						}

						rate = BasePage.toInt(((JLabel) e.getSource()).getName());
						System.out.println(rate);
					}
				});
				stars[i].setName(i + 1 + "");
				c_c_s_w.add(stars[i]);
			}
			c_c_s.add(btn("등록", a -> {
				if (txt.getText().equals("")) {
					eMsg("내용을 입력해야 합니다.");
					return;
				}

				int r = 0;

				if (rate == 0) {
					eMsg("평점을 입력해야 합니다.");
					return;
				}

				execute("insert into community values(0, " + u_serial + ", " + ar_serial + ", " + rate + ", '"
						+ txt.getText() + "', curdate())");
				try {
					var rs = stmt.executeQuery(
							"select u.name, co.rate, co.date, co.content from user u, community co where u.serial = co.user and co.artist = "
									+ ar_serial + " order by co.date desc");
					c_s.removeAll();
					while (rs.next()) {
						Review review = new Review(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4));
						review.init();
						c_s.add(review);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}), "East");

			try {
				var rs = stmt.executeQuery(
						"select u.name, co.rate, co.date, co.content from user u, community co where u.serial = co.user and co.artist = "
								+ ar_serial + " order by co.date desc");
				while (rs.next()) {
					Review review = new Review(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4));
					review.init();
					c_s.add(review);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void data() {
			try {
				var rs1 = stmt.executeQuery(
						"select ar.name, ar.about, count(*) from artist ar, song s, history h, album al where s.serial = h.song and al.serial = s.album and al.artist = ar.serial group by ar.name order by count(*) desc");
				while (rs1.next()) {
					rank.add(rs1.getString(1));
					about.put(rs1.getString(1), rs1.getString(2));
				}

				var rs2 = stmt.executeQuery(
						"select u.name, co.rate, co.date, co.content from user u, community co where u.serial = co.user and co.artist = 26 order by co.date desc");
				while (rs2.next()) {
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class SummaryItem extends JPanel {

		String al_serial;
		String info_text = "<html>";

		JLabel img;
		JLabel info;

		JPanel n, n_c;
		JPanel c;

		public SummaryItem(String al_serial) {
			this.al_serial = al_serial;
			setOpaque(false);

			data();
			ui();
		}

		private void data() {
			try {
				var rs = stmt.executeQuery(
						"select al.name, ar.name, c.name, al.release from album al, category c, artist ar where al.artist= ar.serial and c.serial = al.category and al.serial = "
								+ al_serial);
				if (rs.next()) {
					info_text += rs.getString(1) + "<br>" + rs.getString(2) + "<br>" + rs.getString(3) + " : "
							+ rs.getString(4) + info_text;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void ui() {
			setLayout(new BorderLayout(5, 5));

			n = new JPanel(new BorderLayout(5, 5));
			n_c = new JPanel(new BorderLayout());
			c = new JPanel(new GridLayout(0, 1, 10, 10));
//			line = new TitleLine("", 100);

			n_c.setOpaque(false);
			n.setOpaque(false);
			c.setOpaque(false);

			img = imglbl("./지급자료/images/album/" + al_serial + ".jpg", 180, 180);
			info = lbl(info_text, JLabel.LEFT, Font.PLAIN, 20);

			add(n, "North");
			add(c);

			n.add(img, "West");
			n.add(n_c);
			n_c.add(BasePage.size(info, 500, 150));
			n_c.add(lbl("모두 보기", JLabel.LEFT, Font.BOLD, 15, new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
				}
			}), "South");

			try {
				int rank = 1;
				var rs = stmt.executeQuery(
						"select serial, titlesong, name, if((select count(*) from favorite fa, user u, song s where fa.user = u.serial and fa.song = s.serial and s.serial = song.serial and u.serial = "
								+ u_serial + ")=0, 0, 1) , mid(length, 4) from song where song.album = " + al_serial);
				while (rs.next()) {
					RankItem item = new RankItem(rs.getInt(1), rs.getBoolean(2), rank, rs.getString(3),
							rs.getBoolean(4), rs.getString(5));
					item.init();
					item.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							homePage.pane.setViewportView(new AlbumPage(al_serial));
						}
					});
					c.add(item);
					rank++;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	class Summary extends JPanel {

		JPanel c;
		JPanel c_n;
		JPanel c_c;
		Chart c_e;
		JPanel s;

		public Summary() {
			setOpaque(false);
			setLayout(new BorderLayout());

			c = new JPanel(new BorderLayout());
			c_n = new JPanel(new FlowLayout(FlowLayout.LEFT));
			c_c = new JPanel(new GridLayout(0, 1, 5, 5));
			s = new JPanel(new GridLayout(0, 1));

			c_n.setOpaque(false);
			c_c.setOpaque(false);
			n.setOpaque(false);
			c.setOpaque(false);
			s.setOpaque(false);

			c_n.add(lbl("인기 있는 음악", JLabel.LEFT, Font.BOLD, 25));
			try {
				int cnt = 0;
				var rs = stmt.executeQuery(
						"select s.serial, s.titlesong, s.name, if((select 1 from favorite fa, user u, song s1 where fa.song = s1.serial and fa.user = u.serial and s1.serial = s.serial and u.serial = "
								+ u_serial
								+ ") = null, 1, 0), mid(s.length, 4)  from song s, artist ar, album al, history h where s.serial = h.song and al.serial = s.album and ar.serial = al.artist and ar.name = '"
								+ ar_name + "' group by s.name order by count(*) desc, s.name asc limit 5;");
				while (rs.next()) {
					cnt++;
					RankItem item = new RankItem(rs.getInt(1), rs.getBoolean(2), cnt, rs.getString(3), rs.getBoolean(4),
							rs.getString(5));
					item.init();
					c_c.add(item);
					c_c.setBorder(new MatteBorder(0, 0, 3, 0, Color.WHITE));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			add(c);
			add(s, "South");
			c.add(c_n, "North");
			c.add(c_c);

			try {

				var rs = stmt.executeQuery(
						"select al.serial from album al, artist ar where al.artist = ar.serial and ar.serial = "
								+ ar_serial + " order by al.release desc;");
				while (rs.next()) {
					al_serials.add(rs.getString(1));
				}
				for (String al_serial : al_serials) {
					SummaryItem item = new SummaryItem(al_serial);
					s.add(item);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class North extends JPanel {

		JPanel n;
		JPanel s, s_c;

		JLabel img = imglbl("./지급자료/images/artist/" + ar_serial + ".jpg", 150, 150);

		public North() {
			super(new BorderLayout());

			add(n = new JPanel(new FlowLayout(FlowLayout.LEFT)), "North");
			add(s = new JPanel(new FlowLayout(FlowLayout.LEFT)), "South");

			n.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));
			n.add(img);
			n.add(lbl(ar_name, JLabel.LEFT, Font.BOLD, 25));

			s.add(btn("개요", a -> {
				remove(s);
				c.removeAll();
				c.add(sum);
				c.add(BasePage.size(chart, 250, 100), "East");
				add(s, "South");
				repaint();
				revalidate();
			}));

			s.add(btn("소개", a -> {
				c.removeAll();
				c.add(intro);
				repaint();
				revalidate();
			}));

			setOpaque(false);
			n.setOpaque(false);
			s.setOpaque(false);
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
						var rs = stmt.executeQuery("SELECT "
								+ "sum(if(date_format(now(),'%Y')-substring(u.BIRTH,1,4) between 10 and 19 , 1, 0)) as age_10,"
								+ "sum(if(date_format(now(),'%Y')-substring(u.BIRTH,1,4) between 20 and 29 , 1, 0)) as age_20,"
								+ "sum(if(date_format(now(),'%Y')-substring(u.BIRTH,1,4) between 30 and 39 , 1, 0)) as age_30,"
								+ "sum(if(date_format(now(),'%Y')-substring(u.BIRTH,1,4) between 40 and 49 , 1, 0)) as age_40,"
								+ "sum(if(date_format(now(),'%Y')-substring(u.BIRTH,1,4) between 50 and 59 , 1, 0)) as age_50"
								+ " FROM artist ar, album al, user u, song s, history h WHERE ar.serial = "
								+ ArtistPage.this.ar_serial
								+ " AND h.song = s.serial AND h.user = u.serial AND s.album = al.serial AND al.artist = ar.serial");
						ArrayList<Integer> listValues = new ArrayList<>();

						if (rs.next()) {
							for (int i = 0; i < 5; i++) {
								listValues.add(rs.getInt(i + 1));
							}

						}
						int max = Collections.max(listValues);
						int mvalue = (int) (((double) max / max) * 200); // 사실상 100
						for (int i = 0; i < 5; i++) {
							int value = (int) (((double) listValues.get(i) / max) * 200);
							if (listValues.get(i) == max)
								g2.setColor(Color.RED);
							else
								g2.setColor(Color.WHITE);
							g2.fillRect((i * 40) + 20, mvalue - value, 30, value);
							g2.setColor(Color.WHITE);
							g2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
							g2.drawString(((i + 1) * 10) + "대", (i * 40) + 20, mvalue + 20);
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
		u_serial = 1;
		u_region = 1;
		homePage = new HomePage();
		mf.swapPage(homePage);
		ArtistPage s = new ArtistPage("26");
		homePage.pane.setViewportView(s);
		mf.repaint();
		mf.revalidate();
	}
}
