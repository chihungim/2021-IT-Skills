import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Scehdule extends JPanel {
	DefaultTableModel m = BaseFrame.model("순번,출발지,도착지,출발날짜,이동시간".split(","));
	JTable t = BaseFrame.table(m);
	JScrollPane jsc = new JScrollPane(t);

	JPopupMenu pop = new JPopupMenu();
	JPanel p1 = BaseFrame.gPanel(0, 1), p2 = BaseFrame.gPanel(0, 1);
	JPanel menuP = BaseFrame.gPanel(1, 0);
	JScrollPane jsc1 = new JScrollPane(p1);
	JScrollPane jsc2 = new JScrollPane(p2);
	JButton btn[] = new JButton[16], btn2[];

	int cnt = -1;
	boolean isEdited;

	public Scehdule() {
		ui();
		event();
	}

	void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3 && (t.getSelectedColumn() == 1 || t.getSelectedColumn() == 2)) {
					cnt = t.getSelectedColumn();
					pop.show(t, e.getX(), e.getY());
				}
			}
		});

		for (int i = 0; i < btn.length; i++) {
			int idx = i;
			btn[i].addActionListener(e -> {
				p2.removeAll();

				try {
					var rs = BaseFrame.stmt.executeQuery("select * from location2 where location_no = " + (idx + 1));
					rs.last();
					btn2 = new JButton[rs.getRow()];
					rs.beforeFirst();

					int j = 0;
					while (rs.next()) {
						p2.add(btn2[j] = new JButton(rs.getString(2)));
						btn2[j].addActionListener(e2 -> {
							JButton btn = (JButton) e2.getSource();
							String text = this.btn[idx].getText() + " " + btn.getText();
							t.setValueAt(text, t.getSelectedRow(), cnt);
							isEdited = true;
						});
						j++;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				repaint();
				revalidate();
			});
		}
	}

	void ui() {
		setLayout(null);

		pop.add(menuP);
		menuP.add(jsc1);
		menuP.add(jsc2);

		BaseFrame.size(pop, 240, 280);

		BaseFrame.setBounds(this, BaseFrame.lbl("일정 관리", 0, 25), 0, 20, 150, 30);
		BaseFrame.setBounds(this, jsc, 20, 70, 930, 500);
		BaseFrame.setBounds(this, BaseFrame.btn("저장", e -> save()), 700, 600, 120, 30);
		BaseFrame.setBounds(this, BaseFrame.btn("삭제", e -> delete()), 830, 600, 120, 30);

		try {
			var rs = BaseFrame.stmt.executeQuery("select * from location");
			int i = 0;
			while (rs.next()) {
				p1.add(btn[i] = new JButton(rs.getString("name")));
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		rowData();
	}

	void rowData() {
		m.setRowCount(0);

		BaseFrame.addRow(m,
				"select s.no, a.name, b.name, date, elapsed_time from schedule s, a a, a b where s.departure_location2_no = a.no and s.arrival_location2_no = b.no order by s.no");
	}

	void delete() {
		BaseFrame.eMsg = new BaseFrame.Emsg("삭제를 완료하였습니다.");
		BaseFrame.execute("delete from schedule where no = " + t.getValueAt(t.getSelectedRow(), 0));
	}

	void save() {
		if (isEdited) {
			BaseFrame.iMsg = new BaseFrame.Imsg("수정내용을 저장 완료하였습니다.");
			for (int i = 0; i < t.getRowCount(); i++) {
				int depart = BaseFrame
						.toInt(BaseFrame.getOne("select no from a where name = '" + t.getValueAt(i, 1) + "'"));
				int arrive = BaseFrame
						.toInt(BaseFrame.getOne("select no from a where name = '" + t.getValueAt(i, 2) + "'"));
				BaseFrame.execute("update schedule set departure_location2_no = " + depart + ", arrival_location2_no = "
						+ arrive + " where no = " + BaseFrame.toInt(t.getValueAt(i, 0)));
			}
		}
	}
}
