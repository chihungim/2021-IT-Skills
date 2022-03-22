package �泲;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import �泲.Reserv.item;

public class Purchase extends BaseFrame {
	JTextField tt[] = {
			new JTextField(8),
			new JTextField(3),
			new JTextField(3),
			new JTextField(3),
			new JTextField(3),
			new JTextField(3),
			new JTextField(5)
	};
	ArrayList<Integer> list;
	int rand = 0, tot = 1200;
	LocalTime start, end;
	
	public Purchase(item item, Reserv r) {
		super("����", 750, 400);
		
		add(w = new JPanel(new GridLayout(0, 1, 0, 10)),"West");
		add(c = new JPanel(new BorderLayout()));
		
		w.setBackground(new Color(50, 100, 255));
		siz(w, 250, 1);
		c.setBackground(Color.WHITE);
		
		w.add(label("���� ����", 2, 25, Color.WHITE));
		w.add(label("���� ����", 2, 10, Color.WHITE));
		
		w.add(label(stNames.get(0), 2, 15, Color.WHITE));
		w.add(label(stNames.get(r.path.size() - 1), 2, 15, Color.WHITE));
		w.add(label("����", 2, 15, Color.WHITE));
		
		start = LocalTime.of(item.st/3600, (item.st % 3600) / 60, item.st % 60);
		
		w.add(label("ž�½ð�:", 2, 10, Color.WHITE));
		w.add(label(r.date.toString(), 2, 15, Color.WHITE));
		w.add(label(start+" ����", 2, 15, Color.WHITE));
		
		w.add(label("�� ���� �ݾ�:", 2, 10, Color.WHITE));
		
		if(tot < r.tot * 5) {
			tot = r.tot * 5;
		}
		
		if(age <= 13) {
			tot = (int)(tot * 0.9);
		}
		if(age >= 65) {
			tot = (int)(tot * 0.5);
		}
		
		w.add(label(format(tot, "#,##0��"), 2, 15, Color.WHITE));
		
		JPanel cn = new JPanel(new GridLayout(0, 1));
		cn.add(new JLabel(img("logo.png", 180, 30), 2));
		cn.add(label("Seoul Metro Ticket", 2, 20));
		c.add(cn, "North");
		cn.setOpaque(false);
		
		JPanel cc = new JPanel(new GridLayout(0, 1));
		c.add(cc);
		
		JPanel in = new JPanel(new FlowLayout(0));
		in.add(label("�ȳ��ϼ���, "+NAME+"��.", 2, 15));
		cc.add(in);
		
		JPanel in1 = new JPanel(new FlowLayout(0));
		in1.add(label("ž�±��� �̸��� ", 2, 15));
		in1.add(tt[0]);
		in1.add(label("�̰�,", 2, 15));
		cc.add(in1);
		
		JPanel in2 = new JPanel(new FlowLayout(0));
		in2.add(label("ī���ȣ�� ", 2, 15));
		for (int i = 1; i < 5; i++) {
			in2.add(tt[i]);
		}
		in2.add(label("�̰�,", 2, 15));
		cc.add(in2);
		
		JPanel in3 = new JPanel(new FlowLayout(0));
		in3.add(label("CVC�� ", 2, 15));
		in3.add(tt[5]);
		in3.add(label("ī�� ��й�ȣ�� ", 2, 15));
		in3.add(tt[6]);
		in3.add(label("�Դϴ�.", 2, 15));
		cc.add(in3);
		
		setNum();
		
		c.add(btn("�����ϱ�", a->{
			int yn = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?","�޽���", JOptionPane.YES_NO_OPTION);
			if(yn == JOptionPane.YES_OPTION) {
				for (int i = 0; i < tt.length; i++) {
					if(tt[i].getText().isEmpty()) {
						errmsg("��� �׸��� �Է��ؾ� �մϴ�.");
						return;
					}
				}
				
				for (int i = 1; i < 5; i++) {
					if(tt[i].getText().matches(".*\\D.*")|| tt[i].getText().length() > 4) {
						errmsg("ī�� ��ȣ�� �� 4�ڸ� ���ڷ� �����ؾ��մϴ�.");
						return;
					}
				}
				
				String cv = "";
				
				for (int i = 1; i < 4; i++) {
					cv += tt[i].getText().substring(0,1);
				}
				
				if(!tt[5].getText().equals(cv)) {
					errmsg("CVC �ڵ尡 ��ġ���� �ʽ��ϴ�.");
					return;
				}
				if(!tt[6].getText().equals(birth+"")) {
					errmsg("ī�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					return;
				}
				
				String code = format(rand, "000000");
				execute("insert into purchase values('"+code+"','"+NO+"','"+r.path.get(0)+"','"+r.path.get(r.path.size() - 1)+"','"+tot+"','"+tformat(start, "HH:mm:ss")+"','"+r.date+"','"+r.tot+"')");
				msg("������ �Ϸ�Ǿ����ϴ�.\n���Ź�ȣ:"+code);
				dispose();
			}
		}),"South");
		
		cc.setOpaque(false);
		in.setOpaque(false);
		in1.setOpaque(false);
		in2.setOpaque(false);
		in3.setOpaque(false);
		
		for (int i = 0; i < tt.length; i++) {
			tt[i].setHorizontalAlignment(0);
		}
		
		setVisible(true);
	}

	private void setNum() {
		list = new ArrayList<Integer>();
		try {
			ResultSet rs=  stmt.executeQuery("select * from purchase");
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while(true) {
			int r = (int)(Math.random() * 1000000);
			if(list.contains(rand)) {
				continue;
			}else {
				rand = r;
				break;
			}
		}
	}

}
