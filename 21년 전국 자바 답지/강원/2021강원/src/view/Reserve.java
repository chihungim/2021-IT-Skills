package view;

import java.awt.Component;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class Reserve extends BaseDialog {
	DefaultTableModel m = BaseFrame.model("no,�����,������,��߽ð�,�����ð�, ,��¥,��ȣ,�ð�".split(","));
	JTable t = BaseFrame.table(m, SwingConstants.CENTER);
	JScrollPane jsc = new JScrollPane(t);

	TableButton tableButton = new TableButton();
	int depart, arrive;
	String date;

	public Reserve(int depart, int arrive, String date) {
		super("��������", 900, 500);
		this.depart = depart;
		this.arrive = arrive;
		this.date = date;
		ui();
		setVisible(true);
	}

	void ui() {
		setLayout(null);

		add(lbl("����", JLabel.LEFT, 20), "North");
		add(new JScrollPane(t));

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
					"select a.name, b.name, date, date_add(date, interval elapsed_time hour_second), s.no, elapsed_time from schedule s, ll2 a,ll2 b where s.departure_location2_no = a.no and s.arrival_location2_no = b.no and departure_location2_no = "
							+ depart + " and arrival_location2_no = " + arrive + " and date regexp '" + date + "*'");
			while (rs.next()) {
				Object row[] = { rs.getRow(), rs.getString(1), rs.getString(2), rs.getString(3).split(" ")[1],
						rs.getString(4).split(" ")[1], new JButton("����"), rs.getString(3).split(" ")[0],
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
			btn = new JButton("����");

			btn.addActionListener(e -> {
				int yn = JOptionPane.showConfirmDialog(null,
						"[�̵��ð� ���� �ȳ�]\n1�ð� ���� 100Point,\n2�ð� ���� 300Point,\n3�ð� ���� 500Point,\n�̿� 700Point����\n\n���Ÿ� �����Ͻðڽ��ϱ�?",
						"�ȳ�", JOptionPane.YES_NO_OPTION);

				if (yn == JOptionPane.YES_OPTION) {
					if (LocalDate.parse(t.getValueAt(t.getSelectedRow(), 6) + "").toEpochDay() < LocalDate.now()
							.toEpochDay()) {
						BaseFrame.eMsg("������ �� ���� �����Դϴ�.");
						return;
					}

					try {
						var rs = stmt.executeQuery("select count(*) from reservation where schedule_no = "
								+ BaseFrame.toInt(t.getValueAt(t.getSelectedColumn(), 7)));
						if (rs.getInt(1) == 25) {

							BaseFrame.eMsg("�ش� ������ �¼��� ��� �����Ǿ����ϴ�.");
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
						var rs = stmt.executeQuery("select point from user where no = '" + BaseFrame.uno + "'");
						if (rs.next()) {
							u_point = rs.getInt(1);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (point > u_point) {
						BaseFrame.eMsg("����Ʈ�� �����մϴ�.");
						return;
					}

					BaseFrame.iMsg("���Ű� �Ϸ�Ǿ����ϴ�.");
					execute("update user set point = point - " + point + " where no = " + BaseFrame.uno);
					execute("insert into reservation values(0, " + BaseFrame.uno + ",'"
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

}
