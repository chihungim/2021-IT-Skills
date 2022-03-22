package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import base.BasePage;

public class LikePage extends BasePage {

	JPopupMenu menu = new JPopupMenu();
	JMenuItem albumItem = new JMenuItem("앨범으로 이동");
	JMenuItem artistItem = new JMenuItem("아티스트으로 이동");
	JMenuItem playListItem = new JMenuItem("플레이리스트으로 이동");

	JPanel n = new JPanel(new BorderLayout());
	JPanel n_c = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel c = new JPanel(new GridLayout(0, 1, 10, 10));
	TitleLine line = new TitleLine("", 500);

	int cnt = 0;
	
	public LikePage() {

		setBorder(new EmptyBorder(10, 10, 10, 10));

		menu.add(albumItem);
		menu.add(artistItem);
		menu.add(playListItem);

		albumItem.addActionListener(a->{
			try {
				var rs = stmt.executeQuery("select al.serial from album al, song s where al.serial = s.album and s.serial ="+s_serial);
				if(rs.next()) {
					al_serial = rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			homePage.scr.setViewportView(new AlbumPage(al_serial));
		});
		artistItem.addActionListener(a->{
			try {
				var rs = stmt.executeQuery("select ar.serial from album al, song s, artist ar where s.album = al.serial and ar.serial = al.artist and s.serial = "+s_serial);
				if(rs.next()) {
					ar_serial = rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			homePage.scr.setViewportView(new ArtistPage(ar_serial));
		});
		playListItem.addActionListener(a->{
			iMsg("대기열에 추가되었습니다.");
		});

		n.setOpaque(false);
		n_c.setOpaque(false);
		c.setOpaque(false);

		try {
			var rs1 = stmt.executeQuery("select count(*) from favorite where user =" + u_serial);
			if (rs1.next()) {
				cnt = rs1.getInt(1);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		add(n, "North");
		add(c);
		n.add(lbl("좋아요 한 음악", JLabel.LEFT, Font.BOLD, 20), "North");
		n.add(n_c);
		n.add(line, "South");
		n_c.add(btn("재생하기", a -> {
			iMsg("좋아요한 음악을 재생합니다.");
		}));
		n_c.add(lbl("총 " + cnt + "개의 음악", JLabel.LEFT, 0, 15));

		createList();
	}

	void createList() {
		try {
			int rank = 1;
			var rs2 = stmt.executeQuery(
					"select s.serial, s.titlesong, s.name, mid(s.length, 4) from song s, favorite fa, user u where u.serial = fa.user and s.serial = fa.song and u.serial = "
							+ u_serial);
			while (rs2.next()) {
				RankItem item = new RankItem(rs2.getInt(1), rs2.getBoolean(2), rank, rs2.getString(3), true,
						rs2.getString(4));
				item.init();
				item.setName(rs2.getString(1));
				item.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getButton() == 3 ) {
							s_serial = ((JComponent)e.getSource()).getName();
							System.out.println(s_serial);
							menu.show(((JComponent)e.getSource()), e.getX(), e.getY());
						}
					}
				});
				item.likelbl.setName(rs2.getInt(1) + "");
				item.likelbl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						c.removeAll();
						execute("delete from favorite where user=" + u_serial + " and song="
								+ ((JLabel) e.getSource()).getName());
						createList();
						repaint();
						revalidate();
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
