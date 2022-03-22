import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class Login extends BaseFrame {
	JLabel imgLbl = new JLabel(), loginLbl;
	JLabel lbl[] = new JLabel[2];

	JTextField idTxt = new JTextField();
	JPasswordField pwTxt = new JPasswordField();

	JButton btn;

	String cap[] = "Id,Password".split(",");

	public Login() {
		super("버스예매", 1100, 600);

		ui();
		event();

		new AdminMain().addWindowListener(new Before(Login.this));
		setVisible(true);
	}

	void ui() {
		setLayout(null);

		imgLbl.setIcon(img("login.jpg", 600, 600));
		txt(idTxt, cap[0]);
		txt(pwTxt, cap[1]);

		setBounds(imgLbl, 0, 0, 600, 600);
		setBounds(loginLbl = lbl("로그인", 0, 25), 700, 140, 80, 40);
		setBounds(idTxt, 700, 200, 200, 20);
		setBounds(pwTxt, 700, 250, 200, 20);
		setBounds(lbl[0] = lbl("아이디/비밀번호 찾기", 2), 700, 280, 180, 35);
		setBounds(btn = btn("다음", e -> {
			if (idTxt.getText().contentEquals("") || idTxt.getText().equals("Id")) {
				eMsg = new Emsg("아이디를 입력해주세요.");
				return;
			} else if (pwTxt.getText().contentEquals("") || pwTxt.getText().equals("Password")) {
				eMsg = new Emsg("비밀번호를 입력해주세요.");
				return;
			}

			if (idTxt.getText().equals("admin") && pwTxt.getText().equals("1234")) {
				iMsg = new Imsg("관리자로 로그인합니다.");
				new AdminMain().addWindowListener(new Before(Login.this));
				return;
			}

			new UserMain().addWindowListener(new Before(Login.this));
		}), 910, 190, 80, 80);
		setBounds(themeButton(), 910, 280, 80, 30);
		setBounds(lbl[1] = lbl("새로운 계정 만들기 →", 2), 700, 400, 180, 35);
	}

	void event() {
		lbl[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new FindIdPw().setVisible(true);
			}
		});

		lbl[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Sign().setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		new Login();
		
		//UIDefaults 키 리스트 출력
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		for (Object key : defaults.keySet()) {
			System.out.println(key);
		}
	}
}
