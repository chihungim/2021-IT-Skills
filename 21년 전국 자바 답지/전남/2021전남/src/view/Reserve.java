package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.DBManager;
import util.Util;

public class Reserve extends BaseFrame {
	JPanel n = new JPanel(new BorderLayout());
	JPanel c = new JPanel(new FlowLayout());

	Item cal = new Item(true, false, 0, 7, "October 2021", 400);
	Item region = new Item(false, false, 0, 1, "지역", 50);
	Item shop = new Item(false, true, 0, 1, "매장", 200);
	Item theme = new Item(false, false, 0, 1, "테마", 200);
	Item time = new Item(false, false, 0, 1, "시간", 50);

	public Reserve() {
		super("", 1000, 530);
		setDefaultCloseOperation(2);
		setLocationRelativeTo(null);

		add(n, "North");
		add(c);

		n.add(Util.lbl("방탈출 예약", 0, 30));
		n.add(new JLabel("Room Escape Reservation", JLabel.CENTER), "South");

		c.setPreferredSize(new Dimension(1200, 300));

		c.add(cal);
		c.add(region);
		c.add(shop);
		c.add(theme);
		c.add(time);
	}

	class Item extends JPanel {
		public Item(boolean isCal, boolean isScroll, int row, int column, String title, int width) {
			setPreferredSize(new Dimension(width, 400));
			setLayout(new BorderLayout());
			setBorder(new LineBorder(Color.black));
			var n = isCal ? new JPanel(new BorderLayout()) : new JPanel();
			var c = new JPanel(new GridLayout(row, column));

			n.setBackground(Color.black);

			if (title == "October 2021") {
				JButton btn1 = new JButton("◀");
				btn1.setBackground(Color.black);
				btn1.setForeground(Color.white);
				btn1.addActionListener(a -> {

				});
				n.add(btn1, "West");
				JButton btn2 = new JButton("▶");
				btn2.setBackground(Color.black);
				btn2.setForeground(Color.white);
				btn2.addActionListener(a -> {
				});
				n.add(btn2, "East");

				JLabel lbl[] = { Util.lbl("SUN", 0, 10), Util.lbl("MON", 0, 10), Util.lbl("TUE", 0, 10), Util.lbl("WED", 0, 10),
						Util.lbl("THU", 0, 10), Util.lbl("FRI", 0, 10), Util.lbl("SAT", 0, 10) };
				JLabel idx[] = new JLabel[42];
				for (int i = 0; i < lbl.length; i++) {
					c.add(lbl[i]);
				}
				for (int j = 0; j < idx.length; j++) {
					int index = j;
					idx[j] = new JLabel(j + 1 + "");
					c.add(idx[j]);
					LocalDate today = LocalDate.now();
					int year, month, startDate = 0, final_month = LocalDate.now().getMonthValue();
					year = today.getYear();
					month = today.getMonthValue();
					idx[j].setEnabled(true);
					idx[j].setVisible(false);
					idx[j].setText("");

					LocalDate tmp = LocalDate.of(year, month, 1).plusDays(j - startDate);
					idx[j].setVisible(false);
					if (month == tmp.getMonthValue()) {
						idx[j].setText(tmp.getDayOfMonth() + "");
						idx[j].setVisible(true);
						if ((today.getDayOfMonth() > tmp.getDayOfMonth()) && today.getMonthValue() == final_month) {
							idx[j].setEnabled(false);
						} else {
							idx[j].setEnabled(true);
						}
					}
					idx[j].setBorder(new EmptyBorder(0, 20, 0, 0));
					idx[j].addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							idx[index].setOpaque(true);
							if (idx[index].getBackground() == Color.yellow) {
								idx[index].setBackground(c.getBackground());
							} else
								idx[index].setBackground(Color.yellow);
						}
					});
				}
			} else if (title == "지역") {
				try {
					ResultSet rs = DBManager.rs("SELECT left(c_address, 2) FROM roomescape.cafe group by left(c_address, 2)");
					while (rs.next()) {
						JPanel t = new JPanel();
						JLabel l = new JLabel(rs.getString(1));
						t.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if (((JPanel) e.getSource()).getBackground() == Color.yellow) {
									((JPanel) e.getSource()).setBackground(c.getBackground());
								} else {
									((JPanel) e.getSource()).setBackground(Color.yellow);
								}
								repaint();
							}
						});
						t.add(l);
						c.add(t);
					}
				} catch (SQLException e) {
				}
			} else if (title == "매장") {
				try {
					ResultSet rs = DBManager.rs("SELECT c_name FROM cafe");
					while (rs.next()) {
						JPanel t = new JPanel();
						JLabel l = new JLabel(rs.getString(1));
						t.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if (((JPanel) e.getSource()).getBackground() == Color.yellow) {
									((JPanel) e.getSource()).setBackground(c.getBackground());
								} else {
									((JPanel) e.getSource()).setBackground(Color.yellow);
								}
								repaint();
							}
						});
						t.add(l);
						c.add(t);
					}
				} catch (SQLException e) {
				}
			} else if (title == "테마") {
				try {
					ResultSet rs = DBManager.rs(
							"SELECT * FROM theme t, cafe c, reservation r where t.t_no = r.t_no and c.c_no = r.c_no and c.c_name = '"
									+ cname + "' group by c.c_no");
					while (rs.next()) {
						JPanel t = new JPanel();
						JLabel l = new JLabel(rs.getString(1));
						t.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if (((JPanel) e.getSource()).getBackground() == Color.yellow) {
									((JPanel) e.getSource()).setBackground(c.getBackground());
								} else {
									((JPanel) e.getSource()).setBackground(Color.yellow);
								}
								repaint();
							}
						});
						t.add(l);
						c.add(t);
					}
				} catch (SQLException e) {
				}
			} else {
				for (int i = 0; i < 15; i++) {

				}
			}

			JLabel l = new JLabel(title);
			l.setForeground(Color.white);
			n.add(l);

			add(n, "North");
			add(isScroll ? new JScrollPane(c) : c);
		}

	}

	public static void main(String[] args) {
	}
}
