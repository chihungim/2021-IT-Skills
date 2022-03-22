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

	JButton btn1 = btn("계속", a -> {

	}), btn2 = btn("게속", a -> {

	});

	JTextField idtxt[] = { new TextHolder("Name", 15), new TextHolder("E-mail", 15) };
	JTextField pwtxt[] = { new TextHolder("Name", 15), new TextHolder("Id", 15), new TextHolder("E-mail", 15) };

	JComponent jc[] = { lbl("아이디 찾기", JLabel.LEFT, 20), idtxt[0], idtxt[1], btn1, lbl("비밀번호 찾기", JLabel.LEFT, 20),
			pwtxt[0], pwtxt[1], pwtxt[2], btn2 };

	public Find() {
		super("아이디/비밀번호 찾기", 400, 600);
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
				BaseFrame.eMsg("공란을 확인해주세요.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where name = '" + name + "' and email='" + mail + "'");
				if (rs.next()) {
					BaseFrame.iMsg("귀하의 Id는 " + rs.getString(2) + "입니다.");
				} else {
					BaseFrame.eMsg("존재하지 않는 정보입니다.");
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
				BaseFrame.eMsg("공란을 확인해주세요.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where name = '" + name + "' and email = '" + mail
						+ "' and id = '" + id + "'");
				if (rs.next()) {
					BaseFrame.iMsg("귀하의 Id에 PW는 " + rs.getString(3) + "입니다.");
				} else {
					BaseFrame.eMsg("존재하지 않는 정보 입니다.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
