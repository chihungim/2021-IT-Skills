package View;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StationInfo extends BaseFrame {

	int stationNum = 1; // 임시

	JComboBox<String> box;
	JLabel info;

	public StationInfo() {
		super("역정보", 400, 600);

		var n = new JPanel(new BorderLayout());

		add(n, "North");
		n.add(info = lbl("신정네거리 <- 까치산 -> 종점",0,20),"North");
		n.add(box = new JComboBox<String>());

		try {
			ResultSet rs = stmt.executeQuery(
					"select * from route, metro, station where route.metro = metro.serial and station.serial = route.station and station="
							+ stationNum);
			while (rs.next()) {
				box.addItem(rs.getString(5));
			}
			//쿼리가 마땅히 생각이 안나..

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
	}

	class TimeLine extends JPanel {

	}

	public static void main(String[] args) {
		new StationInfo();
	}
}
