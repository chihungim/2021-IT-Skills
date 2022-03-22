package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class UserMain extends BaseFrame {

	int rxywh[][] = { { 100, 96, 883, 107 }, { 727, 6, 352, 33 }, { 0, 3, 1082, 262 } };
	int bxywh[][] = { { 68, 317, 159, 29 }, { 44, 411, 150, 150 }, { 248, 465, 150, 150 }, { 450, 400, 150, 150 },
			{ 645, 457, 150, 150 }, { 819, 393, 150, 150 } };

	JPanel rbp;
	JPanel rp;
	JPanel rbg;
	JPopupMenu sMenu, dMenu, dateMenu;
	JTextField start = HintText("출발지", new JTextField());
	JTextField des = HintText("도착지", new JTextField());
	JTextField date = HintText("날짜", new JTextField());
	DateItem d;

	JLabel img[] = new JLabel[5];

	JComponent rjc[] = { rbg = new JPanel(new BorderLayout()), rbp = new JPanel(new FlowLayout()),
			imageLabel("./지급파일/images/main.jpg", 1082, 262) };
	{
		rbg.setBackground(Color.WHITE);
		rbp.add(themeButton());
		rbp.setOpaque(false);

		rbg.add(lbl("예매", JLabel.LEFT, 20), "North");
		var c = new JPanel(new FlowLayout());
		rbg.add(c);

		c.add(size(start, 200, 40));

		c.add(size(btn("<html>←<br>→", a -> change()), 50, 40));
		c.add(size(des, 200, 40));

		c.add(size(date, 200, 40));
		c.add(size(btn("조회", a -> lookUp()), 150, 40));
		c.setBorder(new EmptyBorder(15, 0, 0, 0));

		for (var bcap : "계정,예매,로그아웃".split(",")) {
			rbp.add(btn(bcap, a -> {
				switch (a.getActionCommand()) {
				case "계정":
					Account();
					break;
				case "예매":
					new Reserve();
					break;
				default:
					dispose();
					break;
				}
			}));
		}

		c.setOpaque(false);
	}

	{
		int idx = 0;
		try {
			var rs = stmt.executeQuery(
					"select r.no, l.name, title, description, img from recommend r, location l, recommend_info ri where r.location_no = l.no and ri.recommend_no = r.no group by l.name order by r.no asc");
			while (rs.next()) {
				img[idx] = imageLabel(
						"지급파일/images/recommend/" + hashMap.get(rs.getString(2)) + "/" + rs.getInt(3) + ".jpg", 145,

						140);
				img[idx].setBorder(new TitledBorder(new LineBorder(Color.BLACK), rs.getString(2)));
				img[idx].setName(rs.getString(2));
				img[idx].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						new Detail(((JLabel) (e.getSource())).getName(), UserMain.this);
					}
				});

				idx++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	JComponent bjc[] = { lbl("추천여행지", JLabel.LEFT, 20), img[0], img[1], img[2], img[3], img[4] };

	public UserMain() {
		super("버스예매", 1100, 700);
		setUI();
		setPopUp();

		setVisible(true);
	}

	void setUI() {
		setLayout(null);

		for (int i = 0; i < rjc.length; i++) {
			add(rjc[i]);
			rjc[i].setBounds(rxywh[i][0], rxywh[i][1], rxywh[i][2], rxywh[i][3]);
		}

		for (int i = 0; i < bjc.length; i++) {
			add(bjc[i]);
			bjc[i].setBounds(bxywh[i][0], bxywh[i][1], bxywh[i][2], bxywh[i][3]);
		}

	}

	class DateItem extends JPanel {

		LocalDate date = LocalDate.now();
		JButton up[] = new JButton[3];
		JLabel yearmonthday[] = new JLabel[3];
		JButton down[] = new JButton[3];
		String t[] = "년,월,일".split(",");

		public DateItem() {
			setLayout(new GridLayout(0, 3));
			for (int i = 0; i < 3; i++) {
				up[i] = new JButton("▲");
				up[i].setName(t[i]);
				up[i].addActionListener(a -> {
					switch (((JButton) a.getSource()).getName()) {
					case "년":
						date = date.plusYears(1);
						setLabel();
						break;
					case "월":
						date = date.plusMonths(1);
						setLabel();
						break;
					default:
						date = date.plusDays(1);
						setLabel();
						break;
					}
				});
				add(up[i]);
			}
			yearmonthday[0] = lbl(date.getYear() + "", JLabel.CENTER);
			yearmonthday[1] = lbl(date.getMonthValue() + "", JLabel.CENTER);
			yearmonthday[2] = lbl(date.getDayOfMonth() + "", JLabel.CENTER);

			for (int i = 0; i < 3; i++) {
				add(yearmonthday[i]);
			}

			for (int i = 0; i < 3; i++) {
				down[i] = new JButton("▼");
				down[i].setName(t[i]);
				down[i].addActionListener(a -> {
					switch (((JButton) a.getSource()).getName()) {
					case "년":
						date = date.plusYears(-1);
						setLabel();
						break;
					case "월":
						date = date.plusMonths(-1);
						setLabel();
						break;
					default:
						date = date.plusDays(1);
						setLabel();
						break;
					}
				});
				add(down[i]);
			}

		}

		void setLabel() {
			yearmonthday[0].setText(date.getYear() + "");
			yearmonthday[1].setText(date.getMonthValue() + "");
			yearmonthday[2].setText(date.getDayOfMonth() + "");
		}
	}

	class PosItem extends JPanel {

		JScrollPane pane1;
		JScrollPane pane2;

		HashMap<String, String> map = new HashMap<>();

		public PosItem(JTextField txt) {
			super(new BorderLayout());

			var c = new JPanel(new GridLayout(1, 0));
			var c1 = new JPanel(new GridLayout(0, 1));
			var c2 = new JPanel(new GridLayout(0, 1));

			add(c);
			c.add(pane1 = new JScrollPane(c1));
			c.add(pane2 = new JScrollPane(c2));

			try {
				var rs = stmt.executeQuery("select * from location");
				while (rs.next()) {
					map.put(rs.getString(1), rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (String v : map.keySet()) {
				JButton b = new JButton(map.get(v));
				b.setName(v);
				c1.add(b);
				b.addActionListener(a -> {
					c2.removeAll();
					try {
						var rs = stmt.executeQuery(
								"select * from location2 where location_no = " + ((JButton) (a.getSource())).getName());
						while (rs.next()) {
							JButton btn = new JButton(rs.getString(2));
							btn.addActionListener(d -> {
								txt.setForeground(Color.BLACK);
								txt.setText(d.getActionCommand());
								sMenu.setVisible(false);
								dMenu.setVisible(false);
								pane2.setVisible(false);
							});

							c2.add(btn);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					revalidate();
					pane2.setVisible(true);
				});
			}

			pane1.setPreferredSize(new Dimension(150, 300));
			pane2.setVisible(false);
		}
	}

	void setPopUp() {
		start.setComponentPopupMenu(sMenu = new JPopupMenu());
		des.setComponentPopupMenu(dMenu = new JPopupMenu());
		date.setComponentPopupMenu(dateMenu = new JPopupMenu());
		dateMenu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				date.setForeground(Color.BLACK);
				date.setText(d.date.toString());
			}
		});
		sMenu.add(new PosItem(start));
		dMenu.add(new PosItem(des));
		dateMenu.add(d = new DateItem());
	}

	void Account() {
		String yn = JOptionPane.showInputDialog(null, "비밀번호를 입력해주세요.", "입력", JOptionPane.QUESTION_MESSAGE);

		try {
			var rs = stmt.executeQuery("select * from user where id = '" + u_id + "' and pwd = '" + yn + "'");
			if (rs.next()) {
				new Account(this);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void change() {
		if (start.getText().equals("출발지") || des.getText().equals("도착지")) {
			return;
		}

		String tmp = start.getText();
		start.setText(des.getText());
		des.setText(tmp);
	}

	void lookUp() {
		if (start.getText().contentEquals("") || start.getText().equals("출발지") || des.getText().contentEquals("")
				|| des.getText().equals("도착지") || date.getText().contentEquals("") || date.getText().equals("날짜")) {
			emsg = new eMsg("출발지, 도착지, 날짜 중 공란이 있습니다.");
			return;
		}

		try {
			String depart = getOne("select no from a where name = '" + start.getText() + "'");
			String arrive = getOne("select no from a where name = '" + des.getText() + "'");
			var rs = stmt.executeQuery("select * from schedule where departure_location2_no = '" + depart
					+ "' and arrival_location2_no = '" + arrive + "' and date = '" + date.getText() + "'");
			if (rs.next()) {
				new BusReserve(toInt(depart), toInt(arrive), date.getText(), this).addWindowListener(new before(this));
			} else {
				emsg = new eMsg("예매 가능한 일정이 없습니다.");
				return;
			}
		} catch (SQLException e) {
			emsg = new eMsg("예매 가능한 일정이 없습니다.");
		}
	}

	public static void main(String[] args) {
		new UserMain();
	}
}
