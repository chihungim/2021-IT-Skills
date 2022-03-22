package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.DBManager;

public class Login extends BaseFrame {

	String cap[] = "ID,PW".split(",");
	JTextField txt[] = { new JTextField(), new JPasswordField() };
	JCheckBox chk = new JCheckBox("아이디 저장");
	Preferences pre = Preferences.userNodeForPackage(BaseFrame.class);

	public Login() {
		super("로그인", 350, 230);

		JLabel jl;
		this.add(jl = lbl("Orange Ticket", 0, 35), "North");
		jl.setFont(new Font("HY헤드라인M", Font.BOLD + Font.ITALIC, 35));

		this.add(c = new JPanel(new GridLayout(0, 1)));
		this.add(e = new JPanel(new BorderLayout()), "East");
		this.add(s = new JPanel(new BorderLayout()), "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(sz(new JLabel(cap[i], 2), 40, 25));
			tmp.add(sz(txt[i], 150, 25));
			c.add(tmp);
		}

		txt[0].setText(pre.get("id", ""));
		if (!pre.get("id", "").equals(""))
			chk.setSelected(true);
		chk.addActionListener(a -> pre.remove("id"));

		e.add(btn("로그인", a -> {
			String id = txt[0].getText(), pw = txt[1].getText();
			if (id.isEmpty() || pw.isEmpty()) {
				eMsg("빈칸이 존재합니다.");
				return;
			}

			try {
				var rs = DBManager.rs("Select * from user where u_id='" + id + "' and u_pw='" + pw + "'");
				if (rs.next()) {
					iMsg(rs.getString("u_name") + "님 환영합니다.");
					uname = rs.getString("u_name");
					uno = rs.getString(1);
					main.img.setIcon(img("./Datafiles/회원사진/" + uno + ".jpg", 30, 30));
					setLine(main.img, Color.black);
					main.login.setText("LOGOUT");
					if (chk.isSelected())
						pre.put("id", txt[0].getText());
					this.dispose();
				} else {
					eMsg("ID 또는 PW가 일치하지 않습니다.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}));

		s.add(chk, "West");
		s.add(btn("회원가입", a -> {
			new SignUp().addWindowListener(new Before(this));
		}), "East");

		setEmpty(e, 10, 0, 20, 0);
		setEmpty(c, 10, 0, 0, 0);
		setEmpty((JPanel) getContentPane(), 5, 10, 10, 10);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Login();
	}

}
