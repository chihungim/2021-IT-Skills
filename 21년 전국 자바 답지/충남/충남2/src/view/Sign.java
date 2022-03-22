package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.stream.Stream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Execute;

public class Sign extends BaseFrame {
	JTextField txt[] = new JTextField[3];
	JTextField birth[] = new JTextField[3];
	JTextField phone[] = new JTextField[3];
	String cap[] = "아이디,비밀번호,이름,생년월일,휴대전화".split(",");

	public Sign() {
		super("회원가입", 350, 350);

		add(c = new JPanel(new GridLayout(0, 1, 10, 10)));
		add(s = new JPanel(new FlowLayout(FlowLayout.RIGHT)), "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new BorderLayout());
			tmp.add(sz(lbl(cap[i], JLabel.LEFT), 50, 20), "West");
			if (txt.length > i)
				tmp.add(txt[i] = new JTextField(15));
			else if (i == 3) {
				var tmp2 = new JPanel();
				tmp2.setLayout(new BoxLayout(tmp2, BoxLayout.X_AXIS));
				int widths[] = { 80, 60, 60 };
				for (int j = 0; j < txt.length; j++) {
					tmp2.add(Box.createHorizontalStrut(5));
					final int idx = j;
					tmp2.add(birth[j] = new JTextField(5) {
						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);
							if (super.getText().length() == 0) {
								g.drawString("yyyy,mm,dd".split(",")[idx], super.getInsets().left, 10);
							}
						}
					});
					tmp2.add(Box.createHorizontalStrut(5));
					birth[j].setMinimumSize(new Dimension(widths[j], 30));
					birth[j].setMaximumSize(new Dimension(widths[j], 30));
					if (j != 0)
						tmp2.add(lbl("월,일".split(",")[j - 1], JLabel.CENTER));
				}
				tmp.add(tmp2);
			} else {
				var tmp3 = new JPanel();
				tmp3.setLayout(new BoxLayout(tmp3, BoxLayout.X_AXIS));
				for (int k = 0; k < 3; k++) {
					final int idx = k;
					tmp3.add(phone[k] = new JTextField(6) {
						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);
							if (super.getText().length() == 0) {
								g.drawString("000,0000,0000".split(",")[idx], super.getInsets().left, 20);
							}
						}
					});
					if (k < 2) {
						tmp3.add(lbl("-", JLabel.LEFT));
					}
				}
				tmp.add(tmp3);
			}
			c.add(tmp);
		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		s.add(btn("회원가입", a -> {
			Stream.of(txt, phone, birth).flatMap(Stream::of).forEach(t -> {
				if (t.getText().equals("")) {
					eMsg("빈칸을 모두 입력해야합니다");
					return;
				}
			});

			if (!getOne("select * from user where id = '" + txt[0].getText() + "'").equals("")) {
				eMsg("이미 사용중인 아이디 입니다.");
				return;
			}

			if (!(txt[1].getText().matches(".*[a-zA-Z].*") && txt[1].getText().matches(".*[0-9].*")
					&& txt[1].getText().matches(".*[!@#$].*"))) {
				eMsg("비밀번호는 영문, 숫자, 특수문자를 포함해야합니다.");
				return;
			}

			try {
				LocalDate bd = LocalDate.of(toInt(birth[0].getText()), toInt(birth[1].getText()),
						toInt(birth[2].getText()));
				if (LocalDate.now().isBefore(bd)) {
					eMsg("생년월일은 미래가 아니어야 합니다.");
				}

				String pn = phone[0].getText() + "-" + phone[1].getText() + "-" + phone[2].getText();

				if (!pn.matches("\\d{3}-\\d{4}-\\d{4}")) {
					eMsg("전화번호를 올바른 형식으로 입력해야 합니다.");
					return;
				}

				iMsg("회원가입이 완료되었습니다.");
				execute("insert into user values(0,'" + txt[0].getText() + "','" + txt[1].getText() + "','"
						+ txt[2].getText() + "','" + bd + "','" + pn + "')");
				dispose();
			} catch (Exception e) {
				eMsg("생년월일은 올바른 형식으로 입력해야 합니다.");
				return;
			}

		}));

		setVisible(true);
	}

	public static void main(String[] args) {
		new Sign();
	}
}
