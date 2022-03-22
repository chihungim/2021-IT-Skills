package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Register extends BaseFrame {

	String cap[] = "이름,ID,Pw,키,생년월일".split(",");

	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	JCheckBox chk = new JCheckBox("장애인");

	public Register() {
		super("회원가입", 300, 350);
		ui();
		setVisible(true);
	}

	private void ui() {
		var c = new JPanel(new GridLayout(0, 1, 10, 10));
		var s = new JPanel(new FlowLayout(FlowLayout.CENTER));

		add(c);
		add(s, "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new BorderLayout(5, 5));
			tmp.add(sz(lbl(cap[i] + ":", JLabel.LEFT, 15), 90, 15), "West");
			tmp.add(txt[i]);
			c.add(tmp);
			tmp.setOpaque(false);
		}

		c.add(chk);
		s.add(btn("회원가입", a -> btn_event()));

		chk.setHorizontalAlignment(JCheckBox.CENTER);
		chk.setOpaque(false);
		c.setOpaque(false);
		s.setOpaque(false);
		((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 5, 10));
	}

	void btn_event() {
		String name = txt[0].getText(), id = txt[1].getText(), pw = txt[2].getText(), height = txt[3].getText(),
				birth = txt[4].getText();

		if (!getOne("select * from user where u_id='" + id + "'").isEmpty()) {
			eMsg("이미 사용중인 아이디 입니다.");
			return;
		}

		if (!(pw.matches(".*\\d.*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[!@#$].*") && pw.length() >= 4)) {
			eMsg("비밀번호를 확인해주세요.");
			return;
		}

		if (height.matches(".*[^0-9].*")) {
			eMsg("문자는 입력이 불가능합니다.");
			return;
		}

		try {
			var l = LocalDate.parse(birth);
			if (l.isAfter(LocalDate.now())) {
				eMsg("생년월일을 확인해주세요.");
				return;
			}
		} catch (DateTimeParseException e) {
			eMsg("생년월일을 확인해주세요.");
			return;
		}

		int uage = getAge(LocalDate.parse(birth));
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

		iMsg("회원가입이 완료되었습니다.");

		try (var pst = con.prepareStatement("insert into user values(0, ?, ?, ?,?,?,?,?)")) {
			pst.setString(1, name);
			pst.setString(2, id);
			pst.setString(3, pw);
			pst.setInt(4, Integer.parseInt(height));
			pst.setDate(5, Date.valueOf(birth));
			pst.setInt(6, op);
			pst.setInt(7, chk.isSelected() == true ? 1 : 0);
			pst.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dispose();
	}

	public static void main(String[] args) {
		new Register();
	}
}
