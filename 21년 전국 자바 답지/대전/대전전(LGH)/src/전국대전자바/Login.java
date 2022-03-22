package 전국대전자바;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends BaseFrame {
	JPanel mainp[] = { new JPanel(), new JPanel(), new JPanel(new FlowLayout(0)), new JPanel(new FlowLayout(2)) };
	JPanel subp[] = { new JPanel(new GridLayout(0, 1)), new JPanel(new GridLayout(0, 1, 5, 5)),
			new JPanel(new GridLayout()) };
	JLabel lbl[] = { new JLabel("Orange Ticket"), new JLabel("ID"), new JLabel("PW") };
	JTextField txt[] = { new JTextField(15), new JPasswordField(15) };
	JButton btn[] = { new JButton("로그인"), new JButton("회원가입") };
	JCheckBox chk = new JCheckBox("아이디 저장");
	JComponent c[] = { lbl[0], subp[0], subp[1], subp[2], lbl[1], lbl[2], txt[0], txt[1], btn[0], chk, btn[1] };
	JPanel addp[] = { mainp[0], mainp[1], mainp[1], mainp[1], subp[0], subp[0], subp[1], subp[1], subp[2], mainp[2],
			mainp[3] };

	public Login() {
		super("로그인", 370, 230);
		setLayout(new FlowLayout(0));
		setVisible(true);

		cfw = getContentPane().getSize().width;
		cfh = getContentPane().getSize().height;

		ui();
		event();
		data();
	}

	private void data() {
	}

	private void event() {
		for (int i = 0; i < 2; i++) {
			btn[i].addActionListener(a -> {
				if (a.getActionCommand().equals("로그인")) {
					if (txt[0].getText().equals("") || txt[1].getText().equals("")) {
						eMsg("빈칸이 존재합니다.");
						return;
					}
					try {
						ResultSet rs = stmt.executeQuery("select * from user where u_id = '" + txt[0].getText()
								+ "' and u_pw = '" + txt[1].getText() + "'");
						if (rs.next()) {
							u_no = rs.getInt(1);
							u_name = rs.getString(2);
							iMsg(u_name + "님 환영합니다.");
							isLogined = true;
							new Main().addWindowListener(new before(Login.this));
							if (chk.isSelected()) {
								txt[1].setText("");
							} else {
								txt[0].setText("");
								txt[1].setText("");
							}
						} else {
							eMsg("ID 또는 PW가 일치하지 않습니다.");
							return;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					new Sign().addWindowListener(new before(Login.this));
				}
			});
		}
	}

	private void ui() {
		for (int i = 0; i < mainp.length; i++) {
			add(mainp[i]);
		}

		for (int i = 0; i < addp.length; i++) {
			addp[i].add(c[i]);
		}

		mainp[0].setPreferredSize(new Dimension(cfw, (int) (cfh * 0.35)));
		mainp[1].setPreferredSize(new Dimension(cfw, (int) (cfh * 0.4)));
		mainp[2].setPreferredSize(new Dimension((int) (cfw * 0.5), (int) (cfh * 0.25)));
		mainp[3].setPreferredSize(new Dimension((int) (cfw * 0.47), (int) (cfh * 0.25)));
		subp[0].setPreferredSize(new Dimension((int) (cfw * 0.15), (int) (cfh * 0.35)));
		subp[1].setPreferredSize(new Dimension((int) (cfw * 0.55), (int) (cfh * 0.35)));
		subp[2].setPreferredSize(new Dimension((int) (cfw * 0.2), (int) (cfh * 0.37)));
		lbl[0].setFont(new Font("HY헤드라인M", Font.BOLD + Font.ITALIC, 40));
	}

	public static void main(String[] args) {
		new Login();
	}
}
