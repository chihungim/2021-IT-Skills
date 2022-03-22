package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class Sign extends BaseDialog {

	JPanel mPanel;
	CardLayout pages;
	PlaceHolderField txt[] = { new PlaceHolderField("000-0000-0000"), new PlaceHolderField("example@example.net"),
			new PlaceHolderField("대소문자, 숫자, 특수기호를 포함한 6자 이상"), new PlaceHolderField("홍길동"),
			new PlaceHolderField("클릭하여 주소를 입력하기"), null, new PlaceHolderField("배달 수수료 입력") };

	String cap[] = "전화번호를 입력하세요(필수),이메일을 입력하세요.(필수),비밀번호를 입력하세요.(필수),이름을 입력하세요.(필수),주소를 입력하세요.(필수),,배달수수료를 입력하세요.(필수)"
			.split(",");

	JLabel loginlbl;

	public Sign() {
		super("회원가입", 400, 300);
		ui();
		setVisible(true);
	}

	void ui() {

		add(mPanel = new JPanel(pages = new CardLayout()));
		var s = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(s, "South");

		for (int i = 0; i < 7; i++) {
			var c = new JPanel(new BorderLayout());
			var c_c = new JPanel(new BorderLayout());
			c.add(lbl("시작하기.", JLabel.LEFT, 25), "North");
			c.add(c_c);
			if (txt[i] != null) {
				c_c.setLayout(new BorderLayout());
				c_c.add(lbl(cap[i], JLabel.LEFT, 12), "North");
				c_c.add(txt[i]);
				c_c.setBorder(new EmptyBorder(40, 0, 65, 0));
				final int j = i;
				c.add(btn("다음", a -> {
					if (txt[j].getText().equals("")) {
						eMsg("모든 항목을 입력해야 합니다.");
						return;
					}

					switch (j) {
					case 0:
						if (!txt[0].getText().matches(("^\\d{3}-\\d{4}-\\d{4}$"))) {
							eMsg("전화번호 형식이 올바르지 않습니다.");
							return;
						}

						for (var t : "user,seller,rider".split(",")) {
							try {
								if (!getOne("select * from " + t + " where PHONE = '" + txt[0].getText() + "'")
										.equals("")) {
									eMsg("이 번호의 계정이 이미 있습니다.");
									return;
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						break;
					case 1:
						if (!txt[1].getText().matches("^[a-zA-Z]+[0-9]+@(naver|daum|outlook|gmail).(com|net|kr)$")) {
							eMsg("이메일 형식이 올바르지 않습니다.");
							return;
						}
						for (var t : "user,seller,rider".split(",")) {
							try {
								if (!getOne("select * from " + t + " where EMAIL = '" + txt[1].getText() + "'")
										.equals("")) {
									eMsg("이 이메일의 계정이 이미 있습니다.");
									return;
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						break;
					case 2:
						if (!(txt[2].getText().matches(".*[a-z].*") && txt[2].getText().matches(".*[A-Z].*")
								&& txt[2].getText().matches(".*[0-9].*")) || txt[2].getText().length() < 9) {
							eMsg("비밀번호 형식이 올바르지 않습니다.");
							return;
						}
						break;
					case 6:
						if (!txt[6].getText().matches(".*[0-9].*")) {
							eMsg("배달 수수료 형식이 올바르지 않습니다.");
							return;
						}
						var mno = txt[4].getText().split(",")[0];
						JComboBox<String> box = new JComboBox<String>();

						try {
							var rs = stmt.executeQuery("select * from category");
							while (rs.next()) {
								box.addItem(rs.getString(2));
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						var ok = JOptionPane.showInternalConfirmDialog(null, box, "카테고리 선택하기",
								JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (ok == JOptionPane.OK_OPTION) {
							var cno = box.getSelectedIndex() + 1;
							execute("insert seller values(0,'" + txt[1].getText() + "','" + txt[0].getText() + "','"
									+ txt[2].getText() + "','" + txt[3].getText() + "',''," + cno + ","
									+ txt[6].getText() + "," + mno + ")");
							System.out.println("insert seller values(0,'" + txt[1].getText() + "','" + txt[0].getText()
									+ "','" + txt[2].getText() + "','" + txt[3].getText() + "',''," + cno + ","
									+ txt[6].getText() + "," + mno + ")");
						}

						dispose();
						new Login();

						return;
					}
					pages.next(mPanel);
				}), "South");
			} else {
				c_c.setLayout(new GridLayout(0, 1, 5, 5));
				for (var bcap : "일반 회원가입,판매자 회원가입,라이더 회원가입".split(",")) {
					c_c.add(btn(bcap, a -> {
						var mno = txt[4].getText().split(",")[0];
						if (a.getActionCommand().equals("판매자 회원가입")) {
							pages.next(mPanel);
						} else if (a.getActionCommand().equals("일반 회원가입")) {
							execute("insert user values(0,'" + txt[1].getText() + "','" + txt[0].getText() + "','"
									+ txt[2].getText() + "','" + txt[3].getText() + "','" + mno + "')");
							dispose();
							new Login();
						} else {
							execute("insert rider values(0,'" + txt[1].getText() + "','" + txt[0].getText() + "','"
									+ txt[2].getText() + "','" + txt[3].getText() + "','" + mno + "')");
							dispose();
							new Login();
						}
					}));
				}
				c_c.setBorder(new EmptyBorder(0, 0, 30, 0));
			}

			mPanel.add(i + "", c);
		}

		txt[4].addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				new Map(Sign.this).setVisible(true);
				super.mousePressed(e);
			}

		});

		s.add(lbl("이미 기능배달 회원이십니까?", JLabel.RIGHT));
		s.add(loginlbl = lbl("<html><font = \"맑은고딕\", color = 'green'><u>로그인", JLabel.LEFT));
		loginlbl.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				dispose();
				new Login();
			};

		});
		mPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	public static void main(String[] args) {
		new Sign();
	}
}
