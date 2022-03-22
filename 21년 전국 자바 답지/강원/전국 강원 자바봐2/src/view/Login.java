package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Login extends BaseFrame {

	int xywh[][] = { { 0, 0, 755, 565 }, { 800, 123, 174, 38 }, { 800, 172, 172, 26 }, { 800, 207, 171, 26 },
			{ 980, 171, 75, 60 }, { 800, 245, 173, 27 }, { 980, 245, 75, 23 }, { 800, 377, 221, 25 } };

	JTextField id, pw;
	JLabel signlbl, findlbl;
	JComponent jc[] = { imageLabel("./��������/images/login.jpg", xywh[0][2], xywh[0][3] + 20), lbl("�α���", JLabel.LEFT, 20),
			id = HintText("Id", new JTextField()), pw = HintText("Password"), btn("�α���", a -> e_login()),
			findlbl = lbl("���̵�/��й�ȣ ã��", JLabel.LEFT, 10), themeButton(),

			signlbl = lbl("���ο� ���� ����� ->", JLabel.LEFT) };

	public Login() {
		super("��������", 1100, 600);
		setUI();
		addEvents();
		setVisible(true);
	}

	void e_login() {
		if (id.getText().contentEquals("") || id.getText().equals("Id")) {
			emsg = new eMsg("���̵� �Է����ּ���.");
			return;
		} else if (pw.getText().contentEquals("") || pw.getText().equals("Password")) {
			emsg = new eMsg("��й�ȣ�� �Է����ּ���.");
			return;
		}

		if (id.getText().equals("admin") && pw.getText().equals("1234")) {
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
				BaseDialog.u_id = u_id;
				BaseDialog.u_name = u_name;
				BaseDialog.u_no = u_no;
				id.setText("");
				pw.setText("");
				new UserMain().addWindowListener(new before(this));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	void addEvents() {
		findlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new IdPwFind(Login.this);
			}
		});

		signlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Sign(Login.this);
			}
		});

	}

	void setUI() {
		setLayout(null);
		for (int i = 0; i < jc.length; i++) {
			add(jc[i]);
			jc[i].setBounds(xywh[i][0], xywh[i][1], xywh[i][2], xywh[i][3]);
		}
	}

	public static void main(String[] args) {
		new Login();
	}
}
