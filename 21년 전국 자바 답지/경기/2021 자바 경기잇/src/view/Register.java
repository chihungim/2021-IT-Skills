package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Register extends BaseFrame {

	String cap[] = "이름,ID,PW,키,생년월일".split(",");
	JTextField txt[] = new JTextField[5];
	JCheckBox chk = new JCheckBox("장애인");

	public Register() {
		super("회원가입", 300, 350);
		ui();
		setVisible(true);
	}

	void ui() {
		var c = new JPanel(new GridLayout(0, 1, 5, 5));
		var s = new JPanel();

		add(c);
		add(s, "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new BorderLayout());
			tmp.add(sz(lbl(cap[i] + ":", JLabel.LEFT, 15), 70, 30), "West");
			tmp.add(txt[i] = new JTextField());
			c.add(tmp);
			tmp.setOpaque(false);
		}

		c.add(chk);

		s.add(btn("회원가입", a -> btn_event()));

		c.setBorder(new EmptyBorder(5, 5, 5, 5));
		c.setOpaque(false);
		s.setOpaque(false);
		chk.setHorizontalAlignment(SwingConstants.CENTER);
		chk.setOpaque(false);
	}

	void btn_event() {
		if (!getOne("select * from user where u_id='" + txt[1].getText() + "'").isEmpty()) {
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

		execute("insert into user values(0, '" + txt[0].getText() + "','" + txt[1].getText() + "','" + txt[2].getText()
				+ "','" + txt[3].getText() + "','" + txt[4].getText() + "','" + op + "','"
				+ ((chk.isSelected()) ? 1 : 0) + "')");
		dispose();
	}

	public static void main(String[] args) {
		new Register();
	}
}
