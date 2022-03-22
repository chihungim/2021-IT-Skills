package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

public class Reserve extends BaseFrame {
	ArrayList<Integer> path;
	String start, end;
	LocalDateTime time = LocalDateTime.now().plusMinutes(30);
	JLabel lblmore, lbltime, lbltransfer;
	JPanel pane_p, pane_n, pane_c;
	JScrollPane pane = new JScrollPane(pane_p = new JPanel(new BorderLayout()));

	int timeTable[][], eidx;
	int total, transCnt;

	public Reserve(ArrayList<Integer> arr) {
		super("예매", 500, 600);

		try {
			datainit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		path = arr;

		add(n = new JPanel(new BorderLayout()), "North");
		add(c = new JPanel(new BorderLayout()));

		start = stNames.get(arr.get(0));
		end = stNames.get(arr.get(arr.size() - 1));

		n.add(lbl("<html><font face = '맑은 고딕', size = '6', Color = 'blue'>" + start + "→" + end, 0, 25));

		var c_n = new JPanel(new FlowLayout(FlowLayout.LEFT));

		c.add(c_n, "North");

		c_n.add(btn("시간변경", a -> {
			new TimePick(this).addWindowListener(new before(this));
		}));

		c_n.add(lbltime = lbl(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "탑승", JLabel.LEFT, 12));

		c.add(pane);

		pane_p.add(pane_n = new JPanel(new BorderLayout()), "North");
		pane_n.add(lblmore = lbl("<html><font color = 'red'><left>자세히 보기", JLabel.LEFT), "East");
		pane_p.add(pane_c = new JPanel(new GridLayout(0, 1, 5, 5)));
		mkTime();

		lblmore.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Way(Reserve.this).setVisible(true);
				super.mousePressed(e);
			}
		});
		pane_n.add(lbltransfer = lbl(arr.size() + "개역 정차 " + transCnt + "회 환승", 2, 15), "West");

		setTime();

		setVisible(true);
	}

	void setTime() {
		lbltime.setText(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 탑승")));
		pane_c.removeAll();
		for (int i = 4; i < 300; i++) {
			if (timeTable[eidx][i] == 0)
				break;
			pane_c.add(new Item(i));
		}
		revalidate();
		repaint();
	}

	class Item extends JPanel {

		int st, et;
		JPanel c, c_c, c_e, c_w;
		JLabel time;

		public Item(int sidx) {
			setLayout(new BorderLayout(2, 2));
			st = timeTable[0][sidx];
			et = timeTable[eidx][sidx];

			add(c = new JPanel(new BorderLayout()));
			c.add(c_c = new JPanel(new BorderLayout()));
			c.add(c_w = new JPanel(new BorderLayout()), "West");
			c.add(c_e = new JPanel(new BorderLayout()), "East");

			c_w.add(lbl("<html><font color = 'GRAY'>" + start, 2, 12), "South");
			c_e.add(lbl("<html><font color = 'GRAY'>" + end, 2, 12), "South");
			c_w.add(lbl(LocalTime.ofSecondOfDay(st).format(DateTimeFormatter.ofPattern("HH:mm")) + "", 0, 15));
			c_e.add(lbl(LocalTime.ofSecondOfDay(et).format(DateTimeFormatter.ofPattern("HH:mm")) + "", 0, 15));
			add(btn("선택", a -> {
				new Purchase(this, Reserve.this).addWindowListener(new before(Reserve.this));
			}), "East");
			int total = et - st;

			c_c.add(time = lbl(LocalTime.ofSecondOfDay(total) + "", 0, 20));
			time.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
			time.setForeground(Color.BLUE);

		}

	}

	void mkTime() {
		int lidx, inner;

		timeTable = new int[path.size()][300];
		eidx = path.size() - 1;

		for (int i = 0; i <= eidx - 1; i++) {
			timeTable[i][0] = path.get(i);
			timeTable[i][1] = lineDim[path.get(i)][path.get(i + 1)];

			if (i == 0) {
				timeTable[i][3] = 0;
			} else {
				timeTable[i][3] = adjDim[timeTable[i - 1][0]][timeTable[i][0]];
				if (timeTable[i - 1][1] != timeTable[i][1]) {
					timeTable[i][2] = 1;
					transCnt++;
				}
			}
		}

		timeTable[eidx][0] = path.get(path.size() - 1);
		timeTable[eidx][3] = adjDim[timeTable[eidx - 1][0]][timeTable[eidx][0]];

		int col = 4;
		lidx = timeTable[0][1];
		inner = ((ArrayList) mstInfos[lidx][0]).indexOf(stNames.get(path.get(0)));
		for (int i = 0; i < 100; i++) {
			if (mTimes[lidx][inner][i] == 0)
				break;
			if (time.toLocalTime().toSecondOfDay() <= mTimes[lidx][inner][i]) {
				timeTable[0][col++] = mTimes[lidx][inner][i];
			}
		}

		for (int i = 1; i < eidx; i++) {
			lidx = timeTable[i][1];
			inner = ((ArrayList) mstInfos[lidx][0]).indexOf(stNames.get(path.get(i)));

			for (int j = 4; j < 300; j++) {
				if (timeTable[i - 1][j] == 0)
					break;

				for (int k = 0; k < 300; k++) {
					if (mTimes[lidx][inner][k] == 0)
						break;

					if (timeTable[i - 1][j] + timeTable[i][3] <= mTimes[lidx][inner][k]) {
						timeTable[i][j] = mTimes[lidx][inner][k];
						break;
					}
				}
			}
		}

		for (int i = 4; i < 300; i++) {
			if (timeTable[eidx - 1][i] == 0)
				break;

			timeTable[eidx][i] = timeTable[eidx - 1][i] + timeTable[eidx][3];
		}
	}

}
