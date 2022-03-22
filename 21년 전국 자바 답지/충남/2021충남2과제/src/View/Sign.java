package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sign extends BaseFrame {
	String cap[] = "아이디,비밀번호,이름,생년월일,휴대전화".split(",");

	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(),
			new JTextField(), new JTextField(), new JTextField(), new JTextField(), };

	public Sign() {
		super("회원가입", 300, 400);
		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(c);
		add(s, "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout());
			tmp.add(lbl(cap[i], 0));

			if (i == 3) {
				String cap[] = "월,일".split(",");
				for (int j = 3; j < 6; j++) {
					tmp.add(txt[j]);
					if (j == 3)
						continue;
					tmp.add(lbl(cap[j - 4], 0));
				}
				c.add(tmp);
			}

			if (i == 4) {
				String cap[] = "-,-".split(",");
				for (int k = 6; k < 9; k++) {
					tmp.add(txt[k]);
					if (k == 8)
						continue;
					tmp.add(lbl(cap[k - 6], 0));

				}
				c.add(tmp);
			}

			if (i > 2)
				continue;
			tmp.add(txt[i]);

			c.add(tmp);
		}

		s.add(btn("회원가입", a -> {

		}));

		setVisible(true);
	}

	public static void main(String[] args) {
		new Sign();
	}
}
