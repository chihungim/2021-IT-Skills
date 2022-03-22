package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.DBManager;

public class Login extends BaseFrame {

	JTextField txt[] = { new JTextField(), new JPasswordField(), };
	String cap[] = "ID,PW".split(",");

	public Login() {
		super("로그인", 600, 300);

		this.add(w = new JPanel(new BorderLayout()), "West");
		this.add(c = new JPanel(new BorderLayout()));

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);
		c_c.setOpaque(false);

		w.setOpaque(false);
		c.setOpaque(false);

		w.add(new JLabel(img("./datafiles/캐릭터/로티1.jpg", 250, 260)));

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(sz(new JLabel(cap[i] + ":", 2), 80, 25));
			tmp.add(sz(txt[i], 200, 30));
			tmp.setOpaque(false);
			c_c.add(tmp);
		}
		c.add(btn("로그인", a -> {
			String id = txt[0].getText(), pw = txt[1].getText();
			if (id.isEmpty() || pw.isEmpty()) {
				eMsg("ID 또는 PW를 입력해주세요.");
				return;
			}

			if (id.contentEquals("admin") && pw.contentEquals("1234")) {
				iMsg("관리자님 환영합니다..");
				Main.lbl[0].setText("로그아웃");
				Main.loginEnabled(2);
				this.dispose();
			}

			try {
				var rs = DBManager.rs("select * from user where u_id='" + id + "' and u_pw='" + pw + "'");
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
					Main.loginEnabled(1);
					this.dispose();

				} else {
					eMsg("회원정보를 다시 확인해주세요.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}), "South");

		setEmpty(c, 70, 10, 70, 10);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Login();
	}

}
