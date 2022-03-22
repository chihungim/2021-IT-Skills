package view;

import java.awt.Color;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ticket extends BaseFrame {

	String serial;

	public Ticket(String serial) {
		super("티켓", 1000, 400);

		try {
			datainit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JLabel lbl = lbl("열차 승차권 - TRAIN TICKET", JLabel.CENTER, 20);

		add(lbl, "North");
		lbl.setOpaque(true);
		lbl.setForeground(Color.WHITE);
		lbl.setBackground(Color.BLUE);
		add(c = new JPanel(null));
		try {
			var rs = stmt.executeQuery("select * from purchase where serial = '" + serial + "'");
			if (rs.next()) {
				var lbl2 = lbl(stNames.get(rs.getInt(3)) + "▶" + stNames.get(rs.getInt(4)), JLabel.CENTER, 20);
				c.add(lbl2);
				lbl2.setBounds(400, 30, 200, 100);

				var dis = rs.getDouble(8) / 10 + "km";
				var date = LocalDate.parse(rs.getString(7)).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
				var time = LocalTime.parse(rs.getString(6)).format(DateTimeFormatter.ofPattern("HH시 mm분 ss초"));
				var price = new DecimalFormat("#,##0원").format(rs.getInt(5));

				var lbl3 = lbl("<html><left>" + dis + "<br>" + date + "<br>" + time, JLabel.LEFT, 13);
				c.add(lbl3);
				var lbl4 = lbl(price, JLabel.CENTER);
				c.add(lbl4);
				lbl4.setBounds(900, 200, 100, 100);
				lbl3.setBounds(20, 200, 200, 200);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setVisible(true);
	}

}
