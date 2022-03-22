package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyPage extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(0)), c = new JPanel(new GridLayout());
	JLabel name = new JLabel("회원 : "), nameidx = new JLabel(u_name);
	DefaultTableModel m = model("t_no,날짜,공연명,좌석,금액".split(","));
	JTable t = table(m);
	JScrollPane jsc = new JScrollPane(t);
	JPopupMenu menu = new JPopupMenu();
	JMenuItem item[] = { new JMenuItem("취소"), new JMenuItem("수정") };
	int price = 0;

	public MyPage() {
		super("마이페이지", 600, 500);

		ui();
		data();
		event();
	}

	void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e == null)
					return;
				// TODO Auto-generated method stub
				if (e.getButton() == 3) {
					menu.show(t, e.getX(), e.getY());
				}
			}
		});

		item[1].addActionListener(a -> {
			try {
				ResultSet rs = stmt.executeQuery(
						"SELECT p_date, p_place FROM user u, perform p, ticket t where u.u_no = t.u_no and p.p_no = t.p_no and curdate() >= '"
								+ t.getValueAt(t.getSelectedRow(), 1) + "'");
				if (rs.next()) {
					eMsg("수정이 불가합니다.");
					return;
				} else {
					bool = true;
					date = (String) t.getValueAt(t.getSelectedRow(), 1);
					p_name = (String) t.getValueAt(t.getSelectedRow(), 2);
					totalidx.setText(t.getValueAt(t.getSelectedRow(), 4) + "");
					new Stage().addWindowListener(new before(MyPage.this));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		item[0].addActionListener(a -> {
			iMsg("취소되었습니다.");
//			execute("delete from perform where p_name = '" + t.getValueAt(t.getSelectedRow(), 2) + "' and p_date = '"
//					+ t.getValueAt(t.getSelectedRow(), 1) + "'");
			return;
		});
	}

	void data() {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		addrow(m,
				"SELECT t_no, p_date, p_name, t_seat, format(p_price, '#,##0') FROM user u, perform p, ticket t where u.u_no = t.u_no and p.p_no = t.p_no and u.u_name = '"
						+ nameidx.getText() + "' order by p.p_date asc");

	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");

		n.add(name);
		n.add(nameidx);
		c.add(jsc);
		menu.add(item[0]);
		menu.add(item[1]);

		c.setBorder(new EmptyBorder(0, 15, 15, 15));
		name.setFont(new Font("굴림", Font.BOLD, 20));
		nameidx.setFont(new Font("굴림", Font.BOLD, 20));
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
	}

	public static void main(String[] args) {
		u_no = 1;
		u_name = "강찬석";
		new MyPage().setVisible(true);
	}
}
