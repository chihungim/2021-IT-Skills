package ����;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Sign extends Baseframe {
	String[] cap = "�̸�,���̵�,��й�ȣ,�������,������,�����Է�".split(","), bcap = "ȸ������,���".split(",");
	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15) };
	JComboBox birCombo[] = { new JComboBox(), new JComboBox(), new JComboBox() };
	JComboBox<String> reCombo;
	JRadioButton r = new JRadioButton("�������� �̿� ���Ǽ�");
	JPanel c, s;

	public Sign() {
		super("ȸ������", 350, 400);

		add(c = new JPanel(new GridLayout(0, 1)));
		add(s = new JPanel(new FlowLayout(FlowLayout.RIGHT)), "South");

		for (int i = 0, idx = 0; i < cap.length; i++) {
			var t = new JPanel(new FlowLayout(FlowLayout.LEFT));
			t.add(size(label(cap[i], JLabel.LEFT), 80, 20));
			if (i == 3) {
				String c[] = "��,��,��".split(",");
				for (int j = 0; j < birCombo.length; j++) {
					t.add(birCombo[j]);
					t.add(label(c[j], 0));
				}
			} else if (i == 4) {
				t.add(reCombo = new JComboBox<String>());
			} else {
				t.add(txt[idx]);
				idx++;
			}
			c.add(t);
		}

		c.add(r);
		c.add(new JLabel(" ") {
			@Override
			public void paint(Graphics g) {
				super.paint(g);

				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.drawLine(0, 20, getWidth(), 20);
			}
		});

		for (int i = 0; i < bcap.length; i++) {
			s.add(btn(bcap[i], a -> {
				if (a.getActionCommand().contains(bcap[0])) {
					for (int j = 0; j < txt.length; j++) {
						if (txt[j].getText().isEmpty()) {
							errmsg("��ĭ�� �ֽ��ϴ�.");
							return;
						}
					}

					if(!getone("select * from user where u_id='" + txt[1].getText() + "'").equals("")){
						errmsg("���̵� �ߺ��Ǿ����ϴ�.");
						return;
					}

					String t = txt[2].getText();

					if (!(t.matches(".*[0-9].*") && t.matches(".*[a-zA-Z].*") && t.matches(".*[^0-9a-zA-Z��-�R].*"))
							|| t.length() < 5) {
						errmsg("��й�ȣ ���� ��ġ���� �ʽ��ϴ�.");
						return;
					}

					if (!r.isSelected()) {
						errmsg("�������� ���Ǹ� ���ּ���.");
						return;
					}

					msg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
					String u_date = birCombo[0].getSelectedItem() + "-" + birCombo[1].getSelectedItem() + "-"
							+ birCombo[2].getSelectedItem();
					String u_address = reCombo.getSelectedItem() + " " + txt[3].getText();
					execute("insert into user values(0, '" + txt[0].getText() + "', '" + txt[2].getText() + "', '"
							+ txt[0].getText() + "', '" + u_date + "', '" + u_address + "')");
					dispose();
				} else {
					dispose();
				}
			}));
		}

		emp(c, 5, 5, 0, 5);

		for (int i = 1990; i < 2022; i++) {
			birCombo[0].addItem(i);
		}

		for (int i = 1; i < 13; i++) {
			birCombo[1].addItem(i);
		}

		for (int i = 0; i < birCombo.length - 1; i++) {
			birCombo[i].setSelectedIndex(-1);
			birCombo[i].addItemListener(a -> {
				if (birCombo[0].getSelectedIndex() == -1 || birCombo[1].getSelectedIndex() == -1) {
					return;
				}

				birCombo[2].removeAllItems();

				var date = LocalDate.of(toint(birCombo[0].getSelectedItem()), toint(birCombo[1].getSelectedItem()), 1);
				for (int j = 1; j <= date.lengthOfMonth(); j++) {
					birCombo[2].addItem(j);
				}
			});
		}

		birCombo[0].setSelectedIndex(0);
		birCombo[1].setSelectedIndex(0);

		try {
			ResultSet rs = stmt.executeQuery(
					"SELECT left(c_address,2) as loc FROM roomescape.cafe where left(c_address,2) <> '����' group by loc");
			while (rs.next()) {
				reCombo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

}
