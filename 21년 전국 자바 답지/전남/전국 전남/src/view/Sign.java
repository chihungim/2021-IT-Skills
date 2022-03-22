package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Sign extends BaseFrame {
	String cap[] = "�̸�,���̵�,��й�ȣ,�������,������,�����Է�".split(",");

	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15) };
	JComboBox<String> loc;
	JComboBox birth[] = { new JComboBox(), new JComboBox(), new JComboBox() };
	JRadioButton chk;

	public Sign() {
		super("ȸ������", 350, 400);
		setUI();
		setData();
		addEvents();
		setVisible(true);
	}

	void setUI() {
		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(c);
		add(s, "South");
		for (int i = 0, idx = 0; i < cap.length; i++) {
			var t = new JPanel(new FlowLayout(FlowLayout.LEFT));
			t.add(sz(lbl(cap[i], JLabel.LEFT), 80, 20));
			if (i == 3) {
				String cap[] = "��,��,��".split(",");
				for (int j = 0; j < birth.length; j++) {
					t.add(birth[j]);
					t.add(lbl(cap[j], 0));
				}
			} else if (i == 4) {
				t.add(loc = new JComboBox<String>());
			} else {
				t.add(txt[idx]);
				idx++;
			}
			c.add(t);
		}

		for (var bcap : "ȸ������,���".split(",")) {
			s.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("���")) {
					dispose();
				} else {
					sign();
				}
			}));
		}

		c.add(chk = new JRadioButton("�������� �̿� ����"));

		c.setBorder(new MatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void setData() {
		for (int i = 1990; i < 2022; i++) {
			birth[0].addItem(i);
		}

		for (int i = 1; i < 13; i++) {
			birth[1].addItem(i);
		}

		try {
			var rs = stmt.executeQuery(
					"SELECT left(c_address,2) as loc FROM roomescape.cafe where left(c_address,2) <> '����' group by loc");
			while (rs.next()) {
				loc.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void sign() {
		for (int j = 0; j < txt.length; j++) {
			if (txt[j].getText().isEmpty()) {
				eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}
		}
		if (isExists("user", "u_id", txt[1].getText())) {
			eMsg("���̵� �ߺ��Ǿ����ϴ�.");
			return;
		}

		String pw = txt[2].getText();

		if (!(pw.matches(".*[0-9].*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[^0-9a-zA-Z��-�R].*"))
				|| pw.length() < 5) {
			eMsg("��й�ȣ ���� ��ġ���� �ʽ��ϴ�.");
			return;
		}

		if (!chk.isSelected()) {
			eMsg("�������� ���Ǹ� ���ּ���.");
			return;
		}

		String u_date = birth[0].getSelectedItem() + "-" + birth[1].getSelectedItem() + "-"
				+ birth[2].getSelectedItem();
		String u_address = loc.getSelectedItem() + " " + txt[3].getText();

		execute("insert into user values(0, '" + txt[0].getText() + "', '" + txt[2].getText() + "', '"
				+ txt[0].getText() + "', '" + u_date + "', '" + u_address + "')");

		iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
		dispose();
	}

	void addEvents() {
		for (int i = 0; i < birth.length - 1; i++) {
			birth[i].setSelectedIndex(-1);
			birth[i].addItemListener(a -> {
				if (birth[0].getSelectedIndex() == -1 || birth[1].getSelectedIndex() == -1) {
					return;
				}

				birth[2].removeAllItems();

				var date = LocalDate.of(toInt(birth[0].getSelectedItem()), toInt(birth[1].getSelectedItem()), 1);
				for (int j = 1; j <= date.lengthOfMonth(); j++) {
					birth[2].addItem(j);
				}
			});
		}
	}

	public static void main(String[] args) {
		new Sign();
	}
}
