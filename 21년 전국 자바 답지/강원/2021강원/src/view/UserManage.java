package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class UserManage extends JPanel {
	JTextField txt = new BaseFrame.TextHolder("", 15);

	DefaultTableModel m = BaseFrame.model("순번,아이디,비밀번호,성명,이메일,포인트,예매수".split(","));
	JTable t = BaseFrame.table(m, SwingConstants.CENTER);
	JScrollPane jsc = new JScrollPane(t);
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item = new JMenuItem("예매 조회");
	boolean isEdited = false;

	public UserManage() {
		ui();
		event();
		search("");
	}

	void event() {
		t.setComponentPopupMenu(pop);
		item.addActionListener(e -> {
//			new UserInfo(BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
		});
	}

	void ui() {
		setLayout(new BorderLayout());

		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		n.add(BaseFrame.lbl("사용자 관리", JLabel.RIGHT, 20), "West");
		n.add(n_c);

		n_c.add(txt);
		n_c.add(BaseFrame.btn("사용자 조회", e -> search(txt.getText())));
		add(n, "North");
		add(new JScrollPane(t));
		add(s, "South");
		s.add(BaseFrame.btn("저장", e -> save()));
		s.add(BaseFrame.btn("삭제", e -> delete()));

		setBorder(new EmptyBorder(20, 20, 20, 20));
	}

	void delete() {
		BaseFrame.iMsg("삭제를 완료하였습니다.");
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
			BaseFrame.iMsg("수정내용을 저장 완료하였습니다.");
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

	void search(String name) {
		m.setRowCount(0);
		BaseFrame.addRow(
				"select u.no, u.id, u.pwd, u.name, u.email, u.point, count(r.user_no) from user u, reservation r where r.user_no = u.no and name like '%"
						+ name + "%' group by user_no order by u.no",
				m);
	}

}
