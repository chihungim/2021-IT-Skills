import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class IdPwFind extends BaseDialog {
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	String cap[] = "Name,E-mail,Name,Id,E-mail".split(",");
	JLabel lbl[] = new JLabel[2];

	public IdPwFind() {
		super("���̵�/��й�ȣ ã��", 450, 600);

		UI();
		setVisible(true);
	}

	void UI() {
		setLayout(null);

		setBounds(lbl[0] = lbl("���̵� ã��", 2, 30), 20, 10, 200, 70);
		for (int i = 0; i < 2; i++) {
			HintText(txt[i], cap[i]);
			setBounds(txt[i], 20, 80 + i * 50, 380, 30);
		}
		setBounds(btn("���", e -> {
			if ((txt[0].getText().contentEquals("") || txt[0].getText().equals("Name"))
					|| (txt[1].getText().contentEquals("") || txt[1].getText().equals("E-mail"))) {
				emsg = new eMsg("������ Ȯ�����ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where name = '" + txt[0].getText() + "' and email = '"
						+ txt[1].getText() + "'");
				if (rs.next()) {
					imsg = new iMsg("������ Id�� " + rs.getString(2) + "�Դϴ�.");
				} else {
					emsg = new eMsg("�������� �ʴ� �����Դϴ�.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 20, 190, 380, 40);

		setBounds(lbl[1] = lbl("��й�ȣ ã��", 2, 30), 20, 250, 230, 70);
		for (int i = 2; i < 5; i++) {
			HintText(txt[i], cap[i]);
			setBounds(txt[i], 20, 200 + i * 60, 380, 30);
		}

		setBounds(btn("���", e -> {
			if ((txt[2].getText().contentEquals("") || txt[2].getText().equals("Name"))
					|| (txt[3].getText().contentEquals("") || txt[3].getText().equals("Id"))
					|| (txt[4].getText().contentEquals("") || txt[4].getText().equals("E-mail"))) {
				emsg = new eMsg("������ Ȯ�����ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where name = '" + txt[2].getText() + "' and id = '"
						+ txt[3].getText() + "' and email = '" + txt[4].getText() + "'");
				if (rs.next()) {
					imsg = new iMsg("������ Id�� PW�� " + rs.getString(3) + "�Դϴ�.");
				} else {
					emsg = new eMsg("�������� �ʴ� �����Դϴ�.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}), 20, 500, 380, 40);
	}

	public static void main(String[] args) {
		new IdPwFind();
	}
}
