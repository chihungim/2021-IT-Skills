package �泲;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends BaseFrame {
	String str[] = "ID,PW".split(",");
	JTextField tt[] = new JTextField[2];

	public Login(Mainf m) {
		super("�α���", 400, 250);
		setLayout(new GridLayout(0, 1));

		for (int i = 0; i < 2; i++) {
			JPanel temp = new JPanel(new BorderLayout());
			temp.add(siz(label(str[i], 2), 50, 30), "West");
			temp.add(tt[i] = new JTextField());
			add(temp);
		}

		add(btn("�α���", a -> {
			if (tt[0].getText().isEmpty() || tt[1].getText().isEmpty()) {
				errmsg("���̵�� ��й�ȣ�� ��� �Է��ؾ� �մϴ�.");
				return;
			}
			if(tt[0].getText().equals("admin") && tt[1].getText().equals("1334")) {
				new Admin().addWindowListener(new be(this));
				msg("�����ڴ� ȯ���մϴ�.");
				return;
			}
			try {
				ResultSet rs = stmt.executeQuery("select * from user where id = '"+tt[0].getText()+"' and pw = '"+tt[1].getText()+"'");
				if(rs.next()) {
					msg(rs.getString(4)+"�� �ȳ��ϼ���.");
					NO = rs.getInt(1);
					NAME = rs.getString(4);
					birth = LocalDate.parse(rs.getString(5)).getYear();
					age = LocalDate.parse(rs.getString(5)).minusYears(LocalDate.now().getYear()).getYear();
					m.setLog(true);
					dispose();
					return;
				}else {
					errmsg("��ġ�ϴ� ȸ�������� �����ϴ�.");
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}));

		emp((JPanel) getContentPane(), 10, 10, 10, 10);

		setVisible(true);
	}

}
