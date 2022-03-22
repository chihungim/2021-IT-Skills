package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class Station extends BaseFrame {
	ArrayList<Integer> lines;
	JComboBox<String> combo;
	JLabel next, prev, cur;
	JPanel n_c, c_c, n, c_c_c;
	JScrollPane pane;
	String metro_name, timesArr[];
	StringBuffer times;

	int station, metro_serial, stationIndex = 0, interval = 0;
	LocalTime start, end;
	LocalTime arriveTime;

	public Station(int station) {
		super("역정보", 600, 700);

		this.arriveTime = LocalTime.of(0, 0);
		this.times = new StringBuffer();
		this.station = station;
		this.add(n = new JPanel(new FlowLayout(FlowLayout.CENTER)), "North");
		this.add(c = new JPanel(new BorderLayout(10, 10)));

		var c_n = new JPanel(new BorderLayout());
		c_c = new JPanel(new BorderLayout());
		c.add(c_n, "North");
		c.add(pane = new JScrollPane(c_c));
		c_c.add(c_c_c = new JPanel(new GridLayout(0, 1, 5, 5))); // 크기 맞추기 귀찮아

		n.add(prev = new JLabel(""));
		n.add(cur = new JLabel(""));
		n.add(next = new JLabel(""));

		next.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		prev.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		cur.setFont(new Font("맑은 고딕", Font.BOLD, 25));

		cur.setForeground(Color.BLUE);
		c_n.add(combo = new JComboBox<String>());
		c_c.add(lbl("열차 시간표", 0, 20), "North");
//		c_c.add(c_c_c = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)));

		setCombo();
		setData();
		changeStation(0);
		events();
		comboEvent();

		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}

	void events() {
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (prev.getText().equals("기점")) {
					eMsg("해당 노선이 마지막 위치입니다.");
					return;
				}
				changeStation(-1);
				setCombo();
			};
		});

		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (next.getText().equals("종점")) {
					eMsg("해당 노선이 마지막 위치입니다.");
					return;
				}
				changeStation(1);
				setCombo();
			}
		});

		combo.addItemListener(i -> comboEvent());
	}

	void setCombo() {
		combo.removeAllItems();
		try {
			var rs = stmt
					.executeQuery("select m.name from route r, metro m where r.metro = m.serial and r.station = " + station);
			while (rs.next()) {
				combo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setData() {
		try {
			metro_name = combo.getSelectedItem().toString(); // 이름으로
			metro_serial = metroSerial.get(metro_name); // 메트로의 시리얼 넘버를 가져오고
			lines = MetroSerialToLines.get(metro_serial); // 시리얼 번호로 노선목록을 가져온 후
			stationIndex = MetroSerialToLines.get(metro_serial).indexOf(station); // stations 번호의 index위치를 파악
		} catch (NullPointerException e) {
			return;
		}
	}

	void setTime() {
		try {
			var rs = stmt.executeQuery(
					"SELECT `start` , `end` , `interval` from metro where name like '%" + metro_name + "%'");
			if (rs.next()) {
				start = LocalTime.parse(rs.getString(1));
				end = LocalTime.parse(rs.getString(2));
				interval = LocalTime.parse(rs.getString(3)).toSecondOfDay();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arriveTime = start;
		for (int i = 1; i <= stationIndex; i++) {
			arriveTime = arriveTime.plusSeconds(cost2[lines.get(i - 1)][lines.get(i)] * 5);
		}
	}

	void changeStation(int n) {
		stationIndex += n;
		changeLabel();
	}

	void changeLabel() {
		setTime();
		station = lines.get(stationIndex);
		cur.setText("←" + stations.get(lines.get(stationIndex)) + "→");

		if (stationIndex - 1 == -1)
			prev.setText("기점");
		else
			prev.setText(stations.get(lines.get(stationIndex - 1)));

		if (stationIndex + 1 == lines.size())
			next.setText("종점");
		else
			next.setText(stations.get(lines.get(stationIndex + 1)));
	}

	void comboEvent() {
		setData();
		changeLabel();
		setTime();
		int before = arriveTime.getHour(), after = 0;
		times.delete(0, times.length());

		while (!arriveTime.isAfter(end)) {
			times.append(arriveTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "<br>");
			arriveTime = arriveTime.plusSeconds(interval);
			after = arriveTime.getHour();
			if (after > before) {
				times.insert(times.length(), ",");
				before = after;
			}
		}

		timesArr = times.toString().split(",");

		c_c_c.removeAll();

		for (int i = 0; i < timesArr.length; i++) {
			int hour = LocalTime.parse(timesArr[i].substring(0, 8)).getHour();
			JPanel tmp = new JPanel(new FlowLayout());
			tmp.add(new TimeLines(timesArr[i], hour));
			c_c_c.add(tmp);
		}

		c_c_c.revalidate();
		c_c_c.repaint();

	}

	@SuppressWarnings("serial")
	class TimeLines extends JPanel {
		JLabel times;

		public TimeLines(String times, int hour) {
			super(new BorderLayout());
			add(sz(lbl(hour + "시", 0, 18), 50, 50), "West");
			add(sz(this.times = lbl("<html>" + times, 0), 470, times.split("<br>").length * 18));
			this.times.setVerticalAlignment(SwingConstants.TOP);
			this.times.setBorder(new MatteBorder(0, 1, 0, 0, lightBlue));
		}
	}

}
