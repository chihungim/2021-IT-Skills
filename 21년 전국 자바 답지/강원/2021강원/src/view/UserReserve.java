package view;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

public class UserReserve extends BaseDialog {

	DefaultTableModel m = BaseFrame.model("no,출발지,도착지,도착시간,출발날짜,실제번호임ㅋ".split(","));
	JTable t = BaseFrame.table(m, SwingConstants.CENTER);
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public UserReserve() {
		super("예매조회", 600, 400);
		data();
		ui();
	}

	void data() {
		m.setRowCount(0);
		try {
			var rs = stmt.executeQuery("SELECT \r\n" + "\r\n" + "    l1.name,\r\n" + "    l2.name,\r\n"
					+ "    TIME(DATE_ADD(s.date,\r\n" + "        INTERVAL s.elapsed_time HOUR_SECOND)),\r\n"
					+ "    s.date,\r\n" + "    r.no\r\n" + "FROM\r\n" + "    reservation r,\r\n" + "    schedule s,\r\n"
					+ "    ll2 l1,\r\n" + "    ll2 l2\r\n" + "WHERE\r\n" + "    r.schedule_no = s.no\r\n"
					+ "        AND s.departure_location2_no = l1.no\r\n"
					+ "        AND s.arrival_location2_no = l2.no\r\n" + "        and r.user_no = " + BaseFrame.uno
					+ "\r\n" + "GROUP BY r.no order by date asc");

			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				row[0] = rs.getRow();
				row[1] = rs.getString(1);
				row[2] = rs.getString(2);
				row[3] = rs.getString(3);
				row[4] = rs.getString(4);
				row[5] = rs.getString(5);
				m.addRow(row);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	void ui() {
		add(lbl("예매 조회", JLabel.LEFT, 20), "North");
		add(new JScrollPane(t));
		var pop = new JPopupMenu();
		var item = new JMenuItem("취소");
		pop.add(item);
		t.setComponentPopupMenu(pop);
		item.addActionListener(a -> {
			String date = (String) t.getValueAt(t.getSelectedRow(), 4);
			String time = (String) t.getValueAt(t.getSelectedRow(), 3);
			var e = LocalTime.parse(time);
			var s = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			if (s.plusSeconds(e.toSecondOfDay()).toLocalDate().toEpochDay() < LocalDate.now().toEpochDay()) {
				BaseFrame.eMsg("취소가 불가능한 일정입니다.");
				return;
			}

			var end = s.plusSeconds(e.toSecondOfDay()).toLocalTime();
			var r = end.minusSeconds(s.toLocalTime().toSecondOfDay());
			var point = 700;
			switch (r.getHour()) {
			case 1:
			case 0:
				point = 100;
				break;
			case 2:
				point = 300;
				break;
			case 3:
				point = 500;
				break;

			}

			execute("delete from reservation where no = " + t.getValueAt(t.getSelectedRow(), 5));
			execute("update user set point = point + " + point + " where no = " + BaseFrame.uno);
			BaseFrame.iMsg("예매 취소가 완료되었습니다.");
			data();
		});

		t.getColumnModel().getColumn(0).setMaxWidth(60);
		t.getColumnModel().getColumn(0).setMinWidth(60);

		t.getColumnModel().getColumn(5).setMinWidth(0);
		t.getColumnModel().getColumn(5).setMaxWidth(0);

	}

}
