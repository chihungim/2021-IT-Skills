package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBManager;

public class SignUp extends BaseFrame {
	
	JTextField txt[] = new JTextField[3];
	JTextField dtxt[] = new JTextField[3];
	JTextField ptxt[] = new JTextField[3];
	String cap[] = "아이디,비밀번호,이름,생년월일,휴대전화".split(",");
	String dcap[] = "월,일".split(",");
	String dateHolder[] = "yyyy,mm,dd".split(",");
	
	public SignUp() {
		super("회원가입", 380, 350);
		
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");
		
		var cc =new JPanel(new GridLayout(0, 1));
		c.add(cc);
		
		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i]), 60, 25));
			tmp.add(sz(txt[i]= new JTextField(), 260, 30));
			cc.add(tmp);
		}
		
		var dateP = new JPanel(new FlowLayout(0));
		dateP.add(sz(new JLabel(cap[3]), 60, 25));
		for (int i = 0; i < dtxt.length; i++) {
			if (i==0) {
				dateP.add(sz(dtxt[i] = new JTextField(), 90, 30));
			} else {
				dateP.add(sz(dtxt[i] = new JTextField(), 65, 30));
				dateP.add(new JLabel(dcap[i-1]));
			}
			holder(dtxt[i], dateHolder[i]);
		}
		cc.add(dateP);
		
		var phoneP = new JPanel(new FlowLayout(0));
		phoneP.add(sz(new JLabel(cap[4]), 60, 25));
		for (int i = 0; i < dtxt.length; i++) {
			phoneP.add(sz(ptxt[i] = new JTextField(), 78, 30));
			if (i < ptxt.length-1) {
				phoneP.add(new JLabel("-"));
			}
			holder(ptxt[i], "0000");
		}
		cc.add(phoneP);
		
		s.add(btn("회원가입", a->{
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty() || dtxt[i].getText().isEmpty() || ptxt[i].getText().isEmpty()) {
					eMsg("빈칸을 모두 입력해야 합니다.");
					return;
				}
			}
			
			System.out.println("select * from user where id ='"+txt[0].getText()+"'");
			if (!DBManager.getOne("select * from user where id ='"+txt[0].getText()+"'").isEmpty()) {
				eMsg("이미 사용중인 아이디 입니다.");
				return;
			}
			
			String pw =txt[1].getText();
			if (!(pw.matches(".*[0-9].*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[\\!\\@\\#\\$].*"))) {
				eMsg("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
				return;
			}
			
			try {
				LocalDate date= LocalDate.of(rei(dtxt[0].getText()), rei(dtxt[1].getText()), rei(dtxt[2].getText()));
				if (LocalDate.now().isBefore(date)) {
					eMsg("생년월일은 미래가 아니여야 합니다.");
					return;
				}
				
				String phone = ptxt[0].getText()+"-"+ptxt[1].getText()+"-"+ptxt[2].getText();
				if (!phone.matches("^\\d{3}-\\d{4}-\\d{4}$")) {
					eMsg("전화번호를 올바른 형식으로 입력해야 합니다.");
					return;
				}
				
				DBManager.execute("insert into user values(0,'"+txt[0].getText()+"','"+txt[1].getText()+"','"+txt[2].getText()+"','"+date+"','"+phone+"')");
				iMsg("회원가입이 완료되었습니다.");
				this.dispose();
						
				
			} catch (Exception e) {
				eMsg("생년월일은 올바른 형식으로 입력해야 합니다.");
				return;
			}
			
		}));
		
		
		this.setVisible(true);
	}
	
	void holder(JTextField txt, String hol) {
		txt.setText(hol);
		txt.setForeground(Color.LIGHT_GRAY);
		
		txt.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txt.getText().equals(hol)) {
					txt.setText("");
					txt.setForeground(Color.black);
				}
				
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (txt.getText().trim().isEmpty()) {
					txt.setText(hol);
					txt.setForeground(Color.lightGray);
				}
				
			}
		});
	}
	
}
