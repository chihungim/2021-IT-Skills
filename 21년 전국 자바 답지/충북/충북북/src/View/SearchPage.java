package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class SearchPage extends BasePage {

	JPopupMenu menu = new JPopupMenu();
	JMenuItem albumItem = new JMenuItem("앨범으로 이동");
	JMenuItem artistItem = new JMenuItem("아티스트로 이동");

	JTextField txt = new JTextField(25);

	JPanel n = new JPanel(new BorderLayout(5, 5));
	JPanel c = new JPanel(new GridLayout(0, 3, 5, 5));

	JPanel c_c = new JPanel(new GridLayout(0, 2, 5, 5));

	String cap[] = "음악,앨범,아티스트,플레이리스트".split(",");

	public SearchPage() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.black);
		setPreferredSize(new Dimension(1000, 500));

		add(n, "North");
		add(c);

		c.setOpaque(false);
		n.setOpaque(false);
		c_c.setOpaque(false);

		c.setBorder(new EmptyBorder(10, 0, 10, 0));

		txt.setBackground(Color.gray);

		menu.add(albumItem);
		menu.add(artistItem);
		albumItem.addActionListener(a -> {
			homePage.pane.setViewportView(new AlbumPage(((JMenuItem) a.getSource()).getName()));
		});
		artistItem.addActionListener(a -> {
			homePage.pane.setViewportView(new ArtistPage(((JMenuItem) a.getSource()).getName()));
		});

		n.add(txt);
		n.add(btn("검색", a -> {
			remove(c);
			remove(c_c);
			c_c.removeAll();

			var name = "'%" + txt.getText() + "%'";

			if (txt.getText().equals("")) {
				add(n, "North");
				add(c);
			} else {
				String csql[] = {
						"select count(*) from song s inner join album al on s.album = al.serial where s.name like %s or al.name like %s",
						"select count(*) from artist ar inner join album al on ar.serial = al.artist where ar.name like %s or al.name like %s",
						"select count(*) from artist where name like %s",
						"select count(*) from playlist where name like %s" };
				String rsql[] = {
						"select s.name, al.serial, al.artist, s.serial from song s inner join album al on s.album = al.serial where s.name like %s or al.name like %s limit 4",
						"select al.name, al.serial from artist ar inner join album al on ar.serial = al.artist where ar.name like %s or al.name like %s limit 4",
						"select name, serial from artist where name like %s limit 4",
						"select name, serial from playlist where name like %s limit 4" };
				for (int i = 0; i < csql.length; i++) {
					JPanel b = new JPanel(new BorderLayout());
					JPanel b_c = new JPanel(new GridLayout(0, 2, 5, 5));

					try {

						int cnt = 0;
						var rs1 = stmt.executeQuery(csql[i].replaceAll("%s", name));

						if (rs1.next()) {
							cnt = rs1.getInt(1);
							var tmp = new JPanel(new BorderLayout());

							tmp.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));

							tmp.setOpaque(false);
							var all = lbl("모두보기", JLabel.RIGHT, 0, 10);

							tmp.add(lbl(cap[i] + " " + cnt + "건", JLabel.LEFT, 0, 10));
							tmp.add(all, "East");
							all.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									((JLabel) e.getSource()).setForeground(Color.green);
								}

								@Override
								public void mouseExited(MouseEvent e) {
									((JLabel) e.getSource()).setForeground(Color.white);
								}

								@Override
								public void mouseClicked(MouseEvent e) {

									String a[] = ((JLabel) e.getSource()).getName().split("!");
									homePage.pane.setViewportView(
											new DetailSearchPage(toInt(a[0]), a[1].replace("limit 4", "")));
								}
							});
							//

							all.setName(i + "!" + rsql[i].replaceAll("%s", name));
							b.add(tmp, "North");
						}

						var rs2 = stmt.executeQuery(rsql[i].replaceAll("%s", name));

						while (rs2.next()) {
							if (cnt != 0) {
								String path = "./지급자료/images/";
								String t = "<html>";
								t = t + rs2.getString(1) + t;
								if (i < 2) {
									path = path + "album/" + rs2.getInt(2) + ".jpg";
								} else if (i == 2) {
									path = path + "artist/" + rs2.getString(2) + ".jpg";
								}
								SongItem item = new SongItem(path, t, "Center", "West", JLabel.LEFT,
										new MouseAdapter() {
											@Override
											public void mouseClicked(MouseEvent e) {
												String name[] = ((JPanel) e.getSource()).getName().split(",");
												if (e.getButton() == 3
														&& ((JPanel) e.getSource()).getName().split(",")[0]
																.equals(cap[0])) {
													menu.show(((JComponent) e.getSource()), e.getX(), e.getY());
													artistItem.setName(name[2]);
													albumItem.setName(name[1]);
												} else if (e.getButton() == 1) {
													if (name[0].equals(cap[0])) {
														var n = ((SongItem) (e.getSource())).s_serial;
														addtoQue(toInt(n));
														iMsg("대기열에 추가되었습니다.");
													} else if (name[0].equals(cap[1])) {
														homePage.pane.setViewportView(new AlbumPage(name[1]));
													} else if (name[0].equals(cap[2])) {
														homePage.pane.setViewportView(new ArtistPage(name[1]));
													} else if (name[0].equals(cap[3])) {
														homePage.pane.setViewportView(new PlayListPage(name[1]));
													}
												}
											}
										});

								if (i == 0) {
									item.setName(cap[i] + "," + rs2.getString(2) + "," + rs2.getString(3));
									item.s_serial = rs2.getInt(4);
								} else {
									item.setName(cap[i] + "," + rs2.getString(2));
								}
								b_c.add(item);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					b.setOpaque(false);
					b_c.setOpaque(false);

					b.add(b_c);
					c_c.add(b);
				}
				add(n, "North");
				add(c_c);
			}

			BasePage.homePage.pane.setViewportView(SearchPage.this);
			repaint();
			revalidate();
		}), "East");
		setCategory();
	}

	void setCategory() {
		try {
			var rs = stmt.executeQuery("select name from category");
			while (rs.next()) {
				Category cate = new Category(rs.getString(1));
				c.add(cate);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
