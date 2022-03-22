package 광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class Sign extends Basedialog {

	JPanel c = new JPanel(new BorderLayout()), n = new JPanel(new BorderLayout()), ns = new JPanel(new FlowLayout(1));
	static PlaceH tt = new PlaceH(15);
	JLabel jl, err, sign;
	static JButton jb;
	int phase = 0;
	JPanel temp = new JPanel(new GridLayout(0, 1, 0, 10));
	String t = "", email, phone, pw, name, map;
	String str[] = "전화번호,이메일,비밀번호,이름,주소,배달 수수료".split(","),
			aa[] = "000-0000-0000/example@example.net/대소문자, 숫자, 특수기호를 포함한 8자 이상/홍길동/클릭하여 주소를 입력하기/1000".split("/");
	String type[] = "user,seller,rider".split(",");

	public Sign() {
		super("회원가입", 400, 300);
		add(size(labelP("시작하기", 2, 30), 0, 70), "North");
		add(c);
		add(n, "South");

		c.add(jl = label(str[0] + "를 입력하세요.(필수)", 2), "North");
		c.add(tt);
		tt.setPlace(aa[0]);
		c.add(err = label("모든 항목을 입력해야 합니다.", 2), "South");
		err.setForeground(Color.WHITE);

		n.add(jb = btn("다음", a -> {
			if (tt.getText().isEmpty()) {
				setError("모든 항목을 입력해야 합니다.");
				return;
			}
			if (phase == 0) {
				if (!tt.getText().matches("\\d{3}-\\d{4}-\\d{4}")) {
					setError("전화번호 형식이 올바르지 않습니다.");
					return;
				}
				for (int i = 0; i < type.length; i++) {
					if (!getone("select * from " + type[i] + " where phone = '" + tt.getText() + "'").equals("")) {
						setError("이 번호의 계정이 이미 있습니다.");
						return;
					}
				}
				phone = tt.getText();
				phase++;
				nextPhase();
			} else if (phase == 1) {
				if (!tt.getText().contains("@")) {
					setError("이메일 형식이 올바르지 않습니다.");
					return;
				}
				if(!(tt.getText().matches("^[a-zA-Z]+[0-9]+@(outlook|daum|naver|gmail).(net|com|kr)$"))) {
					setError("이메일 형식이 올바르지 않습니다.");
					return;
				}
				for (int i = 0; i < type.length; i++) {
					if (!getone("select * from " + type[i] + " where email = '" + tt.getText() + "'").equals("")) {
						setError("이 이메일의 계정이 이미 있습니다.");
						return;
					}
				}
				email = tt.getText();
				phase++;
				nextPhase();
			} else if (phase == 2) {
				if (!(tt.getText().matches(".*[a-zA-Z].*") && tt.getText().matches(".*[0-9].*")
						&& tt.getText().matches(".*[!@#$].*")) || tt.getText().length() < 8) {
					setError("비밀번호 형식이 올바르지 않습니다.");
					return;
				}
				pw = tt.getText();
				phase++;
				nextPhase();
			} else if (phase == 3) {
				name = tt.getText();
				phase++;
				nextPhase();
				jb.setEnabled(false);
			} else if (phase == 4) {
				map = tt.getText();
				remove(c);
				n.remove(jb);
				String asd[] = "일반,판매자,라이더".split(",");
				JButton ja[] = new JButton[3];
				for (int i = 0; i < asd.length; i++) {
					temp.add(ja[i] = btn(asd[i] + " 회원가입", b -> {
						if (b.getSource().equals(ja[0])) {
							msg("기능배달의 회원이 되신 것을 환영합니다.");
							execute("insert into user values(0,'" + email + "','" + phone + "','" + pw + "','" + name
									+ "','" + map + "')");
							dispose();
							new Login();
						}
						if (b.getSource().equals(ja[1])) {
							remove(temp);
							add(c);
							n.add(jb, "North");
							phase++;
							nextPhase();
							repaint();
							revalidate();
						}
						if (b.getSource().equals(ja[2])) {
							msg("기능배달의 라이더가 되신 것을 환영합니다.");
							execute("insert into rider values(0,'" + email + "','" + phone + "','" + pw + "','" + name
									+ "','" + map + "')");
							dispose();
							new Login();
						}
					}));
				}
				add(temp);
				repaint();
				revalidate();
			} else {
				if(tt.getText().matches(".*\\D.*")) {
					setError("배달 수수료 형식이 올바르지 않습니다.");
					return;
				}
				
				JComboBox<String> combo = new JComboBox<String>();
				try {
					ResultSet rs = stmt.executeQuery("select name from category");
					while (rs.next()) {
						combo.addItem(rs.getString(1));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, combo, "카테고리 선택하기", JOptionPane.INFORMATION_MESSAGE);
				msg("기능배달의 판매자가 되신 것을 환영합니다.");
				execute("insert into seller values(0,'" + email + "','" + phone + "','" + pw + "','" + name
						+ "','','"+getone("select no from category where name = '"+combo.getSelectedItem()+"'")+"','"+tt.getText()+"','"+map+"')");
				dispose();
				new Login();
			}
		}));

		tt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (phase != 4)
					return;
				new Map(Sign.this);
			}
		});
		n.add(ns, "South");

		ns.add(label("이미 기능배달 회원입시니까?", 0));
		ns.add(sign = label("로그인", 2));
		sign.setForeground(Color.GREEN.darker().darker());
		sign.setBorder(new MatteBorder(0, 0, 1, 0, green));

		sign.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Login();
			}
		});

		emp((JPanel) getContentPane(), 10, 10, 10, 10);
		emp(c, 10, 0, 20, 0);

		size(n, 0, 60);

		c.setOpaque(false);
		n.setOpaque(false);
		ns.setOpaque(false);

		setVisible(true);
	}

	void setError(String str) {
		err.setText(str);
		err.setForeground(Color.RED);
	}

	void nextPhase() {
		jl.setText(str[phase] + "를 입력하세요.(필수)");
		err.setForeground(Color.WHITE);
		tt.setText("");
		tt.setPlace(aa[phase]);
	}

	public static void main(String[] args) {
		new Sign();
	}

}
