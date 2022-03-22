package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class PlayHistoryPage extends BasePage {

	JPopupMenu menu = new JPopupMenu();

	String bcap[] = "앨범으로 이동,아티스트으로 이동,플레이리스트으로 이동".split(",");

	JPanel n = new JPanel(new BorderLayout());
	JPanel c = new JPanel();

	public PlayHistoryPage() {
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout(15, 15));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		for (String bc : bcap) {
			JMenuItem item = new JMenuItem(bc);

			item.addActionListener(a -> {
				if (a.getActionCommand().equals(bcap[0])) {
					try {
						var rs = stmt.executeQuery(
								"select al.serial from album al, song s where al.serial = s.album and s.serial ="
										+ s_serial);
						if (rs.next()) {
							al_serial = rs.getString(1);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					homePage.pane.setViewportView(new AlbumPage(al_serial));
				} else if (a.getActionCommand().equals(bcap[1])) {
					try {
						var rs = stmt.executeQuery(
								"select ar.serial from album al, song s, artist ar where s.album = al.serial and ar.serial = al.artist and s.serial = "
										+ s_serial);
						if (rs.next()) {
							ar_serial = rs.getString(1);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					homePage.pane.setViewportView(new ArtistPage(ar_serial));
				} else {
					addtoPlayList(toInt(s_serial));
				}
			});

			menu.add(item);
		}

		n.setOpaque(false);
		c.setOpaque(false);

		add(n, "North");
		add(c);

		n.add(lbl("최근 내가 들은 음악", JLabel.LEFT, Font.BOLD, 25));

		try {
			int rank = 1;
			var rs1 = stmt.executeQuery(
					"select s.serial, s.titlesong, s.name, if((select count(*) from favorite fa, user u, song s1 where fa.user = u.serial and fa.song = s1.serial and s1.serial = s.serial and u.serial = "
							+ u_serial
							+ "  )=0, 0, 1), mid(s.length, 4) from history h, song s, user u where h.song = s.serial and u.serial = h.user and user =1 order by h.serial desc limit 50");
			while (rs1.next()) {
				RankItem item = new RankItem(rs1.getInt(1), rs1.getBoolean(2), rank, rs1.getString(3),
						rs1.getBoolean(4), rs1.getString(5));
				item.init();
				item.setMaximumSize(new Dimension(1000, 20));
				item.setName(rs1.getString(1));
				item.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == 3) {
							s_serial = ((JComponent) e.getSource()).getName();
							menu.show(((JComponent) e.getSource()), e.getX(), e.getY());
						}
					}
				});
				c.add(item);
				rank++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
