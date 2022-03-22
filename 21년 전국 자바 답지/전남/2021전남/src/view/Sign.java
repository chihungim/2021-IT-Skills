package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import db.DBManager;
import util.Util;

public class Sign extends BaseFrame {

	String[] cap = "이름,아이디,비밀번호,생년월일,거주지,직접입력".split(","), bcap = "회원가입,취소".split(",");
	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15) };
	JComboBox birCombo[] = { new JComboBox(), new JComboBox(), new JComboBox() };
	JComboBox<String> reCombo;
	JRadioButton r = new JRadioButton("개인정보 이용 동의서");
	JPanel c, s;

	public Sign() {
		super("회원가입", 350, 400);

		add(c = new JPanel(new GridLayout(0, 1)));
		add(s = new JPanel(new FlowLayout(FlowLayout.RIGHT)), "South");

		for (int i = 0, idx = 0; i < cap.length; i++) {
			var t = new JPanel(new FlowLayout(FlowLayout.LEFT));
			t.add(Util.sz(Util.lbl(cap[i], JLabel.LEFT), 80, 20));
			if (i == 3) {
				String c[] = "년,월,일".split(",");
				for (int j = 0; j < birCombo.length; j++) {
					t.add(birCombo[j]);
					t.add(Util.lbl(c[j], 0));
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
				g2.drawLine(0, 0, getWidth(), 0);
			}
		});

		for (int i = 0; i < bcap.length; i++) {
			s.add(Util.btn(bcap[i], a -> {
				if(a.getActionCommand().contains(bcap[0])) {
					for (int j = 0; j < txt.length; j++) {
						if (txt[j].getText().isEmpty()) {
							Util.eMsg("빈칸이 있습니다.");
							return;
						}
					}

					String t = txt[2].getText();

					if (!(t.matches(".*[0-9].*") || t.matches(".*[a-zA-Z].*") || t.matches(".*[\\W].*")) || t.length() < 5) {
						Util.eMsg("비밀번호 형식 일치하지 않습니다.");
						return;
					}

					if (!r.isSelected()) {
						Util.eMsg("개인정보 동의를 해주세요.");
						return;
					}
					
					var rs = DBManager.getOneRs("select * from user where u_id='" + txt[1].getText() + "'");
					if (rs.isEmpty()) {
						Util.iMsg("회원가입이 완료되었습니다.");
						String u_date = birCombo[0].getSelectedItem() + "-" + birCombo[1].getSelectedItem() + "-"
								+ birCombo[2].getSelectedItem();
						String u_address = reCombo.getSelectedItem() + " " + txt[3].getText();
						DBManager.execute("insert into user values(0, '" + txt[0].getText() + "', '" + txt[2].getText() + "', '"
								+ txt[0].getText() + "', '" + u_date + "', '" + u_address + "')");
					} else {
						Util.eMsg("아이디가 중보되었습니다.");
						return;
					}
				}else {
					dispose();
				}
			}));
		}

		Util.setEmpty(c, 5, 5, 5, 5);

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

				var date = LocalDate.of(Util.toInt(birCombo[0].getSelectedItem()),
						Util.toInt(birCombo[1].getSelectedItem()), 1);
				for (int j = 1; j <= date.lengthOfMonth(); j++) {
					birCombo[2].addItem(j);
				}
			});
		}

		birCombo[0].setSelectedIndex(0);
		birCombo[1].setSelectedIndex(0);

		setVisible(true);
	}

	public static void main(String[] args) {
		new Sign();
	}
}
