import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Login extends BaseFrame {

	String cap[] = "ID,PW".split(",");
	JTextField txt[] = { new JTextField(15), new JPasswordField(15) };
	JCheckBox chk;

	public Login(BaseFrame b) {
		super("로그인", 350, 200);
		setLayout(new BorderLayout(5, 5));

		add(lbl("Orange Ticket", JLabel.CENTER, 40), "North");

		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new BorderLayout(5, 5));

		add(c);
		add(s, "South");

		for (int i = 0; i < 2; i++) {
			var tmp = new JPanel(new FlowLayout());
			tmp.add(size(lbl(cap[i], JLabel.LEFT), 20, 20));
			tmp.add(txt[i]);
			c.add(tmp);
		}

		add(btn("로그인", a -> {
			if (txt[0].getText().equals("") || txt[1].getText().equals("")) {
				eMsg("빈칸이 존재합니다.");
				return;
			}

			try {
				var rs = stmt.executeQuery("select * from user where u_id = '" + txt[0].getText() + "' and u_pw = '"
						+ txt[1].getText() + "'");
				if (rs.next()) {
					u_no = rs.getInt(1);
					u_name = rs.getString(2);
					iMsg(u_name + "님 환영합니다.");
					isLogined = true;
					if (chk.isSelected()) {
						txt[1].setText("");
					} else {
						txt[0].setText("");
						txt[1].setText("");
					}
					remId = true;
					u_id = rs.getString(3);

					var imagebytes = rs.getBytes(5);
					var image = getToolkit().createImage(imagebytes);
					Image img = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(img);
					if (b instanceof Main) {
						Main m = (Main) b;
						m.uImg.setIcon(icon);
						m.uImg.setBorder(new LineBorder(Color.BLACK));
						m.loginlbl.setText("LOGOUT");
					} else {
						for (Window w : getWindows()) {
							if (w instanceof Main) {
								var m = (Main) w;
								m.uImg.setIcon(icon);
								m.uImg.setBorder(new LineBorder(Color.BLACK));
								m.loginlbl.setText("LOGOUT");
							}
						}
						dispose();
					}
					dispose();
				} else {
					eMsg("ID 또는 PW가 일치하지 않습니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}), "East");

		s.add(chk = new JCheckBox("아이디 저장"), "West");

		s.add(btn("회원가입", a -> {

		}), "East");

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(10, 10, 10, 10));
		if (remId) {
			txt[0].setText(u_id);
		}

		chk.setSelected(remId);
		setVisible(true);
	}

	@Override
	public JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("HY헤드라인M", Font.BOLD + Font.ITALIC, size));
		return l;
	}
}
