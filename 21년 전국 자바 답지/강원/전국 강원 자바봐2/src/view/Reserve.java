package view;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Reserve extends BaseFrame {
	DefaultTableModel m = model("no,�����,������,�����ð�,��߳�¥,date".split(","));
	JTable t = table(m);
	JLabel title;
	JPopupMenu menu = new JPopupMenu();
	JMenuItem item = new JMenuItem("���");

	public Reserve() {
		super("���� ��ȸ", 650, 550);
		setUI();
		event();
		setVisible(true);
	}

	private void setUI() {
		setLayout(new BorderLayout(5, 5));
		add(title = lbl("������ȸ", JLabel.LEFT, 30), "North");
		add(new JScrollPane(t));
		t.getColumnModel().getColumn(0).setMinWidth(80);
		t.getColumnModel().getColumn(0).setMaxWidth(80);
		t.getColumnModel().getColumn(5).setMinWidth(0);
		t.getColumnModel().getColumn(5).setMaxWidth(0);
		rowData();
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));

	}

	void rowData() {
		try {
			var rs = stmt.executeQuery(
					"select a.name, b.name, date_add(date, interval elapsed_time HOUR_SECOND), date, r.no from reservation r inner join schedule s on r.schedule_no = s.no inner join a on s.departure_location2_no = a.no inner join a as b on s.arrival_location2_no = b.no where user_no = "
							+ u_no + " order by date asc");
			int i = 1;
			while (rs.next()) {
				Object row[] = { i, rs.getString(1), rs.getString(2), rs.getString(3).split(" ")[1], rs.getString(4),
						rs.getString(5) };
				m.addRow(row);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		menu.add(item);
		t.setComponentPopupMenu(menu);
	}

	void event() {
		if (t.getSelectedRow() == -1)
			return;
		item.addActionListener(a -> {
			String date = (String) t.getValueAt(t.getSelectedRow(), 4);
			if (LocalDate.parse(date.split(" ")[0]).toEpochDay() < LocalDate.now().toEpochDay()) {
				emsg = new eMsg("��� �Ұ����� �����Դϴ�.");
				return;
			}

			imsg = new iMsg("���� ��Ұ� �Ϸ�Ǿ����ϴ�.");
			execute("delete from reservation where no = " + t.getValueAt(t.getSelectedRow(), 5));
			m.setNumRows(0);
			rowData();
		});
	}

	public static void main(String[] args) {
		u_no = 1;
		new Reserve();
	}

}
