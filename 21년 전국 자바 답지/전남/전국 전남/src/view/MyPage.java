package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyPage extends BaseFrame {
	DefaultTableModel m = model("날짜,시간,카페 이름,테마명,인원수,가격".split(","));
	JTable t = table(m);
	String cap[] = "이름,아이디,비밀번호,생년월일,거주지".split(",");
	JTextField txt[] = { new JTextField(20), new JTextField(20), new JTextField(20), new JTextField(20),
			new JTextField(20) };
	JLabel sumlbl;

	public MyPage() {
		super("마이페이지", 1200, 300);
		setUI();

		setVisible(true);
	}

	void setUI() {
		setLayout(new BorderLayout(5, 5));

		var w = new JPanel(new BorderLayout());
		var w_c = new JPanel(new GridLayout(0, 1));
		var c = new JPanel(new BorderLayout());
		add(w, "West");
		w.add(w_c);
		add(c);

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			tmp.add(sz(lbl(cap[i], JLabel.LEFT), 50, 10));
			tmp.add(txt[i]);
			w_c.add(tmp);

		}

		txt[0].setEnabled(false);
		txt[1].setEnabled(false);

		txt[0].setText(uname);
		txt[1].setText(uid);

		try {
			var rs = stmt.executeQuery("select * from user where u_no = " + uno);
			if (rs.next()) {
				txt[2].setText(rs.getString(3));
				txt[3].setText(rs.getString(5));
				txt[4].setText(rs.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		w.add(btn("수정", a -> {
			try {

				var rs = stmt.executeQuery("select * from user where u_no = " + uno + " and u_pw = '" + txt[2].getText()
						+ "' and u_date" + " = '" + txt[3].getText() + "' and u_address = '" + txt[4].getText() + "'");
				if (rs.next()) {
					eMsg("수정한 내용이 없습니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				var rs = stmt.executeQuery(
						"select * from (select distinct left(c_address,2) as loc from cafe)  as l where l.loc ='"
								+ txt[4].getText().substring(0, 2) + "'");
				if (!rs.next()) {
					eMsg("거주지를 다시 입력해주세요.");
					return;
				}
			} catch (SQLException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			execute("update user set u_pw = '" + txt[2].getText() + "' , u_date='" + txt[3].getText() + "', u_address='"
					+ txt[4].getText() + "' where u_no=" + uno);

			iMsg("회원정보가 수정되었습니다.");

		}), "South");

		c.add(new JScrollPane(t));
		c.add(sumlbl = new JLabel("ㅇㅇ", JLabel.RIGHT), "South");

		addRow(m,
				"select r.r_date, r_time, c.c_name, t.t_name, r.r_people, format(r.r_people*c.c_price,'#,##0') from reservation r, cafe c, theme t where r.u_no = "
						+ uno + " and r.t_no = t.t_no and c.c_no = r.c_no");
		try {
			var rs = stmt.executeQuery(
					"select format(sum(r.r_people*c.c_price),'#,##0') from reservation r, cafe c, theme t where r.u_no = "
							+ uno + " and r.t_no = t.t_no and c.c_no = r.c_no");
			if (rs.next()) {
				sumlbl.setText("총금액:" + rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	public static void main(String[] args) {
		uno = 1;
		new MyPage();
	}
}
