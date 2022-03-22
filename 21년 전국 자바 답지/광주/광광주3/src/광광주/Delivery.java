package 광광주;

import java.awt.FlowLayout;
import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Delivery extends BaseFrame {

	DefaultTableModel m = model("주문번호,주문시작,음식점위치,배달위치".split(","));
	JTable t = table(m);
	ArrayList<Integer> deliveryList = new ArrayList<>();
	ArrayList<Integer> chosenList = new ArrayList<>();

	int ux, uy, sx, sy;

	public Delivery() {
		super("딜리버리", 700, 500);
		var s = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(lbl("내 배달", JLabel.LEFT, 20), "North");
		add(new JScrollPane(t));

		add(s, "South");
		s.add(btn("새로운 배달 받기", a -> {
			randomlyAddRow();
		}));

		s.add(btn("배달 출발", a -> {
//			int reserveno = toInt(t.getValueAt(t.getSelectedRow(), 0).toString());
			Point sPos = new Point(toInt(t.getValueAt(t.getSelectedRow(), 2).toString().split(",")[0]),
					toInt(t.getValueAt(t.getSelectedRow(), 2).toString().split(",")[1]));
			Point uPos = new Point(toInt(t.getValueAt(t.getSelectedRow(), 3).toString().split(",")[0]),
					toInt(t.getValueAt(t.getSelectedRow(), 3).toString().split(",")[1]));
			Point rPos = null;
			try {
				var rs = stmt.executeQuery(
						"select m.x, m.y from map m, rider r where m.no = r.map and r.no = '" + rno + "'");
				if (rs.next()) {
					rPos = new Point(rs.getInt(1), rs.getInt(2));
				}

				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Map m = new Map(this, uPos, rPos, sPos, rno);

			m.setVisible(true);
		}));

		setUP();

		setVisible(true);
	}

	void setUP() {
		try {
			var rs = stmt.executeQuery("SELECT * FROM delivery d, receipt r where d.receipt = r.No and d.rider = '"
					+ rno + "' and r.status = 2");
			while (rs.next()) {
				deliveryList.add(rs.getInt(5));
				chosenList.add(rs.getInt(5));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Integer no : deliveryList) {

			try {
				var rs = stmt.executeQuery("SELECT \r\n" + "    r.NO, r.RECEIPT_TIME, m.X, m.Y, u.us, u.uy\r\n"
						+ "FROM\r\n" + "    receipt r,\r\n" + "    map m,\r\n" + "    seller s,\r\n"
						+ "    (SELECT \r\n" + "        r.no rno, m.x us, m.y uy\r\n" + "    FROM\r\n"
						+ "        map m, receipt r, user u\r\n" + "    WHERE\r\n"
						+ "        m.NO = u.MAP and r.USER = u.NO) u\r\n" + "WHERE\r\n"
						+ "    r.SELLER = s.NO AND m.NO = s.MAP and r.NO = u.rno and r.STATUS = 2 and r.no=" + no);
				Object row[] = new Object[m.getColumnCount()];
				if (rs.next()) {
					row[0] = rs.getInt(1);
					row[1] = rs.getString(2);
					row[2] = rs.getString(3) + "," + rs.getString(4);
					row[3] = rs.getString(5) + "," + rs.getString(6);
					m.addRow(row);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			var rs = stmt.executeQuery("SELECT \r\n" + "    r.NO, r.RECEIPT_TIME, m.X, m.Y, u.us, u.uy\r\n" + "FROM\r\n"
					+ "    receipt r,\r\n" + "    map m,\r\n" + "    seller s,\r\n" + "    (SELECT \r\n"
					+ "        r.no rno, m.x us, m.y uy\r\n" + "    FROM\r\n" + "        map m, receipt r, user u\r\n"
					+ "    WHERE\r\n" + "        m.NO = u.MAP and r.USER = u.NO) u\r\n" + "WHERE\r\n"
					+ "    r.SELLER = s.NO AND m.NO = s.MAP and r.NO = u.rno and r.status = 1");
			while (rs.next()) {
				if (deliveryList.contains(rs.getInt(1)))
					continue;
				deliveryList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void randomlyAddRow() {
		if (deliveryList.size() == 0) {
			eMsg("배정 가능한 주문건이 없습니다.");
			return;
		}

		Random rand = new Random();
		int no = deliveryList.get(rand.nextInt(deliveryList.size()));
		while (true) {
			if (chosenList.contains(no)) {
				no = deliveryList.get(rand.nextInt(deliveryList.size()));
			} else
				break;

			if (chosenList.size() == deliveryList.size()) {
				eMsg("배정 가능한 주문건이 없습니다.");
				return;
			}
		}

		chosenList.add(no);

		try {
			var rs = stmt.executeQuery("SELECT \r\n" + "    r.NO, r.RECEIPT_TIME, m.X, m.Y, u.us, u.uy\r\n" + "FROM\r\n"
					+ "    receipt r,\r\n" + "    map m,\r\n" + "    seller s,\r\n" + "    (SELECT \r\n"
					+ "        r.no rno, m.x us, m.y uy\r\n" + "    FROM\r\n" + "        map m, receipt r, user u\r\n"
					+ "    WHERE\r\n" + "        m.NO = u.MAP and r.USER = u.NO) u\r\n" + "WHERE\r\n"
					+ "    r.SELLER = s.NO AND m.NO = s.MAP and r.NO = u.rno and r.STATUS = 1 and r.no=" + no);
			Object row[] = new Object[m.getColumnCount()];

			if (rs.next()) {
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3) + "," + rs.getString(4);
				row[3] = rs.getString(5) + "," + rs.getString(6);
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		execute("update receipt set status = 2 where no = " + no);
	}

	public static void main(String[] args) {
		rno = 3;
		new Delivery();
	}
}
