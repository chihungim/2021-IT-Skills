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
	String cap[] = "���̵�,��й�ȣ,�̸�,�������,�޴���ȭ".split(",");
	String dcap[] = "��,��".split(",");
	String dateHolder[] = "yyyy,mm,dd".split(",");
	
	public SignUp() {
		super("ȸ������", 380, 350);
		
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
		
		s.add(btn("ȸ������", a->{
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty() || dtxt[i].getText().isEmpty() || ptxt[i].getText().isEmpty()) {
					eMsg("��ĭ�� ��� �Է��ؾ� �մϴ�.");
					return;
				}
			}
			
			System.out.println("select * from user where id ='"+txt[0].getText()+"'");
			if (!DBManager.getOne("select * from user where id ='"+txt[0].getText()+"'").isEmpty()) {
				eMsg("�̹� ������� ���̵� �Դϴ�.");
				return;
			}
			
			String pw =txt[1].getText();
			if (!(pw.matches(".*[0-9].*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[\\!\\@\\#\\$].*"))) {
				eMsg("��й�ȣ�� ����, ����, Ư�����ڸ� �����ؾ� �մϴ�.");
				return;
			}
			
			try {
				LocalDate date= LocalDate.of(rei(dtxt[0].getText()), rei(dtxt[1].getText()), rei(dtxt[2].getText()));
				if (LocalDate.now().isBefore(date)) {
					eMsg("��������� �̷��� �ƴϿ��� �մϴ�.");
					return;
				}
				
				String phone = ptxt[0].getText()+"-"+ptxt[1].getText()+"-"+ptxt[2].getText();
				if (!phone.matches("^\\d{3}-\\d{4}-\\d{4}$")) {
					eMsg("��ȭ��ȣ�� �ùٸ� �������� �Է��ؾ� �մϴ�.");
					return;
				}
				
				DBManager.execute("insert into user values(0,'"+txt[0].getText()+"','"+txt[1].getText()+"','"+txt[2].getText()+"','"+date+"','"+phone+"')");
				iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
				this.dispose();
						
				
			} catch (Exception e) {
				eMsg("��������� �ùٸ� �������� �Է��ؾ� �մϴ�.");
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
