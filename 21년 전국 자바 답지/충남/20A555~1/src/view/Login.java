package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.DBManager;

public class Login extends BaseFrame {
	
	String cap[] = "ID,PW".split(",");
	JTextField txt[] = {
			new JTextField(),
			new JPasswordField()
	};
	
	public Login() {
		super("로그인", 350, 200);
		
		this.add(c = new JPanel(new BorderLayout()));
		JPanel cc = new JPanel(new GridLayout(0, 1));
		c.add(cc);
		c.add(s = new JPanel(new BorderLayout()), "South");
		
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(sz(new JLabel(cap[i]), 60, 25));
			tmp.add(sz(txt[i], 240, 30));
			cc.add(tmp);
		}
		
		s.add(sz(btn("로그인", a->{
			String id = txt[0].getText(), pw = txt[1].getText();
			if (id.isEmpty() || pw.isEmpty()) {
				eMsg("아이디와 비밀번호를 모두 입력해야 합니다.");
				return;
			}
			if (id.equals("admin") && pw.equals("1234")) {
				iMsg("관리자님 환영합니다.");
				
				this.dispose();
				h.setVisible(false);
				h.setLog(false);
				
				new Admin();
				return;
			}
			
			try {
				var rs = DBManager.rs("select * from user where id = '"+id+"' and pw = '"+pw+"'");
				if (rs.next()) {
					uno = rs.getString(1);
					uname = rs.getString(4);
					LocalDate birth = LocalDate.parse(rs.getString(5));
					LocalDate now = LocalDate.now();
					ubirth = birth.getYear();
					uage = now.minusYears(birth.getYear()).getYear();
					iMsg(rs.getString("name")+"님 안녕하세요");
					
					h.setLog(true);
					
					h.setVisible(true);
					this.dispose();
				} else {
					eMsg("일치하는 회원정보가 없습니다.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}), 1, 30));
		
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				h.setVisible(true);
			}
		});
		
		this.setVisible(true);
	}

}
