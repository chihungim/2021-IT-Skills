package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBManager;

public class MyPage extends BaseFrame {
	
	JTextField txt[] = new JTextField[3], txt2[] = new JTextField[3], txt3[] = new JTextField[3];
	String cap[] = "���̵�,��й�ȣ,�̸�,�������,�޴���ȭ".split(","), aa[] = "��,��".split(","), bb[] = "yyyy,mm,dd".split(",");
	JPanel cc = new JPanel(new GridLayout(0, 1));
	
	public MyPage() {
		super("����������", 500, 350);
		
		this.add(c = new JPanel(new BorderLayout()));
		this.add(w = new JPanel(new GridLayout(0,1,10,10)), "West");
		
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
		
		w.add(btn("ȸ������ ����", a->{
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
				
				iMsg("ȸ�������� �����Ǿ����ϴ�!");
				DBManager.execute("update user set pw='"+txt[1].getText()+"', name ='"+txt[2].getText()+"', birth='"+l+"', phone = '"+phone+"' where serial="+uno);

			} catch (DateTimeParseException e) {
				eMsg("��������� �ùٸ� �������� �Է��ؾ� �մϴ�.");
				return;
			}
			
		}));
		
		w.add(btn("��������", a->{
			new PurchaseList().addWindowListener(new Before(this));
		}));
		
		w.add(btn("Ƽ�����", a->{
			new KeyPad().addWindowListener(new Before(this));
		}));
		
		txt[0].setEnabled(false);
		try {
			var rs = DBManager.rs("select * from user where serial="+uno);
			if (rs.next()) {
				for (int i = 0; i < txt.length; i++) {
					txt[i].setText(rs.getString(i+2));
				}
				
				String birth[] = rs.getString("birth").split("-"), phone[] = rs.getString("phone").split("-");
				for (int i = 0; i < txt2.length; i++) {
					txt2[i].setText(birth[i]);
					txt2[i].setForeground(Color.black);
				}
				for (int i = 0; i < txt3.length; i++) {
					txt3[i].setText(phone[i]);
					txt3[i].setForeground(Color.black);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		setEmpty(w, 0, 0, 200, 0);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		uno ="1";
		uname ="���ο�";
		uage ="8";
		ubirth = "2013";
		new MyPage();
	}
	
}
