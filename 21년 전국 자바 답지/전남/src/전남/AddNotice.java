package ����;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AddNotice extends Baseframe {
	JPanel c, n, s, sn, sc;
	String str[] = "���̵�,�����,����".split(","), aa[] = "���,���".split(",");
	JTextField tt[] = new JTextField[3];
	JTextArea ta = new JTextArea();
	JRadioButton radio[] = {
			new JRadioButton("����"),
			new JRadioButton("�����")
	};
	
	public AddNotice() {
		super("�Խù� �߰�", 350, 480);
		setLayout(new BorderLayout());
		
		add(n = new JPanel(new GridLayout(0, 1, 0, 15)),"North");
		add(c = new JPanel(new BorderLayout()));
		add(s = new JPanel(new BorderLayout()),"South");
		
		
		s.add(sc = new JPanel(new FlowLayout(0)));
		s.add(sn = new JPanel(new FlowLayout(2)),"South");
		
		for (int i = 0; i < 3; i++) {
			JPanel temp = new JPanel(new BorderLayout());
			temp.add(size(label(str[i], 2), 70, 0),"West");
			temp.add(tt[i] = new JTextField());
			line(tt[i]);
			n.add(temp);
		}
		tt[0].setText(ID);
		tt[1].setText(LocalDate.now().toString());
		tt[0].setEnabled(false);
		tt[1].setEnabled(false);
		
		size(n, 0, 120);
		
		emp((JPanel)getContentPane(), 10, 10, 10, 10);
		emp(c, 10, 0, 10, 0);
		
		c.add(size(label("����", 2), 70, 0),"West");
		c.add(ta);
		
		ta.setLineWrap(true);
		line(ta);
		
		sc.add(size(label("��������", 2), 60, 20));
		for (int i = 0; i < radio.length; i++) {
			sc.add(radio[i]);
			sn.add(btn(aa[i], a->{
				if(a.getActionCommand().equals(aa[0])) {
					if(tt[2].getText().isEmpty() || ta.getText().isEmpty()) {
						errmsg("��ĭ�� �ֽ��ϴ�.");
						return;
					}
					msg("�Խù��� ��ϵǾ����ϴ�.");
					execute("insert into notice values(0,'"+LocalDate.now()+"',"+NO+",'"+tt[2].getText()+"','"+ta.getText()+"',0,'"+(radio[0].isSelected()?"O":"X")+"')");
					dispose();
				}
				if(a.getActionCommand().equals(aa[1])) {
					dispose();
				}
			}));
		}
		radio[0].setSelected(true);
		
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		ID = "room1";
		NO = 1;
		new AddNotice();
	}

}
