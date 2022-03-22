package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import db.DBManager;

public class SignUp extends BaseDialog {
	
	HolderField txt[] = new HolderField[5];
	String cap[] = "Name,Id,Password,PasswordȮ��,E-mail".split(",");
	
	public SignUp() {
		super("ȸ������", 450, 600);
		
		this.add(c = pnl(new GridLayout(0, 1, 10, 10)));
		c.add(lbl("���� ����", 2, 20));
		
		for (int i = 0; i < cap.length; i++) {
			c.add(txt[i] = new HolderField(30, cap[i]));
		}
		
		c.add(btn("ȸ������", a->{
			for (int i = 0; i < cap.length; i++) {
				if (txt[i].getText().isEmpty()) {
					new eMsg("������ Ȯ�����ּ���.");
					return;
				}
			}
			
			if (!txt[2].getText().equals(txt[3].getText())) {
				new eMsg("PWȮ���� ��ġ���� �ʽ��ϴ�.");
				return;
			}
			
			if(!txt[2].getText().matches(".*[\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\?\\\\|].*")){
				new eMsg("Ư�����ڸ� �������ּ���.");
				return;
			}
			
			if (!DBManager.getOne("select * from user whre id='"+txt[1].getText()+"'").isEmpty()) {
				new eMsg("id�� �ߺ��Ǿ����ϴ�.");
				return;
			}
			
			if (!DBManager.getOne("select * from user whre email='"+txt[4].getText()+"'").isEmpty()) {
				new eMsg("E-mail�� �ߺ��Ǿ����ϴ�.");
				return;
			}
			
			new iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
			DBManager.execute("insert into user values(0,"+txt[1].getText()+"','"+txt[2].getText()+"','"+txt[0].getText()+"','"+txt[4].getText()+"', 1000)");
			this.dispose();
			
		}));
		
		setEmpty((JPanel)getContentPane(), 50, 50, 50, 50);
		this.setVisible(true);
	}
	
}
