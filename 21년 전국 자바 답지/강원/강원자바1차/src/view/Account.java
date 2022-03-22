package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBManager;
import view.BaseDialog.eMsg;

public class Account extends BaseDialog {
	
	String cap[] = "id,pwd,name,email,point".split(",");
	JTextField txt[] = new JTextField[5];
	
	public Account() {
		super("계정", 350, 450);
		
		this.add(c = new JPanel(new GridLayout(0, 1, 10, 10)));
		
		c.add(lbl("계정", 2, 20), "North");
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(sz(lbl(cap[i], 2, 11), 60, 25));
			tmp.add(sz(txt[i] = new JTextField(), 230, 30));
			c.add(tmp);
		}
		
		this.add(s = new JPanel(new GridLayout(1, 0, 10, 10)), "South");
		s.add(btn("수정", a->{
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty()) {
					new eMsg("공란을 확인해주세요.");
					return;
				}
			}
			
			if(!txt[1].getText().matches(".*[\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\?\\\\|].*")){
				new eMsg("특수문자를 포함해주세요.");
				return;
			}
			
			DBManager.execute("update user set pwd='"+txt[1].getText()+"', name='"+txt[2].getText()+"', email ='"+txt[3].getText()+"' where no = "+BaseFrame.uno);
			this.dispose();
			
		}));
		s.add(btn("취소", a->{
			this.dispose();
		}));
		txt[0].setEditable(false);
		txt[4].setEditable(false);
		
		try {
			var rs = DBManager.rs("Select * from user where no="+BaseFrame.uno);
			if (rs.next()) {
				for (int i = 2; i < txt.length+2; i++) {
					txt[i-2].setText(rs.getString(i));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}
	
}
