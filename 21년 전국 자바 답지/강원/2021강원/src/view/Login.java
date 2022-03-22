package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Login extends BaseFrame {

	JTextField txt[] = { new TextHolder("ID", 15), new TextHolderPW("PASSWORD", '*', 15) };
	JLabel lblFind, lblRegister;

	public Login() {
		super("버스예매", 1200, 600);
		ui();
		events();
		setVisible(true);
	}

	void ui() {
		var c = new JPanel(new BorderLayout());
		var c_c = new JPanel(new BorderLayout());
		var c_e = new JPanel(null);

		add(c);
		c.add(c_c);
		c.add(sz(c_e, 400, 1), "East");
		c_c.add(new JLabel(getIcon("지급파일/images/login.jpg", 800, 600)));

		// 88,150
		var login_p = new JPanel(new BorderLayout(5, 5));
		var login_c = new JPanel(new GridLayout(0, 1, 5, 5));
		var login_e = new JPanel(new BorderLayout(5, 5));
		login_p.setBounds(50, 150, 300, 120);
		c_e.add(login_p);
		login_p.add(login_c);
		login_p.add(login_e, "East");
		login_p.add(lbl("로그인", JLabel.LEFT, 20), "North");
		login_c.add(txt[0], "North");
		login_c.add(txt[1]);
		login_e.add(btn("다음", a -> {
			var id = txt[0].getText();
			var pw = txt[1].getText();
			if (id.equals("")) {
				eMsg("아이디를 입력해주세요.");
				return;
			} else if (pw.equals("")) {
				eMsg("비밀번호를 입력해주세요.");
				return;
			}

			if (id.equals("admin") && pw.equals("1234")) {
				iMsg("관리자로 로그인 합니다.");
				new AdminMain().addWindowListener(new Before(this));
				return;
			}
			var no = getOne("select * from user where id = '" + id + "' and pw = '" + pw + "'");
			setLogin(no);
			new Main().addWindowListener(new Before(this));
		}));
		login_c.add(lblFind = lbl("아이디/비밀번호 찾기", JLabel.LEFT, 12));
		login_e.add(themeButton(), "South");

		c_e.add(lblRegister = lbl("새로운 계정 만들기→", JLabel.LEFT, 12));
		lblRegister.setBounds(50, 280, 120, 120);
	}

	void events() {
		lblRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Regiser().setVisible(true);
				super.mousePressed(e);
			}
		});

		lblFind.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Find().setVisible(true);
				super.mousePressed(e);
			}
		});
	}

	public static void main(String[] args) {
		new Login();
	}
}
