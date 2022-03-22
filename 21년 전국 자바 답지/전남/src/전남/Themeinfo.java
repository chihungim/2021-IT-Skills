package ����;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Themeinfo extends Baseframe {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), c = new JPanel(), e = new JPanel(new BorderLayout());
	JPanel en = new JPanel(new BorderLayout()), es =new JPanel(new GridLayout(0, 1)), ec = new JPanel(new BorderLayout());
	
	JLabel name;
	JTextArea intro;
	JLabel lbl[] = { label("", 0, 20), label("Theme Introduction", 0, 10) };
	JLabel imglbl = new JLabel();
	JLabel idx[] = { new JLabel("���ѽð� : ", 2), new JLabel("���̵� : ", 2), new JLabel("��õ �ο� : ", 2)};
	JButton btn = new JButton("�����ϱ�");
	
	public Themeinfo() {
		super("�׸� �Ұ�", 600, 500);
		
		String star = "";
		try {
			ResultSet rs = stmt.executeQuery("select t_time, t_difficulty, t_personnel from theme where t_name = '"+tNAME+"'");
			if(rs.next()) {
				idx[0].setText("���ѽð� : "+rs.getInt(1)+"��");
				for (int i = 0; i < rs.getInt(2); i++) {
					star+="��";
				}
				idx[1].setText("���̵� : "+star);
				idx[2].setText("��õ �ο� : "+rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		add(mainp);
		
		mainp.add(imglbl = new JLabel(img("�׸�/"+tNO+".jpg", 290, 400)),"West");
		mainp.add(n,"North");
		mainp.add(e);
		
		e.add(en,"North");
		e.add(ec);
		e.add(es,"South");
		
		en.add(name = label(tNAME, 2, 20));
		name.setForeground(Color.WHITE);
		en.add(btn,"East");
		
		for (int i = 0; i < idx.length; i++) {
			es.add(idx[i]);
			idx[i].setForeground(Color.WHITE);
		}
		
		ec.add(intro = new JTextArea());
		
		intro.setLineWrap(true);
		intro.setOpaque(false);
		intro.setText(getone("select t_explan from theme where t_no = "+tNO).replace(". ", ".\n\n"));
		intro.setFont(new Font("���� ���", Font.BOLD, 15));
		intro.setForeground(Color.WHITE);
		
		btn.addActionListener(a->{
			new Reserve().addWindowListener(new be(Themeinfo.this));
		});
		
		n.add(label("�׸��Ұ�", 0, 30));
		n.add(label("Theme Introduction", 0,15),"South");
		
		e.setBackground(Color.BLACK);
		es.setOpaque(false);
		ec.setOpaque(false);
		en.setOpaque(false);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		tNAME = "404ȣ ���λ��";
		tNO = 1;
		NO = 1;
		new Themeinfo();
	}

}
