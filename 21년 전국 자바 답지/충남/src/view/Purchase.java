package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import db.DBManager;
import view.Reserve.ItemPanel;

public class Purchase extends BaseFrame {
	
	JTextField txt[] = {
		new JTextField(8),	
		new JTextField(3),	
		new JTextField(3),	
		new JTextField(3),	
		new JTextField(3),	
		new JTextField(3),	
		new JTextField(5),	
	};
	ArrayList<Integer> list;
	int rand=0, tot = 1200;
	LocalTime start, end;
	JPanel cc = new JPanel(new GridLayout(0, 1));
	int puno;
	
	public Purchase(ItemPanel item, Reserve r) {
		super("����", 750, 400);
		
		this.add(w = new JPanel(new GridLayout(0, 1, 0, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));
		
		w.setBackground(blue);
		sz(w, 250, 1);
		c.setBackground(Color.white);
		
		tot = tot < r.tot * 5 ? r.tot * 5 : tot;
		tot = rei(uage) <= 13 ? (int) (tot * 0.9) : tot;
		tot = rei(uage) >= 65 ? (int) (tot * 0.5) : tot;
		
		start =LocalTime.of(item.st/3600, (item.st%3600)/item.st/60, (item.st%60));
		
		String str1 = "<html><p style='color:white'>";
		str1 += "<font size=+3>���� ����</font><br><br>";
		str1 += "���� ����:<br>";
		str1 += "<b><font size =+1>" + stNames.get(r.path.get(0))+"<br>"+stNames.get(r.path.size()-1)+"<br>����</font></b><br><br>";
		str1 += "ž�� �ð�:<br>";
		str1 += "<b><font size=+1>" + r.date.toString()+"<br>"+start+"</font></b><br><br>";
		str1 += "�� ���� �ݾ�:<br>";
		str1 += "<b><font size=+1>"+iFormat(tot)+"��</font></b>";
		str1 += "</p>";
		
		JLabel info = new JLabel(str1);
		info.setFont(new Font("���� ���", Font.PLAIN, 12));
		w.add(info);
		setEmpty(w, 10, 10, 10, 10);

		JPanel cn = new JPanel(new GridLayout(0, 1));
		cn.add(new JLabel(img("logo.png", 180, 30), 2));
		cn.add(lbl("Seoul Metro Ticket", 2, 20));
		c.add(cn, "North");
		cn.setOpaque(false);
		c.add(cc);
		
		
		JPanel in = new JPanel(new FlowLayout(0));
		in.add(lbl("�ȳ��ϼ���, "+uname+"��.", 2, 15));
		cc.add(in);
		
		JPanel in1 = new JPanel(new FlowLayout(0));
		in1.add(lbl("ž�±��� �̸��� ", 2, 15));
		in1.add(txt[0]);
		in1.add(lbl("�̰�,", 2, 15));
		cc.add(in1);
		
		JPanel in2 = new JPanel(new FlowLayout(0));
		in2.add(lbl("ī���ȣ�� ", 2, 15));
		for (int i = 1; i < 5; i++) {
			in2.add(txt[i]);
		}
		in2.add(lbl("�̰�,", 2, 15));
		cc.add(in2);
		
		JPanel in3 = new JPanel(new FlowLayout(0));
		in3.add(lbl("CVC�� ", 2, 15));
		in3.add(txt[5]);
		in3.add(lbl("ī�� ��й�ȣ�� ", 2, 15));
		in3.add(txt[6]);
		in3.add(lbl("�Դϴ�.", 2, 15));
		cc.add(in3);
		
		setEmpty(c, 10, 10, 10, 10);
		setEmpty(cc, 20, 0, 20, 0);
		
		setNum();
		
		c.add(btn("�����ϱ�", a->{
			int yn = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "�޼���", JOptionPane.YES_NO_OPTION);
			if (yn == JOptionPane.YES_OPTION) {
				for (int i = 0; i < txt.length; i++) {
					if (txt[i].getText().isEmpty()) {
						eMsg("��� �׸��� �Է��ؾ� �մϴ�.");
						return;
					}
				}
				
				for (int i = 1; i < 5; i++) {
					if (!(txt[i].getText().matches(".*[0-9].*") && txt[i].getText().length()==4)) {
						eMsg("ī�� ��ȣ�� �� 4�ڸ� ���ڷ� �����ؾ� �մϴ�.");
						return;
					}
				}
				
				String cv="";
				for (int i = 1; i < 4; i++) {
					cv += txt[i].getText().substring(0, 1);
				}
				
				if (!txt[5].getText().equals(cv)) {
					eMsg("CVC �ڵ尡 ��ġ���� �ʽ��ϴ�.");
					return;
				}
				
				if (!txt[6].getText().equals(ubirth)) {
					eMsg("ī�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					return;
				}
				
//				String code = new DecimalFormat("000000").format(puno);
//				System.out.println("insert into purchase values('"+code+"','"+uno+"','"+r.path.get(0)+"','"+r.path.get(r.path.size()-1)+"','"+tot+"','"+start+"','"+r.date+"','"+r.tot+"')");
				DBManager.execute("insert into purchase values('"+puno+"','"+uno+"','"+r.path.get(0)+"','"+r.path.get(r.path.size()-1)+"','"+tot+"','"+start+"','"+r.date+"','"+r.tot+"')");
				iMsg("������ �Ϸ�Ǿ����ϴ�!\n���Ź�ȣ:"+puno);
				dispose();
				
			}
		}), "South");
		
		cc.setOpaque(false);
		in.setOpaque(false);
		in1.setOpaque(false);
		in2.setOpaque(false);
		in3.setOpaque(false);
		
		Arrays.stream(txt).forEach(t->{
			t.setHorizontalAlignment(SwingConstants.CENTER);
			t.setFont(new Font("���� ���", Font.BOLD, 15));
			t.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		});
		
		this.setVisible(true);
	}
	
	void setNum() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			var rs = DBManager.rs("select * from purchase");
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true) {
			int ran = (int)(Math.random()*1000000);
			if (list.contains(ran)) {
				continue;
			} else {
				puno = ran;
				return;
			}
		}
	}
	
}
