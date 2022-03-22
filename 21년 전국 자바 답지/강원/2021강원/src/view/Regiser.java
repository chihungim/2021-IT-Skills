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
			new TextHolder("PasswordȮ��", 15), new TextHolder("E-mail", 15) };
	JComponent jc[] = { lbl("���� ����", JLabel.LEFT, 20), txt[0], txt[1], txt[2], txt[3], txt[4],
			btn("ȸ������", a -> event()) };

	public Regiser() {
		super("���� ����ϱ�", 500, 600);
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
				BaseFrame.eMsg("������ Ȯ�����ּ���.");
				return;
			}
		}

		if (!txt[2].getText().equals(txt[3].getText())) {
			BaseFrame.eMsg("PWȮ���� ��ġ���� �ʽ��ϴ�.");
			return;
		}

		if (!txt[2].getText().matches(".*[\\W].*")) {
			BaseFrame.eMsg("Ư�����ڸ� �������ּ���.");
			return;
		}

		if (!BaseFrame.getOne("select * from user where id = '" + txt[1].getText() + "'").equals("")) {
			BaseFrame.eMsg("Id�� �ߺ��Ǿ����ϴ�.");
			return;
		}

		if (!BaseFrame.getOne("select * from user where email = '" + txt[4].getText() + "'").equals("")) {
			BaseFrame.eMsg("E-mail�� �ߺ��Ǿ����ϴ�.");
			return;
		}

		BaseFrame.iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
		execute("insert into user values(0,'" + txt[1].getText() + "','" + txt[2].getText() + "','" + txt[0].getText()
				+ "','" + txt[4].getText() + "'," + 1000 + ")");
	}

	public static void main(String[] args) {
		new Regiser().setVisible(true);
	}
}
