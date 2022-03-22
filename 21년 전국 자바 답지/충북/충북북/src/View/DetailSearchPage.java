package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DetailSearchPage extends BasePage {

	int idx;

	JPopupMenu menu = new JPopupMenu();
	JMenuItem albumItem = new JMenuItem("앨범으로 이동");
	JMenuItem artistItem = new JMenuItem("아티스트로 이동");

	JPanel c = new JPanel();

	String[] t = "음악,앨범,아티스트,플레이리스트".split(",");

	public DetailSearchPage(int idx, String sql) {
		this.idx = idx;
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		c.setOpaque(false);

		menu.add(albumItem);
		menu.add(artistItem);

		albumItem.addActionListener(a -> {
			homePage.pane.setViewportView(new AlbumPage(((JMenuItem) a.getSource()).getName()));
		});

		artistItem.addActionListener(a -> {
			String ar_serial = "";
			try {
				var rs = stmt.executeQuery(
						"select artist from album where serial=" + (((JMenuItem) a.getSource()).getName()));
				if (rs.next()) {
					ar_serial = rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			homePage.pane.setViewportView(new ArtistPage(ar_serial));
		});

		add(c);

		try {
			var rs = stmt.executeQuery(sql);
			var hBox = Box.createHorizontalBox();
			hBox.setAlignmentX(LEFT_ALIGNMENT);
			while (rs.next()) {
				Item item = new Item(rs.getString(2), rs.getString(1));
				item.setPreferredSize(new Dimension(200, 200));
				item.setMaximumSize(new Dimension(200, 200));
				hBox.add(item);
				if (hBox.getComponents().length == 5) {
					c.add(hBox);
					hBox = Box.createHorizontalBox();
					hBox.setAlignmentX(LEFT_ALIGNMENT);
				}
			}

			if (hBox.getComponents().length > 0) {
				c.add(hBox);
				hBox.setAlignmentX(LEFT_ALIGNMENT);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Item extends SongItem {
		public Item(String serial, String name) {
			if (idx == 2) {
				img = imglbl("./지급자료/images/artist/" + serial + ".jpg", 120, 120);
			} else {
				img = imglbl("./지급자료/images/album/" + serial + ".jpg", 120, 120);
			}
			this.name = lbl(name, JLabel.CENTER, 0, 10);
			main.add(img);
			main.add(this.name, "South");

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						menu.show(Item.this, e.getX(), e.getY());
						albumItem.setName(serial);
						artistItem.setName(serial);
					} else if (e.getButton() == 1 && menu.isShowing() == false) {
						if (idx == 0) {
							iMsg("대기열에 추가되었습니다.");
							que.add(toInt(serial));
						} else if (idx == 1) {
							homePage.pane.setViewportView(new AlbumPage(serial));
						} else if (idx == 2) {
							homePage.pane.setViewportView(new ArtistPage(serial));
						} else if (idx == 3) {
							homePage.pane.setViewportView(new PlayListPage(serial));
						}
					}
				}
			});
		}
	}

	public static void main(String[] args) {
		u_serial = 1;
		u_region = 1;
		homePage = new HomePage();
		mf.swapPage(homePage);
		SearchPage s = new SearchPage();
		s.txt.setText("th");
		homePage.pane.setViewportView(s);
		mf.repaint();
		mf.revalidate();
	}
}
