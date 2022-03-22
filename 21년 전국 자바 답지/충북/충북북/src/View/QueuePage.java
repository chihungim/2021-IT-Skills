package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class QueuePage extends BasePage {
	JPanel n, n_c, c, c_c;
	RankItem curPlaying;
	JLabel notPlaying = lbl("재생중이지 않음", JLabel.LEFT, 0, 20);
	ArrayList<Integer> queList = new ArrayList<>();

	public QueuePage(JPanel s) {
		setLayout(new BorderLayout());
		add(n = new JPanel(new BorderLayout()), "North");
		n.add(n_c = new JPanel(new BorderLayout()));

		add(c = new JPanel(new BorderLayout()));

		c.add(c_c = new JPanel());
		c_c.setLayout(new BoxLayout(c_c, BoxLayout.Y_AXIS));
		n.add(lbl("지금 재생중", JLabel.LEFT, 0, 20), "North");
		c.add(lbl("다음에 재생될 항목", JLabel.LEFT, 0, 20), "North");

		notPlaying.setBorder(new EmptyBorder(20, 20, 20, 20));

		n_c.add(notPlaying);

		n.setOpaque(false);
		n_c.setOpaque(false);
		c.setOpaque(false);
		c_c.setOpaque(false);
	}

	void setQue() {
		queList.clear();
		c_c.removeAll();
		c_c.setLayout(new BoxLayout(c_c, BoxLayout.Y_AXIS));
		int rank = 1;
		for (Integer s_serial : que) {
			queList.add(s_serial);
			try {
				var rs = stmt.executeQuery(
						"select serial, titlesong, name, if((select count(*) from favorite fa, user u, song s where fa.user = u.serial and fa.song = s.serial and s.serial = song.serial and u.serial = "
								+ u_serial + ")=0, 0, 1) , mid(length, 4) from song where song.serial = " + s_serial);
				if (rs.next()) {
					RankItem item;
					c_c.add(item = new RankItem(rs.getInt(1), rs.getBoolean(2), rank, rs.getString(3), rs.getBoolean(4),
							rs.getString(5)));
					item.setBorder(new LineBorder(Color.BLACK));
					item.setMaximumSize(new Dimension(1000, 20));
					item.init();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rank++;
		}

		revalidate();
		repaint();
	}

	void setCurPlaying(int s_serial) {
		n_c.removeAll();
		time = 0;
		bar.setValue(0);
		try {
			var rs = stmt.executeQuery(
					"select serial, titlesong, name, if((select count(*) from favorite fa, user u, song s where fa.user = u.serial and fa.song = s.serial and s.serial = song.serial and u.serial = "
							+ u_serial + ")=0, 0, 1) , mid(length, 4) from song where song.serial = " + s_serial);
			if (rs.next()) {
				max = LocalTime.of(0, toInt(rs.getString(5).split(":")[0]), toInt(rs.getString(5).split(":")[1]))
						.toSecondOfDay();
				bar.setMaxValue(max * 60);
				homePage.s.songName.setText(rs.getString(3));
				homePage.s.img.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage("./지급자료/images/album/" + getAlbumRef.get(rs.getInt(1)) + ".jpg")));
				n_c.add(curPlaying = new RankItem(rs.getInt(1), rs.getBoolean(2), 0, rs.getString(3), rs.getBoolean(4),
						rs.getString(5)));
				curPlaying.init();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
