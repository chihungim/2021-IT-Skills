package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBManager;

public class SignUp extends BaseFrame {
	
	JTextField txt[] = new JTextField[3], txt2[] = new JTextField[3], txt3[] = new JTextField[3];
	String cap[] = "���̵�,��й�ȣ,�̸�,�������,�޴���ȭ".split(","), aa[] = "��,��".split(","), bb[] = "yyyy,mm,dd".split(",");
	JPanel cc = new JPanel(new GridLayout(0, 1));
	
	public SignUp() {
		super("ȸ������", 380, 350);
		
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");
		
		c.add(cc);
		
		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(lbl(cap[i], 2), 60, 25));
			tmp.add(sz(txt[i] = new JTextField(), 260, 30));
			cc.add(tmp);
		}
		
		JPanel date = new JPanel(new FlowLayout(0));
		date.add(sz(lbl(cap[3], 2), 60, 25));
		for (int i = 0; i < txt2.length; i++) {
			if (i==0) {
				date.add(sz(txt2[i] = new JTextField(), 90, 30));
			} else {
				date.add(sz(txt2[i] = new JTextField(), 65, 30));
				date.add(lbl(aa[i-1], 2));
			}
			holder(txt2[i], bb[i]);
		}
		cc.add(date);
		
		JPanel p = new JPanel(new FlowLayout(0));
		p.add(sz(lbl(cap[3], 2), 60, 25));
		for (int i = 0; i < txt3.length; i++) {
			p.add(sz(txt3[i] = new JTextField(), 80, 30));
			if (i < txt3.length-1) p.add(new JLabel("-"));
			holder(txt3[i], i==0?"000":"0000");
		}
		cc.add(p);
		
		s.add(btn("ȸ������", a->{
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty() || txt2[i].getText().isEmpty() || txt3[i].getText().isEmpty()) {
					eMsg("��ĭ�� ��� �Է��ؾ� �մϴ�.");
					return;
				}
			}
			
			if (!DBManager.getOne("select * from user where id='"+txt[0].getText()+"'").isEmpty()) {
				eMsg("�̹� ������� ���̵� �Դϴ�.");
				return;
			}
			
			if (!(txt[1].getText().matches(".*[0-9].*") && txt[1].getText().matches(".*[a-zA-Z].*") && txt[1].getText().matches(".*[!@#$].*"))) {
				eMsg("��й�ȣ�� ����, ����, Ư�����ڸ� �����ؾ� �մϴ�.");
				return;
			}
			
			try {
				LocalDate l = LocalDate.of(rei(txt2[0].getText()), rei(txt2[1].getText()), rei(txt2[2].getText()));
				if (LocalDate.now().isBefore(l)) {
					eMsg("��������� �̷��� �ƴϾ�� �մϴ�.");
					return;
				}
				String phone = txt3[0].getText() + "-"+txt3[1].getText()+"-"+txt3[2].getText();
				if (!phone.matches("\\d{3}-\\d{4}-\\d{4}")) {
					eMsg("��ȭ��ȣ�� �ùٸ� �������� �Է��ؾ� �մϴ�.");
					return;
				}
				
				iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
				DBManager.execute("insert into user values(0, '"+txt[0].getText()+"','"+txt[1].getText()+"','"+txt[2].getText()+"','"+l+"','"+phone+"')");
				dispose();
			} catch (DateTimeParseException e) {
				eMsg("��������� �ùٸ� �������� �Է��ؾ� �մϴ�.");
				return;
			}
			
		}));
		
		this.setVisible(true);
	}
	
}
