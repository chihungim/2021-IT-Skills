package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class UserManage extends BaseFrame {

	JTextField txt = new JTextField(20);
	DefaultTableModel m = new DefaultTableModel(null, "번호,아이디,비밀번호,이름,생일,휴대전화".split(","));
	JTable t = new JTable(m) {
		public boolean isCellEditable(int row, int column) {
			return column != 0 && column != 1;
		};

	};
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	public UserManage() {
		super("회원 관리", 700, 400);

		this.add(n = new JPanel(), "North");
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");

		n.add(txt);
		n.add(btn("검색", a -> {
			addRow();
		}));

		c.add(new JScrollPane(t));

		addRow();

		s.add(btn("저장", a -> {
			for (int i = 0; i < t.getRowCount(); i++) {

			}
		}));
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < m.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					BaseFrame.uno = rei(t.getValueAt(t.getSelectedRow(), 0));
					new PurchaseList().addWindowListener(new Before(UserManage.this));
				}
			}
		});

		this.setVisible(true);
	}

	void addRow() {
		m.setRowCount(0);
		try {
			var rs = stmt.executeQuery("select * from user where name like '%" + txt.getText() + "%'");
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i + 1);
				}
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new UserManage();
	}
}
