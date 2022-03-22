package ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import base.BasePage;

public class DetailSearchPage extends BasePage {

	int idx;

	JPopupMenu menu = new JPopupMenu();
	JMenuItem albumItem = new JMenuItem("앨범으로 이동");
	JMenuItem artistItem = new JMenuItem("아티스트로 이동");

	JPanel c = new JPanel(new GridLayout(0, 5, 0, 0));

	String[] t = "음악,앨범,아티스트,플레이리스트".split(",");

	public DetailSearchPage(int idx, String sql) {
		this.idx = idx;
		
		System.out.println("sql: "+ sql);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
//		setOpaque(false);
		c.setOpaque(false);
		
		menu.add(albumItem);
		menu.add(artistItem);

		albumItem.addActionListener(a -> {
			homePage.scr.setViewportView(new AlbumPage(((JMenuItem)a.getSource()).getName()));
		});
		artistItem.addActionListener(a -> {
			String ar_serial = "";
			try {
				var rs = stmt.executeQuery("select artist from album where serial="+(((JMenuItem)a.getSource()).getName()));
				if(rs.next()) {
					ar_serial = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			homePage.scr.setViewportView(new ArtistPage(ar_serial));
		});

		add(new TitleLine(t[idx], 400), "North");
		add(c);

		try {
			var rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Item item = new Item(rs.getString(2), rs.getString(1));
				c.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Item extends SongItem {
		public Item(String serial, String name) {
			if(idx == 2) {
				img = imglbl("./지급자료/images/artist/" + serial + ".jpg", 120, 120);
			}else {
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
					} else if(e.getButton() == 1 && menu.isShowing() == false){
						if (idx == 0) {
							iMsg("대기열에 추가되었습니다.");
						} else if (idx == 1) {
							homePage.scr.setViewportView(new AlbumPage(serial));
						} else if (idx == 2) {
							homePage.scr.setViewportView(new ArtistPage(serial));
						} else if (idx == 3) {
							homePage.scr.setViewportView(new PlayListPage(serial));
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
		mf.add(homePage);
		SearchPage s = new SearchPage();
		s.txt.setText("th");
		homePage.scr.setViewportView(s);
		mf.repaint();
		mf.revalidate();
	}
}
