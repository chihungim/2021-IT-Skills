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
		super("�α���", 350, 200);
		
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
		
		s.add(sz(btn("�α���", a->{
			String id = txt[0].getText(), pw = txt[1].getText();
			if (id.isEmpty() || pw.isEmpty()) {
				eMsg("���̵�� ��й�ȣ�� ��� �Է��ؾ� �մϴ�.");
				return;
			}
			if (id.equals("admin") && pw.equals("1234")) {
				iMsg("�����ڴ� ȯ���մϴ�.");
				
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
					iMsg(rs.getString("name")+"�� �ȳ��ϼ���");
					
					h.setLog(true);
					
					h.setVisible(true);
					this.dispose();
				} else {
					eMsg("��ġ�ϴ� ȸ�������� �����ϴ�.");
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
