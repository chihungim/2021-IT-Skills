package view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import db.DBManager;

public class Find extends BaseDialog {
	
	String cap[] = "Name,E-mail,Name,Id,E-mail".split(",");
	HolderField txt[] = new HolderField[5];
	
	public Find() {
		super("���̵�/��й�ȣ ã��", 450, 600);
		
		this.add(c = pnl(new GridLayout(0, 1, 0, 10)));
		this.add(s = pnl(new GridLayout(0, 1, 0, 10)), "South");
		
		c.add(lbl("���̵� ã��", 2, 20));
		for (int i = 0; i < 2; i++) {
			c.add(txt[i] = new HolderField(30, cap[i]));
		}
		c.add(btn("���", a->{
			String name = txt[0].getText(), email = txt[1].getText();
			if (name.isEmpty() || email.isEmpty()) {
				new eMsg("������ Ȯ�����ּ���.");
				return;
			}
			
			if (!DBManager.getOne("select * from user where name='"+name+"' and email='"+email+"'").isEmpty()) {
				new iMsg("������ Id�� "+DBManager.getOne("select id from user where name='"+name+"' and email='"+email+"'")+"�Դϴ�.");
				return;
			} else {
				new eMsg("�������� �ʴ� �����Դϴ�.");
				return;
			}
			
		}));
		
		s.add(lbl("��й�ȣ ã��", 2, 20));
		for (int i = 0; i < 3; i++) {
			s.add(txt[i+2] = new HolderField(30, cap[i+2]));
		}
		s.add(btn("���", a->{
			String name = txt[2].getText(), id = txt[3].getText(), email = txt[4].getText();
			if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
				new eMsg("������ Ȯ�����ּ���.");
				return;
			}
			
			if (!DBManager.getOne("select * from where user name='"+name+"' and email='"+email+"' and id ='"+id+"'").isEmpty()) {
				new iMsg("������ Id�� PW�� "+DBManager.getOne("select pw from user where name='"+name+"' and email='"+email+"' and id ='"+id+"'")+"�Դϴ�.");
				return;
			} else {
				new eMsg("�������� �ʴ� �����Դϴ�.");
				return;
			}
		}));
		
		sz(s, 1, 300);
		setEmpty(((JPanel)getContentPane()), 30, 30, 30, 30);
		
		this.setVisible(true);
	}
	
}
