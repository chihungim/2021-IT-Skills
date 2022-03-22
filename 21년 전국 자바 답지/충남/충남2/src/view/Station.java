package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Station extends BaseFrame {

	JComboBox<String> box = new JComboBox<String>();
	JLabel next, prev, cur;
	JScrollPane pane = new JScrollPane(c = new JPanel(new BorderLayout()));
	int mNum, sno;
	String st, selSt;
	JPanel tableP;

	public Station(int sno) {
		super("역정보", 700, 800);

		setLayout(new BorderLayout(5, 5));

		try {
			datainit();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.sno = sno;
		selSt = st = stNames.get(sno);
		var n_c = new JPanel(new FlowLayout(1));

		add(n = new JPanel(new BorderLayout()), "North");
		n.add(n_c);
		n_c.add(prev = lbl("", 0, 25));
		n_c.add(cur = lbl("", 0, 25));
		n_c.add(next = lbl("", 0, 25));
		n.add(box, "South");

		cur.setForeground(Color.BLUE);

		add(pane);
		c.add(lbl("열차 시간표", JLabel.CENTER, 20), "North");
		c.add(tableP = new JPanel());
		tableP.setLayout(new BoxLayout(tableP, BoxLayout.Y_AXIS));

		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (prev.getText().equals("기점")) {
					eMsg("해당 노선이 기점입니다.");
					return;
				}
				move(-1);
				update();
				setMetro();
			}
		});

		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (next.getText().equals("종점")) {
					eMsg("해당 노선의 마지막 위치입니다.");
					return;
				}
				move(1);
				update();
				setMetro();
			}
		});

		box.addItemListener(i -> {

			mNum = mNames.indexOf(box.getSelectedItem());
			if (mNum == -1)
				return;

			this.sno = ((ArrayList) mstInfos[mNum][0]).indexOf(st);
			move(0);
			update();
		});

		setMetro();
		move(0);
		update();

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		setVisible(true);

	}

	void setMetro() {
		box.removeAllItems();
		for (int i = 1; i <= 30; i++) {
			var train = (ArrayList) mstInfos[i][0];
			if (train.contains(st))
				box.addItem(mNames.get(i));
		}

		box.setSelectedIndex(1);
	}

	void move(int no) {
		sno += no;

		var sName = (ArrayList) mstInfos[mNum][0];
		cur.setText("←" + sName.get(sno) + "→");
		st = sName.get(sno).toString();

		if (sno - 1 == 0)
			prev.setText("기점");
		else
			prev.setText(sName.get(sno - 1).toString());

		if (sno + 1 == sName.size())
			next.setText("종점");
		else
			next.setText(sName.get(sno + 1).toString());

		update();
	}

	void update() {
		int hour0 = 0, hour1 = 0;
		String timeStr = "";
		tableP.removeAll();

		for (int i = 0; i < 300; i++) {
			int sec = mTimes[mNum][sno][i];

			if (sec == 0)
				break;
			var time = LocalTime.ofSecondOfDay(sec);
			hour1 = time.getHour();
			if ((hour0 != 0 && hour0 != hour1) || hour1 == 0) {
				tableP.add(Box.createVerticalStrut(5));
				tableP.add(new TimeLine(timeStr, hour0));
				tableP.add(Box.createVerticalStrut(5));
				timeStr = time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "<br>";
			} else {
				timeStr += time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "<br>";
			}
			hour0 = hour1;
		}
		repaint();
		revalidate();
	}

	class TimeLine extends JPanel {
		JLabel timeLine;

		public TimeLine(String time, int hour) {
			super(new BorderLayout());
			add(sz(lbl(hour + "시", JLabel.CENTER, 18), 50, 50), "West");
			add(sz(timeLine = lbl("<html>" + time, JLabel.CENTER), 470, time.split("<br>").length * 18));

			timeLine.setVerticalAlignment(JLabel.TOP);

			timeLine.setBorder(new MatteBorder(0, 1, 0, 0, Color.BLUE));
		}
	}

	public static void main(String[] args) {
		new Station(1);
	}

}
