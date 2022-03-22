package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class AlbumPage extends BasePage {

	String al_serial;
	JPanel n, c, n_c;
	JLabel arName;
	ArrayList<Integer> songList = new ArrayList<Integer>();

	public AlbumPage(String al_serial) {
		setLayout(new BorderLayout(5, 5));

		this.al_serial = al_serial;
		add(n = new JPanel(new BorderLayout(5, 5)), "North");
		n.add(n_c = new JPanel(new GridLayout(0, 1, 5, 5)));
		add(c = new JPanel(new GridLayout(0, 1)));
		n.add(imglbl("./지급자료/images/album/" + al_serial + ".jpg", 200, 200), "West");

		n_c.add(Box.createVerticalStrut(20));

		try {
			var rs = stmt.executeQuery(
					"select al.name, ar.name, c.name, year(al.release) from album al, artist ar, category c where c.serial = al.category and ar.serial = al.artist and al.serial = "
							+ al_serial);
			if (rs.next()) {
				n_c.add(lbl(rs.getString(1), JLabel.LEFT, 0, 15));
				n_c.add(arName = lbl(rs.getString(2), JLabel.LEFT, 0, 15));
				n_c.add(lbl(rs.getString(3) + " · " + rs.getString(4), JLabel.LEFT, 0, 15));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		arName.addMouseListener(new MouseAdapter() {
			String ar_serial = "";

			public void mousePressed(java.awt.event.MouseEvent e) {
				try {
					var rs = stmt.executeQuery(
							"select artist.serial from album , artist where artist.serial = album.artist and album.serial = "
									+ al_serial);
					if (rs.next()) {
						ar_serial = rs.getString(1);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				homePage.pane.setViewportView(new ArtistPage(ar_serial));
			};

			@Override
			public void mouseEntered(MouseEvent e) {
				arName.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				arName.setForeground(Color.WHITE);
			}
		});
		n_c.add(Box.createVerticalStrut(20));

		n_c.add(btn("재생하기", a -> {
			iMsg("앨범을 대기열에 추가했습니다.");
			songList.forEach(BasePage::addtoQue);
		}), "South");

		try {
			int rank = 1;
			var rs = stmt.executeQuery(
					"select serial, titlesong, name, if((select count(*) from favorite fa, user u, song s where fa.user = u.serial and fa.song = s.serial and s.serial = song.serial and u.serial = "
							+ u_serial + ")=0, 0, 1) , mid(length, 4) from song where song.album = " + al_serial);

			while (rs.next()) {
				songList.add(rs.getInt(1));
				RankItem item = new RankItem(rs.getInt(1), rs.getBoolean(2), rank, rs.getString(3), rs.getBoolean(4),
						rs.getString(5));
				item.init();
				item.menu.removeAll();
				item.menu.setName(arName.getText());
				item.menu.add(item.item1);
				item.menu.add(item.item2);

				c.add(item);
				rank++;
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		c.setBorder(new MatteBorder(1, 0, 0, 0, Color.WHITE));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		c.setOpaque(false);
		n.setOpaque(false);
		n_c.setOpaque(false);
	}

	public static void main(String[] args) {
		BasePage.mf.swapPage(homePage = new HomePage());
		homePage.pane.setViewportView(new AlbumPage("95"));
	}

}
