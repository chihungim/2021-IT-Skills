package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends BaseFrame {

	JTextField txt[] = { new JTextField(15), new JPasswordField(15) };
	String cap[] = "ID,PW".split(",");

	public Login() {
		super("로그인", 400, 200);
		ui();
		event();
		setVisible(true);
	}

	private void event() {
		// TODO Auto-generated method stub

	}

	private void ui() {

		var c = new JPanel(new BorderLayout());
		var c_c = new JPanel(new GridLayout(0, 1, 5, 5));
		add(c);
		c.add(c_c);
		add(imglbl("./datafiles/캐릭터/로티1.jpg", 150, 150), "West");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new BorderLayout());
			tmp.add(sz(lbl(cap[i] + ":", JLabel.LEFT, 15), 60, 15), "West");
			tmp.add(txt[i]);
			c_c.add(tmp);
			tmp.setOpaque(false);
		}

		c.add(btn("로그인", a -> btn_event()), "South");

		c_c.setBorder(new EmptyBorder(5, 5, 5, 5));
		c.setBorder(new EmptyBorder(20, 5, 40, 5));
		c.setOpaque(false);
		c_c.setOpaque(false);
	}

	void btn_event() {
		var id = txt[0].getText();
		var pw = txt[1].getText();
		if (id.isEmpty() || pw.isEmpty()) {
			eMsg("ID 또는 PW를 입력해주세요.");
			return;
		}

		if (id.contentEquals("admin") && pw.contentEquals("1234")) {
			iMsg("관리자님 환영합니다.");
			Main.enbLabel(Main.ADMIN);
			dispose();
			return;
		}

		try {
			var rs = stmt
					.executeQuery("select * from user where u_id like '%" + id + "%' and u_pw like '%" + pw + "%'");
			if (rs.next()) {
				iMsg(rs.getString("u_name") + "님 환영합니다.");
				setLogin(rs.getInt(1));
				Main.enbLabel(Main.USER);
				dispose();
			} else {
				eMsg("회원정보를 다시 확인해주세요.");
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
