package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.DBManager;

public class MyPage extends BaseFrame {
	
	JTextField txt[] = { new JTextField(), new JPasswordField(), new JTextField() };
	JTextField dateTxt[] = { new JTextField(), new JTextField(), new JTextField() };
	JTextField phoneTxt[] = { new JTextField(), new JTextField(), new JTextField() };
	String cap[] = "���̵�,��й�ȣ,�̸�,�������,�޴���ȭ".split(",");
	String dateCap[] = "��,��".split(",");
	String dateHolder[] = "yyyy,mm,dd".split(",");
	String bcap[] = "ȸ������ ����,��������,Ƽ�����".split(",");
	
	public static void main(String[] args) {
		uno="1";
		uname="���ο�";
		new MyPage();
	}
	
	public MyPage() {
		super("����������", 500, 380);
		dataInit();
		
		this.add(w = new JPanel(new GridLayout(0, 1, 10, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");
		
		for (int i = 0; i < bcap.length; i++) {
			w.add(btn(bcap[i], a->{
				if (a.getActionCommand().contentEquals(bcap[0])) {
					for (int j = 0; j < txt.length; j++) {
						if (txt[j].getText().isEmpty() || dateTxt[j].getText().isEmpty() || phoneTxt[j].getText().isEmpty()) {
							eMsg("��ĭ�� ��� �Է��ؾ� �մϴ�.");
							return;
						}
					}
					
					if (!DBManager.getOne("select * from user where id = '"+txt[0].getText()+"'").isEmpty()) {
						eMsg("�̹� ������� ���̵� �Դϴ�.");
						return;
					}
					
					// ��й�ȣ ���� ���� Ư������
					if(!(txt[1].getText().matches(".*[0-9].*") && txt[1].getText().matches(".*[a-zA-Z].*") && txt[1].getText().matches(".*[^0-9a-zA-Z��-?].*"))) {
						eMsg("��й�ȣ�� ����, ����, Ư�����ڸ� �����ؾ� �մϴ�.");
						return;
					}
					
					try {
						LocalDate date = LocalDate.of(rei(dateTxt[0]), rei(dateTxt[1]), rei(dateTxt[2]));
						if (LocalDate.now().isBefore(date)) {
							eMsg("��������� �̷��� �ƴϾ�� �մϴ�.");
							return;
						}
						
						// ��ȭ��ȣ ����
						String phone = phoneTxt[0].getText() +  "-" + phoneTxt[1].getText() + "-" + phoneTxt[2].getText();
						if(!phone.matches("^\\d{3}-\\d{4}-\\d{4}$")) {
							eMsg("��ȭ��ȣ�� �ùٸ� �������� �Է��ؾ� �մϴ�.");
							return;
						}
						
						iMsg("ȸ�������� �����Ǿ����ϴ�!");
						DBManager.execute("update user set pw = '"+txt[1].getText()+"', name ='"+txt[2].getText()+"', birth = '"+date+"', phone ='"+phone+"' where serial ='"+uno+"'");
						
					} catch (Exception e) {
						eMsg("��������� �ùٸ� �������� �Է��ؾ� �մϴ�.");
						return;
					}
					
					
				} else if (a.getActionCommand().contentEquals(bcap[1])) {
					new PurchaseList().addWindowListener(new Before(this));
				} else {
					new KeyPad().addWindowListener(new Before(this));
				}
			}));
		}
		
		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);
		
		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i]), 60, 25));
			tmp.add(sz(txt[i], 260, 30));
			c_c.add(tmp);
		}
		
		var dateP = new JPanel(new FlowLayout(0));
		dateP.add(sz(new JLabel(cap[3]), 60, 25));
		for (int i = 0; i < dateTxt.length; i++) {
			if (i==0) {
				dateP.add(sz(dateTxt[0], 90, 30));
			} else {
				dateP.add(sz(dateTxt[i], 65, 30));
				dateP.add(new JLabel(dateCap[i-1]));
			}
			c_c.add(dateP);
		}
		
		var phoneP = new JPanel(new FlowLayout(0));
		phoneP.add(sz(new JLabel(cap[4]), 60, 25));
		for (int i = 0; i < phoneTxt.length; i++) {
			phoneP.add(sz(phoneTxt[i], 78, 30));
			if (i<phoneTxt.length-1) {
				phoneP.add(new JLabel("-"));
			}
			c_c.add(phoneP);
		}
		
		txt[0].setEnabled(false);
		try {
			var rs = DBManager.rs("select * from user where serial="+uno);
			if (rs.next()) {
				for (int i = 0; i < 3; i++) {
					txt[i].setText(rs.getString(i+2));
				}
				var birth = rs.getString(5).split("-");
				for (int i = 0; i < birth.length; i++) {
					dateTxt[i].setText(birth[i]);
				}
				var phone = rs.getString(6).split("-");
				for (int i = 0; i < phone.length; i++) {
					phoneTxt[i].setText(phone[i]);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setEmpty(w, 0, 0, 200, 0);
		setEmpty((JPanel)getContentPane(), 10, 0, 0, 10);
		this.setVisible(true);
	}

	
}
