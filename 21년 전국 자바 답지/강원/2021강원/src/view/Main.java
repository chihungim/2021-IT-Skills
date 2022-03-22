package view;

import static javax.swing.JOptionPane.showInputDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class Main extends BaseFrame {

	JTextField start, end, date;
	LocalDate today = LocalDate.now();

	public Main() {
		super("버스 예매", 1200, 700);
		ui();
		events();
		setVisible(true);
	}

	void ui() {
		setLayout(new GridLayout(0, 1));
		var up = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("./지급파일/images/main.jpg"), 0, 0, this);
			}
		};
		var up_n = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var up_c = new JPanel(null);

		var down = new JPanel(new BorderLayout());

		add(up);
		add(down);
		up.add(up_n, "North");
		up.add(getPos(up_c));

		up_n.setOpaque(false);
		up_c.setOpaque(false);

		up_n.add(themeButton());
		for (var bcap : "계정,예매,로그아웃".split(",")) {
			up_n.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("계정")) {
					var yn = showInputDialog(null, "비밀번호를 입력하세요", "입력", JOptionPane.QUESTION_MESSAGE);
					System.out.println(yn);
					if (yn != null) {

						if (yn.equals("")) {
							eMsg("공란을 확인해주세요.");
							return;
						}

						if (pw.equals(yn)) {
							new UserEdit().setVisible(true);
						} else {

						}
					}
				} else if (a.getActionCommand().equals("예매")) {
					new UserReserve().setVisible(true);
				} else {
					dispose();
				}
			}));
		}

		var rp = new JPanel(new BorderLayout());
		var rp_c = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		rp.setBounds(130, 50, 940, 125);

		rp.add(lbl("예매", JLabel.LEFT, 20), "North");
		rp_c.add(start = new TextHolder("출발지", 20));
		rp_c.add(btn("<html>←<br>→", a -> swap()));

		rp.add(rp_c);
		rp_c.add(end = new TextHolder("도착치", 20));
		rp_c.add(date = new TextHolder("날짜", 20));
		rp_c.add(sz(btn("조회", a -> {
			var s = start.getText();
			var e = end.getText();
			var d = date.getText();

			if (s.isEmpty() || e.isEmpty() || d.isEmpty()) {
				eMsg("출발지, 도착지, 날짜 중 공란이 있습니다.");
				return;
			}

			if (getOne(
					"select l1.name , l2.name from schedule s,ll2 l1, ll2 l2 where s.arrival_location2_no = l1.no and s.departure_location2_no = l2.no and l1.name like '%"
							+ s + "%' and l2.name like '%" + e + "%' and s.date in ('" + d + "')").equals("")) {
				eMsg("예매 가능한 일정이 없습니다.");
				return;
			}

			var sno = toInt(ll2map.get(s));
			var eno = toInt(ll2map.get(e));

			new Reserve(sno, eno, d);
		}), 150, 40));
		rp.setBorder(new EmptyBorder(20, 20, 20, 20));
		up_c.add(rp);

		down.add(lbl("추천 여행지", JLabel.LEFT, 20), "North");

		var down_c = new JPanel(null);
		down.add(getPos(down_c));
		// 5 30
		try {
			var rs = stmt.executeQuery(
					"SELECT  r.no ,l.name FROM recommend r left outer join recommend_info ri on r.no = ri.recommend_no inner join location l on r.location_no = l.no group by r.no order by r.no asc");
			while (rs.next()) {
				var img = new JLabel();
				var popup = new JPopupMenu();
				img.setComponentPopupMenu(popup);
				for (var bcap : "상세설명,예매".split(",")) {
					var item = new JMenuItem(bcap);
					popup.add(item);
					item.addActionListener(a -> {
						if (a.getActionCommand().equals("상세설명")) {
							new Detail().setVisible(true);
						} else {
							new UserReserve().setVisible(true);
						}
					});
				}
				img.setIcon(getIcon("./지급파일/images/recommend/" + map.get(rs.getString(2)) + "/1.jpg", 150, 150));
				if (rs.getInt(1) % 2 == 0) {
					img.setBorder(new TitledBorder(new LineBorder(Color.BLACK), rs.getString(2)));
					img.setBounds(5 + (200 * (rs.getInt(1))) - 200, 40, 150, 160);
				} else {
					img.setBorder(new TitledBorder(new LineBorder(Color.BLACK), rs.getString(2)));
					img.setBounds(5 + (200 * (rs.getInt(1))) - 200, 20, 150, 160);
				}
				down_c.add(img);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		down.setBorder(new EmptyBorder(20, 20, 20, 20));
	}

	void swap() {
		var s = start.getText();
		var e = end.getText();
		if (s.isEmpty() || e.isEmpty())
			return;
		var tmp = s;
		s = e;
		e = tmp;
		start.setText(s);
		end.setText(e);

	}

	void events() {
		JPopupMenu pm = new JPopupMenu();
		pm.add(new Popup(start));
		start.setComponentPopupMenu(pm);

		JPopupMenu pm2 = new JPopupMenu();
		pm2.add(new Popup(end));
		end.setComponentPopupMenu(pm2);

		pm.addPopupMenuListener(new PopupMenuListener() {

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
				pm.removeAll();
				pm.add(new Popup(start));
			}
		});

		pm2.addPopupMenuListener(new PopupMenuListener() {

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
				pm2.removeAll();
				pm2.add(new Popup(start));
				// TODO Auto-generated method stub

			}
		});
		pm.addPopupMenuListener(new PopupMenuListener() {

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
				// TODO Auto-generated method stub

			}
		});

		JPopupMenu pm3 = new JPopupMenu();

		var tmp = new JPanel(new GridLayout(1, 0));
		var yy = new JPanel(new BorderLayout());
		var mm = new JPanel(new BorderLayout());
		var dd = new JPanel(new BorderLayout());

		JLabel year = new JLabel(today.getYear() + "", 0);
		JLabel month = new JLabel(today.getMonthValue() + "", 0);
		JLabel day = new JLabel(today.getDayOfMonth() + "", 0);

		tmp.add(yy);
		tmp.add(mm);
		tmp.add(dd);

		yy.add(year);
		mm.add(month);
		dd.add(day);

		yy.add(btn("▲", a -> plustime(1, 0, 0, year, month, day)), "North");
		yy.add(btn("▼", a -> plustime(-1, 0, 0, year, month, day)), "South");
		mm.add(btn("▲", a -> plustime(0, 1, 0, year, month, day)), "North");
		mm.add(btn("▼", a -> plustime(0, -1, 0, year, month, day)), "South");
		dd.add(btn("▲", a -> plustime(0, 0, 1, year, month, day)), "North");
		dd.add(btn("▼", a -> plustime(0, 0, -1, year, month, day)), "South");

		JMenuItem item = new JMenuItem();
		item.add(tmp);
		pm3.add(item);
		item.setPreferredSize(new Dimension(200, 100));

		pm3.addPopupMenuListener(new PopupMenuListener() {

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
				date.setText(today + "");
			}
		});

		date.setComponentPopupMenu(pm3);
	}

	void plustime(int y, int m, int d, JLabel lbl1, JLabel lbl2, JLabel lbl3) {
		today = today.plusYears(y);
		today = today.plusMonths(m);
		today = today.plusDays(d);
		lbl1.setText(today.getYear() + "");
		lbl2.setText(today.getMonthValue() + "");
		lbl3.setText(today.getDayOfMonth() + "");
	}

	public static void main(String[] args) {
		setLogin("1");
		new Main();
	}
}
