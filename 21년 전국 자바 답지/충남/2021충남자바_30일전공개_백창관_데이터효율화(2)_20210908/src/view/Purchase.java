package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import db.stmt;
import view.Reserve.ItemPanel;

public class Purchase extends BaseFrame {
	JTextField txt[] = { new JTextField(8), new JTextField(3), new JTextField(3), new JTextField(3), new JTextField(3),
			new JTextField(3), new JTextField(5) };
	ArrayList<Integer> list;
	int randumber = 0;
	int totPrice = 1200;

	public Purchase(ItemPanel item, Reserve r) {
		super("����", 750, 400);

		this.add(w = new JPanel(new GridLayout(0, 1, 0, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));

		w.setBackground(new Color(50, 100, 255));
		sz(w, 250, 1);
		c.setBackground(Color.WHITE);

		w.add(lbl("���� ����", 2, 25, Color.WHITE));
		w.add(lbl("���� ����:", 2, 10, Color.WHITE));

		w.add(lbl(stations.get(0), 2, 15, Color.WHITE));
		w.add(lbl(stations.get(r.path.size() - 1), 2, 15, Color.WHITE));
		w.add(lbl("����", 2, 15, Color.WHITE));

		w.add(lbl(r.date + ":", 2, 15, Color.WHITE));
		w.add(lbl(item.stime + " ����", 2, 15, Color.WHITE));
		w.add(lbl("�� ���� �ݾ�:", 2, 10, Color.WHITE));

		if (totPrice < r.totDis * 5) {
			totPrice = r.totDis * 5;
		}
		if (uage <= 13) {
			totPrice = (int) (totPrice * 0.9);
		}
		if (uage >= 65) {
			totPrice = (int) (totPrice * 0.5);
		}

		w.add(lbl(new DecimalFormat("#,##0��").format(totPrice), 2, 15, Color.WHITE));
		setEmpty(w, 10, 10, 10, 10);

		JPanel cn = new JPanel(new GridLayout(0, 1));
		cn.add(new JLabel(img("logo.png", 180, 30), 2));
		cn.add(lbl("Seoul Metro Ticket", 2, 20));
		c.add(cn, "North");
		cn.setOpaque(false);

		JPanel cc = new JPanel(new GridLayout(0, 1));
		c.add(cc);

		JPanel in = new JPanel(new FlowLayout(0));
		in.add(lbl("�ȳ��ϼ���, " + uname + "��.", 2, 15, Color.BLACK));
		cc.add(in);

		JPanel in1 = new JPanel(new FlowLayout(0));
		in1.add(lbl("ž�±��� �̸��� ", 2, 15, Color.BLACK));
		in1.add(txt[0]);
		in1.add(lbl("�̰�,", 2, 15, Color.BLACK));
		cc.add(in1);
		
		JPanel in2 = new JPanel(new FlowLayout(0));
		in2.add(lbl("ī���ȣ�� ", 2, 15, Color.BLACK));
		for (int i = 1; i < 5; i++) {
			in2.add(txt[i]);
		}
		in2.add(lbl("�̰�,", 2, 15, Color.BLACK));
		cc.add(in2);
		
		JPanel in3 = new JPanel(new FlowLayout(0));
		in3.add(lbl("CVC�� ", 2, 15, Color.BLACK));
		in3.add(txt[5]);
		in3.add(lbl("ī�� ��й�ȣ�� ", 2, 15, Color.BLACK));
		in3.add(txt[6]);
		in3.add(lbl("�Դϴ�.", 2, 15, Color.BLACK));
		cc.add(in3);
		
		setNum();
		
		c.add(btn("�����ϱ�", a->{
			int yn = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?","�޽���",JOptionPane.YES_NO_OPTION);
			if(yn == JOptionPane.YES_OPTION) {
				for (int i = 0; i < txt.length; i++) {
					if(txt[i].getText().isEmpty()) {
						eMsg("��� �׸��� �Է��ؾ� �մϴ�.");
						return;
					}
				}
				
				for (int i = 1; i < 5; i++) {
					if(txt[i].getText().matches(".*[^0-9].*") || txt[i].getText().length() > 4) {
						eMsg("ī�� ��ȣ�� �� 4�ڸ� ���ڷ� �����ؾ��մϴ�.");
						return;
					}
				}
				
				String cv = "";
				
				for (int i = 1; i < 4; i++) {
					cv += txt[i].getText().substring(0, 1);
				}
				
				if(!txt[5].getText().equals(cv)) {
					eMsg("CVC �ڵ尡 ��ġ���� �ʽ��ϴ�.");
					return;
				}
				
				if(!txt[6].getText().equals(ubirth+"")) {
					eMsg("ī�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					return;
				}
				String code = new DecimalFormat("000000").format(randumber);
				stmt.execute("insert into purchase values('"+code+"','"+uno+"','"+r.path.get(0)+"','"+r.path.get(r.path.size() - 1)+"','"+totPrice+"','"+tformat(item.stime, "HH:mm:ss")+"','"+r.date+"','"+r.totDis+"')");
				iMsg("������ �Ϸ�Ǿ����ϴ�!\n���Ź�ȣ:"+code);
				dispose();
			}
		}),"South");
		
		cc.setOpaque(false);
		in.setOpaque(false);
		in1.setOpaque(false);
		in2.setOpaque(false);
		in3.setOpaque(false);
		setEmpty(cc, 30, 0, 30, 0);
		setEmpty(c, 10, 20, 10, 10);
		
		for (int i = 0; i < txt.length; i++) {
			txt[i].setHorizontalAlignment(0);
			txt[i].setFont(new Font("���� ���", Font.BOLD, 15));
			txt[i].setBorder(new MatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		}
		
		setVisible(true);
	}

	private void setNum() {
		list = new ArrayList<Integer>();
		try {
			var rs = stmt.rs("select * from purchase");
			while(rs.next()){
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while(true) {
			int ran = (int)(Math.random() * 1000000);
			if(list.contains(randumber)) {
				continue;
			}else {
				randumber = ran;
				break;
			}
		}
	}

}
