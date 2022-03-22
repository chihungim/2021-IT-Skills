import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Sign extends BaseDialog {
	String tcap[] = "Name,Id,E-mail".split(","), pcap[] = "Password,Password Ȯ��".split(",");
	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15) };
	JPasswordField pwTxt[] = { new JPasswordField(15), new JPasswordField(15) };

	public Sign() {
		super("���� ����ϱ�", 500, 600);

		ui();

		setVisible(true);
	}

	void ui() {
		setLayout(null);

		setBounds(lbl("���� ����", 0, 30), 30, 60, 150, 30);
		for (int i = 0; i < 2; i++) {
			setBounds(txt(txt[i], tcap[i]), 40, 120 + i * 60, 380, 30);
		}

		for (int i = 0; i < pcap.length; i++) {
			setBounds(txt(pwTxt[i], pcap[i]), 40, 240 + i * 60, 380, 30);
		}

		setBounds(txt(txt[2], tcap[2]), 40, 360, 380, 30);
		setBounds(btn("ȸ������", e -> {
			for (int i = 0; i < tcap.length; i++) {
				if (txt[i].getText().contentEquals("") || txt[i].getText().equals(tcap[i])) {
					eMsg = new Emsg("������ Ȯ�����ּ���.");
					return;
				}
			}

			for (int i = 0; i < pcap.length; i++) {
				if (pwTxt[i].getText().contentEquals("") || pwTxt[i].getText().equals(pcap[i])) {
					eMsg = new Emsg("������ Ȯ�����ּ���.");
					return;
				}
			}

			if (!pwTxt[0].getText().equals(pwTxt[1].getText())) {
				eMsg = new Emsg("PWȮ���� ��ġ���� �ʽ��ϴ�.");
				return;
			}

			if (!(pwTxt[0].getText().matches(".*[a-zA-Z].*") && pwTxt[0].getText().matches(".*[\\W].*")
					&& pwTxt[0].getText().matches(".*[0-9].*"))) {
				eMsg = new Emsg("Ư�����ڸ� �������ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user");
				while (rs.next()) {
					if (txt[1].getText().equals(rs.getString(2))) {
						eMsg = new Emsg("Id�� �ߺ��Ǿ����ϴ�.");
						return;
					}

					if (txt[2].getText().equals(rs.getString(5))) {
						eMsg = new Emsg("E-mail�� �ߺ��Ǿ����ϴ�.");
						return;
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			iMsg = new Imsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
			execute("insert into user values(0, '" + txt[1].getText() + "','" + pwTxt[0].getText() + "','"
					+ txt[0].getText() + "','" + txt[2].getText() + "','" + 1000 + "')");
			dispose();
		}), 40, 420, 380, 40);
	}

	public static void main(String[] args) {
		new Sign();
	}
}
