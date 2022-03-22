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

public class Login extends BaseFrame {

	String cap[] = "ID,PW".split(",");
	JTextField txt[] = { new JTextField(), new JPasswordField() };

	public Login() {
		super("�α���", 350, 200);

		this.add(c = new JPanel(new BorderLayout()));
		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);
		c.add(s = new JPanel(new BorderLayout()), "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i]), 60, 25));
			tmp.add(sz(txt[i], 240, 30));
			c_c.add(tmp);
		}

		s.add(sz(btn("�α���", a -> {
			String id = txt[0].getText(), pw = txt[1].getText();
			if (id.isEmpty() || pw.isEmpty()) {
				eMsg("���̵�� ��й�ȣ�� ��� �Է��ؾ� �մϴ�.");
				return;
			}

			if (id.contentEquals("admin") && pw.contentEquals("1234")) {
				iMsg("�����ڴ� ȯ���մϴ�.");
				closeAll();
				new Admin();
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where id='" + id + "' and pw = '" + pw + "'");
				if (rs.next()) {
					uno = rs.getInt(1);
					uname = rs.getString(4);
					LocalDate birth = LocalDate.parse(rs.getString(5));
					LocalDate now = LocalDate.now();
					birthyear = birth.getYear();
					age = now.minusYears(birth.getYear()).getYear();

					System.out.println(age);

					iMsg(rs.getString(4) + "�� �ȳ��ϼ���.");
					new Main().setLogin(true);
					dispose();
				} else {
					eMsg("��ġ�ϴ� ȸ�������� �����ϴ�.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}), 1, 30));

		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);

		this.setVisible(true);
	}

}
