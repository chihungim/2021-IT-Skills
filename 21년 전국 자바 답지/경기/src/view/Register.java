package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Register extends BaseFrame {

	String cap[] = "�̸�,ID,PW,Ű,�������".split(",");
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	JCheckBox chk = new JCheckBox("�����");

	public Register() {
		super("ȸ������", 300, 400);
		ui();
		setVisible(true);
	}

	void ui() {
		var c = new JPanel(new GridLayout(0, 1, 5, 5));
		var s = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(c);
		add(s, "South");

		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new BorderLayout());
			tmp.add(sz(lbl(cap[i] + ":", JLabel.LEFT, 20), 100, 30), "West");
			tmp.add(txt[i]);
			c.add(tmp);
		}

		c.add(chk);

		chk.setHorizontalAlignment(SwingConstants.CENTER);

		s.add(btn("ȸ������", a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().equals("")) {
					eMsg("��ĭ���� �Է��ϼ���.");
					return;
				}
			}

			if (!getOne("select * from user where u_id like '%" + txt[1].getText() + "%'").equals("")) {
				eMsg("�̹� ������� ���̵� �Դϴ�.");
				return;
			}

			if (!(txt[2].getText().matches(".*[a-zA-Z].*") && txt[2].getText().matches(".*[0-9].*")
					&& txt[2].getText().matches(".*[!@#$].*")) || txt[2].getText().length() < 4) {
				eMsg("��й�ȣ�� Ȯ�����ּ���.");
				return;
			}

			if (txt[3].getText().matches(".*[^0-9].*")) {
				eMsg("���ڴ� �Է��� �Ұ��մϴ�.");
				return;
			}

			int age = 0;

			try {
				var bd = LocalDate.parse(txt[4].getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				if (bd.toEpochDay() > LocalDate.now().toEpochDay()) {
					eMsg("��������� Ȯ�����ּ���.");
					return;
				}

				age = getAge(bd);

			} catch (Exception e) {
				eMsg("��������� Ȯ�����ּ���.");
				return;
			}

			execute("insert user values(0,'" + txt[0].getText() + "','" + txt[1].getText() + "','" + txt[2].getText()
					+ "','" + txt[3].getText() + "','" + txt[4].getText() + "','" + age + "','"
					+ (chk.isSelected() ? 1 : 0) + "')");
			iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
			dispose();

		}));

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	public static void main(String[] args) {
		new Register();
	}
}
