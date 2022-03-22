import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class UserManage extends JPanel {
	JTextField txt = new JTextField();

	DefaultTableModel m = BaseFrame.model("����,���̵�,��й�ȣ,����,�̸���,����Ʈ,���ż�".split(","));
	JTable t = BaseFrame.table(m);
	JScrollPane jsc = new JScrollPane(t);

	JPopupMenu pop = new JPopupMenu();
	JMenuItem item = new JMenuItem("���� ��ȸ");

	boolean isEdited = false;

	public UserManage() {
		ui();
		event();
		search("");
	}

	void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3) {
					pop.show(t, e.getX(), e.getY());
					t.setRowSelectionInterval(t.rowAtPoint(e.getPoint()), t.rowAtPoint(e.getPoint()));
				}
			}
		});

		item.addActionListener(e -> {
			new UserInfo(BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
		});
	}

	void ui() {
		setLayout(null);

		BaseFrame.setBounds(this, BaseFrame.lbl("����� ����", 2, 25), 10, 20, 150, 30);
		BaseFrame.setBounds(this, BaseFrame.txt(txt, "����"), 700, 20, 130, 30);
		BaseFrame.setBounds(this, BaseFrame.btn("����� ��ȸ", e -> search(txt.getText())), 840, 20, 100, 30);
		BaseFrame.setBounds(this, jsc, 10, 70, 930, 500);
		BaseFrame.setBounds(this, BaseFrame.btn("����", e -> save()), 690, 600, 120, 30);
		BaseFrame.setBounds(this, BaseFrame.btn("����", e -> delete()), 820, 600, 120, 30);
	}

	void search(String name) {
		if (name.equals("����")) {
			return;
		}

		BaseFrame.dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		m.setRowCount(0);
		BaseFrame.addRow(m,
				"select u.no, u.id, u.pwd, u.name, u.email, u.point, count(r.user_no) from user u, reservation r where r.user_no = u.no and name like '%"
						+ name + "%' group by user_no order by u.no");
	}

	void delete() {
		BaseFrame.iMsg = new BaseFrame.Imsg("������ �Ϸ��Ͽ����ϴ�.");
		BaseFrame.execute("delete from user where no = " + t.getValueAt(t.getSelectedRow(), 0));
		search("");
	}

	void save() {
		t.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				isEdited = true;
			}
		});

		if (isEdited) {
			BaseFrame.iMsg = new BaseFrame.Imsg("���������� ���� �Ϸ��Ͽ����ϴ�.");
			for (int i = 0; i < t.getRowCount(); i++) {
				int idx = i;

				try {
					var rs = BaseFrame.stmt
							.executeQuery("select * from user where no = '" + t.getValueAt(idx, 0) + "'");
					if (rs.next()) {
						for (int j = 0; j < 6;) {
							BaseFrame.execute("update user set id = '" + t.getValueAt(idx, 1) + "', pwd = '"
									+ t.getValueAt(idx, 2) + "', name = '" + t.getValueAt(idx, 3) + "', email = '"
									+ t.getValueAt(idx, 4) + "', point = '" + t.getValueAt(idx, 5) + "' where no = "
									+ t.getValueAt(idx, 0));
							return;
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			isEdited = false;
		}
	}

	public static void main(String[] args) {
		new AdminMain();
	}
}
