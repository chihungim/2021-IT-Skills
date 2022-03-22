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
	
	String cap[] = "������,���,��¥,�¼�,�ѱݾ�".split(",");
	String info[];
	DecimalFormat f= new DecimalFormat("#,##0");
	JLabel check;
	
	public Purchase(HashMap<String, ItemPanel> items, int tot) {
		super("����", 300, 1);
		this.setSize(350, 300);
		info = new String[]{ pinfo[2], pinfo[3], pdate, "", f.format(tot) };
		
		for (var k : items.keySet()) {
			System.out.println(k+", "+items.get(k).price);
		}
		
		var title = lbl("����", 0, 30);
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
		
		cs.add(btn("���� ����", a->{
			var pw = JOptionPane.showInputDialog("��й�ȣ�� �Է����ּ���.");
			
			if (pw == null) return;
			
			if (DBManager.getOne("select * from user where u_no = "+uno+" and u_pw ='"+pw+"'").isEmpty()) {
				eMsg("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				return;
			}
			
			iMsg("���� ������ �Ϸ�Ǿ����ϴ�.");
			((JButton)a.getSource()).setEnabled(false);
			check.setText("V");
			
		}));
		
		cs.add(check = lbl("", 0));
		check.setForeground(Color.green);
		
		for (var bc : "�����ϱ�,���".split(",")) {
			s.add(btn(bc, a->{
				if (a.getActionCommand().equals("�����ϱ�")) {
					if (check.getText().isEmpty()) {
						eMsg("���������� ���ּ���.");
						return;
					}
					
					iMsg("������ �Ϸ�Ǿ����ϴ�.");
					
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
