package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class UserInfo extends BaseDialog {

	DefaultTableModel m = model("출발지,도착지,도착시간,출발날짜".split(","));

	JTable t = table(m);

	JPanel chartP;
	int uno;

	public UserInfo(BaseFrame b, int uno) {
		super("예매 정보", 800, 600, b);
		this.uno = uno;
		data();
		ui();
		setVisible(true);
	}

	void data() {
		execute("drop view if exists `deq`");
		execute("drop view if exists `start`");

		execute("create view `deq` as select r.no, r.user_no, concat(l.name ,' ',l2.name) as name  from reservation r, schedule s,location l ,location2 l2 where r.schedule_no = s.no and s.arrival_location2_no = l2.no and l2.location_no = l.no");
		execute("create view `start` as select r.no, r.user_no, concat(l.name ,' ',l2.name) as name from reservation r, schedule s,location l ,location2 l2 where r.schedule_no = s.no and s.departure_location2_no = l2.no and l2.location_no = l.no");

		addRow(m,
				"SELECT s.name, d.name, date_add(sc.date, interval sc.elapsed_time HOUR_SECOND), sc.date FROM reservation r,  schedule sc, start s, deq d where r.no = s.no and r.no = d.no  and r.user_no = "
						+ uno);

	}

	void ui() {
		add(lbl("사용자 예매 정보", JLabel.LEFT, 20), "North");
		setChart();
		add(size(new JScrollPane(t), 1, 200), "South");
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(20, 20, 20, 20));
	}

	void setChart() {
		add(chartP = new JPanel() {
			protected void paintComponent(java.awt.Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawLine(20, 30, 20, 250);
				g2d.drawLine(20, 40, 700, 40);
				g2d.drawLine(20, 145, 700, 145);
				g2d.drawLine(20, 250, 700, 250);

				ArrayList<Integer> values = new ArrayList<>();
				try {
					var rs = stmt.executeQuery(
							"SELECT  date_format(sc.date, '%m') as m, count(*) FROM reservation r,  schedule sc, start s, deq d where r.no = s.no and r.no = d.no  and r.user_no and r.schedule_no = sc.no and r.user_no = "
									+ uno + " group by m order by m desc");
					while (rs.next()) {
						values.add(rs.getInt(2));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int xx[] = new int[3];
				int yy[] = new int[3];
				int max = Collections.max(values);
				try {
					var rs = stmt.executeQuery(
							"SELECT  date_format(sc.date, '%m') as m, count(*) FROM reservation r,  schedule sc, start s, deq d where r.no = s.no and r.no = d.no  and r.user_no and r.schedule_no = sc.no and r.user_no = "
									+ uno + " group by m order by m asc");
					int idx = 0;
					while (rs.next()) {
						g2d.setColor(new Color(0, 123, 255));
						int height = (int) ((Math.ceil((rs.getDouble(2) / max) * 10) / 10) * 210);
						g2d.fillRect(165 + (idx * 200), 250 - height, 100, height);
						g2d.setColor(Color.BLACK);
						g2d.fillOval(210 + (idx * 200), 250 - height - 5, 10, 10);
						xx[idx] = 210 + (idx * 200);
						yy[idx] = 250 - height;
						g2d.drawString(String.format("%d월", rs.getInt(1)), 200 + (idx * 200), 270);
						idx++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g2d.drawPolyline(xx, yy, 3);

			};
		});
	}

	public static void main(String[] args) {
		new UserInfo(null, 1);
	}
}
