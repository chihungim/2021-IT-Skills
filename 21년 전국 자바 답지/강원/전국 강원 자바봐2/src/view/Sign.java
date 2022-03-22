package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Sign extends BaseDialog {

	JTextField txt[] = { HintText(new JTextField(), "Name"), HintText(new JTextField(), "Id"),
			HintText(new JPasswordField(), "Password"), HintText(new JPasswordField(), "Password 확인"),
			HintText(new JTextField(), "E-mail"), };

	public Sign(BaseFrame f) {
		super("계정등록하기", 450, 600, f);
		setUI();
		setVisible(true);
	}

	void setUI() {
		setLayout(new BorderLayout());
		var c = new JPanel(new GridLayout(0, 1, 5, 5));
		add(c);

		c.add(lbl("계정 정보", JLabel.LEFT, 20));

		for (int i = 0; i < txt.length; i++) {
			c.add(txt[i]);
		}

		c.add(btn("회원가입", a -> {
			if (txt[0].getText().contentEquals("") || txt[0].getText().equals("Name")
					|| txt[1].getText().contentEquals("") || txt[1].getText().equals("Id")
					|| txt[2].getText().contentEquals("") || txt[2].getText().equals("Password")
					|| txt[3].getText().contentEquals("") || txt[3].getText().equals("Password 확인")
					|| txt[4].getText().contentEquals("") || txt[4].getText().equals("E-mail")) {
				emsg = new eMsg("공란을 확인해주세요.");
				return;
			}

			if (!txt[3].getText().equals(txt[2].getText())) {
				emsg = new eMsg("PW확인이 일치하지 않습니다.");
			}

			if (!txt[3].getText().matches(".*[\\W].*")) {
				emsg = new eMsg("특수문자를 포함해주세요.");
				return;
			}

			try {
				var rs1 = stmt.executeQuery("select * from user where id = '" + txt[1].getText() + "'");
				if (rs1.next()) {
					emsg = new eMsg("Id가 중복되었습니다.");
					return;
				}

				var rs2 = stmt.executeQuery("select * from user where email = '" + txt[2].getText() + "'");
				if (rs2.next()) {
					emsg = new eMsg("E-mail이 중복되었습니다.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			execute("insert user values(0,'" + txt[1].getText() + "','" + txt[2].getText() + "','" + txt[0].getText()
					+ "','" + txt[4].getText() + "',0)");
			
			imsg = new iMsg("회원가입이 완료되었습니다.");
			dispose();
		}));

		c.setOpaque(false);
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(40, 40, 40, 40));
	}

	public static void main(String[] args) {
		new Sign(null);
	}
}
