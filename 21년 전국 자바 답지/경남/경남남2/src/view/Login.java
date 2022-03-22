package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Login extends BaseFrame {

	JRadioButton rbtn[] = { new JRadioButton("����ȸ��"), new JRadioButton("���ȸ��") };
	ButtonGroup bg = new ButtonGroup();
	String cap[] = "���̵�,��й�ȣ".split(",");
	JTextField txt[] = { new JTextField(), new JPasswordField() };
	String bcap[] = "�α���,ȸ������".split(",");

	public Login() {
		super("�α���", 400, 250);

		this.add(c = new JPanel(new BorderLayout()));

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(n = new JPanel(new GridLayout()), "North");
		c.add(c_c);
		c.add(s = new JPanel(new GridLayout(1, 0, 10, 10)), "South");

		n.add(rbtn[0]);
		n.add(rbtn[1]);
		bg.add(rbtn[0]);
		bg.add(rbtn[1]);

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i], 2), 60, 25));
			tmp.add(sz(txt[i], 150, 25));
			if (i == 1) {
				JLabel jl;
				tmp.add(jl = new JLabel("����"));
				jl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (jl.getText().contentEquals("����")) {
							jl.setText("����");
							((JPasswordField) txt[1]).setEchoChar((char) 0);
						} else {
							jl.setText("����");
							((JPasswordField) txt[1]).setEchoChar('��');
						}

					}
				});
			}
			c_c.add(tmp);
		}

		((JPasswordField) txt[1]).setEchoChar('��');
		for (int i = 0; i < bcap.length; i++) {
			s.add(sz(btn(bcap[i], a -> {
				if (a.getActionCommand().contentEquals(bcap[0])) {
					String id = txt[0].getText(), pw = txt[1].getText();
					if (id.isEmpty()) {
						eMsg("���̵� �Է����ּ���.");
						return;
					}
					if (pw.isEmpty()) {
						eMsg("��й�ȣ�� �Է����ּ���.");
						return;
					}

					// �α���
					type = (rbtn[0].isSelected()) ? "user" : "manager";
					try {
						var rs = stmt
								.executeQuery("select * from " + type + " where id='" + id + "' and pw ='" + pw + "'");
						if (rs.next()) {
							iMsg(rs.getString("name") + "�� �α��εǾ����ϴ�.");
							isLogin = true;
							if (type.equals("user")) {
								uno = rs.getInt(1);
								Main.btn[3].setText("�̷¼�");
								Main.btnLogin.setText("�α׾ƿ�");
								Main.btnSign.setText("ȸ������");
							} else {
								mno = rs.getInt(1);
								Main.btn[3].setText("������");
								Main.btnLogin.setText("�α׾ƿ�");
								Main.btnSign.setText("�������");
							}
							this.dispose();
						} else {
							eMsg("���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
							return;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
//					new SignUp();
				}
			}), 1, 30));
		}

		setEmpty(n, 0, 0, 0, 100);
		setEmpty(c_c, 10, 0, 10, 0);
		setEmpty((JPanel) getContentPane(), 20, 20, 20, 20);

		rbtn[0].setSelected(true);

		this.setVisible(true);
	}
}