package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import db.DBManager;
import view.Stage.ItemPanel;

public class Purchase extends BaseFrame {
	
	String cap[] = "공연명,장소,날짜,좌석,총금액".split(",");
	String info[];
	DecimalFormat f= new DecimalFormat("#,##0");
	JLabel check;
	
	public Purchase(HashMap<String, ItemPanel> items, int tot) {
		super("결제", 300, 1);
		this.setSize(350, 300);
		info = new String[]{ pinfo[2], pinfo[3], pdate, "", f.format(tot) };
		
		for (var k : items.keySet()) {
			System.out.println(k+", "+items.get(k).price);
		}
		
		var title = lbl("결제", 0, 30);
		this.add(c = new JPanel(new FlowLayout(0)));
		this.add(s = new JPanel(new GridLayout(1, 0, 5, 5)));
		var cs = new JPanel(new FlowLayout(0));
		
		this.add(title, "North");
		this.add(c);
		
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new BorderLayout());
			if (i!=3) {
				c.add(sz(tmp, 300, 30));
				tmp.add(lbl(cap[i], 2));
				tmp.add(lbl(info[i], JLabel.RIGHT), "East");
			} else {
				var tmp_c = new JPanel(new GridLayout(0, 1));
				tmp.add(lbl(cap[i], 2), "West");
				tmp.add(tmp_c);
				for (var k : items.keySet()) {
					tmp_c.add(lbl(k + " : " + f.format(items.get(k).price), JLabel.RIGHT));
				}
				
				c.add(sz(tmp, 300, items.size()*25));
				tmp.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
				
			}
		}
		
		c.add(cs, "South");
		this.add(s, "South");
		
		cs.add(btn("본인 인증", a->{
			var pw = JOptionPane.showInputDialog("비밀번호를 입력해주세요.");
			
			if (pw == null) return;
			
			if (DBManager.getOne("select * from user where u_no = "+uno+" and u_pw ='"+pw+"'").isEmpty()) {
				eMsg("비밀번호가 일치하지 않습니다.");
				return;
			}
			
			iMsg("본인 인증이 완료되었습니다.");
			((JButton)a.getSource()).setEnabled(false);
			check.setText("V");
			
		}));
		
		cs.add(check = lbl("", 0));
		check.setForeground(Color.green);
		
		for (var bc : "결제하기,취소".split(",")) {
			s.add(btn(bc, a->{
				if (a.getActionCommand().equals("결제하기")) {
					if (check.getText().isEmpty()) {
						eMsg("본인인증을 해주세요.");
						return;
					}
					
					iMsg("결제가 완료되었습니다.");
					
					String seats = "", dis ="";
					for (var k : items.keySet()) {
						if (seats.isEmpty()) 
							seats = k;
						else seats = seats + ","+k;
						
						if (dis.isEmpty()) 
							dis = items.get(k).dis+"";
						else dis = dis + ","+items.get(k).dis;
					}
					
					DBManager.execute("insert into ticket values(0, "+uno+","+pinfo[0]+",'"+seats+"','"+dis+"')");
					
					this.setVisible(false);
					main.setVisible(true);
				} else {
					this.dispose();
				}
			}));
		}
		this.setVisible(true);
	}
	
}
