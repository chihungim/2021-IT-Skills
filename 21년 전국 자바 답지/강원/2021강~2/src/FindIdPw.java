import java.sql.SQLException;

import javax.swing.JTextField;

public class FindIdPw extends BaseDialog {
	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15),
			new JTextField(15) };
	String cap[] = "Name,E-mail,Name,Id,E-mail".split(",");

	public FindIdPw() {
		super("���̵�/��й�ȣ ã��", 500, 600);

		ui();
	}

	void ui() {
		setLayout(null);

		setBounds(lbl("���̵� ã��", 0, 30), 20, 40, 180, 30);
		for (int i = 0; i < 2; i++) {
			setBounds(txt(txt[i], cap[i]), 30, 85 + i * 50, 400, 30);
		}
		setBounds(btn("���", e -> {
			if (txt[0].getText().contentEquals("") || txt[0].getText().equals("Name")) {
				eMsg = new Emsg("������ Ȯ�����ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select id, name, email from user where name = '" + txt[0].getText()
						+ "' and email = '" + txt[1].getText() + "'");
				if (rs.next()) {
					iMsg = new Imsg("������ Id�� " + rs.getString(1) + "�Դϴ�.");
					return;
				} else {
					eMsg = new Emsg("�������� �ʴ� �����Դϴ�.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 30, 180, 400, 35);
		setBounds(lbl("��й�ȣ ã��", 0, 30), 20, 240, 200, 30);
		for (int i = 2; i < 5; i++) {
			setBounds(txt(txt[i], cap[i]), 30, 200 + i * 50, 400, 30);
		}
		setBounds(btn("���", e -> {
			if (txt[2].getText().contentEquals("") || txt[2].getText().equals("Name")
					|| txt[3].getText().contentEquals("") || txt[3].getText().equals("Id")
					|| txt[4].getText().contentEquals("") || txt[4].getText().equals("E-mail")) {
				eMsg = new Emsg("������ Ȯ�����ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select name, id, email, pw from user where name = '" + txt[2].getText()
						+ "' and id = '" + txt[3].getText() + "' and email = '" + txt[4].getText() + "'");
				if (rs.next()) {
					iMsg = new Imsg("������ Id�� PW�� " + rs.getString(4) + "�Դϴ�.");
					return;
				} else {
					eMsg = new Emsg("�������� �ʴ� �����Դϴ�.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 30, 450, 400, 35);
	}
}
