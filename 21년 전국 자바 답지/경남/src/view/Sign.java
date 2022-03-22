package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Sign extends BaseFrame {
	JCheckBox chk[] = { new JCheckBox("<html>�ʼ����� �׸� �� [����] �������� ���� �� �̿뵿��, [����] ���� ���� <br>�̸���/SMS ���� ���ǿ� �ϰ� �����մϴ�."),
			new JCheckBox("[�ʼ�] �� 15�� �̻��Դϴ�."), new JCheckBox("[�ʼ�] ���� �̿��� ����"),
			new JCheckBox("[�ʼ�] �������� ���� �� �̿� ����"), new JCheckBox("[����] ���� ���� �̸���/SMS ���� ����"), };
	String cap[] = "���̵�,��й�ȣ,��й�ȣ Ȯ��,�̸�,��ȭ��ȣ,�������,�̸���,�ּ�".split(",");
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(),
			new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	JComboBox combo[] = { new JComboBox(), new JComboBox(), new JComboBox<String>() };
	boolean state;

	public Sign() {
		super("ȸ������", 450, 700);

		add(n = new JPanel(new GridLayout(0, 1)), "North");
		add(c = new JPanel(new BorderLayout()));
		add(s = new JPanel(), "South");

		for (int i = 0; i < chk.length; i++) {
			n.add(chk[i]);
			chk[i].addActionListener(a -> {
				if (a.getSource().equals(chk[0])) {
					for (int j = 1; j < chk.length; j++) {
						chk[j].setSelected(chk[0].isSelected());
					}
				} else {
					for (int j = 1; j < chk.length; j++) {
						if (!chk[j].isSelected()) {
							chk[0].setSelected(false);
							return;
						}
					}
					chk[0].setSelected(true);
				}
			});
		}

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);
		c_c.setBorder(new TitledBorder(new LineBorder(Color.black), "ȸ������", TitledBorder.LEFT, TitledBorder.TOP));
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i]), 100, 25));
			if (i < 6) {
				tmp.add(sz(txt[i], 150, 25));
				if (i == 0) {
					tmp.add(sz(btn("�ߺ�Ȯ��", a -> {
						if (txt[0].getText().isEmpty()) {
							eMsg("��ĭ�Դϴ�.");
							return;
						}
						if (!checkIsExists("select * from user where id ='" + txt[0].getText() + "'")) {
							eMsg("��� �Ұ����մϴ�.");
							txt[0].setText("");
						} else {
							iMsg("��� �����մϴ�.");
							state = true;
						}
					}), 100, 25));
				} else if (i == 5) {
					tmp.add(sz(new JLabel("-", 0), 20, 25));
					tmp.add(sz(combo[0], 50, 25));
				}
			} else {
				if (i == 6) {
					tmp.add(sz(txt[6], 80, 25));
					tmp.add(new JLabel("@", 0));
					tmp.add(sz(txt[7], 80, 25));
					tmp.add(sz(combo[1], 80, 25));
				} else {
					tmp.add(sz(combo[2], 100, 25));
					tmp.add(sz(txt[8], 170, 25));
				}
			}
			c_c.add(tmp);
		}

		s.add(sz(btn("�����ϱ�", a -> {
			if (!(chk[1].isSelected() && chk[2].isSelected() && chk[3].isSelected())) {
				eMsg("ȸ�� �̿����� �����ϼž� ������ �����մϴ�.");
				return;
			}
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty()) {
					eMsg("��ĭ�� �ֽ��ϴ�.");
					return;
				}
			}
			if (!state) {
				eMsg("�ߺ�Ȯ���� ���ּ���.");
				return;
			}
			String pw = txt[1].getText();
			if (!(pw.matches(".*\\d.*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[!@#$%^&*].*")
					&& pw.length() >= 8 && pw.length() <= 16)) {
				eMsg("��й�ȣ ������ �ƴմϴ�.");
				return;
			}
			if (!pw.contentEquals(txt[2].getText())) {
				eMsg("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				return;
			}
			if (txt[4].getText().length() != 11) {
				eMsg("��ȭ��ȣ�� 11�ڸ����� �մϴ�.");
				return;
			}

			try {
				LocalDate l = LocalDate.parse(txt[5].getText(), DateTimeFormatter.ofPattern("yyyyMMdd"));
				if (l.isAfter(LocalDate.now())) {
					eMsg("��������� �ٽ� �Է����ּ���.");
					return;
				}
//				SELECT distinct SUBSTRING_INDEX(address, ' ', 1) FROM albajava.company where left(SUBSTRING_INDEX(address, ' ', 1), 2)='��õ'
				iMsg("������ �Ϸ�Ǿ����ϴ�.");
				String mail = ((combo[1].getSelectedIndex() == 0) ? txt[7].getText() : combo[1].getSelectedItem() + "");
				String phone = txt[4].getText().replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
				String gender = (combo[2].getSelectedIndex() == 0) ? "��" : "��";
				execute("insert into user values(0,'" + txt[0].getText() + "','" + txt[1].getText() + "','"
						+ txt[3].getText() + "','" + txt[4].getText() + "@" + mail + "','" + phone + "','"
						+ l.toString() + "','" + gender + " " + txt[8].getText() + "')");
			} catch (Exception e1) {
				eMsg("��������� �ٽ� �Է����ּ���.");
				return;
			}

			combo[0].addItem("3");
			combo[0].addItem(4);

			String mail[] = "��Ÿ,empal.com,gmail.com,hanmail.net,kebi.com,korea.com,nate.com,naver.com,yahoo.com"
					.split(",");
			for (int i = 0; i < mail.length; i++) {
				combo[1].addItem(mail[i]);
			}

			combo[1].addItemListener(i -> {
				txt[7].setEditable(combo[1].getSelectedIndex() == 0);
			});

			for (int i = 0; i < addr.length; i++) {
				combo[2].addItem(addr[i]);
			}

			txt[0].addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					state = false;
				}
			});

			txt[4].addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!isNumeric(e.getKeyChar())) {
						e.consume();
					}
				}
			});
			txt[5].addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!isNumeric(e.getKeyChar())) {
						e.consume();
					}
				}
			});

			setEmpty((JPanel) getContentPane(), 5, 10, 5, 10);
			this.setVisible(true);

		}), 100, 25));
		setVisible(true);
	}
}
