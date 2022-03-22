package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import base.BasePage;

public class PlayListPage extends BasePage {

	String f_serial = "";
	String serial;
	String name;
	String bcap[] = "플레이리스트에서 제거,앨범으로 이동,아티스트로 이동".split(",");

	JPopupMenu menu = new JPopupMenu();

	JPanel c = new JPanel(new BorderLayout(5, 5));
	JPanel c_w = new JPanel(new GridLayout(1, 0));
	JPanel c_c = new JPanel(new GridLayout(0, 1, 15, 15));
	JPanel w = new JPanel(new BorderLayout(5, 5));
	JPanel w_n = new JPanel(new BorderLayout(5, 5));
	JPanel w_n_n = new JPanel(new BorderLayout(10, 10));

	JLabel img;

	public PlayListPage(String serial) {
		this.serial = serial;

		setLayout(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		for (int i = 0; i < bcap.length; i++) {
			JMenuItem item = new JMenuItem(bcap[i]);
			item.addActionListener(a -> {
				if (a.getActionCommand().equals(bcap[0])) {
					iMsg("삭제되었습니다.");
					c_c.removeAll();
					execute("delete from songlist where playlist=" + serial + " and song=" + s_serial);
					createList();
					repaint();
					revalidate();
				} else if (a.getActionCommand().equals(bcap[1])) {
					homePage.scr.setViewportView(new AlbumPage(al_serial));
				} else {
					try {
						var rs = stmt.executeQuery(
								"select ar.serial form album al, artist ar where ar.serial = al.artist and al.serial ="
										+ al_serial);
						if (rs.next()) {
							ar_serial = rs.getString(1);
							homePage.scr.setViewportView(new ArtistPage(ar_serial));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			menu.add(item);
		}

		c.setOpaque(false);
		c_w.setOpaque(false);
		c_c.setOpaque(false);
		w.setOpaque(false);
		w_n.setOpaque(false);

		img = f_serial.equals("") ? BasePage.size(lbl("NO IMAGE", JLabel.CENTER, Font.BOLD, 15), 200, 200)
				: imglbl("./지급자료/images/album/" + f_serial + ".jpg", 200, 200);
		img.setForeground(Color.red);
		w_n_n.add(img);
		w_n_n.add(btn("재생하기", a -> {
		}), "South");
		w_n.add(lbl(name, JLabel.CENTER, Font.BOLD, 20));

		try {
			int length = 0, c = 0;
			var rs2 = stmt.executeQuery(
					"select count(*), time_to_sec(s.length) from song s, user u, playlist pl, songlist sl where s.serial = sl.song and u.serial = pl.user and sl.playlist = pl.serial and pl.serial = "
							+ serial + " and u.serial = " + u_serial + " group by s.serial");
			while (rs2.next()) {
				c += rs2.getInt(1);
				length += rs2.getInt(2);
			}
			int h = length / 3600;
			length = length % 3600;
			int m = length / 60;
			int s = length % 60;

			w_n.add(lbl("<html><center>총" + c + "개의 음악<br>총 길이 : " + String.format("%02d:%02d:%02d", h, m, s)
					+ "<center><html>", JLabel.CENTER, Font.BOLD, 15), "South");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		createList();

		add(c);
		add(w, "West");
		c.add(c_w, "West");
		c.add(c_c);
		c_w.setBorder(new LineBorder(Color.white));
		w.add(w_n, "North");
		w_n.add(w_n_n, "North");
	}

	void createList() {
		try {
			int rank = 1;
			var rs = stmt.executeQuery(
					"select s.serial, s.titlesong, s.name, if((select count(*) from favorite fa, user u, song s1 where fa.user = u.serial and s1.serial = fa.song and s1.serial = s.serial and u.serial = "
							+ u_serial
							+ ") = 0, 0, 1), mid(s.length, 4), al.serial, pl.name from songlist sl, playlist pl, user u, song s, album al where pl.user = u.serial and sl.playlist = pl.serial and sl.song = s.serial and al.serial = s.album and u.serial = "
							+ u_serial + " and pl.serial = " + serial);
			while (rs.next()) {
				if (f_serial.equals("")) {
					f_serial = rs.getString(6);
					name = rs.getString(7);
				}
				RankItem item = new RankItem(rs.getInt(1), rs.getBoolean(2), rank, rs.getString(3), rs.getBoolean(4),
						rs.getString(5));
				item.init();
				item.setName(rs.getString(6) + "!" + rs.getString(1));
				item.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == 3) {
							menu.show(((JPanel) e.getSource()), e.getX(), e.getY());
							al_serial = ((JPanel) e.getSource()).getName().split("!")[0];
							s_serial = ((JPanel) e.getSource()).getName().split("!")[1];
							System.out.println(al_serial);
						}
					}
				});
				c_c.add(item);
				rank++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
