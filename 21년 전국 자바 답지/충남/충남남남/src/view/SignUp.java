package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class SignUp extends BaseFrame {

	JTextField txt[] = { new JTextField(), new JPasswordField(), new JTextField() };
	JTextField dateTxt[] = { new JTextField(), new JTextField(), new JTextField() };
	JTextField phoneTxt[] = { new JTextField(), new JTextField(), new JTextField() };
	String cap[] = "아이디,비밀번호,이름,생년월일,휴대전화".split(",");
	String dateCap[] = "월,일".split(",");
	String dateHolder[] = "yyyy,mm,dd".split(",");

	public SignUp() {
		super("회원가입", 380, 350);

		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);

		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i]), 60, 25));
			tmp.add(sz(txt[i], 260, 30));
			c_c.add(tmp);
		}

		var dateP = new JPanel(new FlowLayout(0));
		dateP.add(sz(new JLabel(cap[3]), 60, 25));
		for (int i = 0; i < dateTxt.length; i++) {
			if (i == 0) {
				dateP.add(sz(dateTxt[0], 90, 30));
			} else {
				dateP.add(sz(dateTxt[i], 65, 30));
				dateP.add(new JLabel(dateCap[i - 1]));
			}
			holder(dateTxt[i], dateHolder[i]);
			c_c.add(dateP);
		}

		var phoneP = new JPanel(new FlowLayout(0));
		phoneP.add(sz(new JLabel(cap[4]), 60, 25));
		for (int i = 0; i < phoneTxt.length; i++) {
			phoneP.add(sz(phoneTxt[i], 78, 30));
			if (i < phoneTxt.length - 1) {
				phoneP.add(new JLabel("-"));
			}
			holder(phoneTxt[i], "0000");
			c_c.add(phoneP);
		}

		s.add(btn("회원가입", a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty() || dateTxt[i].getText().isEmpty() || phoneTxt[i].getText().isEmpty()) {
					eMsg("빈칸을 모두 입력해야 합니다.");
					return;
				}
			}

			if (!getOne("select * from user where id = '" + txt[0].getText() + "'").isEmpty()) {
				eMsg("이미 사용중인 아이디 입니다.");
				return;
			}

			// 비밀번호 영문 숫자 특수문자

			try {
				LocalDate date = LocalDate.of(rei(dateTxt[0]), rei(dateTxt[1]), rei(dateTxt[2]));
				if (LocalDate.now().isBefore(date)) {
					eMsg("생년월일은 미래가 아니어야 합니다.");
					return;
				}
			} catch (Exception e) {
				eMsg("생년월일은 올바른 형식으로 입력해야 합니다.");
				return;
			}

			// 전화번호 형식

		}));

		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}

	void holder(JTextField txt, String holderText) {
		txt.setText(holderText);
		txt.setForeground(Color.LIGHT_GRAY);

		txt.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (txt.getText().trim().isEmpty()) {
					txt.setText(holderText);
					txt.setForeground(Color.LIGHT_GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if (txt.getText().contentEquals(holderText)) {
					txt.setText("");
					txt.setForeground(Color.black);
				}
			}
		});

	}

}
