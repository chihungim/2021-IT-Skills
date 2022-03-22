import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class Reserve extends BaseDialog {
	JPanel n, c;

	JLabel lbl = new JLabel("예매조회", 0);

	DefaultTableModel m = model("no,출발지,도착지,도착시간,출발날짜,date".split(","));
	JTable t = table(m);
	JScrollPane jsc = new JScrollPane(t);

	JPopupMenu pop = new JPopupMenu();
	JMenuItem item = new JMenuItem("취소");

	public Reserve() {
		super("예매조회", 700, 650);

		ui();
		event();
	}

	void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3) {
					pop.show(t, e.getX(), e.getY());
				}
			}
		});

		item.addActionListener(e -> {
			String date = (String) t.getValueAt(t.getSelectedRow(), 4);
			if (LocalDate.parse(date.split(" ")[0]).toEpochDay() < LocalDate.now().toEpochDay()) {
				eMsg = new Emsg("취소 불가능한 일정입니다.");
				return;
			}

			execute("delete from reservation where no = " + t.getValueAt(t.getSelectedRow(), 5));
			iMsg = new Imsg("예매 취소가 완료되었습니다.");
			m.setNumRows(0);
			rowData();
		});
	}

	void ui() {
		add(n = new JPanel(new FlowLayout(0, 20, 20)), "North");
		add(c = new JPanel());

		n.add(lbl);
		c.add(jsc);

		pop.add(item);

		rowData();

		t.getColumnModel().getColumn(0).setPreferredWidth(20);
		t.getColumnModel().getColumn(5).setMinWidth(0);
		t.getColumnModel().getColumn(5).setMaxWidth(0);
		jsc.setPreferredSize(new Dimension(650, 500));
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, 30));
	}

	void rowData() {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			var rs = stmt.executeQuery(
					"select a.name, b.name, date_add(date, interval elapsed_time hour_second), date, r.no from reservation r, schedule s, a a, a b where r.schedule_no = s.no and s.departure_location2_no = a.no and b.no = s.arrival_location2_no and user_no = "
							+ BaseFrame.u_no + " order by date asc");
			while (rs.next()) {
				Object row[] = { rs.getRow(), rs.getString(1), rs.getString(2), rs.getString(3).split(" ")[1],
						rs.getString(4), rs.getString(5) };
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
