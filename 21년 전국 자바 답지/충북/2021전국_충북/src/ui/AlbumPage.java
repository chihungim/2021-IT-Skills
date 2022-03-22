package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import base.BasePage;

public class AlbumPage extends BasePage {

	String al_serial;

	JPanel n = new JPanel(new BorderLayout(5, 5));
	JPanel n_c = new JPanel(new BorderLayout());
	JPanel n_c_c = new JPanel(new GridLayout(0, 1));
	JPanel c = new JPanel(new GridLayout(0, 1, 15, 15));

	JMenuItem artistItem = new JMenuItem("아티스트로 이동");

	JLabel l;
	JLabel img;

	public AlbumPage(String al_serial) {
		System.out.println(al_serial);
		this.al_serial = al_serial;

		img = imglbl("./지급자료/images/album/" + al_serial + ".jpg", 200, 200);

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(5, 5));
		n.setOpaque(false);
		n_c.setOpaque(false);
		n_c_c.setOpaque(false);
		c.setOpaque(false);

		artistItem.addActionListener(a -> {
			String ar_serial = "";
			try {
				var rs = stmt.executeQuery("select serial from artist where name = '" + al_serial + "'");
				if(rs.next()) {
					ar_serial = rs.getString(1);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			homePage.scr.setViewportView(new ArtistPage(ar_serial));
		});

		add(n, "North");
		add(c);

		n.add(img, "West");
		n.add(n_c);
		n.add(new TitleLine("", 500), "South");
		n_c.add(n_c_c);
		n_c.add(btn("재생하기", a -> {
			iMsg("앨범을 대기열에 추가했습니다.");
		}), "South");

		try {
			var rs = stmt.executeQuery(
					"select al.name, ar.name, c.name, year(al.release) from album al, artist ar, category c where c.serial = al.category and ar.serial = al.artist and al.serial = "+al_serial);
			if (rs.next()) {
				l = lbl(rs.getString(2), JLabel.LEFT, 0, 15);
				n_c_c.add(lbl(rs.getString(1), JLabel.LEFT, 0, 15));
				n_c_c.add(l);
				n_c_c.add(lbl(rs.getString(3) + " · " + rs.getString(4), JLabel.LEFT, 0, 15));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String ar_serial = "";
				try {
					System.out.println("select serial from artist where name = '" + ((JLabel) e.getSource()).getText());
					var rs = stmt.executeQuery(
							"select serial from artist where name = '" + ((JLabel) e.getSource()).getText() + "'");
					if (rs.next()) {
						ar_serial = rs.getString(1);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				homePage.scr.setViewportView(new ArtistPage(ar_serial));
			}
		});

		try {
			int rank = 1;
			var rs = stmt.executeQuery(
					"select serial, titlesong, name, if((select count(*) from favorite fa, user u, song s where fa.user = u.serial and fa.song = s.serial and s.serial = song.serial and u.serial = "
							+ u_serial + ")=0, 0, 1) , mid(length, 4) from song where song.album = " + al_serial);
			while (rs.next()) {
				RankItem item = new RankItem(rs.getInt(1), rs.getBoolean(2), rank, rs.getString(3), rs.getBoolean(4),
						rs.getString(5));
				item.init();
				item.menu.removeAll();
				item.menu.add(artistItem);
				item.menu.add(item.item);
				item.item.addActionListener(a -> {
					iMsg("대기열에 추가되었습니다.");
				});
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

	public static void main(String[] args) {
		u_serial = 1;
		u_region = 1;
		homePage = new HomePage();
		mf.add(homePage);
		SearchPage s = new SearchPage();
		s.txt.setText("힘든 건");
		homePage.scr.setViewportView(s);
		mf.repaint();
		mf.revalidate();
	}
}
