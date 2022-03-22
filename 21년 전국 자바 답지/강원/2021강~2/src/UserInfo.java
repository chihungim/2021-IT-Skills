import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class UserInfo extends BaseFrame {
	JPanel p;

	DefaultTableModel m = model("no,출발지,도착지,도착시간,출발날짜".split(","));
	JTable t = table(m);
	JScrollPane jsc = new JScrollPane(t);

	int num;

	public UserInfo(int num) {
		super("예매 정보", 800, 600);
		this.num = num;

		ui();
		setVisible(true);
	}

	void ui() {
		add(lbl("사용자 예매 정보", 2, 25), "North");
		setChart();
		add(size(jsc, 0, 200), "South");

		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			var rs = stmt.executeQuery(
					"select a.name, b.name, date_add(date, interval elapsed_time hour_second), date from reservation r, schedule s, a a, a b where r.schedule_no = s.no and s.departure_location2_no = a.no and s.arrival_location2_no = b.no and user_no = "
							+ num + " order by date asc");
			while (rs.next()) {
				Object row[] = { rs.getRow(), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) };
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	void setChart() {
		add(p = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.drawLine(20, 30, 20, 250);
				g2d.drawLine(20, 40, 700, 40);
				g2d.drawLine(20, 145, 700, 145);
				g2d.drawLine(20, 250, 700, 250);

				int nums[] = { 10, 105, 210 };
				int height[] = { 240, 145, 40 };
				Integer val[] = new Integer[3];

				try {
					var rs = stmt.executeQuery(
							"select date_format(date, '%m') m, count(*) from reservation r, schedule s, a b where r.schedule_no = s.no and s.arrival_location2_no = b.no and user_no = "
									+ num + " group by m order by m desc limit 3");
					int i = 0;
					while (rs.next()) {
						val[i] = rs.getInt(2);
						g2d.fillRect(165 + (i * 200), height[i], 100, nums[i]);
						g2d.setColor(Color.BLACK);
						g2d.drawString(String.format("%d월", rs.getInt(1)), 600 - (i * 200), 270);
						i++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				g2d.drawLine(230, 240, 615, 40);
				for (int i = 0; i < height.length; i++) {
					g2d.fillOval(605 - (i * 200), nums[i] + ((i == 0) ? 25 : 35), 15, 15);
				}

				Arrays.sort(val, Collections.reverseOrder());
				for (int j = 0; j < val.length; j++) {
					g2d.drawString(val[j] + "", 2, 40 + (j * 105));
				}
			}
		});

	}

	public static void main(String[] args) {
		new UserInfo(1);
	}
}
