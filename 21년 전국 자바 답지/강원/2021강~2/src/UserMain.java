import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class UserMain extends BaseFrame {
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField() };
	JTextField dates[] = new JTextField[3];

	JPanel imgP[] = new JPanel[5];
	JPanel p = panel();
	JPanel popUp = new JPanel(new BorderLayout()), in = new JPanel(new GridLayout(1, 0));
	JPanel depart = new JPanel(new GridLayout(0, 1)), arrive = new JPanel(new GridLayout(0, 1));
	JPanel cal = new JPanel(new GridLayout(1, 0));

	JScrollPane departJsc = new JScrollPane(depart);
	JScrollPane arriveJsc = new JScrollPane(arrive);

	JPopupMenu pop = new JPopupMenu();
	JMenuItem item[] = { new JMenuItem("상세설명"), new JMenuItem("예매") };

	JLabel imgLbl = new JLabel();

	JButton down[] = new JButton[3], up[] = new JButton[3];
	JButton departBtn[] = new JButton[16];
	JButton[] arriveBtn;

	String cap[] = "출발지,도착지,날짜".split(",");

	LocalDate today = LocalDate.now();
	int cnt = 0;
	boolean bool;
	String name[] = new String[5];

	public UserMain() {
		super("유저 메인", 1100, 700);

		ui();

		event();

		setVisible(true);
	}

	void event() {
		for (int i = 0; i < txt.length - 1; i++) {
			int idx = i;
			txt[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == 3) {
						cnt = idx;
						popUp.setVisible(true);
						popUp.setLocation(txt[idx].getX() + 140, txt[idx].getY() + 150);
						arrive.removeAll();
						cal.setVisible(false);

						if (bool) {
							txt[2].setText(today + "");
							txt[2].setForeground(Color.BLACK);
						}

						repaint();
						revalidate();
					}
				}
			});
		}

		txt[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3) {
					popUp.setVisible(false);
					bool = true;
					cal.setVisible(true);
					cal.setLocation(txt[2].getX() + 140, txt[2].getY() + 150);
				}
			}
		});

		try {
			var rs = stmt.executeQuery("select * from location");
			int i = 0;
			while (rs.next()) {
				depart.add(departBtn[i] = new JButton(rs.getString(2)));
				int idx = i;
				departBtn[i].addActionListener(e -> {
					arrive.removeAll();

					try {
						var rs2 = stmt.executeQuery("select * from location2 where location_no = " + (idx + 1));

						rs2.last();
						arriveBtn = new JButton[rs2.getRow()];
						rs2.beforeFirst();

						int k = 0;
						while (rs2.next()) {
							arrive.add(arriveBtn[k] = new JButton(rs2.getString(2)));
							arriveBtn[k].addActionListener(e2 -> {
								JButton tmp = (JButton) e2.getSource();
								txt[cnt].setText(departBtn[idx].getText() + " " + tmp.getText());
								txt[cnt].setForeground(Color.BLACK);
							});
							k++;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					repaint();
					revalidate();
				});
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				popUp.setVisible(false);
				cal.setVisible(false);
				if (bool) {
					txt[2].setText(today + "");
					txt[2].setForeground(Color.BLACK);
				}
			}
		});

		for (int i = 0; i < imgP.length; i++) {
			int idx = i;
			imgP[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == 3) {
						pop.show(imgP[idx], e.getX(), e.getY());
					}
				}
			});
		}

		for (int i = 0; i < item.length; i++) {
			int idx = i;
			item[i].addActionListener(e -> {
				if (e.getActionCommand().equals("상세설명")) {
					new Detail(name[idx]).setVisible(true);
				} else {
					cnt = 1;
					popUp.setVisible(false);
					popUp.setLocation(txt[1].getX() + 140, txt[1].getY() + 150);
					arrive.removeAll();
					cal.setVisible(false);
					if (bool) {
						txt[2].setText(today + "");
						txt[2].setForeground(Color.BLACK);
					}

					repaint();
					revalidate();
				}
			});
		}
	}

	void ui() {
		setLayout(null);

		for (int i = 0; i < 3; i++) {
			txt(txt[i], cap[i]);
			var tmp = new JPanel(new GridLayout(0, 1));
			tmp.add(up[i] = new JButton("▲"));
			tmp.add(dates[i] = new JTextField());
			tmp.add(down[i] = new JButton("▼"));

			dates[i].setFocusable(false);
			cal.add(tmp);

			int idx = i;
			up[i].addActionListener(e -> {
				int cnt = idx;
				if (cnt == 1 && dates[1].getText().equals("12")) {
					return;
				}

				if (cnt == 1 && toInt(dates[2].getText()) > today.plusMonths(1).lengthOfMonth()) {
					dates[2].setText(today.plusMonths(1).lengthOfMonth() + "");
				}

				if (cnt == 2 && dates[2].getText().equals(today.lengthOfMonth() + "")) {
					return;
				}

				dates[cnt].setText(toInt(dates[cnt].getText()) + 1 + "");
				today = LocalDate.of(toInt(dates[0].getText()), toInt(dates[1].getText()), toInt(dates[2].getText()));
			});

			down[i].addActionListener(e -> {
				int cnt = idx;
				if (cnt == 1 && dates[1].getText().equals("1")) {
					return;
				}

				if (cnt == 1 && toInt(dates[2].getText()) > today.plusMonths(1).lengthOfMonth()) {
					dates[2].setText(today.plusMonths(1).lengthOfMonth() + "");
				}

				if (cnt == 2 && dates[2].getText().equals(today.lengthOfMonth() + "")) {
					return;
				}

				dates[cnt].setText(toInt(dates[cnt].getText()) - 1 + "");
				today = LocalDate.of(toInt(dates[0].getText()), toInt(dates[1].getText()), toInt(dates[2].getText()));
			});

			dates[i].setHorizontalAlignment(SwingConstants.CENTER);
		}

		dates[0].setText(today.getYear() + "");
		dates[1].setText(today.getMonthValue() + "");
		dates[2].setText(today.getDayOfMonth() + "");

		setBounds(popUp, 115, 200, 220, 300);
		setBounds(cal, 115, 200, 220, 100);
		popUp.setVisible(false);
		cal.setVisible(false);
		imgLbl.setIcon(img("main.jpg", 1100, 1100));
		setBounds(imgLbl, 0, 0, 1100, 300);
		setBounds(imgLbl, themeButton(), 770, 5, 60, 30);
		setBounds(imgLbl, btn("계정", e -> accountEvnt()), 840, 5, 60, 30);
		setBounds(imgLbl, btn("예매", e -> new Reserve().setVisible(true)), 910, 5, 60, 30);
		setBounds(imgLbl, btn("로그아웃", e -> dispose()), 980, 5, 100, 30);
		setBounds(imgLbl, p, 120, 100, 860, 100);
		setBounds(p, lbl("예매", 0, 25), 0, 10, 80, 30);
		setBounds(p, txt[0], 20, 50, 200, 30);
		setBounds(p, btn("<html>←<br>→", e -> change()), 230, 50, 50, 40);
		setBounds(p, txt[1], 290, 50, 200, 30);
		setBounds(p, txt[2], 500, 50, 200, 30);
		setBounds(p, btn("조회", e -> lookUp()), 710, 55, 120, 30);

		popUp.add(in);
		in.add(departJsc);
		in.add(arriveJsc);

		pop.add(item[0]);
		pop.add(item[1]);

		setBounds(lbl("추천 여행지", 0, 20), 25, 330, 130, 30);

		try {
			var rs = stmt.executeQuery(
					"select r.no, l.name, title, description, img from recommend r, location l, recommend_info ri where r.location_no = l.no and ri.recommend_no = r.no group by l.name order by r.no asc");
			int i = 0;
			while (rs.next()) {
				if (i % 2 == 0) {
					setBounds(imgP[i] = bPanel(0), 30 + (i * 210), 380, 170, 160);
					imgP[i].setBorder(new TitledBorder(new LineBorder(Color.BLACK), rs.getString(2)));
					imgP[i].add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
							.getImage("지급파일/images/recommend/" + map.get(rs.getString(2)) + "/" + rs.getInt(3) + ".jpg")
							.getScaledInstance(450, 180, Image.SCALE_SMOOTH))));
				} else {
					setBounds(imgP[i] = bPanel(0), 30 + (i * 210), 420, 170, 160);
					imgP[i].setBorder(new TitledBorder(new LineBorder(Color.BLACK), rs.getString(2)));
					imgP[i].add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
							.getImage("지급파일/images/recommend/" + map.get(rs.getString(2)) + "/" + rs.getInt(3) + ".jpg")
							.getScaledInstance(450, 180, Image.SCALE_SMOOTH))));
				}

				name[i] = rs.getString(2);
				imgP[i].setOpaque(false);
				depart.setOpaque(true);
				arrive.setOpaque(true);
				departJsc.setBorder(new LineBorder(Color.BLACK));
				arriveJsc.setBorder(new LineBorder(Color.BLACK));
				popUp.setBorder(new LineBorder(Color.BLACK));
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		p.setOpaque(true);
	}

	void lookUp() {
		if (txt[0].getText().contentEquals("") || txt[0].getText().equals("출발지") || txt[1].getText().contentEquals("")
				|| txt[1].getText().equals("도착지") || txt[2].getText().contentEquals("")
				|| txt[2].getText().equals("날짜")) {
			eMsg = new Emsg("출발지, 도착지, 날짜 중 공란이 있습니다.");
			return;
		}

		try {
			String depart = getOne("select no from a where name = '" + txt[0].getText() + "'");
			String arrive = getOne("select no from a where name = '" + txt[1].getText() + "'");
			var rs = stmt.executeQuery("select * from schedule where departure_location2_no = '" + depart
					+ "' and arrival_location2_no = '" + arrive + "' and date = '" + txt[2].getText() + "'");
			if (rs.next()) {
				new BusReserve(toInt(depart), toInt(arrive), txt[2].getText()).setVisible(true);
			} else {
				eMsg = new Emsg("예매 가능한 일정이 없습니다.");
				return;
			}
		} catch (Exception e) {
			eMsg = new Emsg("예매 가능한 일정이 없습니다.");
		}
	}

	void change() {
		if (txt[0].getText().equals("출발지") || txt[1].getText().equals("도착지")) {
			return;
		}

		String tmp = txt[1].getText();
		txt[1].setText(txt[0].getText());
		txt[0].setText(tmp);
	}

	void accountEvnt() {
		String input = JOptionPane.showInputDialog(null, "비밀번호를 입력해주세요.", "입력", JOptionPane.QUESTION_MESSAGE);

		try {
			var rs = stmt.executeQuery("select * from user where id = '" + u_id + "' and pwd = '" + input + "'");
			if (rs.next()) {
				new Account().setVisible(true);
			} else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		u_no = 100;
		u_id = "asd";
		new UserMain();
	}
}
