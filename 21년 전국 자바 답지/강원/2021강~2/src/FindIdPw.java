import java.sql.SQLException;

import javax.swing.JTextField;

public class FindIdPw extends BaseDialog {
	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15),
			new JTextField(15) };
	String cap[] = "Name,E-mail,Name,Id,E-mail".split(",");

	public FindIdPw() {
		super("아이디/비밀번호 찾기", 500, 600);

		ui();
	}

	void ui() {
		setLayout(null);

		setBounds(lbl("아이디 찾기", 0, 30), 20, 40, 180, 30);
		for (int i = 0; i < 2; i++) {
			setBounds(txt(txt[i], cap[i]), 30, 85 + i * 50, 400, 30);
		}
		setBounds(btn("계속", e -> {
			if (txt[0].getText().contentEquals("") || txt[0].getText().equals("Name")) {
				eMsg = new Emsg("공란을 확인해주세요.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select id, name, email from user where name = '" + txt[0].getText()
						+ "' and email = '" + txt[1].getText() + "'");
				if (rs.next()) {
					iMsg = new Imsg("귀하의 Id는 " + rs.getString(1) + "입니다.");
					return;
				} else {
					eMsg = new Emsg("존재하지 않는 정보입니다.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 30, 180, 400, 35);
		setBounds(lbl("비밀번호 찾기", 0, 30), 20, 240, 200, 30);
		for (int i = 2; i < 5; i++) {
			setBounds(txt(txt[i], cap[i]), 30, 200 + i * 50, 400, 30);
		}
		setBounds(btn("계속", e -> {
			if (txt[2].getText().contentEquals("") || txt[2].getText().equals("Name")
					|| txt[3].getText().contentEquals("") || txt[3].getText().equals("Id")
					|| txt[4].getText().contentEquals("") || txt[4].getText().equals("E-mail")) {
				eMsg = new Emsg("공란을 확인해주세요.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select name, id, email, pw from user where name = '" + txt[2].getText()
						+ "' and id = '" + txt[3].getText() + "' and email = '" + txt[4].getText() + "'");
				if (rs.next()) {
					iMsg = new Imsg("귀하의 Id에 PW는 " + rs.getString(4) + "입니다.");
					return;
				} else {
					eMsg = new Emsg("존재하지 않는 정보입니다.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 30, 450, 400, 35);
	}
}
