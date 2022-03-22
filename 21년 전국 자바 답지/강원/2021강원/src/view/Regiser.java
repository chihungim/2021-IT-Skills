package view;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Regiser extends BaseDialog {

	JTextField txt[] = { new TextHolder("Name", 15), new TextHolder("Id", 15), new TextHolder("Password", 15),
			new TextHolder("Password확인", 15), new TextHolder("E-mail", 15) };
	JComponent jc[] = { lbl("계정 정보", JLabel.LEFT, 20), txt[0], txt[1], txt[2], txt[3], txt[4],
			btn("회원가입", a -> event()) };

	public Regiser() {
		super("계정 등록하기", 500, 600);
		ui();
	}

	void ui() {
		setLayout(new GridLayout(0, 1, 20, 20));
		Arrays.stream(jc).forEach(this::add);
		((JPanel) getContentPane()).setBorder(new EmptyBorder(50, 50, 50, 50));
	}

	void event() {
		for (int i = 0; i < txt.length; i++) {
			if (txt[i].getText().equals("")) {
				BaseFrame.eMsg("공란을 확인해주세요.");
				return;
			}
		}

		if (!txt[2].getText().equals(txt[3].getText())) {
			BaseFrame.eMsg("PW확인이 일치하지 않습니다.");
			return;
		}

		if (!txt[2].getText().matches(".*[\\W].*")) {
			BaseFrame.eMsg("특수문자를 포함해주세요.");
			return;
		}

		if (!BaseFrame.getOne("select * from user where id = '" + txt[1].getText() + "'").equals("")) {
			BaseFrame.eMsg("Id가 중복되었습니다.");
			return;
		}

		if (!BaseFrame.getOne("select * from user where email = '" + txt[4].getText() + "'").equals("")) {
			BaseFrame.eMsg("E-mail이 중복되었습니다.");
			return;
		}

		BaseFrame.iMsg("회원가입이 완료되었습니다.");
		execute("insert into user values(0,'" + txt[1].getText() + "','" + txt[2].getText() + "','" + txt[0].getText()
				+ "','" + txt[4].getText() + "'," + 1000 + ")");
	}

	public static void main(String[] args) {
		new Regiser().setVisible(true);
	}
}
