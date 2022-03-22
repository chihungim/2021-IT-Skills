package 광광주;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Sign extends BaseFrame {

	JPanel page[] = new JPanel[7];
	PlaceHolderTextField txt[] = { new PlaceHolderTextField(20), new PlaceHolderTextField(20),
			new PlaceHolderTextField(20), new PlaceHolderTextField(20), new PlaceHolderTextField(20),
			new PlaceHolderTextField(20), };
	String placeHolders[] = "000-0000-0000+example@example.net+대소문자,숫자,특수기호를 포함한 8자 이상+홍길동+클릭하여 주소를 입력하기+1000"
			.split("\\+");
	String cap[] = "전화번호를 입력하세요.(필수),이메일을 입력하세요.(필수),비밀번호를 입력하세요(필수),이름을 입력하세요(필수),주소를 입력하세요(필수),배달 수수료를 입력하세요(필수)"
			.split(",");
	String type[] = "rider,user,seller".split(",");
	JComboBox<String> box = new JComboBox<String>();

	public Sign() {
		super("회원가입", 300, 300);
		init();
		setVisible(true);
	}

	void init() {
		add(masterP = new JPanel(pages = new CardLayout(0, 0)));
		for (int i = 0; i < 6; i++) {
			page[i] = new JPanel(new BorderLayout());
			JLabel login;
			JButton next;
			var c = new JPanel(new FlowLayout());
			var s = new JPanel(new FlowLayout());

			page[i].add(lbl("시작하기", JLabel.LEFT, 20), "North");
			page[i].add(c);
			page[i].add(s, "South");

			c.add(lbl(cap[i], JLabel.LEFT));
			c.add(txt[i]);

			if (i == 4)
				txt[i].setEnabled(false);

			txt[i].setPlaceHolder(placeHolders[i]);
			s.add(next = btn("다음", a -> {
				int idx = toInt(((JButton) a.getSource()).getName()) + 1;

				switch (idx - 1) {
				case 0:

					if (!txt[0].getText().matches(("^\\d{3}-\\d{4}-\\d{4}$"))) {

						eMsg("전화번호 형식이 올바르지 않습니다.");
						return;

					}

					for (String u : type) {
						try {
							var rs = stmt
									.executeQuery("select * from " + u + " where PHONE ='" + txt[0].getText() + "'");
							if (rs.next()) {
								eMsg("이 번호의 계정이 이미 있습니다.");
								return;
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					break;
				case 1:
					if (!txt[1].getText().matches("^[a-zA-Z]+[0-9]+@(naver|daum|outlook|gmail).(com|net|kr)$")) {
						eMsg("이메일 형식이 올바르지 않습니다.");
						return;
					}

					for (String u : type) {
						try {
							var rs = stmt
									.executeQuery("select * from " + u + " where EMAIL ='" + txt[1].getText() + "'");
							if (rs.next()) {
								eMsg("이 이메일의 계정이 이미 있습니다.");
								return;
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					break;
				case 2:
					if (!(txt[2].getText().matches(".*[a-z].*") && txt[2].getText().matches(".*[A-Z].*")
							&& txt[2].getText().matches(".*[0-9].*")) || txt[2].getText().length() < 9) {
						eMsg("비밀번호 형식이 올바르지 않습니다.");
						return;
					}
				case 4:
					txt[4].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							new Map(Sign.this).setVisible(true);
						}
					});
					break;
				case 6:
					if (!txt[5].getText().matches(".*[0-9].*")) {
						eMsg("배달 수수료 형식이 올바르지 않습니다.");
						return;
					}
					// insert seller values()
					break;
				}
				pages.show(masterP, idx + "");
			}));

			if (i == 5)
				next.setName(6 + "");
			else
				next.setName(i + "");

			s.add(lbl("이미 기능배달 회원입니까?", JLabel.RIGHT));
			s.add(login = lbl("<html><font color = \"green\"><u>로그인", JLabel.LEFT));

			login.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
				}
			});
			if (i == 5) {
				masterP.add(page[5], "6");
			} else {
				masterP.add(page[i], i + "");
			}
		}

		page[6] = new JPanel(new BorderLayout());

		JLabel login;
		var c = new JPanel(new FlowLayout());
		var s = new JPanel(new FlowLayout());

		page[6].add(c);
		page[6].add(s, "South");

		page[6].add(lbl("시작하기.", JLabel.LEFT), "North");
		for (String bcap : "일반 회원가입,관리자 회원가입,라이더 회원가입".split(",")) {
			c.add(btn(bcap, a -> {
				switch (((JButton) a.getSource()).getText()) {
				case "일반 회원가입":
					iMsg("기능배달의 회원이 되신 것을 환영합니다.");
					// insert user values()
					break;
				case "관리자 회원가입":
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
					JOptionPane.showMessageDialog(null, box, "카테고리 선택하기", JOptionPane.INFORMATION_MESSAGE);
					pages.show(masterP, "6");
					break;
				case "라이더 회원가입":
					iMsg("기능배달의 라이더가 되신 것을 환영합니다.");
					// insert rider values()
					break;
				}
			}));
		}

		s.add(lbl("이미 기능배달 회원입니까?", JLabel.RIGHT));
		s.add(login = lbl("<html><font color = \"green\"><u>로그인", JLabel.LEFT));

		login.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		masterP.add(page[6], "5");
	}

	public static void main(String[] args) {
		new Sign();
	}
}
