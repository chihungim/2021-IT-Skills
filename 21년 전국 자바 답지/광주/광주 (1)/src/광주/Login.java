package ����;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class Login extends Basedialog {
	JPanel c = new JPanel(new BorderLayout()), n = new JPanel(new BorderLayout()), ns = new JPanel(new FlowLayout(1));
	PlaceH tt = new PlaceH(15);
	JLabel jl, err, sign;
	JButton jb;
	int phase = 0;
	String t = "";
	
	public Login() {
		super("�α���", 450, 300);
		
		add(size(labelP("���ƿ��Ű��� ȯ���մϴ�.", 2, 30), 0, 70),"North");
		add(c);
		add(n,"South");
		
		c.add(jl = label("�̸��� �ּ� �Ǵ� �޴�����ȣ�� �α����ϼ���.", 2),"North");
		c.add(tt);
		tt.setPlace("�̸��� �ּ� �Ǵ� �޴��� ��ȣ");
		c.add(err = label("�� �̸��� �Ǵ� �޴����� ã�� �� �����ϴ�.", 2),"South");
		err.setForeground(Color.WHITE);
		
		
		n.add(jb = btn("�α���", a->{
			if(tt.getText().isEmpty()) {
				err.setForeground(Color.RED);
			}
			if(phase == 0) {
				String type[] = "user,seller,rider".split(",");
				for (int i = 0; i < type.length; i++) {
					try {
						ResultSet rs = stmt.executeQuery("select * from "+type[i]+" where email = '"+tt.getText()+"' or  phone = '"+tt.getText()+"'");
						if(rs.next()) {
							tt.setPlace("��ҹ���, ����, Ư����ȣ�� ������ 8�� �̻�");
							tt.setText("");
							jl.setText("��й�ȣ�� �Է��ϰ� �α����ϼ���.");
							err.setForeground(Color.WHITE);
							t = type[i];
							phase++;
							break;
						}else {
							err.setForeground(Color.RED);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} 
				}
			}else {
				try {
					ResultSet rs = stmt.executeQuery("select * from "+t+" where pw = '"+tt.getText()+"'");
					if(rs.next()) {
						NO = rs.getInt(1); 
						NAME = rs.getString("NAME");
						TYPE = t;
						jl.setText("�̸��� �ּ� �Ǵ� �޴�����ȣ�� �α����ϼ���.");
						tt.setPlace("�̸��� �ּ� �Ǵ� �޴��� ��ȣ");
						if(t.equals("user")) {
							new Mainf().addWindowListener(new be(Login.this));
						}else if(t.equals("seller")) {
							new Seller().addWindowListener(new be(Login.this));
						}else {
							new Delivery().addWindowListener(new be(Login.this));
						}
						tt.setText("");
						t = "";
						phase = 0;
					}else {
						errmsg("�Է��� ��й�ȣ�� �ùٸ��� �ʽ��ϴ�.");
						return;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}));
		n.add(ns,"South");
		
		ns.add(label("��ɹ���� ó���̽ʴϱ�?", 0));
		ns.add(sign = label("���� �����", 2));
		sign.setForeground(Color.GREEN.darker().darker());
		sign.setBorder(new MatteBorder(0, 0, 1, 0, green));
		
		sign.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jl.setText("�̸��� �ּ� �Ǵ� �޴�����ȣ�� �α����ϼ���.");
				tt.setPlace("�̸��� �ּ� �Ǵ� �޴��� ��ȣ");
				tt.setText("");
				t = "";
				phase = 0;
				new Sign().addWindowListener(new be(Login.this));
			}
		});
		
		emp((JPanel)getContentPane(), 10,10,10,10);
		emp(c, 10, 0, 20, 0);
		
		size(n, 0, 60);
		
		c.setOpaque(false);
		n.setOpaque(false);
		ns.setOpaque(false);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Login();
	}

}
