package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends BaseFrame {
	String cap[] = "ID,PW".split(",");
	JTextField txt[] = { new JTextField(20), new JPasswordField(20) };

	public Login() {
		super("로그인", 300, 150);
		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new GridLayout(0, 1));
		add(c);
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout());
			tmp.add(size(lbl(cap[i], FlowLayout.LEFT), 40, 20));
			tmp.add(txt[i]);
			c.add(tmp);
		}

		add(s,"South");

		s.add(btn("로그인", a -> {

		}));
		
		((JPanel)getContentPane()).setBorder(new EmptyBorder(5,5,5,5));
		setVisible(true);
	}

	public static void main(String[] args) {
		new Login();
	}
}
