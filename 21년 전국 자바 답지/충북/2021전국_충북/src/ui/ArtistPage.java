package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


import base.BasePage;

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

		JLabel rate[] = { lbl("☆", 0, 0, 20), lbl("☆", 0, 0, 20), lbl("☆", 0, 0, 20), lbl("☆", 0, 0, 20),
				lbl("☆", 0, 0, 20) };

		ArrayList<String> rank = new ArrayList<String>();
		HashMap<String, String> about = new HashMap<String, String>();

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
				rate[i].setForeground(Color.orange);
				rate[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						for (int j = 0; j < rate.length; j++) {
							rate[j].setText("☆");
						}

						for (int j = 0; j < BasePage.toInt(((JLabel) e.getSource()).getName()); j++) {
							rate[j].setText("★");
						}
					}
				});
				rate[i].setName(i + 1 + "");
				c_c_s_w.add(rate[i]);
			}
			c_c_s.add(btn("등록", a -> {
				if(txt.getText().equals("")) {
					eMsg("내용을 입력해야 합니다.");
					return;
				}
				
				int r = 0;
				for (int i = 0; i < rate.length; i++) {
					if(rate[i].getText().equals("☆")) {
						eMsg("평점을 입력해야 합니다.");
						return;
					}else if(rate[i].getText().equals("★")) {
						r++;
					}
				}
				
				execute("insert into community values(0, "+u_serial+", "+ar_serial+", "+r+", '"+txt.getText()+"', curdate())");
				
			}), "East");

			try {
				var rs = stmt.executeQuery(
						"select u.name, co.rate, co.date, co.content from user u, community co where u.serial = co.user and co.artist = "+ar_serial+" order by co.date desc");
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
		TitleLine line;

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
			line = new TitleLine("", 100);

			n_c.setOpaque(false);
			n.setOpaque(false);
			c.setOpaque(false);

			img = imglbl("./지급자료/images/album/" + al_serial + ".jpg", 180, 180);
			info = lbl(info_text, JLabel.LEFT, Font.PLAIN, 20);

			add(n, "North");
			add(c);

			n.add(img, "West");
			n.add(n_c);
			n.add(line, "South");
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
							homePage.scr.setViewportView(new AlbumPage(al_serial));
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
		TitleLine c_s;
		JPanel s;

		int values[] = { 0, 0, 0, 0, 0 };

		public Summary() {
			setOpaque(false);
			setLayout(new BorderLayout());

			try {
				var rs1 = stmt.executeQuery(
						"select (2021 - year(u.birth)) as age, count(*) from artist ar, album al, user u, song s, history h where h.song = s.serial and h.user = u.serial and s.album = al.serial and al.artist = ar.serial and ar.serial = "
								+ ar_serial + " group by age order by age");
				while (rs1.next()) {
					if (rs1.getInt(1) < 20) {

						values[0] += rs1.getInt(2);
					} else if (rs1.getInt(1) < 30) {
						System.out.println(rs1.getInt(1));
						values[1] += rs1.getInt(2);
					} else if (rs1.getInt(1) < 40) {
						values[2] += rs1.getInt(2);
					} else if (rs1.getInt(1) < 50) {
						values[3] += rs1.getInt(2);
					} else if (rs1.getInt(1) < 60) {
						values[4] += rs1.getInt(2);
					}
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			for (int i : values) {
				System.out.println(i);
			}

			c = new JPanel(new BorderLayout());
			c_n = new JPanel(new FlowLayout(FlowLayout.LEFT));
			c_c = new JPanel(new GridLayout(0, 1, 5, 5));
			c_s = new TitleLine("", 1);
			s = new JPanel(new GridLayout(0, 1));

			c_n.setOpaque(false);
			c_c.setOpaque(false);
			n.setOpaque(false);
			c.setOpaque(false);
			s.setOpaque(false);

			c_n.add(lbl("인기 있는 음악", JLabel.LEFT, Font.BOLD, 25));
			try {
				int cnt = 0;
//				System.out.println("select s.serial, s.titlesong, s.name, mid(s.length, 4), count(*) from song s, artist ar, album al, history h w/here s.serial = h.song and al.serial = s.album and ar.serial = al.artist and ar.name = '방탄소년단' group by s.name order by count(*) desc, s.name asc limit 5;");
				var rs2 = stmt.executeQuery(
						"select s.serial, s.titlesong, s.name, if((select 1 from favorite fa, user u, song s1 where fa.song = s1.serial and fa.user = u.serial and s1.serial = s.serial and u.serial = "
								+ u_serial
								+ ") = null, 1, 0), mid(s.length, 4)  from song s, artist ar, album al, history h where s.serial = h.song and al.serial = s.album and ar.serial = al.artist and ar.name = '"
								+ ar_name + "' group by s.name order by count(*) desc, s.name asc limit 5;");
				while (rs2.next()) {
					cnt++;
					RankItem item = new RankItem(rs2.getInt(1), rs2.getBoolean(2), cnt, rs2.getString(3),
							rs2.getBoolean(4), rs2.getString(5));
					item.init();
					c_c.add(item);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			add(c);
			add(s, "South");
			c.add(c_n, "North");
			c.add(c_c);
			c.add(BasePage.size(c_s, 100, 10), "South");

			try {
				System.out.println(
						"select al.serial from album al, artist ar where al.artist = ar.serial and ar.serial = "
								+ ar_serial + " order by al.release desc;");
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

		JPanel n = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel t = new TitleLine("", 500);
		JPanel s1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel img = imglbl("./지급자료/images/artist/" + ar_serial + ".jpg", 150, 150);

		public North() {
			setOpaque(false);
			setLayout(new BorderLayout());

			n.setOpaque(false);
			s1.setOpaque(false);

			add(n, "North");
			add(t);
			add(s1, "South");

			n.add(img);
			n.add(lbl(ar_name, JLabel.LEFT, Font.BOLD, 25));

			s1.add(btn("개요", a -> {
				remove(s);
				c.removeAll();
				c.add(sum);
				c.add(BasePage.size(chart, 250, 100), "East");
				add(s, "South");
				repaint();
				revalidate();
			}));
			s1.add(btn("소개", a -> {
				remove(s);
				c.removeAll();
				c.add(intro);
				repaint();
				revalidate();
			}));
		}
	}

	public static void main(String[] args) {
		u_serial = 1;
		u_region = 1;
		homePage = new HomePage();
		mf.add(homePage);
		ArtistPage s = new ArtistPage("26");
		homePage.scr.setViewportView(s);
		mf.repaint();
		mf.revalidate();
	}
}
