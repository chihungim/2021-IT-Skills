import java.sql.SQLException;

import javax.swing.JTextField;

public class Account extends BaseDialog {
	String cap[] = "Id,pwd,name,email,point".split(",");
	JTextField txt[] = new JTextField[5];

	public Account() {
		super("계정", 350, 450);

		ui();
		setVisible(true);
	}

	void ui() {
		setLayout(null);

		setBounds(lbl("계정", 0, 30), 10, 30, 80, 30);
		for (int i = 0; i < 5; i++) {
			setBounds(lbl(cap[i], 2), 25, 90 + (i * 50), 40, 30);
			setBounds(txt[i] = new JTextField(), 70, 90 + (i * 50), 250, 30);
		}

		try {
			var rs = stmt.executeQuery("select * from user where id = '" + BaseFrame.u_id + "'");
			if (rs.next()) {
				txt[0].setText(rs.getString(2));
				txt[1].setText(rs.getString(3));
				txt[2].setText(rs.getString(4));
				txt[3].setText(rs.getString(5));
				txt[4].setText(rs.getInt(6) + "");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		setBounds(btn("수정", e -> {
			for (int i = 0; i < cap.length; i++) {
				if (txt[i].getText().contentEquals("") || txt[i].getText().equals(cap[i])) {
					eMsg = new Emsg("공란을 확인해주세요.");
					return;
				}
			}

			execute("update user set pwd = '" + txt[1].getText() + "', name = '" + txt[2].getText() + "', email = '"
					+ txt[3].getText() + "' where id = '" + BaseFrame.u_id + "'");
		}), 25, 350, 140, 30);
		setBounds(btn("취소", e -> dispose()), 180, 350, 140, 30);

		txt[0].setEditable(false);
		txt[4].setEditable(false);
	}

	public static void main(String[] args) {
		BaseFrame.u_id = "asd";
		new Account();
	}
}
