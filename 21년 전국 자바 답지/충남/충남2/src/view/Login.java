package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends BaseFrame {

	String str[] = "ID,PW".split(",");
	JTextField txt[] = { new JTextField(15), new JPasswordField(15) };

	public Login() {
		super("로그인", 300, 180);
		setLayout(new GridLayout(0, 1, 5, 5));
		for (int i = 0; i < str.length; i++) {
			var tmp = new JPanel(new BorderLayout());
			tmp.add(sz(lbl(str[i], JLabel.LEFT, 12), 30, 20), "West");
			tmp.add(txt[i]);
			add(tmp);
		}

		add(btn("로그인", a -> {
			try {
				var rs = stmt.executeQuery(
						"select * from user where id = '" + txt[0].getText() + "' and pw = '" + txt[1].getText() + "'");
				if (rs.next()) {
					no = rs.getInt(1);
					name = rs.getString(4);
					iMsg(name + "님 안녕하세요.");
					birth = LocalDate.parse(rs.getString(5)).getYear();
					age = LocalDate.parse(rs.getString(5)).minusYears(LocalDate.now().getYear()).getYear();
					Main.setLogin(true);
					dispose();
				} else {
					eMsg("일치하는 회원정보가 없습니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}));

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		setVisible(true);
	}

	public static void main(String[] args) {
		new Login();
	}
}
