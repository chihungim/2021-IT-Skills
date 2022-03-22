package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db.DBManager;

public class SignUp extends BaseFrame {

	String cap[] = "이름,ID,PW,키,생년월일".split(",");
	JTextField txt[] = new JTextField[5];
	JCheckBox chk = new JCheckBox("�����");

	public SignUp() {
		super("회원가입", 350, 500);

		this.add(c = new JPanel(new GridLayout(0, 1)));
		this.add(s = new JPanel(), "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(1));
			tmp.setOpaque(false);
			tmp.add(sz(lbl(cap[i] + ":", 2, 15), 80, 30));
			tmp.add(sz(txt[i] = new JTextField(), 200, 30));
			c.add(tmp);
		}

		c.add(chk);
		chk.setHorizontalAlignment(SwingConstants.CENTER);
		chk.setOpaque(false);

		s.add(btn("회원가입", a -> {
			if (!DBManager.getOne("select * from user where u_id='" + txt[1].getText() + "'").isEmpty()) {
				eMsg("이미 사용중인 아이디입니다.");
				return;
			}

			if (!(txt[2].getText().matches(".*\\d.*") && txt[2].getText().matches(".*[a-zA-Z].*")
					&& txt[2].getText().matches(".*[!@#$].*") && txt[2].getText().length() >= 4)) {
				eMsg("비밀번호를 확인해주세요.");
				return;
			}

			if (!isNumeric(txt[3].getText())) {
				eMsg("문자는 입력이 불가합니다.");
				return;
			}

			try {
				LocalDate l = LocalDate.parse(txt[4].getText());
				if (l.isAfter(LocalDate.now())) {
					eMsg("생년월일을 확인해주세요.");
					return;
				}
			} catch (DateTimeParseException e) {
				eMsg("생년월일을 확인해주세요.");
				return;
			}

			int uage = getAge(LocalDate.parse(txt[4].getText()));
			int op = 0;
			if (uage >= 0 && uage <= 12) {
				op = 3;
			} else if (uage >= 13 && uage <= 19) {
				op = 2;
			} else if (uage >= 20) {
				op = 1;
			} else if (uage >= 65) {
				op = 4;
			}
			String msg = (chk.isSelected()) ? "장애인 " : "";
			msg += age[op] + "으로 회원가입이 완료되었습니다.";
			iMsg(msg);

			DBManager.execute("insert into user values(0, '" + txt[0].getText() + "','" + txt[1].getText() + "','"
					+ txt[2].getText() + "','" + txt[3].getText() + "','" + txt[4].getText() + "','" + op + "','"
					+ ((chk.isSelected()) ? 1 : 0) + "')");
			this.dispose();

		}));

		c.setOpaque(false);
		s.setOpaque(false);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new SignUp();
	}

}
