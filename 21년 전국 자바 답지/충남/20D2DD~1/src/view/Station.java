package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import db.DBManager;

public class Station extends BaseFrame {
//	ArrayList<Integer> line;
	JComboBox<String> combo;
	JLabel next, prev, cur;
	JPanel nc, cc, ccc;
	JScrollPane scr;
//	String mname, time[];
//	StringBuffer times;

//	int sta, mserial, inter = 0;
//	int stationidx = 0;
//	int stationidx = 1;
//	LocalTime start, end;
//	LocalTime atime;

	// 추가
	int mNum, innerStaIdx;
	String stName, selStName;

	public Station(int no) {
		super("역 정보", 600, 700);

		try {
			dataInit2();
		} catch (SQLException e) {
		}

		selStName = stName = stNames.get(no);

		this.add(n = new JPanel(new FlowLayout(FlowLayout.CENTER)), "North");
		this.add(c = new JPanel(new BorderLayout(10, 10)));

		JPanel cn = new JPanel(new BorderLayout());
		cc = new JPanel(new BorderLayout());
		c.add(cn, "North");
		c.add(scr = new JScrollPane(cc));
		cc.add(ccc = new JPanel(new GridLayout(0, 1, 5, 5)));

		n.add(prev = new JLabel(""));
		n.add(cur = new JLabel(""));
		n.add(next = new JLabel(""));

		next.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		cur.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		prev.setFont(new Font("맑은 고딕", Font.BOLD, 25));

		cur.setForeground(Color.BLUE);
		cn.add(combo = new JComboBox<String>());
		cc.add(lbl("열차 시간표", 0, 20), "North");

		setCombo();
//		setData();
		cevnt();
		evnt();
		change(0);

		setEmpty(((JPanel) getContentPane()), 10, 10, 10, 10);
		setVisible(true);
	}

	private void evnt() {
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (prev.getText().equals("기점")) {
					eMsg("해당 노선이 마지막 위치입니다.");
					return;
				}
				change(-1);
//				setCombo();
//				cevnt();
			}
		});

		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (next.getText().equals("종점")) {
					eMsg("해당 노선이 마지막 위치입니다.");
					return;
				}
				change(1);
			}
		});
		combo.addItemListener(a -> {
			cevnt();
		});
	}

	private void cevnt() {
		if (combo.getSelectedItem() == null)
			return;
		if (combo.getItemCount() == 0)
			return;
		mNum = metroNames.indexOf(combo.getSelectedItem().toString());
		stName = selStName;
		innerStaIdx = ((ArrayList) metroStInfos[mNum][0]).indexOf(stName);

//		change(0);

	}

	void update() {
		int hour0 = 0, hour1 = 0;
		String timeStr = "";
		ccc.removeAll();
		for (int i = 0; i < 100; i++) {
			int sec = metroTimeDim[mNum][innerStaIdx][i];
			if (sec == 0)
				break;

			hour1 = sec / 3600;
			if ((hour0 != 0 && hour0 != hour1) || hour1 == 0) {
				JPanel temp = new JPanel(new FlowLayout());
				temp.add(new TimeLine(timeStr, hour0));
				ccc.add(temp);

				timeStr = String.format("%02d:%02d:%02d", sec / 3600, (sec % 3600) / 60, sec % 60) + "<br>";
			} else {
				timeStr += String.format("%02d:%02d:%02d", sec / 3600, (sec % 3600) / 60, sec % 60) + "<br>";
			}

			hour0 = hour1;
		}

		ccc.repaint();
		ccc.revalidate();

	}

	class TimeLine extends JPanel {
		JLabel times;

		public TimeLine(String time, int hour) {
			super(new BorderLayout());
			add(sz(lbl(hour + "시", 0, 18), 50, 50), "West");
			add(sz(times = lbl("<html>" + time, 0), 470, time.split("<br>").length * 18));
			this.times.setVerticalAlignment(SwingConstants.TOP);
			this.times.setBorder(new MatteBorder(0, 1, 0, 0, Color.blue));
		}
	}

	private void change(int i) {
		innerStaIdx += i;
		cur.setText("←" + ((ArrayList) metroStInfos[mNum][0]).get(innerStaIdx) + "→");
		stName = ((ArrayList) metroStInfos[mNum][0]).get(innerStaIdx) + "";
		setCombo();

		if (innerStaIdx - 1 == 0) {
			prev.setText("기점");
		} else {
			prev.setText(((ArrayList) metroStInfos[mNum][0]).get(innerStaIdx - 1) + "");
		}

		if (innerStaIdx + 1 == ((ArrayList) metroStInfos[mNum][0]).size()) {
			next.setText("종점");
		} else {
			next.setText(((ArrayList) metroStInfos[mNum][0]).get(innerStaIdx + 1) + "");
		}
		update();
	};

	private void setCombo() {
		combo.removeAllItems();
		for (int i = 1; i <= 30; i++) {
			if (((ArrayList) metroStInfos[i][0]).contains(stName))
				combo.addItem(metroNames.get(i));
		}
	}

}
