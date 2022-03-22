package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import db.DB;
import db.DBManager;
import util.Util;

public class MyPage extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel w = new JPanel(new BorderLayout()), c = new JPanel(new BorderLayout());
	JPanel w_n = new JPanel(new BorderLayout()), w_c = new JPanel(new FlowLayout(1, 0, 15));
	JPanel c_n = new JPanel(), c_c = new JPanel(new FlowLayout(2));
	JPanel w_n_w = new JPanel(new GridLayout(0, 1, 20, 20)), w_n_e = new JPanel(new GridLayout(0, 1, 20, 20));
	JLabel lbl[] = { Util.lbl("이름", 2, 12), Util.lbl("아이디", 2, 12), Util.lbl("비밀번호", 2, 12), Util.lbl("생년월일", 2, 12), Util.lbl("거주지", 2, 12),
			Util.lbl("총 금액 : ", 4, 12), Util.lbl("", 4, 12) };
	JTextField txt[] = { new JTextField(16), new JTextField(16), new JTextField(16), new JTextField(16),
			new JTextField(16) };
	JButton btn = Util.btn("수정", a -> modify());
	DefaultTableModel m = new DefaultTableModel(null, "날짜,시간,카페 이름,테마명,인원수,가격".split(",") ) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	JTable t = new JTable(m);
	JScrollPane jsc = new JScrollPane(t);
	boolean bool;

	public MyPage() {
		super("마이페이지", 1200, 300);

		ui();
		data();
		setVisible(true);
	}

	void data() {
		txt[0].setEnabled(false);
		txt[1].setEnabled(false);
		txt[0].setText(uname);
		txt[1].setText(uid);

		for (int i = 2; i < 5; i++) {
			txt[i].addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					bool = true;
				}
			});
		}
	}

	void rowData() {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		Util.addRow(m,
				"SELECT r_date, r_time, c_name, t_name, t_personnel, format(c_price*t_personnel, '#,##0') FROM reservation r, cafe c, theme t where r.c_no = c.c_no and r.t_no = t.t_no and r.u_no = "
						+ uno + " group by c.c_no", 0);

		try {
			ResultSet rs = DBManager.rs(
					"SELECT sum(c_price*t_personnel) FROM theme t, roomescape.cafe c, reservation r where c.c_no = r.c_no and t.t_no = r.t_no and r.u_no = '"
							+ uno + "'");
			if (rs.next()) {
				DecimalFormat format = new DecimalFormat("#,##0");
				lbl[6].setText(format.format(rs.getInt(1)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void modify() {
		if (txt[2].getText().equals("") || txt[3].getText().equals("") || txt[4].getText().equals("")) {
			Util.eMsg("빈칸이 있습니다.");
			return;
		} else if (bool == false) {
			Util.eMsg("수정한 내용이 없습니다.");
			return;
		}

		Util.iMsg("회원정보가 수정되었습니다.");
		DBManager.execute("update user set u_pw = '" + txt[2].getText() + "' and u_date = '" + txt[3].getText()
				+ "' and u_address = '" + txt[4].getText() + "' where u_id = '" + txt[1].getText() + "'");
	}

	void ui() {
		add(mainp);
		mainp.add(w, "West");
		mainp.add(c, "Center");
		w.add(w_n, "North");
		w.add(w_c, "Center");
		c.add(c_n, "North");
		c.add(c_c, "Center");
		w_n.add(w_n_w, "West");
		w_n.add(w_n_e, "East");

		for (int i = 0; i < 5; i++) {
			w_n_w.add(lbl[i]);
			w_n_e.add(txt[i]);
		}
		w_c.add(btn);
		c_n.add(jsc);
		c_c.add(lbl[5]);
		c_c.add(lbl[6]);

		w.setBorder(new EmptyBorder(0, 10, 0, 0));
		jsc.setPreferredSize(new Dimension(900, 220));
		btn.setPreferredSize(new Dimension(250, 30));

		rowData();
	}

}
