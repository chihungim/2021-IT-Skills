import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Sign extends BaseDialog {
	String tcap[] = "Name,Id,E-mail".split(","), pcap[] = "Password,Password 확인".split(",");
	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15) };
	JPasswordField pwTxt[] = { new JPasswordField(15), new JPasswordField(15) };

	public Sign() {
		super("계정 등록하기", 500, 600);

		ui();

		setVisible(true);
	}

	void ui() {
		setLayout(null);

		setBounds(lbl("계정 정보", 0, 30), 30, 60, 150, 30);
		for (int i = 0; i < 2; i++) {
			setBounds(txt(txt[i], tcap[i]), 40, 120 + i * 60, 380, 30);
		}

		for (int i = 0; i < pcap.length; i++) {
			setBounds(txt(pwTxt[i], pcap[i]), 40, 240 + i * 60, 380, 30);
		}

		setBounds(txt(txt[2], tcap[2]), 40, 360, 380, 30);
		setBounds(btn("회원가입", e -> {
			for (int i = 0; i < tcap.length; i++) {
				if (txt[i].getText().contentEquals("") || txt[i].getText().equals(tcap[i])) {
					eMsg = new Emsg("공란을 확인해주세요.");
					return;
				}
			}

			for (int i = 0; i < pcap.length; i++) {
				if (pwTxt[i].getText().contentEquals("") || pwTxt[i].getText().equals(pcap[i])) {
					eMsg = new Emsg("공란을 확인해주세요.");
					return;
				}
			}

			if (!pwTxt[0].getText().equals(pwTxt[1].getText())) {
				eMsg = new Emsg("PW확인이 일치하지 않습니다.");
				return;
			}

			if (!(pwTxt[0].getText().matches(".*[a-zA-Z].*") && pwTxt[0].getText().matches(".*[\\W].*")
					&& pwTxt[0].getText().matches(".*[0-9].*"))) {
				eMsg = new Emsg("특수문자를 포함해주세요.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user");
				while (rs.next()) {
					if (txt[1].getText().equals(rs.getString(2))) {
						eMsg = new Emsg("Id가 중복되었습니다.");
						return;
					}

					if (txt[2].getText().equals(rs.getString(5))) {
						eMsg = new Emsg("E-mail이 중복되었습니다.");
						return;
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			iMsg = new Imsg("회원가입이 완료되었습니다.");
			execute("insert into user values(0, '" + txt[1].getText() + "','" + pwTxt[0].getText() + "','"
					+ txt[0].getText() + "','" + txt[2].getText() + "','" + 1000 + "')");
			dispose();
		}), 40, 420, 380, 40);
	}

	public static void main(String[] args) {
		new Sign();
	}
}
