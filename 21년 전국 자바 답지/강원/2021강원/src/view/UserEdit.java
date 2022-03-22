package view;

import static javax.swing.JOptionPane.showInputDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class UserEdit extends BaseDialog {

	String cap[] = "Id,pwd,name,email,point".split(",");
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	String id, pw, name, point, email;

	public UserEdit() {
		super("계정", 300, 400);
		data();
		ui();
	}

	void data() {
		try {
			var rs = stmt.executeQuery("select * from user where no = " + BaseFrame.uno);
			if (rs.next()) {
				id = rs.getString("id");
				pw = rs.getString("pwd");
				name = rs.getString("name");
				point = rs.getString("point");
				email = rs.getString("email");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void ui() {
		add(lbl("계정", JLabel.LEFT, 20), "North");
		var c = new JPanel(new GridLayout(0, 1, 10, 10));
		var s = new JPanel(new GridLayout(1, 0, 5, 5));
		add(c);
		add(s, "South");

		String txtC[] = { id, pw, name, point, email };

		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new BorderLayout(5, 5));
			tmp.add(sz(lbl(cap[i], JLabel.LEFT), 50, 30), "West");
			txt[i].setText(txtC[i]);
			tmp.add(txt[i]);
			c.add(tmp);
		}

		c.setBorder(new EmptyBorder(20, 0, 20, 0));

		txt[0].setEnabled(false);
		txt[4].setEnabled(false);
		txt[0].setDisabledTextColor(Color.BLACK);
		txt[4].setDisabledTextColor(Color.BLACK);

		for (var bcap : "수정,취소".split(",")) {
			s.add(btn(bcap, a -> {

				if (a.getActionCommand().equals("수정")) {
					if (!txt[1].getText().matches(".*[\\W].*")) {
						BaseFrame.eMsg("특수문자를 포함해주세요.");
						return;
					}

					execute("update user set(pwd = '" + txt[1].getText() + "', name = '" + txt[2].getText()
							+ "', email='" + txt[3].getText() + "' where no = " + BaseFrame.uno);

				} else {
					dispose();
				}
			}));
		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
	}

}
