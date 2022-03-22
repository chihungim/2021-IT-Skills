package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import additional.Pattern;
import additional.Util;
import db.DBManager;

public class EditProfilePage extends BasePage {
	
	String cap[] = "아이디,비밀번호,이름,주소".split(",");
	JTextField txt[] = {
		new JTextField(),	
		new JPasswordField(),	
		new JTextField(),	
	};
	JComboBox<String> combo = new JComboBox<String>();
	JPanel c_c,c_s;
	HashMap<String, String> hashPoint = new HashMap<String, String>();
	
	public EditProfilePage() {
		super();
		
		this.add(n = new JPanel(), "North");
		this.add(c = new JPanel(new BorderLayout()));
		c.add(c_c = new JPanel(new GridLayout(0, 1)));
		c.add(c_s = new JPanel(new FlowLayout()), "South");
		n.setOpaque(false);
		c.setOpaque(false);
		c_c.setOpaque(false);
		c_s.setOpaque(false);
		
		n.add(Util.lbl("회원 정보 수정", 0, 25));
		
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(1));
			tmp.add(Util.sz(new JLabel(cap[i]+" : "), 60, 25));
			if (i==3) {
				tmp.add(Util.sz(combo, 150, 25));
			} else 
				tmp.add(Util.sz(txt[i], 150, 25));
			tmp.setOpaque(false);
			c_c.add(tmp);
		}
		txt[0].setEnabled(false);
		combo.setBackground(Color.white);
		
		try {
			var rs = DBManager.rs("select * from user where u_No="+uno);
			if (rs.next()) {
				for (int i = 1; i < 4; i++) {
					txt[i-1].setText(rs.getString(i+1));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c_s.add(Util.sz(Util.btn("수정", a->{
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty()) {
					Util.eMsg("빈칸이 있습니다.");
					return;
				}
			}
			if (txt[1].getText().length()<=3) {
				Util.eMsg("패턴은 3자리 이상으로 입력해주세요.");
				return;
			}
			
			DBManager.execute("update user set u_Pattern = '"+txt[1].getText()+"', u_Name ='"+txt[2].getText()+"', u_Addr = '"+getKey(combo.getSelectedItem().toString())+"' where u_No="+uno);
			Util.iMsg("회원정보가 수정되었습니다.");
			mf.addPage(new MyPage());
		}), 60, 25));
		
		
		Util.setEmpty(c, 30, 0, 30, 0);
		Util.setEmpty(this, 30, 0, 30, 0);
		
		txt[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Pattern(txt[1]).setVisible(true);
			}
		});
		
		addItem();
	}
	
	String getKey(String value) {
		for (var key : hashPoint.keySet()) {
			if (value.contentEquals(hashPoint.get(key))) {
				return key;
			}
		}
		return null;
	}
	
	void addItem() {
		try {
			var rs = DBManager.rs("SELECT po.* FROM point po left join user u on u.u_Addr=po.po_No left join seller s on s.s_Addr=po.po_No where u.u_No is null and s.s_No is null or u_No="+uno+" order by po_X, po_Y");
			while (rs.next()) {
				hashPoint.put(rs.getString(1), rs.getString(2)+"-"+rs.getString(3));
				combo.addItem(hashPoint.get(rs.getString(1)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		combo.setSelectedItem(hashPoint.get(DBManager.getOneRs("SELECT u_Addr from user where u_No="+uno)));
	}
	
	public static void main(String[] args) {
		uno = 1;
		BasePage.mf.addPage(new EditProfilePage());
		BasePage.mf.setVisible(true);
	}
	
}
