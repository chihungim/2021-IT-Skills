package view;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Find extends BaseDialog {

	JButton btn1 = btn("���", a -> {

	}), btn2 = btn("�Լ�", a -> {

	});

	JTextField idtxt[] = { new TextHolder("Name", 15), new TextHolder("E-mail", 15) };
	JTextField pwtxt[] = { new TextHolder("Name", 15), new TextHolder("Id", 15), new TextHolder("E-mail", 15) };

	JComponent jc[] = { lbl("���̵� ã��", JLabel.LEFT, 20), idtxt[0], idtxt[1], btn1, lbl("��й�ȣ ã��", JLabel.LEFT, 20),
			pwtxt[0], pwtxt[1], pwtxt[2], btn2 };

	public Find() {
		super("���̵�/��й�ȣ ã��", 400, 600);
		ui();
		events();
	}

	void ui() {
		setLayout(new GridLayout(0, 1, 20, 20));
		for (int i = 0; i < jc.length; i++) {
			add(jc[i]);
		}
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(20, 20, 20, 20));
	}

	void events() {
		btn1.addActionListener(a -> {
			var name = idtxt[0].getText();
			var mail = idtxt[1].getText();
			if (name.equals("") || mail.equals("")) {
				BaseFrame.eMsg("������ Ȯ�����ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where name = '" + name + "' and email='" + mail + "'");
				if (rs.next()) {
					BaseFrame.iMsg("������ Id�� " + rs.getString(2) + "�Դϴ�.");
				} else {
					BaseFrame.eMsg("�������� �ʴ� �����Դϴ�.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		btn2.addActionListener(a -> {
			var name = pwtxt[0].getText();
			var id = pwtxt[1].getText();
			var mail = pwtxt[2].getText();

			if (name.isEmpty() || id.isEmpty() || mail.isEmpty()) {
				BaseFrame.eMsg("������ Ȯ�����ּ���.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where name = '" + name + "' and email = '" + mail
						+ "' and id = '" + id + "'");
				if (rs.next()) {
					BaseFrame.iMsg("������ Id�� PW�� " + rs.getString(3) + "�Դϴ�.");
				} else {
					BaseFrame.eMsg("�������� �ʴ� ���� �Դϴ�.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
