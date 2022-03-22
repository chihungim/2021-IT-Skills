package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends BaseFrame {
	JTextField txt[] = { new JTextField(16), new JPasswordField(16) };
	String cap[] = "ID,PW".split(",");

	public Login() {
		super("로그인", 400, 200);
		ui();
		setVisible(true);
	}

	void ui() {
		var c = new JPanel(new BorderLayout());
		var c_c = new JPanel(new GridLayout(0, 1));

		add(c);
		c.add(c_c);
		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			tmp.add(sz(lbl(cap[i] + ":", JLabel.LEFT, 15), 50, 20));
			tmp.add(txt[i]);
			c_c.add(tmp);
			tmp.setOpaque(false);
		}

		c.setBorder(new EmptyBorder(20, 5, 30, 5));

		add(imglbl("./datafiles/캐릭터/로티1.jpg", 150, 150), "West");

		c.add(btn("로그인", a -> btn_event()), "South");
		c.setOpaque(false);
		c_c.setOpaque(false);
	}

	void btn_event() {
		String id = txt[0].getText(), pw = txt[1].getText();

		if (id.isEmpty() || pw.isEmpty()) {
			eMsg("ID 또는 PW를 입력해주세요.");
			return;
		}

		if (id.contentEquals("admin") && pw.contentEquals("1234")) {
			iMsg("관리자님 환영합니다..");
			Main.lbl[0].setText("로그아웃");
			Main.enableLabel(Main.ADMIN);
			this.dispose();
			return;
		}
		try {
			var rs = stmt.executeQuery("select * from user where u_id='" + id + "' and u_pw='" + pw + "'");
			if (rs.next()) {
				Main.lbl[0].setText("로그아웃");
				uno = rs.getString(1);
				uname = rs.getString(2);
				uheight = rs.getInt(5);
				uold = getAge(LocalDate.parse(rs.getString(6)));
				udisable = rs.getInt(8);

				String msg = uname + "님 환영합니다.(" + age[rs.getInt("u_age")] + "";
				if (rs.getInt("u_disable") == 1) {
					msg += ", 장애인";
				}
				msg += ")";

				iMsg(msg);
				Main.enableLabel(Main.USER);
				this.dispose();

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
