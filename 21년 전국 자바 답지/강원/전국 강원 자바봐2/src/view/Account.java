package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Account extends BaseDialog {

	String lcap[] = "Id,pwd,name,email,point".split(",");
	JTextField txt[] = { new JTextField(20), new JTextField(20), new JTextField(20), new JTextField(20),
			new JTextField(20) };

	public Account(BaseFrame f) {
		super("계정", 400, 530, f);
		setUI();
		setData();
		setVisible(true);
	}

	void setUI() {

		setLayout(new BorderLayout());

		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new GridLayout(1, 0, 5, 5));

		add(c);
		add(s, "South");

		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout());
			tmp.add(size(lbl(lcap[i], JLabel.LEFT, 15), 60, 15));
			tmp.add(txt[i]);
			c.add(tmp);
			tmp.setOpaque(false);
		}

		txt[0].setEnabled(false);
		txt[4].setEnabled(false);

		for (String bcap : "수정,취소".split(",")) {
			s.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("수정")) {
					for (int i = 0; i < txt.length; i++) {
						if (txt[i].getText().isEmpty()) {
							emsg = new eMsg("공란을 확인해주세요.");
							return;
						}
					}
					execute("update user set pwd = '" + txt[1].getText() + "', name = '" + txt[2].getText()
							+ "', email = '" + txt[3].getText() + "' where id = '" + txt[0].getText() + "'");
				} else {
					dispose();
				}
			}));
		}

		c.setOpaque(false);
		s.setOpaque(false);

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(20, 20, 20, 20));
	}

	void setData() {
		try {
			var rs = stmt.executeQuery("select * from user where id = '" + u_id + "'");
			System.out.println(u_id);
			if (rs.next()) {
				for (int i = 0; i < txt.length; i++) {
					txt[i].setText(rs.getString(i + 2));
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
