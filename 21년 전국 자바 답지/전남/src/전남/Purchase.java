package ����;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Purchase extends Baseframe {
	JPanel n, c, s;
	String str[] = "��¥,����,�׸�,�ð�,����,�ο���,�ѱݾ�".split(","), bb[] = "����,����".split(",");
	JTextField tt = new JTextField();
	DecimalFormat format = new DecimalFormat("#,##0");
	JButton jb[] = new JButton[2];
	JLabel jl[] = new JLabel[str.length - 1];
	int total = 0;
	
	public Purchase(LocalDate date, String caf, String theme, String time) {
		super("����", 350, 400);
		
		add(n = new JPanel(new BorderLayout()), "North");
		
		n.add(label("����", 0, 30));
		n.add(label("P A Y M E N T", 0, 20), "South");
		
		add(c = new JPanel(new GridLayout(0, 1, 0, 10)));
		
		String aa[] = {date.toString(), caf, theme, time, format.format(toint(getone("select c_price from cafe where c_name = '"+caf+"'"))),"0"};
		for (int i = 0; i < str.length; i++) {
			JPanel temp = new JPanel(new BorderLayout());
			JLabel jl2;
			temp.add(jl2 = size(label(str[i], 0), 60, 30),"West");
			if(i < str.length - 2) {
				temp.add(jl[i] = label(aa[i], 2));
				jl[i].setForeground(Color.WHITE);
			}else if(i == str.length - 2) {
				temp.add(tt);
			}else {
				temp.add(jl[i - 1] = label(aa[i-1], 2));
				jl[i - 1].setForeground(Color.WHITE);
			}
			emp(temp, 0, 0, 0, 10);
			jl2.setForeground(Color.WHITE);
			
			c.add(temp);
			temp.setOpaque(false);
		}
		
		tt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(tt.getText().matches(".*\\D.*")) {
					errmsg("���ڴ� �Է��� �Ұ��մϴ�.");
					tt.setText("");
					return;
				}
				if(tt.getText().isEmpty()) return;
				if(toint(tt.getText()) < 0) {
					errmsg("�ο����� 1����� �����մϴ�.");
					tt.setText("");
					return;
				}
				int max = toint(getone("select t_personnel from theme where t_name = '"+theme+"'"));
				if(toint(tt.getText()) > max) {
					errmsg("�ش� �ο����� �ʰ��߽��ϴ�.");
					tt.setText("");
					return;
				}
				int pri = toint(getone("select c_price from cafe where c_name = '"+caf+"'"));
				total = pri * toint(tt.getText());
				jl[5].setText(format.format(total));
			}
		});
		
		c.setBackground(Color.BLACK);
		
		add(s = new JPanel(new FlowLayout()),"South");
		
		for (int i = 0; i < jb.length; i++) {
			s.add(jb[i] = btn(bb[i], a->{
				if(a.getSource().equals(jb[0])) {
					if(tt.getText().isEmpty()) {
						errmsg("�ο����� �Է����ּ���.");
						return;
					}
					msg(caf+"�� "+theme+"�� "+time+"�� ����Ǿ����ϴ�\n�ѱݾ� : "+format.format(total)+"��");
					String cno = getone("select c_no from cafe where c_name = '"+caf+"'");
					String tno = getone("select t_no from theme where t_name = '"+theme+"'");
					System.out.println("insert into reservation values(0,"+NO+",'"+cno+"','"+tno+"','"+date+"','"+time+"','"+tt.getText()+"',0)");
					execute("insert into reservation values(0,"+NO+",'"+cno+"','"+tno+"','"+date+"','"+time+"','"+tt.getText()+"',0)");
					new Mainf().addWindowListener(new be(this));
				}
				if(a.getSource().equals(jb[1])) {
					dispose();
				}
			}));
		}
		
		s.setBackground(Color.BLACK);
		
		setVisible(true);
	}

}
