package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Login extends BaseDialog {

	PlaceHolderField txt[] = { new PlaceHolderField("이메일 주소 또는 휴대폰 번호", 15),
			new PlaceHolderField("대소문자, 숫자, 특수기호를 포함한 8자 이상", 15) };
	CardLayout pages;
	JPanel mPanel;
	JLabel elbl[] = new JLabel[2];
	JPanel page[] = new JPanel[2];
	String cap[] = ("이메일 주소 또는 휴대폰번호로 로그인하세요.,비밀번호를 입력하고 로그인하세요.").split(","), bcap[] = "다음,로그인".split(",");
	String eCap[] = "이메일 주소 또는 휴대폰 번호로 로그인하세요.,비밀번호를 입력하고 로그인하세요".split(",");

	public Login() {
		super("로그인", 400, 300);
		ui();
		setVisible(true);
	}

	void ui() {
		add(mPanel = new JPanel(pages = new CardLayout()));
		for (var i = 0; i < 2; i++) {
			var p = new JPanel(new BorderLayout());
			var c = new JPanel(new BorderLayout());
			var s = new JPanel(new BorderLayout());
			var sign = lbl("<html><font color = \"green\"><u>계정만들기", JLabel.RIGHT);
			p.add(lbl("돌아오신것을 환영합니다.", JLabel.LEFT, 25), "North");
			p.add(c);
			p.add(s, "South");

			c.add(lbl(cap[i], JLabel.LEFT), "North");
			c.add(txt[i]);
			c.add(elbl[i] = new JLabel("", JLabel.LEFT), "South");

			elbl[i].setForeground(Color.RED);

			c.setBorder(new EmptyBorder(55, 0, 55, 0));

			s.add(btn(bcap[i], a -> {
				if (a.getActionCommand().equals("다음")) {
					if (txt[0].getText().equals("")) {
						eMsg("모든항목을 입력해야 합니다.");
						return;
					}

					try {
						var rs = stmt.executeQuery("select * from user u , seller s, rider r where u.EMAIL = '"
								+ txt[0].getText() + "' or u.PHONE = '" + txt[0].getText() + "' or s.EMAIL = '"
								+ txt[0].getText() + "' or s.PHONE = '" + txt[0].getText() + "' or r.EMAIL  = '"
								+ txt[0].getText() + "' or r.PHONE = '" + txt[0].getText() + "' ");
						if (rs.next()) {
							pages.show(mPanel, "1");
						} else {
							elbl[0].setText(eCap[0]);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						String type[] = "user,seller,rider".split(",");
						for (int j = 0; j < 3; j++) {
							var t = type[j];
							var rs = stmt.executeQuery("select * from " + t + " where (" + t + ".email = '"
									+ txt[0].getText() + "' or " + t + ".phone = '" + txt[0].getText() + "') and " + t
									+ ".PW = '" + txt[1].getText() + "'");
							if (rs.next()) {
								switch (j) {
								case 0:
									uno = rs.getInt(1);
									uname = rs.getString(5);
									break;
								case 1:
									sno = rs.getInt(1);
									sname = rs.getString(5);
									break;
								default:
									rno = rs.getInt(1);
									rname = rs.getString(5);
									break;
								}
							}
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (uno == 0 && sno == 0 && rno == 0) {
						eMsg("입력한 비밀번호가 올바르지 않습니다.");
						return;
					}

					if (uno != 0) {
						new Main().addWindowListener(new Before(this));
					} else if (sno != 0) {
						new DashBoard().addWindowListener(new Before(this));
					} else if (rno != 0) {
						new Delivery().addWindowListener(new Before(this));
					}
				}
			}), "North");
			var s_c = new JPanel(new FlowLayout(FlowLayout.CENTER));
			s.add(s_c);
			s_c.add(lbl("기능배달이 처음이십니까?", JLabel.RIGHT));
			s_c.add(sign);
			sign.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					new Sign().addWindowListener(new Before(Login.this));
					super.mousePressed(e);
				}
			});
			mPanel.add(i + "", p);
		}

		setBorder((javax.swing.JPanel) getContentPane(), new EmptyBorder(5, 5, 5, 5));
	}

	public static void main(String[] args) {
		new Login();
	}
}
