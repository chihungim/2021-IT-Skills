package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.sun.jdi.connect.TransportTimeoutException;

public class Reserve extends BaseFrame {

	JLabel timeLabel, readMoreLabel;
	LocalDate date = LocalDate.now();
	LocalTime time = LocalTime.now().plusMinutes(30); // 솔찍히 정확한 기준 시간을 모르겠어서 그냥 얘를 기준으로 만듦;
	String s_station, e_station;
	ArrayList<Integer> cpath;
	ArrayList<Integer> ho = new ArrayList<>();
	HashSet<Integer> distinctSet = new HashSet<>();
	int transCnt = 0, interval = 0;
	LocalTime toalTimeWithoutTranfer = LocalTime.of(0, 0, 0), start, end;
	JPanel c_c, c_c_c;
	boolean breakPoint = false; // 미한하다..
	int totalDistance = 0;
//	LocalTime baseTime = LocalTime.of(20, 44);

	public Reserve(ArrayList<Integer> cpath) {
		super("예매", 500, 600);
		this.cpath = cpath;

		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		s_station = stations.get(cpath.get(0));
		e_station = stations.get(cpath.get(cpath.size() - 1));
		n.add(lbl(stations.get(cpath.get(0)) + "→" + stations.get(cpath.get(cpath.size() - 1)), 0, 23, lightBlue));

		var c_n = new JPanel(new FlowLayout(0));
		c.add(c_n, "North");
		c_n.add(btn("시간 변경", a -> {
			new TimePicker(Reserve.this);
		}));

		c_n.add(timeLabel = new JLabel(date + " " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " 탑승"));
		c_n.add(sz(new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.fillRect(0, 0, 470, 2);
			}
		}, 500, 25));

		c.add(new JScrollPane(c_c = new JPanel(new BorderLayout())));

		var c_c_n = new JPanel(new BorderLayout());
		c_c.add(c_c_c = new JPanel(new GridLayout(0, 1, 5, 5)));

		for (int i = 1; i < cpath.size(); i++) {
			totalDistance += cost2[cpath.get(i - 1)][cpath.get(i)];
			ho.add(lineMap.get(cpath.get(i - 1) + "," + cpath.get(i)));
		}

		for (Integer v : ho) {
			distinctSet.add(v);
			// 중복 제거
		}

		transCnt = distinctSet.size() - 1;

		for (int i = 1; i < cpath.size(); i++) {
			toalTimeWithoutTranfer = toalTimeWithoutTranfer.plusSeconds(cost2[cpath.get(i - 1)][cpath.get(i)] * 5);
		}

		c_c.add(c_c_n, "North");
		c_c_n.add(lbl(cpath.size() + "개역 정차 / " + transCnt + "회 환승", 0, 15, Color.blue, Font.PLAIN), "West");
		c_c_n.add(readMoreLabel = lbl("자세히보기", 0, 15, Color.RED, Font.PLAIN), "East");

		readMoreLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Waypoint(Reserve.this).setVisible(true);
			}
		});

		sz(c_n, 1, 50);
		sz(n, 1, 80);

		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);

		setTime();

		this.setVisible(true);
	}

	public void setTime() {
		timeLabel.setText(date + " " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " 탑승");

		c_c_c.removeAll();
		try {
			var rs = stmt.executeQuery(
					"select * from metro where name like '%" + lineName.get(cpath.get(0) + "," + cpath.get(1))
							.substring(0, lineName.get(cpath.get(0) + "," + cpath.get(1)).length() - 2) + "%'");
			if (rs.next()) {
				start = LocalTime.parse(rs.getString(3));
				end = LocalTime.parse(rs.getString(4));
				interval = LocalTime.parse(rs.getString(5)).toSecondOfDay();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (this.start.isBefore(this.end)) {
			this.start = this.start.plusSeconds(interval);
			if (start.isBefore(time))
				continue;
			if (breakPoint)
				break;
			c_c_c.add(new Item(this.start));
		}

		breakPoint = false;
		revalidate();
		repaint();
	}

	class Item extends JPanel {
		String metro_name;
		int end = 0;
		JLabel s_station, e_station;
		JPanel c_e, c_w, c, c_c;
		LocalTime sTime, totalTime;
		JLabel time;

		public Item(LocalTime sTime) {
			setLayout(new BorderLayout(5, 5));

			this.sTime = sTime;

			add(c = new JPanel(new BorderLayout()));
			c.add(c_c = new JPanel(new BorderLayout()));
			c.add(c_e = new JPanel(new BorderLayout()), "East");
			c.add(c_w = new JPanel(new BorderLayout()), "West");

			c_w.add(s_station = lbl(Reserve.this.s_station, JLabel.LEFT, 15), "South");
			c_e.add(e_station = lbl(Reserve.this.e_station, JLabel.RIGHT, 15), "South");

			s_station.setForeground(Color.LIGHT_GRAY);
			e_station.setForeground(Color.LIGHT_GRAY);

			c.setBackground(Color.WHITE);

			c_c.setOpaque(false);
			c_e.setOpaque(false);
			c_w.setOpaque(false);

			// 히히

			c_w.add(lbl(sTime + "", JLabel.LEFT, 35));
			add(btn("선택", a -> {
				if (uno == 0) {
					eMsg("로그인 후 예매 가능합니다.");
					return;
				}

				int yesno = JOptionPane.showConfirmDialog(null,
						this.sTime.format(DateTimeFormatter.ofPattern("HH:mm")) + "시간 지하철을 예매할까요?", "메시지",
						JOptionPane.YES_NO_OPTION);

				if (yesno == JOptionPane.YES_OPTION) {
					new Purchase(Item.this, Reserve.this).addWindowListener(new Before(Reserve.this));
				}
			}), "East");
			if (transCnt > 0) {
				int from = ho.get(0);
				int to;
				LocalTime baseTime;
				for (int i = 1; i < ho.size(); i++) {
					to = ho.get(i);
					sTime = sTime.plusSeconds(cost2[cpath.get(i - 1)][cpath.get(i)] * 5);
					if (from != to) {
						baseTime = sTime;
						metro_name = lineName.get(cpath.get(i) + "," + cpath.get(i + 1));
						ArrayList<Integer> lines = new ArrayList<>();
						try {
							var rs = stmt.executeQuery(
									"select r.station, m.name from route r, metro m where r.metro = m.serial and name like '%"
											+ metro_name + "%'");
							while (rs.next()) {
								lines.add(rs.getInt(1));
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						end = lines.indexOf(cpath.get(i));

						for (int k = end; k > 1; k--) {
							baseTime = baseTime.minusSeconds(cost2[lines.get(k)][lines.get(k - 1)] * 5);
						}

						String s = null, interval = null;

						try {
							var rs = stmt.executeQuery("select * from metro where name like '%" + metro_name + "%'");
							if (rs.next()) {
								s = rs.getString(3);
								interval = rs.getString(5);
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						LocalTime start = LocalTime.parse(s);

						int ival = LocalTime.parse(interval).toSecondOfDay();
						while (!start.isAfter(baseTime)) {
							start = start.plusSeconds(ival);
						}

						LocalTime dif = start.minusSeconds(baseTime.toSecondOfDay());
						this.totalTime = Reserve.this.toalTimeWithoutTranfer.plusSeconds(dif.toSecondOfDay());

						if (this.totalTime.getHour() > 0)
							c_c.add(time = lbl(totalTime.getHour() + "시간 " + totalTime.getMinute() + "분 "
									+ totalTime.getSecond() + "초 소요", 0));
						else
							c_c.add(time = lbl(totalTime.getMinute() + "분 " + totalTime.getSecond() + "초 소요", 0));

						c_e.add(lbl(this.sTime.plusSeconds(totalTime.toSecondOfDay())
								.format(DateTimeFormatter.ofPattern("HH:mm")), JLabel.RIGHT, 35));
						if (sTime.plusSeconds(totalTime.toSecondOfDay()).isAfter(Reserve.this.end)) {
							breakPoint = true;
						}
						from = to;
					}
				}
			} else {
				c_c.add(time = lbl(Reserve.this.toalTimeWithoutTranfer.getMinute() + "분 "
						+ Reserve.this.toalTimeWithoutTranfer.getSecond() + "초 소요", 0));
				c_e.add(lbl(this.sTime.plusSeconds(Reserve.this.toalTimeWithoutTranfer.toSecondOfDay())
						.format(DateTimeFormatter.ofPattern("HH:mm")), JLabel.RIGHT, 35));
				if (sTime.plusSeconds(Reserve.this.toalTimeWithoutTranfer.toSecondOfDay()).isAfter(Reserve.this.end)) {
					breakPoint = true;
				}
			}
			time.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
			time.setForeground(lightBlue);

			c_c.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

			c_c.setBorder(new EmptyBorder(20, 20, 0, 20));
			c.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
	}
}
