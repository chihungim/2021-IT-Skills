import java.awt.Component;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class BusReserve extends BaseDialog {
	DefaultTableModel m = model("no,출발지,도착지,출발시간,도착시간, ,날짜,번호,시간".split(","));
	JTable t = table(m);
	JScrollPane jsc = new JScrollPane(t);

	TableButton tableButton = new TableButton();
	int depart, arrive;
	String date;

	public BusReserve(int depart, int arrive, String date) {
		super("버스예매", 900, 500);
		this.depart = depart;
		this.arrive = arrive;
		this.date = date;
		ui();
		setVisible(true);
	}

	void ui() {
		setLayout(null);

		setBounds(lbl("예매", 0, 30), 10, 20, 80, 30);
		setBounds(jsc, 10, 70, 850, 350);

		t.getColumnModel().getColumn(5).setCellRenderer(tableButton);
		t.getColumnModel().getColumn(5).setCellEditor(tableButton);
		t.getColumnModel().getColumn(6).setMinWidth(0);
		t.getColumnModel().getColumn(6).setMaxWidth(0);
		t.getColumnModel().getColumn(7).setMinWidth(0);
		t.getColumnModel().getColumn(7).setMaxWidth(0);
		t.getColumnModel().getColumn(8).setMinWidth(0);
		t.getColumnModel().getColumn(8).setMaxWidth(0);
		t.setRowHeight(25);

		try {
			var rs = stmt.executeQuery(
					"select a.name, b.name, date, date_add(date, interval elapsed_time hour_second), s.no, elapsed_time from schedule s, a a, a b where s.departure_location2_no = a.no and s.arrival_location2_no = b.no and departure_location2_no = "
							+ depart + " and arrival_location2_no = " + arrive + " and date regexp '" + date + "*'");
			while (rs.next()) {
				Object row[] = { rs.getRow(), rs.getString(1), rs.getString(2), rs.getString(3).split(" ")[1],
						rs.getString(4).split(" ")[1], new JButton("예매"), rs.getString(3).split(" ")[0],
						rs.getString(5), rs.getString(6) };
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class TableButton extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		JButton btn;

		public TableButton() {
			btn = new JButton("예매");

			btn.addActionListener(e -> {
				int yn = JOptionPane.showConfirmDialog(null,
						"[이동시간 가격 안내]\n1시간 이하 100Point,\n2시간 이하 300Point,\n3시간 이하 500Point,\n이외 700Point차감\n\n예매를 진행하시겠습니까?",
						"안내", JOptionPane.YES_NO_OPTION);

				if (yn == JOptionPane.YES_OPTION) {
					if (LocalDate.parse(t.getValueAt(t.getSelectedRow(), 6) + "").toEpochDay() < LocalDate.now()
							.toEpochDay()) {
						eMsg = new Emsg("예매할 수 없는 일정입니다.");
						return;
					}

					try {
						var rs = stmt.executeQuery("select * from reservation where schedule_no = "
								+ toInt(t.getValueAt(t.getSelectedColumn(), 7)));
						rs.last();
						if (rs.getRow() == 25) {
							eMsg = new Emsg("해당 일정에 좌석이 모두 매진되었습니다.");
							return;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					int point = 0;

					if (LocalTime.parse(t.getValueAt(t.getSelectedRow(), 8) + "").getHour() <= 1)
						point = 100;

					if (LocalTime.parse(t.getValueAt(t.getSelectedRow(), 8) + "").getHour() <= 2)
						point = 300;

					if (LocalTime.parse(t.getValueAt(t.getSelectedRow(), 8) + "").getHour() <= 3)
						point = 500;

					if (LocalTime.parse(t.getValueAt(t.getSelectedRow(), 8) + "").getHour() > 3)
						point = 500;

					int u_point = 0;

					try {
						var rs = stmt.executeQuery("select point from user where id = '" + BaseFrame.u_id + "'");
						if (rs.next()) {
							u_point = rs.getInt(1);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (point > u_point) {
						eMsg = new Emsg("포인트가 부족합니다.");
						return;
					}

					iMsg = new Imsg("예매가 완료되었습니다.");
					execute("update user set point = point - " + point + " where no = " + BaseFrame.u_no);
					execute("insert into reservation values(0, " + BaseFrame.u_no + ",'"
							+ t.getValueAt(t.getSelectedRow(), 7) + "')");
				}
			});
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			// TODO Auto-generated method stub
			return btn;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			// TODO Auto-generated method stub
			return btn;
		}
	}

	public static void main(String[] args) {
		BaseFrame.u_no = 1;
		BaseFrame.u_id = "VRIdflpkE686";
		new BusReserve(2, 97, "2021-09-13");
	}

}
