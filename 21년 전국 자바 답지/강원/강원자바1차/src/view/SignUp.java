package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import db.DBManager;

public class SignUp extends BaseDialog {
	
	HolderField txt[] = new HolderField[5];
	String cap[] = "Name,Id,Password,Password확인,E-mail".split(",");
	
	public SignUp() {
		super("회원가입", 450, 600);
		
		this.add(c = pnl(new GridLayout(0, 1, 10, 10)));
		c.add(lbl("계정 정보", 2, 20));
		
		for (int i = 0; i < cap.length; i++) {
			c.add(txt[i] = new HolderField(30, cap[i]));
		}
		
		c.add(btn("회원가입", a->{
			for (int i = 0; i < cap.length; i++) {
				if (txt[i].getText().isEmpty()) {
					new eMsg("공란을 확인해주세요.");
					return;
				}
			}
			
			if (!txt[2].getText().equals(txt[3].getText())) {
				new eMsg("PW확인이 일치하지 않습니다.");
				return;
			}
			
			if(!txt[2].getText().matches(".*[\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\?\\\\|].*")){
				new eMsg("특수문자를 포함해주세요.");
				return;
			}
			
			if (!DBManager.getOne("select * from user whre id='"+txt[1].getText()+"'").isEmpty()) {
				new eMsg("id가 중복되었습니다.");
				return;
			}
			
			if (!DBManager.getOne("select * from user whre email='"+txt[4].getText()+"'").isEmpty()) {
				new eMsg("E-mail이 중복되었습니다.");
				return;
			}
			
			new iMsg("회원가입이 완료되었습니다.");
			DBManager.execute("insert into user values(0,"+txt[1].getText()+"','"+txt[2].getText()+"','"+txt[0].getText()+"','"+txt[4].getText()+"', 1000)");
			this.dispose();
			
		}));
		
		setEmpty((JPanel)getContentPane(), 50, 50, 50, 50);
		this.setVisible(true);
	}
	
}
