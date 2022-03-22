package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Favorite extends BaseDialog {

	JPanel c;

	public Favorite() {
		super("찜한 음식점", 300, 400);
		add(c = new JPanel());
		c.setLayout(new GridLayout(5, 5, 5, 5));
		try {
			var rs = stmt.executeQuery(
					"select s.no, s.name, c.name, format(s.DELIVERYFEE,'#,##0'), round(avg(minute(m.COOKTIME)),0) from menu m, seller s, favorite f, category c where f.SELLER = s.NO and m.SELLER = f.SELLER and s.CATEGORY = c.NO and f.USER = "
							+ uno + " group by s.name");
			while (rs.next()) {
				c.add(new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) + "분"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	public static void main(String[] args) {
		uno = 1;
		new Favorite();
	}

	class Item extends JPanel {

		JLabel img;

		int sno = 0;

		public Item(int sno, String name, String type, String price, String minute) {
			setLayout(new BorderLayout(5, 5));

			this.sno = sno;

			var c = new JPanel(new GridLayout(0, 1, 5, 5));
			add(img = new JLabel(getIcon("지급자료/배경/" + sno + ".png", 80, 80)), "West");
			img.setPreferredSize(new Dimension(80, 80));
			add(c);
			c.add(lbl(name, JLabel.LEFT, 15));
			c.add(lbl(type, JLabel.LEFT));
			c.add(lbl(price + "원 배달 수수료 / 조리평균 " + minute, JLabel.LEFT));

			setBorder(new EmptyBorder(5, 5, 5, 5));

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						String rate = null;
						try {
							var rs = stmt.executeQuery(
									"select round(avg(r.rate),1) from seller s left outer join review r on s.NO = r.SELLER where s.NO = "
											+ sno);
							if (rs.next()) {
								if (rs.getString(1) == null)
									rate = "0.0";
								else
									rate = rs.getString(1);
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String minute = null;

						try {
							var rs = stmt.executeQuery(
									"select m.seller,  minute(min(m.cooktime)), minute(max(m.COOKTIME)) from menu m where m.SELLER = "
											+ sno + " group by m.seller");
							if (rs.next()) {
								minute = rs.getString(2) + "~" + rs.getString(1) + "분";
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						new Restaurant(name, sno, "평점 " + rate, price + "원 배달 수수료/", minute);
					}
				}
			});

			img.setBorder(new LineBorder(Color.BLACK));
			setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 5, 5, 5)));
		}

	}

}
