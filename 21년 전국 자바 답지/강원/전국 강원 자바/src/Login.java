import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends BaseFrame {
	JLabel imglbl = new JLabel();
	JLabel namelbl;
	JLabel lbl[] = new JLabel[2];
	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();

	JButton btn;
	String cap[] = "ID,Password".split(",");

	public Login() {
		super("��������", 1100, 600);
		setLayout(null);
		UI();
		event();
		setVisible(true);
	}

	void event() {
		for (int i = 0; i < lbl.length; i++) {
			lbl[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getSource().equals(lbl[0]))
						new IdPwFind();
					else
						new AddAcount();
				}
			});
		}
	}

	void UI() {
		imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("��������/images/login.jpg")
				.getScaledInstance(600, 600, Image.SCALE_SMOOTH)));

		HintText(id, "Id");
		HintText(pw, "Password");

		setBounds(imglbl, 0, 0, 600, 600);
		setBounds(namelbl = lbl("�α���", 0, 25), 695, 140, 80, 40);
		setBounds(id, 700, 200, 180, 25);
		setBounds(pw, 700, 250, 180, 25);
		setBounds(lbl[0] = lbl("���̵�/��й�ȣ ã��", 2), 700, 290, 180, 35);
		setBounds(btn = btn("����", e -> {
			if (id.getText().contentEquals("") || id.getText().equals("Id")) {
				emsg = new eMsg("���̵� �Է����ּ���.");
				return;
			} else if (pw.getText().contentEquals("") || pw.getText().equals("Password")) {
				emsg = new eMsg("��й�ȣ�� �Է����ּ���.");
				return;
			}

			if (id.getText().equals("admin") && id.getText().equals("1234")) {
				imsg = new iMsg("�����ڷ� �α����մϴ�.");
				new AdminMain().addWindowListener(new before(Login.this));
				return;
			}

			try {
				var rs = stmt.executeQuery(
						"select * from user where id = '" + id.getText() + "' and pwd = '" + pw.getText() + "'");
				if (rs.next()) {
					u_no = rs.getInt(1);
					u_id = rs.getString(2);
					u_name = rs.getString(4);
					id.setText("");
					pw.setText("");
					new UserMain();
					dispose();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 900, 200, 80, 80);
		setBounds(themeButton(), 900, 290, 80, 30);
		setBounds(lbl[1] = lbl("���ο� ���� ����� ��", 2), 700, 390, 180, 35);
	}

	public static void main(String[] args) {
		new Login();
	}
}
