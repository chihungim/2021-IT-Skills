package ������;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ������.BaseFrame.Before;

public class Login extends BaseFrame {
	PlaceHolderTextField txt[] = { new PlaceHolderTextField(15), new PlaceHolderTextField(15) };
	JPanel masterP;
	JPanel page[] = new JPanel[2];
	JLabel errorLabel[] = new JLabel[2];
	String cap[] = ("�̸��� �ּ� �Ǵ� �޴�����ȣ�� �α����ϼ���.,��й�ȣ�� �Է��ϰ� �α����ϼ���.").split(",");
	String placeHolders[] = "�̸��� �ּ� �Ǵ� �޴��� ��ȣ+��ҹ���,����,Ư����ȣ�� ������ 8�� �̻�".split("\\+");
	int tmpNo = 0;

	public Login() {
		super("�α���", 400, 300);
		init();
		setVisible(true);
	}

	void init() {
		add(masterP = new JPanel(pages = new CardLayout(0, 0)));

		for (int i = 0; i < 2; i++) {
			page[i] = new JPanel(new BorderLayout());
			JButton btn;
			var c = new JPanel(new FlowLayout(FlowLayout.LEFT));
			var s = new JPanel(new FlowLayout(FlowLayout.CENTER));
			var sign = lbl("<html><font color = \"green\"><u>���������", JLabel.RIGHT);

			page[i].add(lbl("���ƿ��Ű��� ȯ���մϴ�.", JLabel.LEFT, 25), "North");
			page[i].add(c);
			page[i].add(s, "South");

			c.add(lbl(cap[i], JLabel.LEFT));
			c.add(txt[i]);

			txt[i].setPlaceHolder(placeHolders[i]);

			c.add(errorLabel[i] = new JLabel("", JLabel.LEFT));

			errorLabel[i].setForeground(Color.RED);

			if (i < 1)
				s.add(btn = btn("����", a -> {
					if (txt[0].getText().equals("")) {
						errorLabel[0].setText("��� �׸��� �Է��ؾ��մϴ�.");
						return;
					}

					try {
						var rs = stmt.executeQuery("SELECT * FROM eats.seller s, user u, rider r where s.email = '"
								+ txt[0].getText() + "' or s.phone = '" + txt[0].getText() + "' or u.email = '"
								+ txt[0].getText() + "' or u.phone = '" + txt[0].getText() + "' or r.PHONE = '"
								+ txt[0].getText() + "' or r.EMAIL = '" + txt[0].getText() + "'");
						if (rs.next()) {
							var idx = toInt(((JButton) a.getSource()).getName()) + 1;
							pages.show(masterP, idx + "");
							masterP.revalidate();
						} else {
							errorLabel[0].setText("�� �̸��� �Ǵ� �޴����� ã�� �� �����ϴ�.");
							return;
						}

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}));
			else
				s.add(btn = btn("�α���", a -> {
					try {
						var rs1 = stmt.executeQuery("select * from seller s where (s.email = '" + txt[0].getText()
								+ "' or s.phone = '" + txt[0].getText() + "') and s.PW='" + txt[1].getText() + "'");
						if (rs1.next()) {
							sno = rs1.getInt(1);
							sname = rs1.getString(5);
						} else {
							sno = 0;
						}

						var rs2 = stmt.executeQuery("select * from user u where (u.email = '" + txt[0].getText()
								+ "' or u.phone = '" + txt[0].getText() + "') and u.PW='" + txt[1].getText() + "'");
						if (rs2.next()) {
							uno = rs2.getInt(1);
							uname = rs2.getString(5);
						} else {
							uno = 0;
						}

						var rs3 = stmt.executeQuery("select * from rider r where (r.email = '" + txt[0].getText()
								+ "' or r.phone = '" + txt[0].getText() + "') and r.PW = '" + txt[1].getText() + "'");

						if (rs3.next()) {
							rno = rs3.getInt(1);
							rname = rs3.getString(5);
						} else {
							rno = 0;
						}

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					errorLabel[1].setText(sno != 0 ? "" : uno != 0 ? "" : rno != 0 ? "" : "�Է��� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					if (!errorLabel[1].getText().equals("")) {
						return;
					}

					if (uno != 0) {
						new Main();
					} else if (sno != 0) {
						new DashBorard();
					} else if (rno != 0) {
						new Delivery();
					}

					dispose();
				}));

			btn.setName(i + "");

			s.add(lbl("��ɹ���� ó���̽ʴϱ�?", JLabel.RIGHT));
			s.add(sign);

			sign.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new Sign().addWindowListener(new Before(Login.this));
				}
			});

			masterP.add(i + "", page[i]);
		}

	}

	public static void main(String[] args) {
		new Login();
	}
}
