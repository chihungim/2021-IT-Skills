package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.BaseFrame.Before;

public class Login extends BaseFrame {

	JPanel c, s;
	String cap[] = "���̵�,��й�ȣ".split(","), bcap[] = "�α���,ȸ������".split(",");
	JTextField txt[] = { new JTextField(15), new JTextField(15) };

	@Override
	public JLabel lbl(String cap, int alig, int size) {
		var a = super.lbl(cap, alig, size);
		a.setFont(new Font("", Font.TRUETYPE_FONT, size));
		return a;
	}

	public Login() {
		super("�α���", 300, 200);

		add(lbl("Room escape", JLabel.CENTER, 30), "North");
		add(c = new JPanel(new GridLayout(0, 1)));
		add(s = new JPanel(new GridLayout(1, 0, 5, 5)), "South");

		for (int i = 0; i < cap.length; i++) {
			var t = new JPanel();
			t.add(sz(lbl(cap[i], JLabel.LEFT), 60, 15));
			t.add(txt[i]);
			c.add(t);
		}

		for (int i = 0; i < bcap.length; i++) {
			s.add(btn(bcap[i], a -> {
				if (a.getActionCommand().equals(bcap[0])) {
					for (int j = 0; j < txt.length; j++) {
						if (txt[j].getText().isEmpty()) {
							eMsg("��ĭ�� �ֽ��ϴ�.");
							return;
						}
					}
					try {
						var rs = stmt.executeQuery("select u_no, u_name from user where u_id='" + txt[0].getText()
								+ "' and u_pw='" + txt[1].getText() + "'");
						if (rs.next()) {
							uno = rs.getInt(1);
							uid = txt[0].getText();
							uname = rs.getString(2);
							iMsg(uname + "�� ȯ���մϴ�.");
							new Main().addWindowListener(new Before(Login.this));
							txt[0].setText("");
							txt[1].setText("");
						} else {
							eMsg("���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					new Sign().addWindowListener(new Before(Login.this));
				}
				
			}));
		}

		setEmpty(((JPanel) getContentPane()), 5, 5, 5, 5);

		setVisible(true);
	}

	public static void main(String[] args) {
		new Login();
	}
}
